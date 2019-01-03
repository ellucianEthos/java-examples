package com.ellucian.integration.publish;

import java.io.IOException;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class PublishApp {
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String jwt = "";
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
		
		//publish a change-notification
		HttpPost httpPost2 = new HttpPost("https://integrate.elluciancloud.com/publish");
		httpPost2.addHeader("authorization", jwt);
		httpPost2.addHeader("content-type", "application/vnd.hedtech.change-notifications.v2+json");
		//get an array of change-notifications for the payload
		//You can send a JSON array of up to 20 change-notifications, or a single change-notification object
		JSONArray cns = getChangeNotifications(20);
		HttpEntity requestEntity = EntityBuilder.create().setText(cns.toString()).build();
		httpPost2.setEntity(requestEntity);
		CloseableHttpResponse publishResponse = httpclient.execute(httpPost2);

		try {
			int status = publishResponse.getStatusLine().getStatusCode();
		    System.out.println("Publish response status: " + status);
		} finally {
			publishResponse.close();
		}
	}
	
	// Get a JSON array of change-notifications...you can request 1 to 20.
	// Ethos Integration will not accept more than 20 at once
	private static JSONArray getChangeNotifications(int capacity) {
		JSONArray cns = new JSONArray();
		if(capacity > 0 && capacity < 21){
			for(int i = 0; i < capacity; i++){
				//create a change-notification object with the appropriate contents
				String id = UUID.randomUUID().toString();
				JSONObject resource = new JSONObject()
						.put("name", "buildings")
						.put("id", id)
						.put("version", "application/vnd.hedtech.integration.v8+json");
				JSONObject content = new JSONObject()
						.put("code", "BLD1")
						.put("title", "Building 1")
						.put("description", "the first building")
						.put("id", id);
				JSONObject cn = new JSONObject()
						.put("resource", resource)
						.put("operation", "replaced")
						.put("contentType", "resource-representation")
						.put("content", content);
				cns.put(cn);
			}
		}
		return cns;
	}

}
