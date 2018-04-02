package com.ellucian.integration.consume;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConsumeApp2 {

	public static void main(String[] args) throws IOException, InterruptedException {
		int statusCode = 401;
        String jwt = "";
        OkHttpClient client = new OkHttpClient();
        RequestBody payload = RequestBody.create(null, ""); // okhttp requires a payload for POSTs, however the /auth endpoint takes a null payload, which is created here.

        /*  THE FOLLOWING IS REQUIRED FOR ANY POST/PUT ROUTES WHICH REQUIRE A VALID PAYLOAD

            MediaType JSON = MediaType.parse("application/vnd.hedtech.integration.v3+json; charset=utf-8"); // Creates the appropriate Content-Type, which will often be in this json form
            
            RequestBody payload = RequestBody.create(JSON, "{{Payload as String}}"); EXAMPLE: "{\"resource\": {\"name\": \"academic-catalogs\",\"id\": \"00000000-0000-0000-0000-000000000000\",\"version\"\"application/vnd.hedtech.integration.v3+json\"},\"operation\": \"created\"}"

            Pass the payload in as you would in the sample application
        */

        while (true) {
            if (statusCode != 200) {
                Request createToken = new Request.Builder()
                    .url("https://integrate.elluciancloud.com/auth")
                    .post(payload)
                    .addHeader("authorization", "Bearer API KEY") // The authorization here is an API key from an application
                    .addHeader("cache-control", "no-cache")
                    .build();

                Response responseToken = client.newCall(createToken).execute();
                jwt = "Bearer " + responseToken.body().string(); // responseToken.body().string() returns the response JWT as a usable string.
                System.out.println(jwt);
            }

            Request consume = new Request.Builder()
                .url("https://integrate.elluciancloud.com/consume")
                .get()
                .addHeader("authorization", jwt) // The authorization here is the JWT from the created token above.
                .addHeader("cache-control", "no-cache")
                .build();

            Response consumeResponse = client.newCall(consume).execute();
            statusCode = consumeResponse.code(); //Returns the status code of the response
            System.out.println(consumeResponse.body().string());

            Thread.sleep(10000);
        }

	}

}
