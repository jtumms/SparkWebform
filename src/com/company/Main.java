package com.company;


import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        HashMap<String, User> userHashMap = new HashMap();

        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("loginName");
                    User user = userHashMap.get(name);
                    if (user != null){
                        response.redirect("/create-message");
                    }
                    return new ModelAndView(null, "/index.html");
                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/login",
                (request, response) -> {
                    String name = request.queryParams("loginName");
                    String password = request.queryParams("password");
                    User user = new User();
                    user = new User(name, password, user.messages);
                    if (!userHashMap.containsKey(name)){
                        response.redirect("/newuser");
                    }
                    else if (!password.equals(user.password)){
                        response.redirect("/");
                        return null;
                    }

                    Session session = request.session();
                    session.attribute("loginName", name);

                    response.redirect("/create-message");
                    return "";
                }
        );
        Spark.get(
                "/create-message",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("loginName");
                    User user = userHashMap.get(name);
                    HashMap m = new HashMap();
                    m.put("messages", user.messages);
                    m.put("name", name);
                    return new ModelAndView(m, "create-message.html");
                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/create-message",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("loginName");
                    User user = userHashMap.get(name);
                    if (user == null) {
                        throw new Exception("User not logged in");
                    }
                    String input = request.queryParams("message");
                    Message message = new Message(input);
                    user.messages.add(message);
                    System.out.println(user.messages);
                    response.redirect("/create-message");
                    return "";
                })
        );
        Spark.get(
                "/newuser",
                ((request, response) -> {
                    return new ModelAndView(userHashMap, "newuser.html");


                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/newuser",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    String password = request.queryParams("password");
                    User user = new User();
                    userHashMap.put(name, user);
                    Session session = request.session();
                    session.attribute("loginName", name);
                    response.redirect("/");
                    return null;
                })
        );


        Spark.post(
                "/logout",
                (request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return null;
                }
        );
        Spark.post(
                "/delete",
                (request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("loginName");
                    User user = userHashMap.get(name);
                    if (session == null){
                        response.redirect("/");
                    }
                    String selected = request.queryParams("id");
                    user.messages.remove(Integer.valueOf(selected) - 1);
                    response.redirect("/create-message");
                    return "";
                }
        );
        Spark.post(
                "/edit",
                (request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("loginName");
                    User user = userHashMap.get(name);
                    if (session == null){
                        response.redirect("/");
                    }
                    String someText = request.queryParams("edit");
                    Message message = new Message(someText);
                    String idSelected = request.queryParams("id");
                    user.messages.set(Integer.valueOf(idSelected) - 1, message);
                    response.redirect("/create-message");
                    return "";
                }
        );

    }
}
