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
