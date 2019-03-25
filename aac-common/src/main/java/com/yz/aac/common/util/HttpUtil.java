package com.yz.aac.common.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.yz.aac.common.exception.RpcException;

@SuppressWarnings("unused")
public class HttpUtil {

    public static OkHttpClient createClient() {
        return new OkHttpClient();
    }

    public static Pair<Boolean, String> get(OkHttpClient client, String url, Map<String, Object> params) throws RpcException {
        Pair<Boolean, String> result;
        StringBuilder sb = new StringBuilder(url);
        if (null != params) {
            Iterator<String> it = params.keySet().iterator();
            int index = 0;
            while (it.hasNext()) {
                String key = it.next();
                Object value = params.get(key);
                sb.append(String.format("%s%s=%s", index == 0 ? "?" : "&", key, value));
                index++;
            }
        }
        Request request = new Request.Builder().url(sb.toString()).build();
        try (Response response = client.newCall(request).execute()) {
            result = new ImmutablePair<>(
                    response.isSuccessful(),
                    null != response.body() ? response.body().string() : null
            );
        } catch (IOException e) {
            throw new RpcException(e);
        }
        return result;
    }

    public static Pair<Boolean, String> post(OkHttpClient client, String contentType, String url, String body, Headers headers) throws RpcException {
        Pair<Boolean, String> result;
        MediaType mediaType = MediaType.parse(contentType);
        RequestBody requestBody = RequestBody.create(mediaType, body);
        Builder builder = new Request.Builder().url(url).post(requestBody);
        
        if (null != headers) {
        	builder.headers(headers);
        }
        
        Request request = builder.build();
        
        try (Response response = client.newCall(request).execute()) {
            result = new ImmutablePair<>(
                    response.isSuccessful(),
                    null != response.body() ? response.body().string() : null
            );
        } catch (IOException e) {
            throw new RpcException(e);
        }
        return result;
    }
}
