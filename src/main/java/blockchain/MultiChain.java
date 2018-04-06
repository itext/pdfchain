/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2018 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS

    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/

    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.

    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.

    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.

    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package blockchain;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Implementation of IBlockChain using MultiChain
 */
public class MultiChain implements IBlockChain {

    // blockchain information
    private String host;
    private int port;
    private String chainName;
    private String streamName;

    // credentials
    private String username;
    private String password;

    // random (for generating a random ID)
    private static final Random rnd = new Random(System.currentTimeMillis());

    public MultiChain(String host, int port, String chainName, String streamName, String username, String password) {
        this.host = host;
        this.port = port;
        this.chainName = chainName;
        this.streamName = streamName;
        this.username = username;
        this.password = password;
    }

    public boolean put(String key, Record data) {

        // convert data to string
        String hexString = new String(Hex.encodeHex(new JSONObject(data).toString(3).getBytes()));

        // build request
        Map<String, Object> request = new HashMap<>();
        request.put("method", "publish");
        request.put("chain_name", chainName);
        request.put("params", new String[]{streamName, key, hexString});

        // execute request
        try {
            // post JSON data
            JSONObject responseObject = postJSON(request);
            // response should not contain errors
            return responseObject.get("error").toString().equals("null");
        } catch (IOException e) {
            return false;
        }
    }

    public List<Record> all() {
        // build request
        Map<String, Object> request = new HashMap<>();
        request.put("method", "liststreamitems");
        request.put("chain_name", chainName);
        request.put("params", new Object[]{streamName, false, 1024});

        // parse return value
        try {
            return processJSON(postJSON(request));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // default
        return java.util.Collections.emptyList();
    }

    public List<Record> get(String key) {

        // build request
        Map<String, Object> request = new HashMap<>();
        request.put("method", "liststreamkeyitems");
        request.put("chain_name", chainName);
        request.put("params", new Object[]{streamName, key});

        // parse return value
        try {
            return processJSON(postJSON(request));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // default
        return java.util.Collections.emptyList();
    }

    private List<Record> processJSON(JSONObject responseObject) {
        // parse return value
        try {
            JSONArray resultArr = responseObject.getJSONArray("result");
            List<Record> retval = new ArrayList<>();
            for (int i = 0; i < resultArr.length(); i++) {
                JSONObject resultObj = resultArr.getJSONObject(i);
                String dataBytes = resultObj.getString("data");
                dataBytes = new String(Hex.decodeHex(dataBytes.toCharArray()));
                try {
                    Record data = new Record(new JSONObject(dataBytes).toMap());
                    Map<String, Object> objectMap = resultObj.toMap();
                    for (String objectDataKey : objectMap.keySet()) {
                        if (!objectDataKey.equals("data")) {
                            data.put(objectDataKey, objectMap.get(objectDataKey));
                        }
                    }
                    retval.add(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return retval;
        } catch (DecoderException e) {
            e.printStackTrace();
        }

        // default
        return java.util.Collections.emptyList();
    }

    private static String generateRandomID(int len) {
        String chars = "abcdefghijklmnopqrstuvwxyz123456";
        StringBuilder retval = new StringBuilder();
        while (retval.length() < len) {
            retval.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return retval.toString();
    }

    private JSONObject postJSON(Map<String, Object> data) throws IOException {

        // request id
        String id = generateRandomID(32);

        // build json
        data.put("jsonrpc", "2.0");
        data.put("id", id);
        if (!data.containsKey("params"))
            data.put("params", new String[]{});

        String jsonString = new JSONObject(data).toString(3);

        // build request entity
        StringRequestEntity requestEntity = new StringRequestEntity(
                jsonString,
                "application/json",
                "UTF-8");

        // build method
        PostMethod post = new PostMethod(host + ":" + port);

        // add the payload
        post.setRequestEntity(requestEntity);

        // execute method
        HttpClient client = new HttpClient();
        Credentials defaultCredentials = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(org.apache.commons.httpclient.auth.AuthScope.ANY, defaultCredentials);

        int status = client.executeMethod(post);
        if (status != 200)
            return null;

        // collect response
        JSONObject responseObject = new JSONObject(post.getResponseBodyAsString());

        // check id
        if (!responseObject.getString("id").equals(id))
            return null;

        // return
        return responseObject;
    }
}
