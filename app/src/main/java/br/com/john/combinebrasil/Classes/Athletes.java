package br.com.john.combinebrasil.Classes;

import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by GTAC on 24/10/2016.
 */

public class Athletes{
    String Id;
    String Name;
    String Birthday;
    String CPF;
    String Address;
    String DesirablePosition;
    String CreatedAt;
    String UpdateAt;
    double Height;
    double Weight;
    String Code;
    String Email;
    String PhoneNumber;
    boolean Sync;
    boolean TermsAccepted;

    public Athletes(){}

    public Athletes(String id, String name, String birthday, String cpf, String address, String desirablePosition,
                    double height, double weight, String createdAt,
                    String updateAt, String code, String email, String phoneNumber, boolean sync, boolean termsAccepted) {
        Id = id;
        Name = name;
        Birthday = birthday;
        this.CPF = cpf;
        this.Address = address;
        this.DesirablePosition = desirablePosition;
        this.CreatedAt = createdAt;
        this.UpdateAt = updateAt;
        this.Height = height;
        this.Weight = weight;
        this.Code = code;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
        Sync = sync;
        TermsAccepted = termsAccepted;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getUpdateAt() {
        return UpdateAt;
    }

    public void setUpdateAt(String updateAt) {
        UpdateAt = updateAt;
    }

    public double getHeight() {
        return Height;
    }

    public void setHeight(double height) {
        Height = height;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDesirablePosition() {
        return DesirablePosition;
    }

    public void setDesirablePosition(String desirablePosition) {
        DesirablePosition = desirablePosition;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public boolean getSync() {
        return Sync;
    }

    public void setSync(boolean sync) {
        Sync = sync;
    }

    public boolean getTermsAccepted() {
        return TermsAccepted;
    }

    public void setTermsAccepted(boolean termsAccepted) {
        TermsAccepted = termsAccepted;
    }
}
