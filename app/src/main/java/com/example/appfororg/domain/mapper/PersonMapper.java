package com.example.appfororg.domain.mapper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.appfororg.R;
import com.example.appfororg.domain.Person;
import com.example.appfororg.fragment.SignInFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PersonMapper {
    public static Person personFromJson(JSONObject jsonObject, Context context) {
        Person person = null;
        try {
            String data = jsonObject.getString("telephone");
            if(data.isEmpty() ||
                    data.equals("null")) {
                data = jsonObject.getString("email");
            }else {
                data = jsonObject.getString("telephone");
            }
            SharedPreferences sharedPreferences = SignInFragment.sharedPreferences;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("per_pass" + jsonObject.getString("name"),
                    jsonObject.getString("password"));
            editor.putString("per_fav_org" + jsonObject.getString("name"),
                    jsonObject.getString("favourite_organization"));
            editor.commit();
            person = new Person(jsonObject.getInt("id"), data,
                    jsonObject.getString("name"), jsonObject.getInt("age"),
                    jsonObject.getString("photo"),
                    jsonObject.getString("date_of_birth"),
                    jsonObject.getString("city"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return person;
    }

    public static Person personFromChatJson(JSONObject jsonObject, Context context) {
        Person person = null;
        try {
            person = personFromJson(jsonObject.getJSONObject("personDto"), context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return person;
    }
}
