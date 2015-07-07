/**
 *
 * ThingBench - Things and Devices Simulator
 *
 * http://github.com/frapu78/thingbench
 *
 * @author Frank Puhlmann
 *
 */
package thingbench;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author fpu
 */
public class TestLampCall {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String command = "ON";

        try {
                    
            URL url = new URL("http://localhost:9099/things/1692623602/1253071236");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write("<thing><property name=\"power\" value=\""+command+"\"/></thing>");
            httpCon.getInputStream();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
