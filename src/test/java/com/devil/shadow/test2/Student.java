package com.devil.shadow.test2;

import java.io.Serializable;

public class Student implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8847488027537208842L;

    private Integer id;

    private String account;

    private String password;

    private String name;

    private String icon;

    private Integer sex;

    private Integer grade;

    private String aclass;

    private Double credit;

    private String role;

    private Integer warn;

    private Integer status;

    @Override
    public String toString() {
        return "Student [id=" + id + ", account=" + account + ", password=" + password + ", name=" + name + ", icon=" + icon + ", sex=" + sex + ", grade=" + grade + ", aclass=" + aclass + ", credit="
                + credit + ", role=" + role + ", warn=" + warn + ", status=" + status + "]";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getAclass() {
        return aclass;
    }

    public void setAclass(String aclass) {
        this.aclass = aclass;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getWarn() {
        return warn;
    }

    public void setWarn(Integer warn) {
        this.warn = warn;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}