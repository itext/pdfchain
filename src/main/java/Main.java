import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

/**
 * Created by Joris Schellekens on 7/10/2017.
 */
public class Main {

    public static void main(String[] args) throws IOException, GeneralSecurityException {

        IBlockChain mc = new MultiChain()
                .setHost("http://127.0.0.1")
                .setPort(4352)
                .setUsername("multichainrpc")
                .setPassword("BHcXLKwR218R883P6pjiWdBffdMx398im4R8BEwfAxMm")
                .setChainName("chain1")
                .setStream("stream1");

        AbstractExternalSignature sgn = new DefaultExternalSignature(new File("C:\\Users\\Joris Schellekens\\Downloads\\ks"), "demo", "password");

        File inputFile = new File("C:\\Users\\Joris Schellekens\\Desktop\\pdfs\\30_marked.pdf");

        PdfChain chain = new PdfChain(mc, sgn);
        for (Map<String, Object> docEntry : chain.get(inputFile)) {
            for (Map.Entry<String, Object> entry : docEntry.entrySet())
                System.out.println(padRight(entry.getKey(), 32) + " : " + entry.getValue());
            System.out.println("");
        }

    }

    private static String padRight(String s, int len) {
        while (s.length() < len)
            s += " ";
        return s;
    }

}
