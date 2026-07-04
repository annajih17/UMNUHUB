package com.example.umnu_hub;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public abstract class VolleyMultipartRequest extends Request<NetworkResponse> {

    private final Response.Listener<NetworkResponse> listener;
    private final String boundary = "apiclient-" + System.currentTimeMillis();

    public VolleyMultipartRequest(
            int method,
            String url,
            Response.Listener<NetworkResponse> listener,
            Response.ErrorListener errorListener) {

        super(method, url, errorListener);
        this.listener = listener;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response,
                HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        listener.onResponse(response);
    }

    protected abstract Map<String,String> getParams();

    protected abstract Map<String,DataPart> getByteData();

    @Override
    public byte[] getBody() throws AuthFailureError {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {

            if(getParams()!=null){

                for(Map.Entry<String,String> entry : getParams().entrySet()){

                    dos.writeBytes("--"+boundary+"\r\n");

                    dos.writeBytes(
                            "Content-Disposition: form-data; name=\""
                                    +entry.getKey()+"\"\r\n\r\n");

                    dos.writeBytes(entry.getValue());

                    dos.writeBytes("\r\n");
                }
            }

            if(getByteData()!=null){

                for(Map.Entry<String,DataPart> entry : getByteData().entrySet()){

                    DataPart dataPart = entry.getValue();

                    dos.writeBytes("--"+boundary+"\r\n");

                    dos.writeBytes(
                            "Content-Disposition: form-data; name=\""
                                    +entry.getKey()
                                    +"\"; filename=\""
                                    +dataPart.fileName
                                    +"\"\r\n");

                    dos.writeBytes("Content-Type: image/jpeg\r\n\r\n");

                    dos.write(dataPart.content);

                    dos.writeBytes("\r\n");
                }
            }

            dos.writeBytes("--"+boundary+"--");

        } catch(IOException e){
            e.printStackTrace();
        }

        return bos.toByteArray();
    }

    public static class DataPart{

        String fileName;
        byte[] content;

        public DataPart(String fileName, byte[] content){

            this.fileName=fileName;
            this.content=content;

        }
    }
}