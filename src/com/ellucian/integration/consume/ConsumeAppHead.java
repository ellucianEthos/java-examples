package com.ellucian.integration.consume;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConsumeAppHead {

    public static void main(String[] args) throws ClientProtocolException, IOException, InterruptedException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String apiKey = "API KEY";
        String jwt = "";
        int statusCode = 401;

        //get new token
        HttpPost httpPost = new HttpPost("https://integrate.elluciancloud.com/auth");
        httpPost.addHeader("authorization", "Bearer " + apiKey);
        httpPost.addHeader("cache-control", "no-cache");
        CloseableHttpResponse tokenResponse = httpclient.execute(httpPost);

        try {
            HttpEntity entity = tokenResponse.getEntity();
            jwt = "Bearer " + EntityUtils.toString(entity);
            System.out.println(jwt);
        } finally {
            tokenResponse.close();
        }

        HttpHead HttpHead = new HttpHead("https://integrate.elluciancloud.com/consume");
        HttpHead.addHeader("authorization", jwt);
        HttpHead.addHeader("cache-control", "no-cache");
        CloseableHttpResponse consumeResponse = httpclient.execute(HttpHead);

        try {
            statusCode = consumeResponse.getStatusLine().getStatusCode();
            Header[] remainHeader = consumeResponse.getHeaders("x-remaining");
            System.out.println(remainHeader[0]);
        } finally {
            consumeResponse.close();
        }


    }

}