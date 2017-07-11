/**
 * Created by Benoit Lagae on 2017-07-11.
 */
public class Utils {

    private Utils() {}

    /**
     * Utility function for pretty printing
     *
     * @param s
     * @param len
     * @return a String padded to a specific length, if necessary
     */
    protected static String padRight(String s, int len) {
        if (s.length() >= len)
            return s;
        StringBuilder sb = new StringBuilder(len);
        sb.append(s);
        while (sb.length() < len)
            sb.append(' ');
        return sb.toString();
    }
}
