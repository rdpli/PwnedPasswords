import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class PwnedPasswords {

    private static String userAgent = "PwnedPasswords by RandomAdversary | github.com/RandomAdversary/PwnedPasswords";
    private static String apiBaseURL = "https://api.pwnedpasswords.com";
    private static String apiPwnedPasswordURL = "/pwnedpassword/";
    private static String apiRangeURL = "/range/";

    /**
     * Checks for hacked passwords using have I been pwned API.
     * Read more about the project at https://haveibeenpwned.com/About
     *
     * @param password The password
     * @return true if the password was found in the Pwned Passwords repository, false if  the password was not found in the Pwned Passwords repository.
     */
    public static boolean pwnPassword(String password) throws IOException, RateLimitException, BadRequestException, UnknownResponseCode {
        int responseCode = requestPwnStatus(password);
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
     * Checks for hacked passwords using have I been pwned API.
     * The password is hashed(SHA1) and then sent to the API.
     * Read more about the project at https://haveibeenpwned.com/About
     *
     * @param password The password
     * @return true if the password was found in the Pwned Passwords repository, false if  the password was not found in the Pwned Passwords repository.
     */
    public static boolean hashAndPwn(String password) throws BadRequestException, NoSuchAlgorithmException, IOException, RateLimitException, UnknownResponseCode {
        return pwnPassword(hashPassword(password));
    }

    private static String hashPassword(String password) throws BadRequestException, NoSuchAlgorithmException, UnsupportedEncodingException {
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
        return sb.toString();
    }

 /*   public static boolean pwnByRange(String password) throws NoSuchAlgorithmException, BadRequestException, IOException, UnknownResponseCode, RateLimitException {
        String hashedPassword = hashPassword(password).toUpperCase();
        String range = hashedPassword.substring(0, 5).toUpperCase();
        //Note: The api is case insensitive.

        String URL = apiBaseURL + apiRangeURL + range;
        //Get request to get the range
        URL obj = new URL(URL);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", userAgent);
        int responseCode = con.getResponseCode();
        //Validate the response code
        if (responseCode == 429)
            throw new RateLimitException("Too many requests — the rate limit has been exceeded.\nLearn more at https://haveibeenpwned.com/API/v2#RateLimiting");

        if (responseCode != 200) {
            throw new UnknownResponseCode("Sorry, something went wrong.\nHttp status code of the response: " + responseCode);
        }
        //If everything is ok, read the data
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String data = response.toString().toUpperCase();
        return data.contains(hashedPassword);
    }*/

    private static int requestPwnStatus(String password) throws IOException {
        String URL = apiBaseURL + apiPwnedPasswordURL + password;
        return GET(URL);
    }

    // Code shamelessly copied from https://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
    private static int GET(String URL) throws IOException {
        URL obj = new URL(URL);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", userAgent);
        return con.getResponseCode();
    }

}
