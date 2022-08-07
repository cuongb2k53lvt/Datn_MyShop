package com.example.modelfashion.Model.response.User;

import java.util.List;

public class User {
    private String id;
    private String taiKhoan;
    private String matKhau;
    private String email;
    private String fullName;
    private String phone;
    private String sex;
    private String birthdate;
    private String address;
    private String fund;
    private String avatar;

    public User(String id, String taiKhoan, String matKhau, String email, String fullName, String phone, String sex, String birthdate, String address, String fund, String avatar) {
        this.id = id;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.sex = sex;
        this.birthdate = birthdate;
        this.address = address;
        this.fund = fund;
        this.avatar = avatar;
    }

    public User(String s, String s1, String s2, String s3, String s4, String s5, String s6) {

    }
    @Override
    public String toString() {
        return "{"+
                "id='" + id + '\'' +
                ", taiKhoan='" + taiKhoan + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", sex='" + sex + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", address='" + address + '\'' +
                ", fund='" + fund + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
