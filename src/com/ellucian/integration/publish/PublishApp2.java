package com.ellucian.integration.publish;

import java.io.IOException;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class PublishApp2 {
	
	public static void main(String[] args) throws IOException {
		OkHttpClient client = new OkHttpClient();
		String jwt = "";
		String apiKey = "API KEY";
		
		//get new token
		RequestBody payload = RequestBody.create(null, ""); // okhttp requires a payload for POSTs, however the /auth endpoint takes a null payload, which is created here.
		Request tokenRequest = new Request.Builder()
			.url("https://integrate.elluciancloud.com/auth")
			.post(payload)
			.addHeader("authorization", "Bearer " + apiKey)
			.build();
		Response tokenResponse = client.newCall(tokenRequest).execute();
		jwt = "Bearer " + tokenResponse.body().string();
		System.out.println(jwt);
		
		//publish a change-notification
		MediaType type = MediaType.parse("application/vnd.hedtech.change-notifications.v2+json");
		//get an array of change-notifications for the payload
		//You can send a JSON array of up to 20 change-notifications, or a single change-notification object
		JSONArray cns = getChangeNotifications(20);
		RequestBody publishPayload = RequestBody.create(type, cns.toString());
		Request publishRequest = new Request.Builder()
			.url("https://integrate.elluciancloud.com/publish")
			.post(publishPayload)
			.addHeader("authorization", jwt)
			.addHeader("content-type", "application/vnd.hedtech.change-notifications.v2+json")
			.build();
		Response publishResponse = client.newCall(publishRequest).execute();
		int status = publishResponse.code();
		System.out.println("Publish response status: " + status);
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
