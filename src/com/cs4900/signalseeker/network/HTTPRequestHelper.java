package com.cs4900.signalseeker.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs4900.signalseeker.Constants;
/**
 * Wrapper to help make HTTP requests easier - after all, we want to make it nice for the people.
 * 
 */
public class HTTPRequestHelper {

    private static final String CLASSTAG = HTTPRequestHelper.class.getSimpleName();

    private static final int POST_TYPE = 1;
    private static final int GET_TYPE = 2;
    private static final int PUT_TYPE = 3;
    private static final int DELETE_TYPE = 4;
    private static final String CONTENT_TYPE = "Content-Type";
    public static final String MIME_FORM_ENCODED = "application/x-www-form-urlencoded";
    public static final String MIME_TEXT_PLAIN = "text/plain";

    private final ResponseHandler<String> responseHandler;
    
    // By Xu
    // Important: this is to implement session control
    private static DefaultHttpClient client = new DefaultHttpClient();  
      
    
    public HTTPRequestHelper(final ResponseHandler<String> responseHandler) {
        this.responseHandler = responseHandler;
    }

    /**
     * Perform an HTTP GET operation.
     * 
     */
    public void performGet(final String url, final String user, final String pass,
        final Map<String, String> additionalHeaders) {
        performRequest(null, url, user, pass, additionalHeaders, null, HTTPRequestHelper.GET_TYPE);
    }

    /**
     * Perform an HTTP POST operation with specified content type.
     * 
     */
    public void performPost(final String contentType, final String url, final String user, final String pass,
        final Map<String, String> additionalHeaders, final Map<String, String> params) {
        performRequest(contentType, url, user, pass, additionalHeaders, params, HTTPRequestHelper.POST_TYPE);
    }

    /**
     * Perform an HTTP POST operation with a default conent-type of
     * "application/x-www-form-urlencoded."
     * 
     */
    public void performPost(final String url, final String user, final String pass,
        final Map<String, String> additionalHeaders, final Map<String, String> params) {
        performRequest(HTTPRequestHelper.MIME_FORM_ENCODED, url, user, pass, additionalHeaders, params,
            HTTPRequestHelper.POST_TYPE);

    }

    /**
     * Perform an HTTP PUT operation with specified content type.
     * 
     */
    public void performPut(final String contentType, final String url, final String user, final String pass,
        final Map<String, String> additionalHeaders, final Map<String, String> params) {
        performRequest(contentType, url, user, pass, additionalHeaders, params, HTTPRequestHelper.PUT_TYPE);
    }
    
    /**
     * Perform an HTTP Delete operation with specified content type.
     * 
     */
    public void performDelete(final String contentType, final String url, final String user, final String pass,
        final Map<String, String> additionalHeaders, final Map<String, String> params) {
        performRequest(contentType, url, user, pass, additionalHeaders, params, HTTPRequestHelper.DELETE_TYPE);
    }
    
    /**
     * Private heavy lifting method that performs GET or POST with supplied url, user, pass, data,
     * and headers.
     * 
     * @param contentType
     * @param url
     * @param user
     * @param pass
     * @param headers
     * @param params
     * @param requestType
     */
    private void performRequest(final String contentType, final String url, final String user, final String pass,
        final Map<String, String> headers, final Map<String, String> params, final int requestType) {

        Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " making HTTP request to url - " + url);

        // establish HttpClient
        //DefaultHttpClient client = new DefaultHttpClient();       

