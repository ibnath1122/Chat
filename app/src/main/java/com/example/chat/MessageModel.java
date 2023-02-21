package com.example.chat;


public class MessageModel {

    String from;
    String uId, message , messageId;
    Long msTime;

    public MessageModel(String uId, String message, Long msTime){
        this.uId = uId;
        this.message = message;
        this.msTime = msTime;
    }

    public MessageModel (String uId , String message){
        this.uId = uId;
        this.message  = message ;
    }

    public MessageModel(){}


    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {this.uId = uId;}

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