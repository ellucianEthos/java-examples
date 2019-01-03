package com.ellucian.integration.consume;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConsumeApp {

	public static void main(String[] args) throws ClientProtocolException, IOException, InterruptedException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String apiKey = "API KEY";
		String jwt = "";
		int statusCode = 401;
		
		while (true) {
			if (statusCode != 200){
				//get new token
				HttpPost httpPost = new HttpPost("https://integrate.elluciancloud.com/auth");
				httpPost.addHeader("authorization", "Bearer " + apiKey);
				httpPost.addHeader("cache-control", "no-cache");
				CloseableHttpResponse tokenResponse = httpclient.execute(httpPost);
	
				try {
				    HttpEntity entity = tokenResponse.getEntity();
				    jwt = "Bearer " + EntityUtils.toString(entity);
				    System.out.println(jwt);
				} finally {
					tokenResponse.close();
				}
			}
			
			HttpGet httpGet = new HttpGet("https://integrate.elluciancloud.com/consume");
			httpGet.addHeader("authorization", jwt);
			httpGet.addHeader("cache-control", "no-cache");
			CloseableHttpResponse consumeResponse = httpclient.execute(httpGet);
			
			try {
				statusCode = consumeResponse.getStatusLine().getStatusCode();
			    HttpEntity entity = consumeResponse.getEntity();
			    String payload = EntityUtils.toString(entity);
			    System.out.println(payload);
			    //example of parsing json response
			    JSONArray array = new JSONArray(payload);
			    for(int i = 0; i < array.length(); i++){
			    	JSONObject obj = array.getJSONObject(i);
			    	System.out.println("Found message ID: " + obj.getInt("id"));
			    }
			} finally {
				consumeResponse.close();
			}
			
			Thread.sleep(10000);
		}

	}

}
