package com.ifsc.tarefas.auth;

import jakarta.servlet.http.HttpServletRequest;

public final class RequestAuth {

    private RequestAuth() {}

    public static String getUser(HttpServletRequest request) {
        Object v = request.getAttribute("AUTH_USER");
        return v != null ? v.toString() : null;
    }

    public static String getRole(HttpServletRequest request) {
        Object v = request.getAttribute("AUTH_ROLE");
        return v != null ? v.toString() : null;
    }
}


