package com.example;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getRequestURI();
        String[] uriEndPoints = action.substring(1).split("/");
        int length= uriEndPoints.length;
        if(length%2==0) length=length-1;
        try {
            Class<?> classname = Class
                    .forName("com.example.Controller." + Character.toUpperCase(uriEndPoints[length-1].charAt(0))
                            + uriEndPoints[length-1].substring(1) + "Dao");
            @SuppressWarnings("deprecation")
            Object instance = classname.newInstance();
            JSONArray jsonArray = (JSONArray) classname.getMethod(uriEndPoints[length-1] + "Main", HttpServletRequest.class)
                    .invoke(instance,
                            request);
            if (jsonArray.isEmpty()){
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
            }
            else{
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

            }
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(jsonArray);
        } catch (Exception e) { 
            Throwable cause = e.getCause();
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            cause.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getRequestURI();
        JSONObject responseStatus  = new JSONObject();
        String[] uriEndPoints = action.substring(1).split("/");
        int length= uriEndPoints.length;
        if(length%2==0) length=length-1;
        try {
            Class<?> classname = Class
                    .forName("com.example.Controller." + Character.toUpperCase(uriEndPoints[length-1].charAt(0))
                            + uriEndPoints[length-1].substring(1) + "Dao");
            @SuppressWarnings("deprecation")
            Object instance = classname.newInstance();
            int rowsAffected = (Integer) classname.getMethod(uriEndPoints[length-1] + "Main", HttpServletRequest.class)
                    .invoke(instance, request);
            if (rowsAffected == 0) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                responseStatus.put("success",false);
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(responseStatus.toString());

            } else {
                responseStatus.put("success",true);
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(responseStatus.toString());
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            Throwable cause = e.getCause();
            cause.printStackTrace();
        }

    }


    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getRequestURI();
        JSONObject responseStatus  = new JSONObject();
        String[] uriEndPoints = action.substring(1).split("/");
        int length= uriEndPoints.length;
                if(length%2==0) length=length-1;

        try {
            Class<?> classname = Class
                    .forName("com.example.Controller." + Character.toUpperCase(uriEndPoints[length-1].charAt(0))
                            + uriEndPoints[length-1].substring(1) + "Dao");
            @SuppressWarnings("deprecation")
            Object instance = classname.newInstance();
            int rowsAffected = (Integer) classname.getMethod(uriEndPoints[length-1] + "Main", HttpServletRequest.class)
                    .invoke(instance, request);
            if (rowsAffected == 0) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                responseStatus.put("success",false);
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(responseStatus.toString());
            } 
            else {
                responseStatus.put("success",true);
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(responseStatus.toString());
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            Throwable cause = e.getCause();
            cause.printStackTrace();
        }

    }

}
