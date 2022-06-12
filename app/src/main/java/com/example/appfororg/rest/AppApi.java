package com.example.appfororg.rest;


import com.example.appfororg.domain.Message;
import com.example.appfororg.domain.Organization;

public interface AppApi {
    void addOrganization(Organization organization);
    void updateOrganization(int id, String name, String login, String type,
                            String photoOrg, String description, String address,
                            String needs, String linkToWebsite, String pass);
    void fillChats();

    void fillPeople();

    void fillOrganization();
    void checkNewMsg();
    void deleteChatById(int id);
    void fillMsg();
    void addMessages(Message message);

}