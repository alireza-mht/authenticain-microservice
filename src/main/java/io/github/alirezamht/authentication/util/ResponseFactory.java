package io.github.alirezamht.authentication.util;

import org.json.simple.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ResponseFactory {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public static JSONObject getErrorResponse(int status,String error,String message,String path){

        JSONObject response=new JSONObject();
        response.put("timestamp",getTimestamp());
        response.put("status",status);
        response.put("error",error);
        response.put("message",message);
        response.put("path",path);
        return response;
    }

    private static String getTimestamp() {

        Timestamp timestamp=new Timestamp(System.currentTimeMillis());

        return sdf.format(timestamp);
    }

    public static JSONObject getSuccessResponse(int status,JSONObject result){
        JSONObject response=new JSONObject();

        response.put("timestamp",getTimestamp());
        response.put("status",status);
        if(result!=null)
            response.put("result",result);
        return response;

    }
}


