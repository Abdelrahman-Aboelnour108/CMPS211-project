package com.DOCS_ProMax.APT_3ala_barka_el_allah;

public class SessionJoinResult {
    public boolean success;
    public String username;
    public String sessionCode;
    public String message;

    public SessionJoinResult(boolean success, String username, String sessionCode, String message) {
        this.success = success;
        this.username = username;
        this.sessionCode = sessionCode;
        this.message = message;
    }
}