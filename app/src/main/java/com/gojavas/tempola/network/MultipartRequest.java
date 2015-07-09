package com.gojavas.tempola.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

/**
 * Created by GJS280 on 8/7/2015.
 */
public class MultipartRequest extends Request<String> {

    private MultipartEntity entity = new MultipartEntity();

    private static final String FILE_PART_NAME = "picture";
    private static final String STRING_PART_NAME = "text";

    private final Response.Listener<String> mListener;
    private final File mFilePart;
//    private final String mStringPart;
    private final JSONObject mJsonRequest;

    public MultipartRequest(String url, Response.ErrorListener errorListener, Response.Listener<String> listener, File file, JSONObject jsonRequest) {
        super(Method.POST, url, errorListener);

        mListener = listener;
        mFilePart = file;
//        mStringPart = stringPart;
        mJsonRequest = jsonRequest;
        buildMultipartEntity();
    }

    private void buildMultipartEntity() {
        if(mFilePart != null) {
            entity.addPart(FILE_PART_NAME, new FileBody(mFilePart));
        }
        try {
//            entity.addPart(STRING_PART_NAME, new StringBody(mStringPart));
            Iterator<String> iterator = mJsonRequest.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                try {
                    String value = (String) mJsonRequest.get(key);
                    entity.addPart(key, new StringBody(value));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            VolleyLog.e("UnsupportedEncodingException");
        }
    }

    @Override
    public String getBodyContentType() {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String json;
        try {
            json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
        return Response.success(json.toString(), getCacheEntry());
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}
