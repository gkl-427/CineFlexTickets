package com.example.Controller;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.Entity.Users;
import com.example.Util.Dao;

public class UsersDao {
    public static Object usersMain(HttpServletRequest request) throws Exception {
        if (request.getMethod().equals("GET"))
            return UsersDao.get(request);
        else if (request.getMethod().equals("POST"))
            return (Integer) UsersDao.post(request);
        else if (request.getMethod().equals("PUT"))
            return (Integer) UsersDao.put(request);
        return null;
    }

    public static JSONArray get(HttpServletRequest request) throws Exception {
        String[] uriEndPoints = request.getRequestURI().substring(1).split("/");
        JSONArray jsonArray = new JSONArray();
        String[] query = new String[] {};
        ArrayList<String> columnName = new ArrayList<>();
        LinkedHashMap<String, String[]> queryParams = new LinkedHashMap<>();
        try {
            if (request.getQueryString() != null) {
                query = request.getQueryString().split("=");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (uriEndPoints.length == 3) {
            if (query.length % 2 == 0 && query.length != 0) {
                for (int i = 0; i < query.length; i = i + 2) {
                    queryParams.put(query[i], new String[] { query[i + 1] });
                }
            }
        } else {
            queryParams.put("userid", new String[] { uriEndPoints[3] });
        }
        jsonArray = Dao.selectFromTable("users", columnName, queryParams);
        for(int i=0;i<jsonArray.length();i++){
            jsonArray.getJSONObject(i).remove("password");
        }
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
        Users userObject = obj.readValue(sb.toString(), Users.class);
        Class<?> PojoClass = userObject.getClass();
        Field[] fields = PojoClass.getDeclaredFields();
        LinkedHashMap<String, Object> tablevalues = new LinkedHashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldValue;
            try {
                fieldValue = field.get(userObject);
            } catch (IllegalAccessException e) {
                fieldValue = "Error accessing field";
            }
            if (fieldName != "userid" && fieldValue!="" && fieldValue!=null)
                tablevalues.put(fieldName, fieldValue);
        }
        tablevalues.put("role", 1);
        rowsAffected = Dao.insertToTable("users", tablevalues);
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
        Users userObject = obj.readValue(sb.toString(), Users.class);
        Class<?> PojoClass = userObject.getClass();
        Field[] fields = PojoClass.getDeclaredFields();
        LinkedHashMap<String, Object> tablevalues = new LinkedHashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldValue;
            try {
                fieldValue = field.get(userObject);
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
        conditionParams.put("userid ", Integer.parseInt(uriEndPoints[3]));
        rowsAffected = Dao.updateTheTable("users", tablevalues, conditionParams);
        return rowsAffected;
    }
}
