package com.example.Controller;

import java.io.BufferedReader;
import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;

import com.example.Entity.Movies;
import com.example.Util.Dao;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MoviesDao {
    public static Object moviesMain(HttpServletRequest request) throws Exception {
        if (request.getMethod().equals("GET"))
            return MoviesDao.get(request);
        else if (request.getMethod().equals("POST"))
            return (Integer) MoviesDao.post(request);
        return null;
    }

    public static JSONArray get(HttpServletRequest request) throws Exception {
        String[] uriEndPoints = request.getRequestURI().substring(1).split("/");
        JSONArray jsonArray = new JSONArray();
        String[] query = new String[] {};
        ArrayList<String> columnName = new ArrayList<>();
        boolean joinFlag = false;
        LinkedHashMap<String, String[]> queryParams = new LinkedHashMap<>();
        try {
            if (request.getQueryString() != null) {
                if (request.getQueryString().contains("listalltheatres")) {
                    query = request.getQueryString().substring(request.getQueryString().indexOf('&') + 1).split("=");
                    joinFlag = true;
                } else {
                    query = request.getQueryString().split("=");
                }
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
            queryParams.put("movieid", new String[] { uriEndPoints[3] });
        }
        if (joinFlag){
            jsonArray = Dao.selectwithJoins(new String[]{"shows","theatres"},new String[]{"theatreid"}, columnName, queryParams);
            return jsonArray;
        }
        else
            jsonArray = Dao.selectFromTable("movies", columnName, queryParams);
        return jsonArray;
    }

    public static int post(HttpServletRequest request) throws Exception {
        int rowsAffected = 0;
        ArrayList<String> columnName = new ArrayList<>();
        LinkedHashMap<String, String[]> queryParams = new LinkedHashMap<>();
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
        ObjectMapper obj = new ObjectMapper();
        Movies MovieObject = obj.readValue(sb.toString(), Movies.class);
        Class<?> PojoClass = MovieObject.getClass();
        Field[] fields = PojoClass.getDeclaredFields();
        LinkedHashMap<String, Object> tablevalues = new LinkedHashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldValue;
            try {
                fieldValue = field.get(MovieObject);
            } catch (IllegalAccessException e) {
                request.setAttribute("X-Error", "Error Message: " + e.getLocalizedMessage());
                fieldValue = "Error accessing field";
                e.printStackTrace();
            }
            if (fieldName != "movieid")
                tablevalues.put(fieldName, fieldValue);

        }
        rowsAffected = Dao.insertToTable("movies", tablevalues);
        return rowsAffected;
    }
}
