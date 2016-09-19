package com.shedhack.requestbody.cache.filter;


public class Message {

    private String message, language;

    public Message() {
    }

    public Message(String mess, String lang) {
        this.message = mess;
        this.language = lang;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "{\"Message\":{"
                + "\"message\":\"" + message + "\""
                + ", \"language\":\"" + language + "\""
                + "}}";
    }
}
