package api.generics;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiRequest implements Runnable {
    private final static String BASE_URL = "https://quizAppApi.uwuwhatsthis.de";

    private String endpoint;
    private String body;
    private RequestType requestType;
    private RequestCallback callback;
    private Map<String, String> headerMap;

    private ThreadFactory factory;

    public ApiRequest(){
        this.headerMap = null;
    }

    public ApiRequest(String endpoint, String json, RequestType requestType, RequestCallback callback){
        this.endpoint = endpoint;
        this.body = json;
        this.requestType = requestType;
        this.callback = callback;
    }

    public void run(){
        OkHttpClient client = new OkHttpClient();

        Request request;

        String requestUrl = BASE_URL + endpoint;

        if (body == null){
            body = "{}";
        }

        RequestBody requestBody = RequestBody.create(body, MediaType.get("application/json; charset=utf-8"));

        switch (requestType) {
            case GET:
                Request.Builder getBuilder = new Request.Builder()
                        .url(requestUrl)
                        .get();

                if (headerMap != null){
                    for (String key: this.headerMap.keySet()){
                        getBuilder.addHeader(key, headerMap.get(key));
                    }
                }

                request = getBuilder.build();

                break;

            case POST:


                Request.Builder postBuilder = new Request.Builder()
                        .url(requestUrl)
                        .post(requestBody);

                if (headerMap != null){
                    for (String key: this.headerMap.keySet()){
                        postBuilder.addHeader(key, headerMap.get(key));
                    }
                }

                request = postBuilder.build();

                break;

            default:
                throw new IllegalStateException("Unexpected value: " + requestType);
        }

        try(Response response = client.newCall(request).execute()){
            this.callback.execute(new ApiResponse(response));
        } catch (IOException e) {
            e.printStackTrace();
            this.callback.execute(new ApiResponse(null));
        }
    }

    public void doRequest(){
        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(this);
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public RequestCallback getCallback() {
        return callback;
    }

    public void setCallback(RequestCallback callback) {
        this.callback = callback;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public void addHeader(String key, String value){
        if (this.headerMap == null){
            this.headerMap = new HashMap<>();
        }

        this.headerMap.put(key, value);
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}
