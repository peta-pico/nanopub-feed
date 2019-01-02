package org.petapico.nanopubfeed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.commonjava.mimeparse.MIMEParse;

public class Utils {

	private Utils() {}  // no instances allowed

	private static HttpClient httpClient;

	static {
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000)
				.setConnectionRequestTimeout(100).setSocketTimeout(10000).build();
		httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
	}

	public static String getMimeType(HttpServletRequest req, String supported) {
		List<String> supportedList = Arrays.asList(StringUtils.split(supported, ','));
		String mimeType = supportedList.get(0);
		try {
			mimeType = MIMEParse.bestMatch(supportedList, req.getHeader("Accept"));
		} catch (Exception ex) {}
		return mimeType;
	}

	public static List<String> getSigningUsers() throws IOException {
		List<String> signingUsers = new ArrayList<>();
		HttpGet get = new HttpGet("http://grlc.io/api/peta-pico/nanopub-api/find_all_signing_users?endpoint=http%3A%2F%2Fgraphdb.dumontierlab.com%2Frepositories%2Fnanopubs");
		get.setHeader("Accept", "text/csv");
		BufferedReader reader = null;
		try {
			HttpResponse resp = httpClient.execute(get);
			if (!wasSuccessful(resp)) {
				EntityUtils.consumeQuietly(resp.getEntity());
				throw new IOException(resp.getStatusLine().toString());
			}
			reader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
			String line = null;
			int n = 0;
			while ((line = reader.readLine()) != null) {
				n++;
				if (n == 1) continue; // skip header
				signingUsers.add(line);
			}
		} finally {
			if (reader != null) reader.close();
		}
		return signingUsers;
	}

	private static boolean wasSuccessful(HttpResponse resp) {
		int c = resp.getStatusLine().getStatusCode();
		return c >= 200 && c < 300;
	}

}
