package com.ellucian.integration.proxy;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeleteExample2 {

	public static void main(String[] args) throws IOException {
		OkHttpClient client = new OkHttpClient();
		String jwt = "";

		//change these variables to match your data
		String apiKey = "API KEY";
		String id = "a2065aa3-bfe2-48d5-b742-453c2d53da91";
		
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
		
		//DELETE a person-holds resources
		Request proxyRequest = new Request.Builder()
			.url("https://integrate.elluciancloud.com/api/person-holds/" + id)
			.delete()
			.addHeader("authorization", jwt)
			.build();
		Response proxyResponse = client.newCall(proxyRequest).execute();
		System.out.println("Delete Status Code: " + proxyResponse.networkResponse().code());
	}

}
