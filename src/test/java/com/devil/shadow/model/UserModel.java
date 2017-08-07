package com.devil.shadow.model;

/**
 * Created by devil on 2017/8/7.
 */
public class UserModel {
    private Long id;
    private String name;
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TestModel{" +
                "id=" + id +
                ", NAME='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
