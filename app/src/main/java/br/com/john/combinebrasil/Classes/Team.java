package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 26/12/2016.
 */

public class Team {
    private String Id;
    private String Name;
    private String City;
    private String Modality;
    private String Addres;
    private String Foundation;
    private String PresidentName;
    private String Email;
    private String PhoneNumber;
    private String SocialLink;
    private String Training;

    public Team(){}

    public Team(String id, String name, String city, String modality) {
        Id = id;
        Name = name;
        City = city;
        Modality = modality;
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

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getModality() {
        return Modality;
    }

    public void setModality(String modality) {
        Modality = modality;
    }

    public String getAddres() {
        return Addres;
    }

    public void setAddres(String addres) {
        Addres = addres;
    }

    public String getFoundation() {
        return Foundation;
    }

    public void setFoundation(String foundation) {
        Foundation = foundation;
    }

    public String getPresidentName() {
        return PresidentName;
    }

    public void setPresidentName(String presidentName) {
        PresidentName = presidentName;
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

    public String getSocialLink() {
        return SocialLink;
    }

    public void setSocialLink(String socialLink) {
        SocialLink = socialLink;
    }

    public String getTraining() {
        return Training;
    }

    public void setTraining(String training) {
        Training = training;
    }

}
