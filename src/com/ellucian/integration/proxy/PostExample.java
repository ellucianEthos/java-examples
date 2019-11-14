package com.ellucian.integration.proxy;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class PostExample {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String jwt = "";

		//change these variables to match your data
		String apiKey = "API KEY";
		String personId = "080c43e1-1782-4ed9-98cf-1aa68810b179";
		String detailId = "04664c5f-bac6-43bf-abe4-ac785be86416";
		
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
		
		//POST a new person-holds resource
		HttpPost httpPost2 = new HttpPost("https://integrate.elluciancloud.com/api/person-holds");
		httpPost2.addHeader("authorization", jwt);
		httpPost2.addHeader("content-type", "application/vnd.hedtech.integration.v6+json");
		httpPost2.addHeader("accept", "application/json");
		HttpEntity requestEntity = EntityBuilder.create().setText("{\"id\":\"00000000-0000-0000-0000-000000000000\",\"person\":{\"id\":\"" + personId +
				"\"},\"startOn\":\"2012-03-30T04:00:00Z\",\"type\":{\"category\":\"academic\",\"detail\":{\"id\":\"" + detailId + "\"}}}").build();
		httpPost2.setEntity(requestEntity);
		CloseableHttpResponse proxyResponse = httpclient.execute(httpPost2);

		try {
			HttpEntity entity = proxyResponse.getEntity();
		    System.out.println(EntityUtils.toString(entity));
		} finally {
			proxyResponse.close();
		}
	}

}
