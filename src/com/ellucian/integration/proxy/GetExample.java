package com.ellucian.integration.proxy;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class GetExample {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String jwt = "";

		//change this variable to match your data
		String apiKey = "API KEY";
		
		//get new token
		HttpPost httpPost = new HttpPost("https://integrate.elluciancloud.com/auth");
		httpPost.addHeader("authorization", "Bearer " + apiKey);
		CloseableHttpResponse tokenResponse = httpclient.execute(httpPost);

		try {
		    HttpEntity entity = tokenResponse.getEntity();
		    jwt = "Bearer " + EntityUtils.toString(entity);
		    System.out.println(jwt);
		} finally {
			tokenResponse.close();
		}
		
		//get person-holds resources
		HttpGet httpGet = new HttpGet("https://integrate.elluciancloud.com/api/person-holds");
		httpGet.addHeader("authorization", jwt);
		httpGet.addHeader("accept", "application/json");
		CloseableHttpResponse proxyResponse = httpclient.execute(httpGet);
		
		try {
			HttpEntity entity = proxyResponse.getEntity();
		    System.out.println(EntityUtils.toString(entity));
		} finally {
			proxyResponse.close();
		}

	}

}
