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
