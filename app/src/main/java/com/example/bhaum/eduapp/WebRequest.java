package com.example.bhaum.eduapp; /**
 * Created by CFILT on 3/5/2018.
 */

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class WebRequest {


    public final static int GET = 1;
    public final static int POST = 2;
    private String UrlForToken = "auth/token";
    private Context context;
    private String apiUrl;


    public WebRequest(Context context) {
        this.context = context;
        apiUrl = context.getString(R.string.url) + "auth/token";
        UrlForToken = apiUrl + UrlForToken;
    }

    /**
     * Making web service call
     *
     * @url - url to make request
     * @requestmethod - http request method
     */
    public String makeWebServiceCall(String url, int reqmethod) {

        return this.makeWebServiceCall(url, reqmethod, null);
    }

    /**
     * Making service call
     *
     * @url - url to make request
     * @requestmethod - http request method
     * @params - http request params
     */
    public String makeWebServiceCall(String urladdress, int reqmethod, LinkedHashMap<String, String> params) {
        URL url;
        HttpURLConnection conn = null;
        StringBuilder response = new StringBuilder();
        try {
            url = new URL(urladdress);

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("authorization", "JWT " + this.fetchToken());
            if (reqmethod == POST) {
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                if (params != null) {
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                    StringBuilder result = new StringBuilder();
                    boolean first = true;
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        if (first)
                            first = false;
                        else
                            result.append("&");

                        result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                        result.append("=");
                        result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                        result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                    }

                    writer.write(result.toString());
                    writer.flush();
                    writer.close();
                    os.close();
                }
            } else if (reqmethod == GET) {
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
            }

            int responseCode = conn.getResponseCode();


            Log.d("response code", "Response code = " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

            } else {
                response = new StringBuilder("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }

        return response.toString();
    }

    /*
    * Fetch Token for authentication
    * @username is admin
    * @password is cfilt1234
    */
    public String fetchToken() {
        URL url;
        String urladdress = this.UrlForToken;
        HttpURLConnection conn = null;
        StringBuilder response = new StringBuilder();
        JSONObject postDataParams = new JSONObject();
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("username", "prit123");
        params.put("password", "Hello123");
        try {
            url = new URL(urladdress);

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            if (params != null) {
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                StringBuilder result = new StringBuilder();
                boolean first = true;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (first)
                        first = false;
                    else
                        result.append("&");

                    result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                }
                writer.write(result.toString());
                writer.flush();
                writer.close();
                os.close();

            }
            int responseCode = conn.getResponseCode();

            Log.d("response", responseCode + "");
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                if (response.charAt(response.length() - 1) != '}') response.append("}");
                JSONObject jsonObject = new JSONObject(response.toString());
                br.close();
                conn.disconnect();
                String token = jsonObject.getString("token");
                //   Log.d("token",token);
                return token;
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d("unsupp", "exc");
        } catch (ProtocolException e) {
            e.printStackTrace();
            Log.d("protocol", "exc");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d("malformed", "exc");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Ioexc", "exc");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }
}

