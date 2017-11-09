package com.mv.Model;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mv.Utils.PreferenceHelper;

/**
 * Created by acer on 8/8/2017.
 */


public class User {


    private static User currentUser = null;

    public static User getCurrentUser(Context context) {
        PreferenceHelper mPreferenceHelper;

        if (currentUser == null) {
            mPreferenceHelper = new PreferenceHelper(context);
            if (TextUtils.isEmpty(mPreferenceHelper.getString(PreferenceHelper.UserData)))
                currentUser = new User();
            else {
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                currentUser = gson.fromJson(mPreferenceHelper.getString(PreferenceHelper.UserData), User.class);
            }
        }
        return currentUser;
    }

    public static void clearUser() {
        currentUser = null;
    }

    @SerializedName("Id")
    @Expose
    private String Id;
    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("User_Email__c")
    @Expose
    private String Email;
    @SerializedName("User_Cluster__c")
    @Expose
    private String Cluster;
    @SerializedName("User_District__c")
    @Expose
    private String District;
    @SerializedName("User_Password__c")
    @Expose
    private String Password;
    @SerializedName("User_Mobile_No__c")
    @Expose
    private String Phone;
    @SerializedName("Role_Name__c")
    @Expose
    private String Roll;
    @SerializedName("User_SchoolID__c")
    @Expose
    private String School_Code;
    @SerializedName("UserSchoolName__c")
    @Expose
    private String School_Name;
    @SerializedName("User_Taluka__c")
    @Expose
    private String Taluka;
    @SerializedName("User_Village__c")
    @Expose
    private String Village;
    @SerializedName("User_State__c")
    @Expose
    private String State;
    @SerializedName("Middle_Name__c")
    @Expose
    private String MiddleName;
    @SerializedName("Last_Name__c")
    @Expose
    private String LastName;
    @SerializedName("ImageId__c")
    @Expose
    private String ImageId;

    public String getImageId() {
        return ImageId;
    }

    public void setImageId(String imageId) {
        ImageId = imageId;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCluster() {
        return Cluster;
    }

    public void setCluster(String cluster) {
        Cluster = cluster;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getRoll() {
        return Roll;
    }

    public void setRoll(String roll) {
        Roll = roll;
    }

    public String getSchool_Code() {
        return School_Code;
    }

    public void setSchool_Code(String school_Code) {
        School_Code = school_Code;
    }

    public String getSchool_Name() {
        return School_Name;
    }

    public void setSchool_Name(String school_Name) {
        School_Name = school_Name;
    }

    public String getTaluka() {
        return Taluka;
    }

    public void setTaluka(String taluka) {
        Taluka = taluka;
    }

    public String getVillage() {
        return Village;
    }

    public void setVillage(String village) {
        Village = village;
    }


}