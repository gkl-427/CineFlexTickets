package com.example.Controller;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.Entity.Theatres;
import com.example.Util.Dao;
import com.example.Util.DbConnection;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TheatresDao {
    public static Object theatresMain(HttpServletRequest request) throws Exception {
        if (request.getMethod().equals("GET"))
            return TheatresDao.get(request);
        else if (request.getMethod().equals("POST"))
            return (Integer) TheatresDao.post(request);
        return null;
    }

    public static JSONArray get(HttpServletRequest request) throws Exception {
        JSONArray jsonArray = new JSONArray();
        String[] uriEndPoints = request.getRequestURI().substring(1).split("/");
        String[] query = new String[] {};
        ArrayList<String> columnName = new ArrayList<>();
        boolean join =false;
        LinkedHashMap<String, String[]> queryParams = new LinkedHashMap<>();
        try {
            if (request.getQueryString() != null) {
                if(request.getQueryString().contains("includeusers")){
                        join=true;
                }
                query = request.getQueryString().split("=");
            }
        } catch (Exception e) {
            request.setAttribute("X-Error", "Error Message: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        if (uriEndPoints.length == 3) {
            if (query.length % 2 == 0 && query.length != 0) {
                for (int i = 0; i < query.length; i = i + 2) {
                    queryParams.put(query[i], new String[] { query[i + 1] });
                }
            }
        } else {
            queryParams.put("theatreid", new String[] { uriEndPoints[3] });
        }
        if(join){
            jsonArray=Dao.selectwithJoins(new String[]{"theatres","users"}, new String[]{"userid"}, columnName,queryParams);
            for(int i=0;i<jsonArray.length();i++){
                jsonArray.getJSONObject(i).remove("password");
            }
            return jsonArray;
        }
        jsonArray = Dao.selectFromTable("theatres", columnName, queryParams);
        return jsonArray;
    }

    public static int post(HttpServletRequest request) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        HashMap<String,Object> conditionParams= new HashMap<>();
        LinkedHashMap<String,Object> setParams=new LinkedHashMap<>();
        reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
            
        if (request.getQueryString()!=null && request.getQueryString().contains("addseats")) {
            reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject js=new JSONObject(sb.toString());
            int t1price = Integer.parseInt(js.getString("tieroneprice"));
            int t2price = Integer.parseInt(js.getString("tiertwoprice"));
            int t3price = Integer.parseInt(js.getString("tierthreeprice"));
            String query = "SELECT insert_multiple_rows(?, ?, ?)";
            Connection con = null;
            PreparedStatement stmt = null;
            try {
                con = DbConnection.connect_to_db();
                stmt = con.prepareStatement(query);
                stmt.setInt(1, t1price);
                stmt.setInt(2, t2price);
                stmt.setInt(3, t3price);
                stmt.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                con.close();
                stmt.close();
            }
            return 1;
        }
        ObjectMapper obj = new ObjectMapper();
        Theatres TheatreObject = obj.readValue(sb.toString(), Theatres.class);
        Class<?> PojoClass = TheatreObject.getClass();
        Field[] fields = PojoClass.getDeclaredFields();
        LinkedHashMap<String, Object> tablevalues = new LinkedHashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldValue;
            try {
                fieldValue = field.get(TheatreObject);
            } catch (IllegalAccessException e) {
                fieldValue = "Error accessing field";
            }
            if (fieldName != "theatreid")
                tablevalues.put(fieldName, fieldValue);
            if(fieldName=="userid")
                conditionParams.put("userid",fieldValue);
        }
        setParams.put("role",2);
        Dao.updateTheTable("users", setParams, conditionParams);
        Dao.insertToTable("theatres", tablevalues);
        JSONArray jsonArray;
        ArrayList<String> columnName = new ArrayList<>();
        LinkedHashMap<String, String[]> queryParams = new LinkedHashMap<>();
        jsonArray = Dao.selectFromTable("theatres", columnName, queryParams);
        int theatreid = 0;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jso = jsonArray.getJSONObject(i);
            theatreid = (int) jso.get("theatreid");
        }
        return theatreid;
    }

}