        // add user and pass to client credentials if present
        if ((user != null) && (pass != null)) {
            Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG
                + " user and pass present, adding credentials to request.");
            client.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, pass));
        }
        
        // process headers using request interceptor
        final Map<String, String> sendHeaders = new HashMap<String, String>();
        if ((headers != null) && (headers.size() > 0)) {
            sendHeaders.putAll(headers);
        }
        if (requestType == HTTPRequestHelper.POST_TYPE || requestType == HTTPRequestHelper.PUT_TYPE) {
            sendHeaders.put(HTTPRequestHelper.CONTENT_TYPE, contentType);
        }
        if (sendHeaders.size() > 0) {
            client.addRequestInterceptor(new HttpRequestInterceptor() {

                public void process(final HttpRequest request, final HttpContext context) throws HttpException,
                    IOException {
                    for (String key : sendHeaders.keySet()) {
                        if (!request.containsHeader(key)) {
                            Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " adding header: " + key + " | "
                                + sendHeaders.get(key));
                            request.addHeader(key, sendHeaders.get(key));
                        }
                    }
                }
            });
        }

        // handle POST or GET or PUT request respectively
        if (requestType == HTTPRequestHelper.POST_TYPE) {
            Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " performRequest POST");
            HttpPost method = new HttpPost(url);

            // data - name/value params
            List<NameValuePair> nvps = null;
            if ((params != null) && (params.size() > 0)) {
                nvps = new ArrayList<NameValuePair>();
                for (String key : params.keySet()) {
                    Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " adding param: " + key + " | "
                        + params.get(key));
                    nvps.add(new BasicNameValuePair(key, params.get(key)));
                }
            }
            
            if ((user != null) && (pass != null)) {
        		nvps.add(new BasicNameValuePair("name", user));
        		nvps.add(new BasicNameValuePair("password", pass));
        	}
            
            if (nvps != null) {
                try {
                    method.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
                } catch (UnsupportedEncodingException e) {
                    Log.e(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG, e);
                }
            }
            execute(client, method);            
        } else if (requestType == HTTPRequestHelper.PUT_TYPE) {
            Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " performRequest PUT");
            HttpPut method = new HttpPut(url);

            // data - name/value params
            List<NameValuePair> nvps = null;
            if ((params != null) && (params.size() > 0)) {
                nvps = new ArrayList<NameValuePair>();
                for (String key : params.keySet()) {
                    Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " adding param: " + key + " | "
                        + params.get(key));
                    nvps.add(new BasicNameValuePair(key, params.get(key)));
                }
            }
            if (nvps != null) {
                try {
                    method.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
                } catch (UnsupportedEncodingException e) {
                    Log.e(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG, e);
                }
            }
            execute(client, method);            
        } 
        else if (requestType == HTTPRequestHelper.DELETE_TYPE) {
            Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " performRequest DELETE");
            HttpDelete method = new HttpDelete(url);
            execute(client, method);
        } else 	if (requestType == HTTPRequestHelper.GET_TYPE) {
            Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " performRequest GET");
            HttpGet method = new HttpGet(url);
            execute(client, method);
        }
    }
    
    /**
     * Once the client and method are established, execute the request. 
     * 
     * @param client
     * @param method
     */
    private void execute(HttpClient client, HttpRequestBase method) {
        Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " execute invoked");
        
        // create a response specifically for errors (in case)
        BasicHttpResponse errorResponse = 
            new BasicHttpResponse(new ProtocolVersion("HTTP_ERROR", 1, 1), 500, "ERROR");
        
        try {
            client.execute(method, this.responseHandler);
            Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " request completed");
        } catch (Exception e) {
            Log.e(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG, e);
            errorResponse.setReasonPhrase(e.getMessage());
            try {
                this.responseHandler.handleResponse(errorResponse);
            } catch (Exception ex) {
                Log.e(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG, ex);
            }
        }
    }
    

    /**
     * Static utility method to create a default ResponseHandler that sends a Message to the passed
     * in Handler with the response as a String, after the request completes.
     * 
     * @param handler
     * @return
     */
    public static ResponseHandler<String> getResponseHandlerInstance(final Handler handler) {
        final ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

            public String handleResponse(final HttpResponse response) {
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                StatusLine status = response.getStatusLine();
                Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " statusCode - " + status.getStatusCode());
                Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " statusReasonPhrase - "
                    + status.getReasonPhrase());
                HttpEntity entity = response.getEntity();
                String result = null;
                if (entity != null) {
                    try {
                        result = StringUtils.inputStreamToString(entity.getContent());
                        bundle.putString("RESPONSE", result);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    } catch (IOException e) {
                        Log.e(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG, e);
                        bundle.putString("RESPONSE", "Error - " + e.getMessage());
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                } else {
                    Log.w(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG
                        + " empty response entity, HTTP error occurred");
                    bundle.putString("RESPONSE", "Error - " + response.getStatusLine().getReasonPhrase());
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
                return result;
            }
        };
        return responseHandler;
    }
}
