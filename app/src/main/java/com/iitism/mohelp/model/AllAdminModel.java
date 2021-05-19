package com.iitism.mohelp.model;

public class AllAdminModel {
    String Name,Title,Email,Phone,AdminUId;

    public AllAdminModel() {
    }

    public AllAdminModel(String name, String title, String email, String phone, String adminUId) {
        Name = name;
        Title = title;
        Email = email;
        Phone = phone;
        AdminUId = adminUId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAdminUId() {
        return AdminUId;
    }

    public void setAdminUId(String adminUId) {
        AdminUId = adminUId;
    }
}
