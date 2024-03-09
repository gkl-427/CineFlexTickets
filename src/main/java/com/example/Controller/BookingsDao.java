package com.example.Controller;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.Entity.Bookings;
import com.example.Util.Dao;
import com.example.Util.DbConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class BookingsDao {
    public static Object bookingsMain(HttpServletRequest request) throws Exception {
        if (request.getMethod().equals("GET"))
            return BookingsDao.get(request);
        else if (request.getMethod().equals("POST"))
            return BookingsDao.post(request);
        return null;
    }

    public static JSONArray get(HttpServletRequest request) throws Exception {
        String[] uriEndPoints = request.getServletPath().substring(1).split("/");
        JSONArray jsonArray = new JSONArray();
        String[] query = new String[] {};
        ArrayList<String> columnName = new ArrayList<>();
        LinkedHashMap<String, String[]> queryParams = new LinkedHashMap<>();
        boolean flag=false;
        try {
            if (request.getQueryString() != null) {
                if(request.getQueryString().contains("getdetails")){
                    String req= request.getQueryString().substring(11);
                    query = req.split("=");
                    query[0]="bookings.userid";
                    flag=true;
            }
            else query = request.getQueryString().split("=");
            }
        } catch (Exception e) {
            request.setAttribute("X-Error", "Error Message: " + e.getMessage());
            e.printStackTrace();
        }
        if (uriEndPoints.length == 1) {
            if (query.length % 2 == 0 && query.length != 0) {
                for (int i = 0; i < query.length; i = i + 2) {
                    queryParams.put(query[i], new String[] { query[i + 1] });
                }
            }
        } else {
            queryParams.put("ticketid", new String[] { uriEndPoints[1] });
        }
        if(flag){
            jsonArray = Dao.selectwithJoins(new String[]{"bookings","shows","theatres"}, new String[]{"showid","theatreid"}, columnName, queryParams);
            return jsonArray;
        }
        jsonArray = Dao.selectFromTable("bookings", columnName, queryParams);
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
        if (request.getQueryString() != null && request.getQueryString().contains("addentry")) {
            reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject js = new JSONObject(sb.toString());
            int show_id = Integer.parseInt(js.getString("showid"));
            String paymentmode =(String) js.getString("paymentmode");
            boolean paymentstatus =js.getBoolean("paymentstatus");
            int amount=(int) js.get("amount");
            BigInteger paymentime=js.getBigInteger("paymenttime");
            int userid=(int) js.get("userid");
            String seat_ids=(String) js.get("seatid");
            String seatnumber =(String) js.get("seatnumber");
            Gson gson = new Gson();
            int[] intArray = gson.fromJson(seat_ids, int[].class);
            String query = "SELECT payments(?, ?, ?, ?, ?, ?, ?, ?)";
            Connection con = null;
            PreparedStatement stmt = null;
            try {
                con = DbConnection.connect_to_db();
                stmt = con.prepareStatement(query);
                stmt.setObject(1, paymentmode);
                stmt.setBoolean(2,paymentstatus);
                stmt.setInt(3,amount);
                stmt.setObject(4, paymentime);
                stmt.setInt(5,userid);
                stmt.setInt(6,show_id);
                stmt.setObject(7, intArray);
                stmt.setObject(8,seatnumber);

                stmt.execute();
            } catch (Exception e) {
                request.setAttribute("X-Error", "Error Message: " + e.getMessage());
                e.printStackTrace();
            }finally{
                con.close();
                stmt.close();
            }
             return 1;
        }
        ObjectMapper obj = new ObjectMapper();
        Bookings Object = obj.readValue(sb.toString(), Bookings.class);
        Class<?> PojoClass = Object.getClass();
        Field[] fields = PojoClass.getDeclaredFields();
        LinkedHashMap<String, Object> tablevalues = new LinkedHashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldValue;
            try {
                fieldValue = field.get(Object);
            } catch (IllegalAccessException e) {
                fieldValue = "Error accessing field";
            }
            if (fieldName != "transactionid")
                tablevalues.put(fieldName, fieldValue);

        }
        rowsAffected = Dao.insertToTable("Bookings", tablevalues);
        return rowsAffected;
    }
}
