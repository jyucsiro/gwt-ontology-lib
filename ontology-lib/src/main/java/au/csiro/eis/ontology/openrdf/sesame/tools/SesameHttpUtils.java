package au.csiro.eis.ontology.openrdf.sesame.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class SesameHttpUtils {
	public static String getStatementsViaBuilder(String scheme, String host, String path, String context, String format) throws ClientProtocolException, IOException, URISyntaxException {
		URIBuilder builder = new URIBuilder();
		builder.setScheme(scheme).setHost(host).setPath(path);

		if(context != null) {
			builder.setParameter("context", context);
		}

		return getStringFromHttpCall(builder.build(), format);		
	}

	public static  String getStatements(String server, String context, String format) throws ClientProtocolException, IOException, URISyntaxException {
		String uriToCreate = server;

		if(context != null) {
			String encodedContext = URLEncoder.encode(context, "UTF-8");
			uriToCreate = server + "?context=" + encodedContext;
		}
		URI httpCall = URI.create(uriToCreate); 

		return getStringFromHttpCall(httpCall, format);		
	}

	public static  File getStatementsAsFile(String server, String context, String format) throws ClientProtocolException, IOException, URISyntaxException {
		String uriToCreate = server;

		if(context != null) {
			String encodedContext = URLEncoder.encode(context, "UTF-8");
			uriToCreate = server + "?context=" + encodedContext;
		}
		URI httpCall = URI.create(uriToCreate); 

		return getFileFromHttpCall(httpCall, format);		
	}
	
	public static String getStringFromHttpCall(URI uri, String format) throws ClientProtocolException, IOException, URISyntaxException {
		String result = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(uri);

		if(format!=null) {
			httpGet.addHeader("Accept", format);
		}
		else {
			httpGet.addHeader("Accept", "application/rdf+xml");
		}
		System.out.println(httpGet.getURI());

		HttpResponse response1 = httpclient.execute(httpGet);

		// The underlying HTTP connection is still held by the response object 
		// to allow the response content to be streamed directly from the network socket. 
		// In order to ensure correct deallocation of system resources 
		// the user MUST either fully consume the response content  or abort request 
		// execution by calling HttpGet#releaseConnection().

		try {
			System.out.println(response1.getStatusLine());
			HttpEntity entity1 = response1.getEntity();
			// do something useful with the response body
			Header responseContentType = entity1.getContentType();
			System.out.println(responseContentType.toString());


			result = getStringFromInputStream(entity1.getContent());

			//System.out.println(result);
			//System.out.println("Done");

			// and ensure it is fully consumed
			EntityUtils.consume(entity1);
		} finally {
			httpGet.releaseConnection();
		}

		return result;
	}
	
	/**
	 * Caller must ensure input stream is closed
	 * @param uri
	 * @param format
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static File getFileFromHttpCall(URI uri, String format) throws ClientProtocolException, IOException, URISyntaxException {
		File result= null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(uri);

		if(format!=null) {
			httpGet.addHeader("Accept", format);
		}
		else {
			httpGet.addHeader("Accept", "application/rdf+xml");
		}
		System.out.println(httpGet.getURI());

		HttpResponse response1 = httpclient.execute(httpGet);

		// The underlying HTTP connection is still held by the response object 
		// to allow the response content to be streamed directly from the network socket. 
		// In order to ensure correct deallocation of system resources 
		// the user MUST either fully consume the response content  or abort request 
		// execution by calling HttpGet#releaseConnection().
		

		
		try {
			if(response1.getStatusLine().getStatusCode() != HttpStatus.SC_OK ) {
		        System.err.println("Method failed: " + response1.getStatusLine());
				return null;
			}

			else {
				System.out.println(response1.getStatusLine());
				HttpEntity entity1 = response1.getEntity();
				// do something useful with the response body
				Header responseContentType = entity1.getContentType();
				System.out.println(responseContentType.toString());
	
				result = getFileFromInputStream(entity1.getContent());
				EntityUtils.consume(entity1);
			}	
			//EntityUtils.consume(entity1);
		} finally {
			httpGet.releaseConnection();
		}

		return result;
	}

	// convert InputStream to String
	private static File getFileFromInputStream(InputStream is) throws IOException {

			File temp = File.createTempFile("temp-file-name", ".tmp"); 
			FileOutputStream outputStream =  new FileOutputStream(temp);
			try {

				int read = 0;
				byte[] bytes = new byte[1024];
		 
				while ((read = is.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (outputStream != null) {
					try {
						// outputStream.flush();
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		 
				}
			}

			return temp;

		}
	
	// convert InputStream to String
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}
}
