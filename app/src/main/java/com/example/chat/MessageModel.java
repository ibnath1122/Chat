package com.example.chat;


public class MessageModel {

    String from, message;
    Long msTime;


    /*public MessageModel() {

    }

    public MessageModel(String from, String message, Long msTime)
    {
        this.from=from;
        this.message=message;
        this.msTime = msTime;

    }*/

    public String getFrom()
    {
        return from;
    }
    public void setFrom(String from)
    {
        this.from=from;
    }

    public String getMessage()
    {
        return message;
    }
    public void setMessage(String message)
    {
        this.message=message;
    }

    public Long getMsTime() {
        return msTime;
    }

    public void setMsTime(Long msTime) {
        this.msTime = msTime;
    }
}