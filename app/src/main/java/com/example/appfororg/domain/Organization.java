package com.example.appfororg.domain;

import java.util.Objects;

public class Organization {
    private int id;
    private String name;
    private String pass;
    private String login;
    private String type;
    private String photoOrg;
    private String description;
    private String address;
    private String needs;
    private String linkToWebsite;

    public Organization(String name, String login, String type, String photoOrg,
                        String description, String address,
                        String needs, String linkToWebsite, String pass) {
        this.name = name;
        this.type = type;
        this.login = login;
        this.photoOrg = photoOrg;
        this.description = description;
        this.address = address;
        this.needs = needs;
        this.linkToWebsite = linkToWebsite;
        this.pass = pass;
    }

    public Organization(String name, String login, String type, String photoOrg,
                        String description, String address, String needs, String pass) {
        this.name = name;
        this.type = type;
        this.login = login;
        this.photoOrg = photoOrg;
        this.description = description;
        this.address = address;
        this.needs = needs;
        this.pass = pass;
    }

    public Organization(int id, String name, String login, String type, String photoOrg,
                        String description, String address,
                        String needs, String linkToWebsite, String pass) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.login = login;
        this.photoOrg = photoOrg;

        this.description = description;
        this.address = address;
        this.needs = needs;
        this.linkToWebsite = linkToWebsite;
        this.pass = pass;
    }

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPhotoOrg() {
        return photoOrg;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getNeeds() {
        return needs;
    }

    public String getLinkToWebsite() {
        return linkToWebsite;
    }

    public String getPass() {
        return pass;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", type='" + type + '\'' +
                ", photoOrg='" + photoOrg + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", needs='" + needs + '\'' +
                ", linkToWebsite='" + linkToWebsite + '\'' +
                '}';
    }
}