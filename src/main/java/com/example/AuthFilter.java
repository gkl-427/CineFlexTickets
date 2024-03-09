package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.Util.Dao;

public class AuthFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (req.getRequestURI().contains("login")) {
            AuthFilter.LoginFilter(request, response, chain);
        } else if (req.getRequestURI().contains("logout")) {
            AuthFilter.LogoutFilter(request, response, chain);
        } else {
            String requestURI = req.getRequestURI();
            boolean isValidEndpoint = isValidEndpoint(requestURI);
            if(!isValidEndpoint){
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            if (isValidEndpoint || checkAccess(req, res))
                chain.doFilter(request, response);
               
        }
    }

    public static void LoginFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        StringBuilder jsonObject = new StringBuilder();
        ServletInputStream js = request.getInputStream();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(js))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonObject.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder JSON = new StringBuilder();
        JSON.append("{");
        String[] json = jsonObject.toString().split("&");
        for (int i = 0; i < json.length; i++) {
            String key = "", value = "";
            String[] parts = json[i].split("=");
            if (parts.length == 2) {
                key = parts[0];
                value = parts[1];
            }
            JSON.append("\"" + key + "\" :\"" + value + "\" ,");
        }
        JSON.deleteCharAt(JSON.length() - 1);
        JSON.append("}");
        String jsonString = JSON.toString();
        JSONObject jsonObjectmapper = new JSONObject(jsonString);
        String mobilenumber = jsonObjectmapper.getString("mobilenumber");
        String password = jsonObjectmapper.getString("password");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        JSONObject responseStatus = new JSONObject();
        try {
            JSONObject userdetails = checkuser(mobilenumber, password);
            String userExist = userdetails.optString("userid");
            if (!userExist.isEmpty()) {
                HttpSession session = req.getSession(false);
                if (session == null || session.isNew()) {
                    session = req.getSession(true);
                    session.setAttribute("userid", userdetails.get("userid"));
                    session.setAttribute("role", userdetails.get("role"));

                }
                boolean isLogged = (session != null && session.getAttribute("userid") != null);
                if (isLogged) {
                    Cookie cookie = new Cookie("sessionid", session.getId());
                    cookie.setMaxAge(365 * 24 * 60 * 60);
                    res.addCookie(cookie);
                    userdetails.put("success", true);
                    PrintWriter out = response.getWriter();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    out.print(userdetails.toString());
                }
            } else {
                responseStatus.put("success", false);
                responseStatus.put("message", "Invalid username or password");
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(responseStatus.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void LogoutFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) request;
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(session.getId())) {
                    cookie.setMaxAge(0);
                    res.addCookie(cookie);
                    break;
                }
            }
        }
        res.setHeader("Cache-Control", "no-cache,no-store,must-revlaidate");

    }

    public static JSONObject checkuser(String mobilenumber, String password) throws Exception {
        ArrayList<String> columnname = new ArrayList<String>();
        LinkedHashMap<String, String[]> query = new LinkedHashMap<>();
        query.put("mobilenumber", new String[] { mobilenumber });
        query.put("password", new String[] { password });
        boolean isManager;
        JSONArray jsonArray = Dao.selectFromTable("users", columnname, query);
        JSONObject jsonObject = new JSONObject();
        JSONArray js = new JSONArray();
        query.remove("mobilenumber");
        query.remove("password");
        for (int incrementcounter = 0; incrementcounter < jsonArray.length(); incrementcounter++) {
            try {
                jsonObject = jsonArray.getJSONObject(incrementcounter);
                jsonObject.remove("passoword");
                isManager  = (int)(jsonObject.get("role")) ==2 ? true : false;
                if(isManager){
                    String userid=String.valueOf(jsonObject.get("userid"));
                    query.put("userid",new String[]{userid});
                    js = Dao.selectFromTable("theatres", columnname,query);
                }
                jsonObject.append("theatredetails", js.getJSONObject(0));
                return jsonObject;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    public void destroy() {

    }

    public static boolean isValidEndpoint(String endpoint) {
        List<String> restApiPattern = new ArrayList<>();
        restApiPattern.add("/ticket-app/api/users/?");
        restApiPattern.add("/ticket-app/api/shows/?");
        restApiPattern.add("/ticket-app/api/users/\\d+");
        restApiPattern.add("/ticket-app/api/shows/\\d+");
        restApiPattern.add("/ticket-app/api/theatres/?");
        restApiPattern.add("/ticket-app/api/theatres/\\d+");
        restApiPattern.add("/ticket-app/api/theatres/\\d+/shows/?");
        restApiPattern.add("/ticket-app/api/theatres/\\d+/shows/\\d+");
        restApiPattern.add("/ticket-app/api/theatres/\\d+/shows/\\d+\\?listallseats");
        restApiPattern.add("/ticket-app/api/movies/\\d+\\?listalltheaters");
        restApiPattern.add("/ticket-app/api/movies/?");
        restApiPattern.add("/ticket-app/api/bookings/?");
        restApiPattern.add("/ticket-app/api/bookings/?userid");
        restApiPattern.add("/ticket-app/api/movies/\\d+");
        restApiPattern.add("/ticket-app/api/bookings/?userid=\\d+");

        Pattern pattern;
        Matcher matcher;

        for (int i = 0; i < restApiPattern.size(); i++) {
            pattern = Pattern.compile(restApiPattern.get(i));
            matcher = pattern.matcher(endpoint);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;

    }

    public static boolean checkAccess(HttpServletRequest request,HttpServletResponse res){

    List<String> superadminGetAccess = new ArrayList<>(Arrays.asList("^\\/users$","^\\/theatres$"));
	List<String> superadminPostAccess = new ArrayList<>(Arrays.asList("^\\/theatres$"));
	List<String> managerGetAccess = new ArrayList<>(Arrays.asList("^\\/movies$"));
	List<String> managerPutAccess = new ArrayList<>(Arrays.asList("^\\/shows$"));
	List<String> managerPostAccess = new ArrayList<>(Arrays.asList("^\\/shows$"));
	List<String> GetAccess = new ArrayList<>(Arrays.asList("^\\/user\\/\\d+$","^\\/theatres$","^\\/movies$"));
	List<String> PostAccess = new ArrayList<>(Arrays.asList("^\\/bookings?userid\\d+"));
    List<String> PutAccess = new ArrayList<>(Arrays.asList(""));

	
        Cookie[] cookies = request.getCookies();
		boolean sessionExists = false;
		
		if(cookies!=null) {
			for(Cookie cookie: cookies) {
				if(cookie.getName().equals("sessionid")) {
					sessionExists = true;
					break;
				}
			}
		}
        int role=0;
		String requestURI = request.getPathInfo();
		boolean isLoggedIn = false;
        if (sessionExists) {
            HttpSession session = request.getSession(false); 
            isLoggedIn = session != null && session.getAttribute("role") != null;
            if(isLoggedIn) {
            	role = (int) session.getAttribute("role");
            }
        }
        boolean isAuthorised = false;
        String method = request.getMethod();
        switch(method) {
        case "GET":
        	if(role==3) {
        		GetAccess.addAll(superadminGetAccess);
        	}
        	else if(role ==2){
        		GetAccess.addAll(managerGetAccess);
        	}
        	
        	for(String uri : GetAccess) {
    			if(requestURI.matches(uri)) {
    				isAuthorised = true;
    				break;
    			}
    		}
        	break;
        case "POST":
        	if(role==2){
        		PostAccess.addAll(managerPostAccess);
        	}else if(role ==3){
                PostAccess.addAll(superadminPostAccess);
            }
        	
        	for(String uri : PostAccess) {
    			if(requestURI.matches(uri)) {
    				isAuthorised = true;
    				break;
    			}
    		}
        	break;
        case "PUT":
        	if(role==2){
        		PutAccess.addAll(managerPutAccess);
        	}
        	for(String uri : PutAccess) {
    			if(requestURI.matches(uri)) {
    				isAuthorised = true;
    				break;
    			}
    		}
        	break;        		
        }
        
        if(isAuthorised) {
            return true;
        }else {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
	return false;

}
}