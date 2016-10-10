package com.company;


import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    static User user;
    static HashMap<String, User> userHashMap = new HashMap();




    public static void main(String[] args) {
        ArrayList<String> messages = new ArrayList<String>();


        Spark.get(
                "/",
                ((request, response) -> {
                    return new ModelAndView(null, "index.html");


                }),
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/login",
                ((request, response) -> {
                    return new ModelAndView(null, "login.html");
                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/login",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    String password = request.queryParams("password");
                    if (!userHashMap.containsKey(name)){
                        response.redirect("/newuser");
                    }
                    else if (!password.equals(user.password)){
                        response.redirect("/login");
                        return null;
                    }

                    response.redirect("/create-message");
                    return null;
                })
        );
        Spark.get(
                "/create-message",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    m.put("messages", messages);
                    return new ModelAndView(m, "create-message.html");


                }),
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/newuser",
                ((request, response) -> {
                    return new ModelAndView(null, "newuser.html");


                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/newuser",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    String password = request.queryParams("password");
                    user = new User(name, password);
                    userHashMap.put(name, user);
                    response.redirect("/login");
                    return null;
                })
        );
        Spark.post(
                "/create-message",
                ((request, response) -> {
                    String message = request.queryParams("message");

                    messages.add(message);

                    response.redirect("/create-message");
                    return null;
                })
        );

        Spark.post(
                "/logout",
                (request, response) -> {
                    user = null;
                    userHashMap.clear();
                    response.redirect("/");
                    return null;
                }
        );



    }
}
