package com.example.Controller;

import javax.servlet.http.HttpServletRequest;


public class SeatsmappingsDao {

    public static Object seatsmappingMain(HttpServletRequest request) throws Exception {
        if (request.getMethod().equals("GET"))
            return ShowsDao.get(request);
        else if(request.getMethod().equals("POST"))
            return (Integer) ShowsDao.post(request);
        return null;
    }
}
