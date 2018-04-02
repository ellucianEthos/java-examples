package com.ellucian.integration.proxy;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PostExample2 {

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
		
		//POST a buildings resources
		MediaType type = MediaType.parse("application/vnd.hedtech.integration.v8+json");
		RequestBody proxyPayload = RequestBody.create(type, "{ \"id\": \"00000000-0000-0000-0000-000000000000\", \"title\": \"new building\" }");
		Request proxyRequest = new Request.Builder()
			.url("https://integrate.elluciancloud.com/api/buildings")
			.post(proxyPayload)
			.addHeader("authorization", jwt)
			.addHeader("content-type", "application/vnd.hedtech.integration.v8+json")
			.build();
		Response proxyResponse = client.newCall(proxyRequest).execute();
		System.out.println(proxyResponse.body().string());
	}

}
