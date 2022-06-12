package com.example.appfororg.rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appfororg.OpenHelper;
import com.example.appfororg.domain.Chat;
import com.example.appfororg.domain.Message;
import com.example.appfororg.domain.Organization;
import com.example.appfororg.domain.Person;
import com.example.appfororg.domain.mapper.ChatMapper;
import com.example.appfororg.domain.mapper.MessageMapper;
import com.example.appfororg.domain.mapper.OrganizationMapper;
import com.example.appfororg.domain.mapper.PersonMapper;
import com.example.appfororg.fragment.ChatFragment;
import com.example.appfororg.fragment.ListOfChatsFragment;
import com.example.appfororg.fragment.SignInFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AppApiVolley implements AppApi {

    public static final String API_TEST = "API_TEST";
    private final Context context;
    public static final String BASE_URL = "http://192.168.1.103:8081";
    private Response.ErrorListener errorListener;


    public AppApiVolley(Context context) {
        this.context = context;
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(API_TEST, error.toString());
                error.printStackTrace();
            }
        };
    }

    @Override
    public void addOrganization(Organization organization) {
        String url = BASE_URL + "/organization";

        JSONObject params = new JSONObject();
        try {
            params.put("id", organization.getId());
            params.put("name", organization.getName());
            params.put("type", organization.getType());
            params.put("login", organization.getLogin());
            params.put("organizationPhoto", organization.getPhotoOrg());
            params.put("description", organization.getDescription());
            params.put("address", organization.getAddress());
            params.put("needs", organization.getNeeds());
            params.put("linkToWebsite", organization.getLinkToWebsite());
            params.put("password", organization.getPass());
        } catch (JSONException e) {
            Log.e("API_TASK", e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                OpenHelper openHelper = new OpenHelper(context,
                        "op", null, OpenHelper.VERSION);
                if (!openHelper.findAllOrganizations().contains(organization))
                    openHelper.insertOrg(organization);
                Log.d(API_TEST, response.toString());
            }
        }, errorListener
        );
        RequestQueue referenceQueue = Volley.newRequestQueue(context);
        referenceQueue.add(jsonObjectRequest);
    }


    @Override
    public void updateOrganization(int id, String name, String login, String type,
                                   String photoOrg, String description, String address, String needs,
                                   String linkToWebsite, String pass) {
        String url = BASE_URL + "/organization/" + id;
        JSONObject params = new JSONObject();
        try {
            params.put("id", id);
            params.put("name", name);
            params.put("type", type);
            params.put("login", login);
            params.put("organizationPhoto", photoOrg);
            params.put("description", description);
            params.put("address", address);
            params.put("needs", needs);
            params.put("linkToWebsite", linkToWebsite);
            params.put("password", pass);
        } catch (JSONException e) {
            Log.e("API_TASK_UPD_ORG", e.getMessage());
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("API_TEST_UPD_ORG", response.toString());
            }
        }, errorListener
        );
        RequestQueue referenceQueue = Volley.newRequestQueue(context);
        referenceQueue.add(jsonObjectRequest);


    }

    @Override
    public void fillChats() {
        String url = BASE_URL + "/chat";
        RequestQueue referenceQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        OpenHelper openHelper = new OpenHelper(context,
                                "op", null, OpenHelper.VERSION);
                        openHelper.deleteAllChat();
                        openHelper.deleteAllPeople();
                        Log.e("API_TEST_FILL_CHAT", response.length() + "");
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Chat chat = ChatMapper.chatFromJson(jsonObject, context);
                                openHelper.insertChat(chat);
                                Log.e("FILL CHAT", chat.toString());
                                try {
                                    Log.e("AFTER FILL CHAT", openHelper.findAllChatId().toString());
                                } catch (Exception e) {
                                    Log.e("AFTER FILL CHAT", e.getMessage());
                                }
                            }
                        } catch (JSONException e) {
                            Log.e("API_TEST", e.getMessage());
                        }
                    }
                },
                errorListener);
        jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 30000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        referenceQueue.add(jsonArrayRequest);
    }

    @Override
    public void fillPeople() {
        String url = BASE_URL + "/person";
        RequestQueue referenceQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        OpenHelper openHelper = new OpenHelper(
                                context, "op", null, OpenHelper.VERSION);
                        openHelper.deleteAllPeople();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Person person = PersonMapper.personFromJson(jsonObject, context);

                                ArrayList<String> arrListName = new ArrayList<String>();
                                for (int j = 0; j < openHelper.findAllPeople().size(); j++) {
                                    arrListName.add(openHelper.findAllPeople().get(j).getName());
                                }

                                if(!arrListName.contains(person.getName())) {
                                    String data = person.getEmail() == null || person.getEmail().isEmpty() ? person.getTelephone() : person.getEmail();
                                    openHelper.insertPerson(new Person(
                                            data, person.getName(), person.getAge(),
                                            person.getPhotoPer(), person.getDateOfBirth(),
                                            person.getCity()));
                                }
                                else{
                                    openHelper.changePhotoByPersonLogin(person.getName(), person.getPhotoPer());
                                }
                            }

                        }catch (JSONException e) {
                            Log.e("API_TEST_FILL_ORG", e.getMessage());
                        }
                    }
                },
                errorListener);
        referenceQueue.add(jsonArrayRequest);
    }

    @Override
    public void fillOrganization() {
        String url = BASE_URL + "/organization";
        RequestQueue referenceQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        OpenHelper openHelper = new OpenHelper(
                                context, "op", null, OpenHelper.VERSION);
                        openHelper.deleteAllOrganization();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Organization organization = OrganizationMapper.
                                        organizationFromJson(jsonObject, context);

                                ArrayList<String> arrListName = new ArrayList<String>();
                                for (int j = 0; j < openHelper.findAllOrganizations().size(); j++) {
                                    arrListName.add(openHelper.findAllOrganizations().get(j).getName());
                                }

                                if(!arrListName.contains(organization.getName())) {
                                    openHelper.insertOrg(new Organization(organization.getName(),
                                            organization.getLogin(), organization.getType(),
                                            organization.getPhotoOrg(),
                                            organization.getDescription(), organization.getAddress(),
                                            organization.getNeeds(), organization.getLinkToWebsite(),
                                            organization.getPass()));
                                }
                                else{
                                    openHelper.changeDescByLog(organization.getLogin(),
                                            organization.getDescription());
                                    openHelper.changeNeedsByLog(organization.getLogin(),
                                            organization.getNeeds());
                                    openHelper.changePhotoByOrgLog(organization.getLogin(),
                                            organization.getPhotoOrg());
                                }
                            }

                        }catch (JSONException e) {
                            Log.e("API_TEST_FILL_ORG", e.getMessage());
                        }
                    }
                },
                errorListener);
        referenceQueue.add(jsonArrayRequest);
    }

    @Override
    public void deleteChatById(int id) {
        String url = BASE_URL + "/chat/" + id;
        RequestQueue referenceQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fillChats();
                        Log.d(API_TEST, response);
                    }
                },
                errorListener);
        referenceQueue.add(stringRequest);
    }

    @Override
    public void fillMsg() {
        String url = BASE_URL + "/message";
        RequestQueue referenceQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        OpenHelper openHelper = new OpenHelper(context,
                                "op", null, OpenHelper.VERSION);
                        openHelper.deleteAllMessage();
                        Log.e("API_TEST_FILL_MSG", response.length() + "");
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Message message = MessageMapper.messageFromJson(jsonObject, context);
                                openHelper.insertMsg(message);
                                Log.e("FILL MSG", message.toString());
                                try {
                                    Log.e("AFTER FILL MSG", openHelper.findAllMsgVal().toString());
                                } catch (Exception e) {
                                    Log.e("AFTER FILL MSG", e.getMessage());
                                }
                            }
                        } catch (JSONException e) {
                            Log.e("API_TEST", e.getMessage());
                        }
                    }
                },
                errorListener);
        referenceQueue.add(jsonArrayRequest);
    }

    @Override
    public void addMessages(Message message) {
        OpenHelper openHelper = new OpenHelper(context,
                "op", null, OpenHelper.VERSION);
        String url = BASE_URL + "/message";
        JSONObject params = new JSONObject();
        try {
            params.put("id", message.getId());
            params.put("whose", message.getWhose());
            params.put("value", message.getValues());
            params.put("time", message.getTime());
            params.put("chatDto",getChatJson(openHelper.findChatById(message.getChat_id())));
        } catch (JSONException e) {
            Log.e("API_TASK_ADD_MSG", e.getMessage());
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("API_TEST_ADD_MSG", response.toString());
            }
        }, errorListener
        );
        RequestQueue referenceQueue = Volley.newRequestQueue(context);
        referenceQueue.add(jsonObjectRequest);
    }

    @NonNull
    private JSONObject getChatJson(Chat chat) {
        JSONObject params = new JSONObject();
        SharedPreferences sharedPreferences = SignInFragment.sharedPreferences;
        try {
            params.put("id", chat.getId());

            JSONObject person = new JSONObject();
            person.put("id", chat.getPerson().getId());
            person.put("name", chat.getPerson().getName());
            person.put("telephone", chat.getPerson().getTelephone());
            person.put("email", chat.getPerson().getEmail());
            person.put("city", chat.getPerson().getCity());
            person.put("photo", chat.getPerson().getPhotoPer());
            person.put("date_of_birth", chat.getPerson().getDateOfBirth());
            person.put("age", chat.getPerson().getAge());
            person.put("password", sharedPreferences.getString("per_pass" + chat.getPerson().getName(), "Password Not Found!"));


            params.put("personDto", person);

            JSONObject org = new JSONObject();
            Organization organization = chat.getOrganization();
            org.put("id", organization.getId());
            org.put("name", organization.getName());
            org.put("type", organization.getType());
            org.put("login", organization.getLogin());
            org.put("organizationPhoto", chat.getOrganization().getPhotoOrg());
            org.put("description", organization.getDescription());
            org.put("address", organization.getAddress());
            org.put("needs", organization.getNeeds());
            org.put("linkToWebsite", organization.getLinkToWebsite());
            org.put("password", organization.getPass());



            params.put("organizationDto", org);
            Log.e("aavac", params.toString());
        } catch (JSONException e) {
            Log.e("API_TASK_ADD_CHAT", e.getMessage());
        }
        return params;
    }

    @Override
    public void checkNewMsg() {
        String url = BASE_URL + "/message/size";
        RequestQueue referenceQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int size = Integer.parseInt(response);
                OpenHelper openHelper = new OpenHelper(context, "op",
                        null, OpenHelper.VERSION);
                if(openHelper.findAllMsg().size() != size){
                    fillMsg();

                }
            }
        }, errorListener);
        referenceQueue.add(stringRequest);
    }
}