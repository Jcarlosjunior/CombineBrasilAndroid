package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 19/10/2016.
 */

public class Tests {
    String id;
    String name;
    String type;
    String description;
    String idUser;
    String idSelective;
    String code;

    public Tests(){
    }

    public Tests(String id, String name, String type, String description, String idUser, String idSelective, String code) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this. idUser = idUser;
        this.idSelective = idSelective;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIdSelective() {
        return idSelective;
    }

    public void setIdSelective(String idSelective) {
        this.idSelective = idSelective;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
