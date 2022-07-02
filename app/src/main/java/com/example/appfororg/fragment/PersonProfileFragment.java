package com.example.appfororg.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.example.appfororg.OpenHelper;
import com.example.appfororg.R;
import com.example.appfororg.domain.Person;
import com.squareup.picasso.Picasso;

public class PersonProfileFragment extends Fragment {


    private TextView tv_age, tv_city, tv_dateOfBirth, tv_data, tv_name, tv_forData;
    private ImageView iv_ava, iv_arrow_back;
    private final int height  = Resources.getSystem().getDisplayMetrics().heightPixels;
    private final int width  = Resources.getSystem().getDisplayMetrics().widthPixels;
    private float scale = Resources.getSystem().getDisplayMetrics().density;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_profile_fragment, container, false);

        iv_ava = view.findViewById(R.id.iv_perProf_ava);
        tv_age = view.findViewById(R.id.tv_perProf_age);
        tv_city = view.findViewById(R.id.tv_perProf_city);
        tv_dateOfBirth = view.findViewById(R.id.tv_perProf_dateOfBirth);
        tv_data = view.findViewById(R.id.tv_perProf_data);
        tv_name = view.findViewById(R.id.tv_perProf_name);
        tv_forData = view.findViewById(R.id.tv_perProf_forData);
        iv_arrow_back = view.findViewById(R.id.bt_perProfile_arrowBack);

        int data = Math.max(width, height);
        int size20 = (int) (scale * (data / 80) + 0.5f);
        int size10 = (int) (scale * (data / 140) + 0.5f);
        int size50 = (int) (scale * (data / 37) + 0.5f);
        int size60 = (int) (scale * (data / 30) + 0.5f);
        int size30 = (int) (scale * (data / 60) + 0.5f);
        int size40 = (int) (scale * (data / 50) + 0.5f);
        LinearLayout llForPhotoAndName = view.findViewById(R.id.ll_perProf_forPhotoAndName);
        TextView tv_prof = view.findViewById(R.id.tv_perProf_profile);
        tv_prof.setTextSize((float) (data / 85));
        ViewGroup.MarginLayoutParams paramsProf = (ViewGroup.MarginLayoutParams) tv_prof.getLayoutParams();
        paramsProf.setMargins(0, size60, 0, size60);
        tv_prof.requestLayout();
        ViewGroup.MarginLayoutParams paramsArrow = (ViewGroup.MarginLayoutParams) iv_arrow_back.getLayoutParams();
        paramsArrow.setMargins(size10, 0, 0 ,0);
        iv_arrow_back.requestLayout();
        llForPhotoAndName.setPadding(size30, size10, size20, size20);
        tv_name.setTextSize((float) (data / 120));
        tv_name.setPadding(size50, size20, size20, size20);
        tv_forData.setTextSize((float) (data / 160));
        tv_forData.setPadding(size40, size30, size40, size10);
        tv_data.setTextSize((float) (data / 160));
        tv_data.setPadding(size40, 0, size40, size10);

        TextView tv_forDate = view.findViewById(R.id.tv_perProf_forDate);
        tv_forDate.setPadding(size40, size20, size40, size10);
        tv_forDate.setTextSize((float) (data / 160));
        tv_dateOfBirth.setPadding(size40, 0, size40, size10);
        tv_dateOfBirth.setTextSize((float) (data / 160));

        TextView tv_forAge = view.findViewById(R.id.tv_perProf_forAge);
        tv_forAge.setPadding(size40, size20, size40, size10);
        tv_forAge.setTextSize((float) (data / 160));
        tv_age.setPadding(size40, 0, size40, size10);
        tv_age.setTextSize((float) (data / 160));

        TextView tv_forCity = view.findViewById(R.id.tv_perProf_forCity);
        tv_forCity.setPadding(size40, size20, size40, size10);
        tv_forCity.setTextSize((float) (data / 160));
        tv_city.setPadding(size40, 0, size40, size50);
        tv_city.setTextSize((float) (data / 160));


        OpenHelper openHelper = new OpenHelper(getContext(), "op", null, OpenHelper.VERSION);
        Person person = openHelper.findPersonByLogin(getArguments().getString("NamePer"));

        try{
            Picasso.get().load(person.getPhotoPer()).into(iv_ava);
        }catch (Exception e){
            iv_ava.setImageDrawable(getResources().getDrawable(R.drawable.ava_for_project));
        }

        iv_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundleLog = new Bundle();
                bundleLog.putString("LOG", getArguments().getString("LOG"));
                bundleLog.putString("NamePer", getArguments().getString("NamePer"));
                iv_arrow_back.setOnClickListener((view1) -> {
                    NavHostFragment.
                            findNavController(PersonProfileFragment.this).navigate(
                            R.id.action_personProfileFragment_to_chatFragment, bundleLog);
                });
                iv_arrow_back.performClick();
            }
        });
        String dataValue;
        if(person.getTelephone() != null){
            if(person.getTelephone().isEmpty() || person.getTelephone().equals("null")){
                tv_forData.setText("Адрес электронной почты: ");
                dataValue = person.getEmail();
            } else{
                tv_forData.setText("Номер телефона : ");
                dataValue = person.getTelephone();
            }
        }else{
            tv_forData.setText("Адрес электронной почты: ");
            dataValue = person.getEmail();
        }
        tv_data.setText(dataValue);
        tv_name.setText(person.getName());
        tv_age.setText(String.valueOf(person.getAge()));
        tv_dateOfBirth.setText(person.getDateOfBirth());
        tv_city.setText(person.getCity());

        return view;
    }

}