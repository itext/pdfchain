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
package sign;

import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class aggregates all the information needed to produce a (signed) hash
 * This includes: a hashing algorithm, a signing algorithm,  a public/private keypair
 */
public abstract class AbstractExternalSignature {

    /**
     * Get the hashing algorithm
     *
     * @return a String representing the hashing algorithm being used for this signature
     */
    public abstract String getHashAlgorithm();

    /**
     * Get the encryption algorithm
     *
     * @return a String representing the hashing algorithm being used for this signature
     */
    public abstract String getEncryptionAlgorithm();

    /**
     * Get the private key
     *
     * @return the private key
     */
    public abstract Key getPrivateKey();

    /**
     * Get the public key
     *
     * @return the public key
     */
    public abstract Key getPublicKey();

    /**
     * Calculate the unsigned hash for a given pdf file
     *
     * @param pdfFile the input PDF (file)
     * @return the hash of the input PDF
     */
    public byte[] hash(InputStream pdfFile) {
        try {
            MessageDigest complete = MessageDigest.getInstance(getHashAlgorithm());
            return complete.digest(IOUtils.toByteArray(new BufferedInputStream(pdfFile)));
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    /**
     * Calculate the the signed hash for a given pdf file
     *
     * @param pdfFile the input PDF file
     * @return an encrypted hash for the input PDF file
     * @throws GeneralSecurityException if the signature could not be generated
     * @throws IOException              if the file could not be read, or is not a valid PDF document
     */
    public byte[] encryptHash(InputStream pdfFile) throws GeneralSecurityException, IOException {
        Key privKey = getPrivateKey();
        if (privKey == null) {
            return new byte[]{};
        }
        Cipher cipher = Cipher.getInstance(getEncryptionAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privKey);
        return cipher.doFinal(hash(pdfFile));
    }

}
