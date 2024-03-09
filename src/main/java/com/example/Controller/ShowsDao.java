package com.example.Controller;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;

import com.example.Entity.Shows;
import com.example.Util.Dao;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ShowsDao {
    public static Object showsMain(HttpServletRequest request) throws Exception {
        if (request.getMethod().equals("GET"))
            return ShowsDao.get(request);
        else if (request.getMethod().equals("POST"))
            return (Integer) ShowsDao.post(request);
        else if (request.getMethod().equals("PUT"))
            return (Integer) ShowsDao.put(request);
        return null;
    }

    public static JSONArray get(HttpServletRequest request) throws Exception {
        JSONArray jsonArray = new JSONArray();
        String[] uriEndPoints = request.getRequestURI().substring(1).split("/");
        String[] query = new String[] {};
        ArrayList<String> columnName = new ArrayList<>();
        boolean joinFlag = false;
        boolean seatFlag = false;
        boolean available =false;
        boolean addMovie = false;
        int length = uriEndPoints.length;
        LinkedHashMap<String, String[]> queryParams = new LinkedHashMap<>();
        try {
            if (request.getRequestURI() != null) {
                if (request.getQueryString().contains("listallseats")) {
                    seatFlag = true;
                } else if( request.getQueryString().contains("getdetails")){
                    available=true;
                }
                else if(request.getQueryString().contains("includemovies")){
                   addMovie = true;
                }
                else if (length == 6) {
                    joinFlag = true;
                } else {
                    query = request.getQueryString().split("=");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (uriEndPoints.length == 3 || uriEndPoints.length == 6) {
            if (query.length % 2 == 0 && query.length != 0) {

                for (int i = 0; i < query.length; i = i + 2) {
                    queryParams.put(query[i], new String[] { query[i + 1] });
                }
            }
        } else {
            if (uriEndPoints.length == 5)
                queryParams.put("theatreid", new String[] { uriEndPoints[3] });
            else
                queryParams.put("showid", new String[] { uriEndPoints[3] });
        }
        if(available){
            queryParams.put("theatreid", new String[] {uriEndPoints[3]});
            jsonArray=Dao.selectwithJoins(new String[]{"shows","seatsmapping"}, new String[]{"showid"}, columnName, queryParams);
            return jsonArray;

        }
        if (seatFlag) {
            columnName.add("tierid");
            columnName.add("ticketprice");
            queryParams.put("showid", new String[] { uriEndPoints[5] });
            jsonArray = Dao.selectwithJoins(new String[] { "tierseatavailability", "shows" },
                    new String[] { "theatreid" }, columnName, queryParams);
            columnName.remove("tierid");
            columnName.remove("ticketprice");
            queryParams.remove("showid");
            String[] qp = new String[3];
            int j=0;
            for (int i = 0; i < jsonArray.length(); i++) {
                org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
                qp[j++] = String.valueOf(jsonObject.get("tierid"));
            }
            queryParams.put("tierid", qp);
            JSONArray tier = jsonArray;
            jsonArray = Dao.selectFromTable("seatdetails", columnName, queryParams);
            for (int i = 0; i < jsonArray.length(); i++) {
                for ( j = 0; j < tier.length(); j++) {
                    org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
                    org.json.JSONObject js = tier.getJSONObject(j);
                    if (js.get("tierid") == jsonObject.get("tierid")) {
                        jsonArray.getJSONObject(i).put("ticketprice", tier.getJSONObject(j).get("ticketprice"));
                    }
                }
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonObject.put("seatAvailable", true);
                jsonObject.put("isSelected",false);
            }
            JSONArray js = new JSONArray();
            queryParams.remove("tierid");
            queryParams.put("showid", new String[] { uriEndPoints[5] });
            js = Dao.selectFromTable("seatsmapping", columnName, queryParams);
            for (int i = 0; i < jsonArray.length(); i++) {
                for (j = 0; j < js.length(); j++) {
                    org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
                    org.json.JSONObject jsObj = js.getJSONObject(j);
                    if ((int)jsObj.get("seatid") == (int)jsonObject.get("seatid")) {
                        jsonArray.getJSONObject(i).put("seatAvailable", false);
                    }
                }
            }
            return jsonArray;
        } 
        else if(addMovie){
            HashMap<String,String[]> params = new HashMap<>();
            params.put("theatreid", new String[]{uriEndPoints[3]});
            jsonArray=Dao.selectwithJoins(new String[]{"shows","movies"},new String[]{"movieid"}, columnName, params);
            return jsonArray;
        }
        
        else if (joinFlag) {
            queryParams.put("theatreid", new String[] { uriEndPoints[3] });
            jsonArray = Dao.selectwithJoins(new String[] { uriEndPoints[3], uriEndPoints[5] },
                    new String[] { "theatreid" }, columnName, queryParams);
        } else
            jsonArray = Dao.selectFromTable("shows", columnName, queryParams);
        return jsonArray;
    }

    public static int post(HttpServletRequest request) throws Exception {
        int rowsAffected = 0;
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        ObjectMapper obj = new ObjectMapper();
        Shows ShowObject = obj.readValue(sb.toString(), Shows.class);
        Class<?> PojoClass = ShowObject.getClass();
        Field[] fields = PojoClass.getDeclaredFields();
        LinkedHashMap<String, Object> tablevalues = new LinkedHashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldValue;
            try {
                fieldValue = field.get(ShowObject);
            } catch (IllegalAccessException e) {
                fieldValue = "Error accessing field";
            }
            if (fieldName != "showid")
                tablevalues.put(fieldName, fieldValue);

        }
        rowsAffected = Dao.insertToTable("shows", tablevalues);
        return rowsAffected;
    }

    public static int put(HttpServletRequest request) throws Exception {
        int rowsAffected = 0;
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        ObjectMapper obj = new ObjectMapper();
        Shows showsObject = obj.readValue(sb.toString(), Shows.class);
        Class<?> PojoClass = showsObject.getClass();
        Field[] fields = PojoClass.getDeclaredFields();
        LinkedHashMap<String, Object> tablevalues = new LinkedHashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldValue;
            try {
                fieldValue = field.get(showsObject);
            } catch (IllegalAccessException e) {
                fieldValue = "Error accessing field";
            }
            if (fieldValue == null || (fieldValue instanceof Integer && ((Integer) fieldValue).intValue() == 0))
                continue;
            else
                tablevalues.put(fieldName, fieldValue);

        }
        HashMap<String, Object> conditionParams = new HashMap<>();
        String action = request.getRequestURI();
        String[] uriEndPoints = action.substring(1).split("/");
        conditionParams.put("showid", Integer.parseInt(uriEndPoints[3]));
        rowsAffected = Dao.updateTheTable("shows", tablevalues, conditionParams);
        return rowsAffected;
    }
}
