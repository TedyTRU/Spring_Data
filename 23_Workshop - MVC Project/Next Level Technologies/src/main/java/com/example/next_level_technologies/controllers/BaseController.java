package com.example.next_level_technologies.controllers;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    protected boolean isLogged(HttpServletRequest request) {
        Object userId = request.getSession().getAttribute("userId");

        return userId != null;
    }

}
