package com.example.chat;

import java.io.Serializable;

public class UserModel implements Serializable {

    String name;
    String id;
    String bio;
    String imageUri;
    String gender;


   public UserModel()
    {

    }

    public UserModel(String id, String name, String bio, String imageUri, String gender)
    {
        this.id=id;
        this.name=name;
        this.bio=bio;
        this.imageUri=imageUri;
        this.gender=gender;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    boolean status;

    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id=id;
    }


    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getBio()
    {
        return bio;
    }
    public void setBio(String bio)
    {
        this.bio=bio;
    }
    public String getImageUri()
    {
        return imageUri;
    }
    public void setImageUri(String imageUri)
    {
        this.imageUri=imageUri;
    }

    public String getGender()
    {
        return gender;
    }
    public void setGender(String gender)
    {
        this.gender=gender;
    }
}