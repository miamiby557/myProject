package com.lnet.tmsapp.model;


import java.util.HashMap;
import java.util.Map;

public class ServiceResult {


    private boolean success = true;
    private Map<String, String> messages = new HashMap<>();
    private Object content;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, String> messages) {
        this.messages = messages;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content){
       this.content = content;
    }

    public ServiceResult() {

    }

    public ServiceResult(Object content) {
        this.setContent(content);
    }

    public void addMessage(String name, String message){
        messages.put(name, message);
    }

    public void addMessage(String message){
        messages.put("default", message);
    }


}
