import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PwnedPasswords {

    private static String userAgent = "PwnedPasswords by RandomAdversary | github.com/RandomAdversary/PwnedPasswords";
    private static String apiVersion = "V2";
    private static String apiBaseURL = "https://haveibeenpwned.com/api/";
    private static String apiPwnedPasswordURL = "/pwnedpassword/";
    private static String apiPwnedAccountURL = "/breachedaccount/";

    /**
     * Checks for hacked passwords using have I been pwned API.
     * Read more about the project at https://haveibeenpwned.com/About
     * @param  password The password
     * @return true if the password was found in the Pwned Passwords repository, false if the password was not found in the Pwned Passwords repository.
     */
    public static boolean pwnPassword(String password) throws
        IOException, RateLimitException, BadRequestException, UnknownResponseCode {
        int responseCode = requestPwnStatusP(password);
        if (responseCode == 200)
            return true;

        if (responseCode == 404)
            return false;

        if (responseCode == 429)
            throw new RateLimitException("Too many requests — the rate limit has been exceeded.\nLearn more at https://haveibeenpwned.com/API/v2#RateLimiting");

        if (responseCode == 400)
            throw new BadRequestException("Bad request — the password does not comply with an acceptable format.");

        if (responseCode != 200 && responseCode != 404)
            throw new UnknownResponseCode("Sorry, something went wrong.\nHttp status code of the response: " + responseCode);

        return false;
    }
    
    /**
     * Checks for breached accounts using have I been pwned API.
     * Read more about the project at https://haveibeenpwned.com/About
     * @param  account The account
     * @return true if the account was found in the Pwned account repository, false if the account was not found in the Pwned account repository.
     */
    public static boolean pwnAccount(String account) throws
        IOException, RateLimitException, BadRequestException, UnknownResponseCode {
        int responseCode = requestPwnStatusA(account);
        if (responseCode == 200)
            return true;

        if (responseCode == 404)
            return false;

        if (responseCode == 429)
            throw new RateLimitException("Too many requests — the rate limit has been exceeded.\nLearn more at https://haveibeenpwned.com/API/v2#RateLimiting");

        if (responseCode == 400)
            throw new BadRequestException("Bad request — the account does not comply with an acceptable format.");

        if (responseCode != 200 && responseCode != 404)
            throw new UnknownResponseCode("Sorry, something went wrong.\nHttp status code of the response: " + responseCode);

        return false;
    }

    /**
     * Checks for hacked passwords using have I been pwned API.
     * The password is hashed(SHA1) and then sent to the API.
     * Read more about the project at https://haveibeenpwned.com/About
     * @param  password The password
     * @return true if the password was found in the Pwned Passwords repository, false if  the password was not found in the Pwned Passwords repository.
     */
    public static boolean hashAndPwnPassword(String password) throws
        BadRequestException, NoSuchAlgorithmException, IOException, RateLimitException, UnknownResponseCode {

        if (password.length() == 0)
            throw new BadRequestException("Bad request — the password does not comply with an acceptable format.");

        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(password.getBytes("UTF-8"));
        byte[] result = crypt.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return pwnPassword(sb.toString());
    }

    // Code shamelessly copied from https://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
    private static int requestPwnStatusP(String password) throws IOException {
        URL obj = new URL(apiBaseURL + apiVersion + apiPwnedPasswordURL);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", userAgent);
        String urlParameters = "Password=" + password;

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        return con.getResponseCode();
    }
    
    private static int requestPwnStatusA(String account) throws IOException {
        URL obj = new URL(apiBaseURL + apiVersion + apiPwnedAccountURL);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", userAgent);
        String urlParameters = "Account=" + account;

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        return con.getResponseCode();
    }
}
