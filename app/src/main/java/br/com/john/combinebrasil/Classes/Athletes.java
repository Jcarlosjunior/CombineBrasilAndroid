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
    String CreatedAt;
    String UpdateAt;
    int Height;
    int Weight;
    String Code;

    public Athletes(){}

    public Athletes(String id, String name, String birthday, String cpf, int height, int weight, String createdAt,
                    String updateAt, String code) {
        Id = id;
        Name = name;
        Birthday = birthday;
        this.CPF = cpf;
        this.CreatedAt = createdAt;
        this.UpdateAt = updateAt;
        this.Height = height;
        this.Weight = weight;
        this.Code = code;
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

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
