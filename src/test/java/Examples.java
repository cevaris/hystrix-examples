import static org.junit.Assert.*;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import org.junit.Test;

import com.hystrix.utils.files.Files;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class Examples {

	public class CommandDomainCheck extends HystrixCommand<String> {

		String domain;

		public CommandDomainCheck(String domain) {
			super(HystrixCommandGroupKey.Factory.asKey("CommandDomainCheck"));
			this.domain = domain;
		}

		@Override
		protected String run() {

//			String urlString = "http://wherever.com/someAction?param1=value1&param2=value2....";
			try {

//				String url = "http://www.twitter.com";

				URL obj = new URL(this.domain);
				HttpURLConnection conn = (HttpURLConnection) obj
						.openConnection();
				conn.setReadTimeout(5000);
				conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
				conn.addRequestProperty("User-Agent", "Mozilla");
				conn.addRequestProperty("Referer", "google.com");

				System.out.println("Request URL ... " + this.domain);

				boolean redirect = false;

				// normally, 3xx is redirect
				int status = conn.getResponseCode();
				if (status != HttpURLConnection.HTTP_OK) {
					if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER)
						redirect = true;
				}

				System.out.println("Response Code ... " + status);

				if (redirect) {

					// get redirect url from "location" header field
					String newUrl = conn.getHeaderField("Location");

					// get the cookie if need, for login
					String cookies = conn.getHeaderField("Set-Cookie");

					// open the new connnection again
					conn = (HttpURLConnection) new URL(newUrl).openConnection();
					conn.setRequestProperty("Cookie", cookies);
					conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
					conn.addRequestProperty("User-Agent", "Mozilla");
					conn.addRequestProperty("Referer", "google.com");

					System.out.println("Redirecting to URL : " + newUrl);
					this.domain = newUrl;
					return run();

				}

//				BufferedReader in = new BufferedReader(new InputStreamReader(
//						conn.getInputStream()));
//				String inputLine;
//				StringBuffer html = new StringBuffer();
//
//				while ((inputLine = in.readLine()) != null) {
//					html.append(inputLine);
//				}
//				in.close();

//				System.out.println("URL Content... \n" + html.toString());
				System.out.println("Done with URL" + this.domain);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return "false";
		}
	}

	@Test
	public void syncTest() {

		String[] domains = Files.readLines("/data/top-hundred-domains.txt");
		Random rand = new Random();
		for (int i = 0; i < 20; i++) {
			int index = rand.nextInt(domains.length);
			String domain = domains[index];
			String isValid = new CommandDomainCheck(domain).execute();
			System.out.println(String
					.format("%s is valid %s", domains, isValid));
		}

		// assertEquals("Hello World!", new
		// CommandHelloWorld("World").execute());
		// assertEquals("Hello Bob!", new CommandHelloWorld("Bob").execute());

	}

}
