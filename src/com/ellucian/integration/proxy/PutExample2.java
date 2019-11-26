package com.ellucian.integration.proxy;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PutExample2 {

	public static void main(String[] args) throws IOException {
		OkHttpClient client = new OkHttpClient();
		String jwt = "";

		//change these variables to match your data
		String apiKey = "API KEY";
		String id = "8734be28-9f7e-4d31-963f-654712f37b4b";
		String personId = "080c43e1-1782-4ed9-98cf-1aa68810b179";
		String detailId = "04664c5f-bac6-43bf-abe4-ac785be86416";

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
		
		//PUT a person-holds resource
		MediaType type = MediaType.parse("application/vnd.hedtech.integration.v6+json");
		RequestBody proxyPayload = RequestBody.create(type, "{\"id\":\"" + id + "\",\"endOn\":\"2019-12-31T04:00:00Z\",\"person\":{\"id\":\"" + personId +
				"\"},\"startOn\":\"2012-03-30T04:00:00Z\",\"type\":{\"category\":\"academic\",\"detail\":{\"id\":\"" + detailId + "\"}}}");
		Request proxyRequest = new Request.Builder()
			.url("https://integrate.elluciancloud.com/api/person-holds/" + id)
			.post(proxyPayload)
			.addHeader("authorization", jwt)
			.addHeader("content-type", "application/vnd.hedtech.integration.v6+json")
			.addHeader("accept", "application/vnd.hedtech.integration.v6+json")
			.build();
		System.out.println(proxyRequest.url());
		Response proxyResponse = client.newCall(proxyRequest).execute();
		System.out.println(proxyResponse.body().string());
	}

}
