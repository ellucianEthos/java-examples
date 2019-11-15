package com.ellucian.integration.proxy;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class PutExample {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String jwt = "";

		//change these variables to match your data
		String apiKey = "API KEY";
		String id = "fd50c823-05fe-4db7-9dea-510aebf60fb6";
		String personId = "0e0f8b8e-d267-4307-86d1-25af131ee55f";
		String detailId = "e031411f-b413-4992-80d4-bf7fd55f20ad";
		
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
		
		//PUT a person-holds resource
		HttpPut httpPut = new HttpPut("https://integrate.elluciancloud.com/api/person-holds/" + id);
		httpPut.addHeader("authorization", jwt);
		httpPut.addHeader("content-type", "application/vnd.hedtech.integration.v6+json");
		httpPut.addHeader("accept", "application/vnd.hedtech.integration.v6+json");
		String payload = "{\"endOn\":\"2019-12-31T04:00:00Z\",\"person\":{\"id\":\"" + personId +
				"\"},\"startOn\":\"2012-03-30T04:00:00Z\",\"type\":{\"category\":\"academic\",\"detail\":{\"id\":\"" + detailId + "\"}}}";
		HttpEntity requestEntity = EntityBuilder.create().setText(payload).build();
		httpPut.setEntity(requestEntity);
		System.out.println(httpPut.getURI());
		CloseableHttpResponse proxyResponse = httpclient.execute(httpPut);

		try {
		    HttpEntity entity = proxyResponse.getEntity();
		    System.out.println(EntityUtils.toString(entity));
		} finally {
			proxyResponse.close();
		}
	}

}
