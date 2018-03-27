package com.example.callback.Activity.ContactModel;

/**
 * Created by Devroop Kar on 12-08-2017.
 */

public class ContactModel {
    private int id;
    private String name;
    private String personal_ph;
    private String office_ph;
    private String home_ph;
    private String addr;
    private String email;
    private String gender;
    private String path = null;

    public ContactModel() {
    }

    public ContactModel(String name, String personal_ph, String office_ph, String home_ph) {
        this.name = name;
        this.personal_ph = personal_ph;
        this.office_ph = office_ph;
        this.home_ph = home_ph;
    }

    public ContactModel(int id, String name, String personal_ph, String office_ph, String home_ph) {
        this.id = id;
        this.name = name;
        this.personal_ph = personal_ph;
        this.office_ph = office_ph;
        this.home_ph = home_ph;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPersonal_ph(String personal_ph) {
        this.personal_ph = personal_ph;
    }

    public void setOffice_ph(String office_ph) {
        this.office_ph = office_ph;
    }

    public void setHome_ph(String home_ph) {
        this.home_ph = home_ph;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPersonal_ph() {
        return personal_ph;
    }

    public String getOffice_ph() {
        return office_ph;
    }

    public String getHome_ph() {
        return home_ph;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
