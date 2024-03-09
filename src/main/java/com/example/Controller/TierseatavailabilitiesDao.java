package com.example.Controller;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;

import com.example.Entity.TierSeatAvailability;
import com.example.Util.Dao;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TierseatavailabilitiesDao {
    public static Object tierseatavailabilitiesMain(HttpServletRequest request) throws Exception {
        if (request.getMethod().equals("GET"))
            return TierseatavailabilitiesDao.get(request);
        else if (request.getMethod().equals("POST"))
            return (Integer) TierseatavailabilitiesDao.post(request);
        return null;
    }

    public static JSONArray get(HttpServletRequest request) throws Exception {
        JSONArray jsonArray = new JSONArray();
        String[] uriEndPoints = request.getServletPath().substring(1).split("/");
        String[] query = new String[] {};
        ArrayList<String> columnName = new ArrayList<>();
        boolean joinFlag = false;
        LinkedHashMap<String, String[]> queryParams = new LinkedHashMap<>();
        try {
            if (request.getQueryString() != null) {
                if (request.getQueryString().contains("listallseats")) {
                    query = request.getQueryString().substring(request.getQueryString().indexOf('&') + 1).split("=");
                    joinFlag = true;
                } else
                    query = request.getQueryString().split("=");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (uriEndPoints.length == 1) {
            if (query.length % 2 == 0 && query.length != 0) {
                for (int i = 0; i < query.length; i = i + 2) {
                    queryParams.put(query[i], new String[] { query[i + 1] });
                }
            }
        } else {
            queryParams.put("showid", new String[] { uriEndPoints[1] });
        }
        if (joinFlag)
            jsonArray = Dao.selectwithJoins(new String[]{"tierseatavailability", "seatdetails"}, new String[]{"tierid"}, columnName, queryParams);
        else
            jsonArray = Dao.selectFromTable("tierseatavailability", columnName, queryParams);
        return jsonArray;
    }

    public static int post(HttpServletRequest request) throws Exception {
        int rowsAffected = 0;
        StringBuilder jsonPayload = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonPayload.append(line);
            }
        }
        ObjectMapper obj = new ObjectMapper();
        TierSeatAvailability tierobject = obj.readValue(jsonPayload.toString(), TierSeatAvailability.class);
        Class<?> PojoClass = tierobject.getClass();
        Field[] fields = PojoClass.getDeclaredFields();
        LinkedHashMap<String, Object> tablevalues = new LinkedHashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldValue;
            try {
                fieldValue = field.get(tierobject);
            } catch (IllegalAccessException e) {
                fieldValue = "Error accessing field";
            }
            if (fieldName != "tierid")
                tablevalues.put(fieldName, fieldValue);

        }
        rowsAffected = Dao.insertToTable("tierseatavailabilty", tablevalues);
        return rowsAffected;
    }
}
