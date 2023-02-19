package com.consumer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainClass {
	public static void main (String[] args) {
		System.out.println(makePost("http://10.10.11.27:8082/fm/cache/invalidate", new String[]{"botName"}, new String[]{"linefinalbot"}, new String[0], new String[0]));
	}


	public static String makePost (String baseUrl, String[] paramname, String[] paramvalues, String[] headername, String[] headervals) {
		StringBuilder paramstr = new StringBuilder();
		for (int i = 0; i < paramname.length; i++) {
			if (i > 0)
				paramstr.append("&");
			paramstr.append(paramname[i]);
			paramstr.append("=");
			paramstr.append(URLEncoder.encode(paramvalues[i]));
		}

		HttpURLConnection conn = null;
		InputStream       is   = null;
		try {
			URL url = new URL(baseUrl);

			// (set connection and read timeouts on the connection)
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setReadTimeout(30 * 1000);
			conn.setConnectTimeout(30 * 1000);

			for (int i = 0; i < headername.length; i++)
				 conn.setRequestProperty(headername[i], headervals[i]);
			conn.setRequestProperty("Content-Length", "" + Integer.toString(paramstr.toString().getBytes().length));

			conn.setDoInput(true);
			conn.setDoOutput(true);

			OutputStreamWriter connwriter = new OutputStreamWriter(conn.getOutputStream());
			System.out.println("Request Parameter : " + paramstr.toString());
			connwriter.write(paramstr.toString());
			connwriter.flush();
			connwriter.close();

			String resp = convertStreamToString(conn.getInputStream());
			return resp;
		} catch (Exception ex) {
			System.out.println("Making POST:" + baseUrl);
			System.out.println("Exception : " + ex);
			String errorResp = "";
			try {
				errorResp = convertStreamToString(conn.getErrorStream());
			} catch (IOException e) {
				System.out.println("Exception : " + ex);
			}
			try {
				System.out.println("Http request error url : " + baseUrl + "; respCode : " + conn.getResponseCode() + "; response : " + errorResp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}


	public static String convertStreamToString (java.io.InputStream is) throws IOException {
		return new String(readFully(is), "UTF-8");
	}


	private static byte[] readFully (InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos   = new ByteArrayOutputStream();
		byte[]                buffer = new byte[1024];
		int                   length = 0;
		while ((length = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		return baos.toByteArray();
	}


}
