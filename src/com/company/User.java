package com.company;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by john.tumminelli on 10/3/16.
 */
public class User {
    String name;
    String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(String name) {
    }
}
