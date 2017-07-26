package com.bokun.bkjcb.chengtou.Http;

import com.bokun.bkjcb.chengtou.Domain.Detail;
import com.bokun.bkjcb.chengtou.Domain.JsonResult;
import com.bokun.bkjcb.chengtou.Domain.Result;
import com.bokun.bkjcb.chengtou.Util.L;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

/**
 * Created by BKJCB on 2017/3/16.
 */

public class JsonParser {
    public static JsonResult parseSoap(SoapObject object) {
        JsonResult result = new JsonResult();
        if (object == null) {
            return result;
        }
        L.i(object.toString());
        SoapObject detail = (SoapObject) object.getProperty(0);
        String content = XmlParser.parseSoapObject(detail);
        if (content.length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(content);
                result.success = jsonObject.getBoolean("success");
                result.message = jsonObject.getString("message");
                try {
                    result.resData = jsonObject.getString("data");
                } catch (JSONException e) {
                    result.resData = "";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            result.success = false;
            result.message = "请求出错，请稍后再试！";
        }
        return result;
    }

    public static String parseJSON(String result,String name) {
        String json = null;
        try {
            JSONObject object = new JSONObject(result);
            json = object.get(name).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static ArrayList<Result> getResultData(String json) {
        L.i(json);
        json = parseJSON(json,"GetyearResult");
        L.i(json);
        ArrayList<Result> results = new ArrayList<>();
        if (json.equals("{}")) {
            return results;
        }
        //将JSON的String 转成一个JsonArray对象
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement element : array) {
            Result result = gson.fromJson(element, Result.class);
            results.add(result);
        }
        return results;
    }
 public static ArrayList<Detail> getResultDetail(String json) {
//        L.i(json);
        json = parseJSON(json,"GetxinxichaxunResult");
        L.i(json);
        ArrayList<Detail> results = new ArrayList<>();
        if (json.equals("{}")) {
            return results;
        }
        //将JSON的String 转成一个JsonArray对象
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement element : array) {
            Detail result = gson.fromJson(element, Detail.class);
            results.add(result);
        }
        return results;
    }

    public static ArrayList<String> getYear(String json) {
        L.i(json);
        if (json.equals("{}")) {
            return null;
        }
        json = parseJSON(json,"GetyearResult");
        ArrayList<String> strings = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = (JSONObject) array.opt(i);
                try {
                    int year = (int) object.get("year");
                    strings.add(String.valueOf(year));
                } catch (ClassCastException e) {
                    String year = object.getString("year");
                    strings.add(year);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return strings;
    }
}
