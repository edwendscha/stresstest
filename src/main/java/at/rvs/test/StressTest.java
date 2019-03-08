package at.rvs.test;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Controller
public class StressTest {
	private static final String HTTPS = "https://";
	private static final String CLUSTER_URL = ".its-coding.lab.cloudwerkstatt.com";
	@Autowired
	RestTemplate restTemplate;

	@Bean
	public RestTemplate rest() {
		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
				.build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		return new RestTemplate(requestFactory);
	}

	public void startStressTest(String url, long zeit, long delay, String param) throws Exception {
		long start = System.currentTimeMillis(); 
		while ((start + zeit) > System.currentTimeMillis()) {
			testGet(HTTPS + url + CLUSTER_URL + param);
			Thread.sleep(delay); 
		}
	}

	public void startStressTestPost(String postAufruf,long zeit, long delay, String param) throws Exception {
		long start = System.currentTimeMillis();
		while (start + zeit > System.currentTimeMillis()) {
			testPost(HTTPS + postAufruf + CLUSTER_URL, param);
			Thread.sleep(delay); 
		}
	}

	public void testGet(String getAufruf) throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		URL url = new URL(getAufruf);
		URLConnection con = url.openConnection();
		Reader reader = new InputStreamReader(con.getInputStream());
		while (true) {
			int ch = reader.read();
			if (ch == -1) {
				break;
			}
			System.out.print((char) ch);
		}
		System.out.println("");
	}

	public void testPost(String postAufruf, String param) throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		URL url = new URL(postAufruf);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);
		con.setInstanceFollowRedirects(false);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("charset", "utf-8");
		con.setRequestProperty("Content-Length", Integer.toString(param.length()));
		con.setUseCaches(false);
		try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
			wr.writeChars(param);
		}
		 Reader reader = new InputStreamReader(con.getInputStream());
		while (true) {
			int ch = reader.read();
			if (ch == -1) {
				break;
			}
			System.out.print((char) ch);
		}
		System.out.println("");
	}
}
