package com.main;

import com.GUI.LoginUI;

public class Client {
    public static String u_id;
    public static String password;

    public int test(){
        return 1;
    }

    public static void main(String[] args) {
        LoginUI loginUI = new LoginUI();
        loginUI.main();
    }
}
