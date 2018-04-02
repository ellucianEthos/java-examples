package com.ellucian.integration.proxy;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.apache.http.client.ClientProtocolException;

public class GetExample2 {

	public static void main(String[] args) throws ClientProtocolException, IOException {
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
		
		//get buildings resources
		Request proxyRequest = new Request.Builder()
			.url("https://integrate.elluciancloud.com/api/buildings")
			.get()
			.addHeader("authorization", jwt)
			.addHeader("accept", "application/json")
			.build();
		Response proxyResponse = client.newCall(proxyRequest).execute();
		System.out.println(proxyResponse.body().string());
	}

}
