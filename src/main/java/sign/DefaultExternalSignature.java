package sign;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Default implementation of AbstractExternalSignature
 */
public class DefaultExternalSignature extends AbstractExternalSignature {

    private KeyStore ks;
    private String alias;
    private String password;

    public DefaultExternalSignature(InputStream keystoreFile, String alias, String password) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        this.alias = alias;
        this.password = password;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(keystoreFile, password.toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw e;
        }
    }

    @Override
    public String getHashAlgorithm() {
        return "SHA-256";
    }

    @Override
    public String getEncryptionAlgorithm() {
        return "RSA";
    }

    @Override
    public Key getPrivateKey() {
        try {
            return ks.getKey(alias, password.toCharArray());
        } catch (KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Key getPublicKey() {
        try {
            return ks.getCertificate(alias).getPublicKey();
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return null;
        }
    }

}
