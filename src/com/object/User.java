package com.object;

import com.util.SqlControler;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class User {
    private String u_id;
    private String password;
    private String phone;
    private String address;

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getU_id() {
        return u_id;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }


}
