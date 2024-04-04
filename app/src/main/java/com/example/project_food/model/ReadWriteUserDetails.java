package com.example.project_food.model;

public class ReadWriteUserDetails
{
    public  String fullName, birthday, gender, mobile;

    //Constuctor
    public ReadWriteUserDetails()
    {};

    public ReadWriteUserDetails (String txtfullName, String txtBirthday, String txtGender, String txtPhone)
    {
        this.fullName = txtfullName;
        this.birthday = txtBirthday;
        this.gender = txtGender;
        this.mobile = txtPhone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
