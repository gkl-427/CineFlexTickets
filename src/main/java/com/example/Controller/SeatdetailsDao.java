package com.example.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;

import com.example.Entity.SeatDetails;
import com.example.Util.Dao;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SeatdetailsDao {
    public static Object seatdetailsMain(HttpServletRequest request) throws Exception {
        if (request.getMethod().equals("GET"))
            return SeatdetailsDao.get(request);
        else if (request.getMethod().equals("POST"))
            return (Integer) SeatdetailsDao.post(request);
        return null;
    }

    public static JSONArray get(HttpServletRequest request) throws Exception {
        String[] uriEndPoints = request.getServletPath().substring(1).split("/");
        JSONArray jsonArray = new JSONArray();
        String[] query = new String[] {};
        ArrayList<String> columnName = new ArrayList<>();
        LinkedHashMap<String, String[]> queryParams = new LinkedHashMap<>();
        try {
            if (request.getQueryString() != null) {
                query = request.getQueryString().split("=");
            }
        } catch (Exception e) {
            request.setAttribute("X-Error", "Error Message: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        if (uriEndPoints.length == 1) {
            if (query.length % 2 == 0 && query.length != 0) {
                for (int i = 0; i < query.length; i = i + 2) {
                    queryParams.put(query[i], new String[] { query[i + 1] });
                }
            }
        } else {
            queryParams.put("seatid", new String[] { uriEndPoints[1] });
        }
        jsonArray = Dao.selectFromTable("seatdetails", columnName, queryParams);
        return jsonArray;
    }

    public static int post(HttpServletRequest request) throws Exception {
        int rowsAffected = 0;
        StringBuilder jsonObject = new StringBuilder();
        ServletInputStream js=request.getInputStream();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(js))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonObject.append(line);
            }
        }catch(Exception e){
            request.setAttribute("X-Error", "Error Message: " + e.getMessage());
            e.printStackTrace();
        }
        StringBuilder JSON = new StringBuilder();
        JSON.append("{");
        String[] json = jsonObject.toString().split("&");
        for(int i=0;i<json.length;i++){
            String key="",value="";
            String [] parts =json[i].split("=");
            if(parts.length==2){
                key=parts[0];
                value=parts[1];
            }
            JSON.append("\""+ key + "\" :\"" +value +"\" ," );
        }
        JSON.deleteCharAt(JSON.length()-1);
        JSON.append("}");
        ObjectMapper obj = new ObjectMapper();
        SeatDetails seatdetailsObject = obj.readValue(JSON.toString(), SeatDetails.class);
        Class<?> PojoClass = seatdetailsObject.getClass();
        Field[] fields = PojoClass.getDeclaredFields();
        LinkedHashMap<String, Object> tablevalues = new LinkedHashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldValue;
            try {
                fieldValue = field.get(seatdetailsObject);
            } catch (IllegalAccessException e) {
                fieldValue = "Error accessing field";
            }
            if (fieldName != "seatid")
                tablevalues.put(fieldName, fieldValue);

        }
        rowsAffected = Dao.insertToTable("users", tablevalues);
        return rowsAffected;
    }
}
