package com.example.appfororg.rest;


import com.example.appfororg.domain.Message;
import com.example.appfororg.domain.Organization;

public interface AppApi {

    void fillChats();
    void fillPeople();
    void fillOrganization();
    void fillMsg();

    void addOrganization(Organization organization);
    void addMessages(Message message);

    void updateOrganization(int id, String name, String login, String type,
                            String photoOrg, String description, String address,
                            String needs, String linkToWebsite, String pass);

    void checkNewMsg();
    void checkNewChat();

    void deleteChatById(int id);

}