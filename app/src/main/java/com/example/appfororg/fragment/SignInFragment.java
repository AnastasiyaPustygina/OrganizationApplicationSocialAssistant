package com.example.appfororg.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.appfororg.OpenHelper;
import com.example.appfororg.R;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SignInFragment extends Fragment {
    private EditText ed_data;
    private EditText ed_pass;
    private AppCompatButton btSignIn, btReg;
    private final int height  = Resources.getSystem().getDisplayMetrics().heightPixels;
    private final int width  = Resources.getSystem().getDisplayMetrics().widthPixels;
    private float scale = Resources.getSystem().getDisplayMetrics().density;
    public static final String APP_PREFERENCES = "my_pref";
    public static SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_in_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        sharedPreferences = getContext().getSharedPreferences
                (APP_PREFERENCES, Context.MODE_PRIVATE);
        OpenHelper openHelper = new OpenHelper(getContext(), "op", null, OpenHelper.VERSION);
        TextView checking = getActivity().findViewById(R.id.tv_check);
        ed_data = getActivity().findViewById(R.id.ed_signIn_data);
        ed_pass = getActivity().findViewById(R.id.ed_signIn_pass);
        btSignIn = getActivity().findViewById(R.id.bt_signIn_fr_signIn);
        btReg = getActivity().findViewById(R.id.bt_reg_fr_signIn);

        int data = Math.max(width, height);
        int size70 = (int) (scale * (data / 18) + 0.5f);
        int size10 = (int) (scale * (data / 140) + 0.5f);
        int size60 = (int) (scale * (data / 30) + 0.5f);
        int size30 = (int) (scale * (data / 70) + 0.5f);
        int size50 = (int) (scale * (data / 40) + 0.5f);
        float sizeForTV15 = (float) data / 160;

        TextView tv_header = getActivity().findViewById(R.id.tv_signIn_header);
        tv_header.setTextSize((float) data / 85);
        tv_header.setPadding(size30, size60, 0, 0);
        TextView tv_forLog = getActivity().findViewById(R.id.tv_signIn_log);
        tv_forLog.setTextSize(sizeForTV15);
        tv_forLog.setPadding(size30, size50, 0, 0);
        ed_data.setTextSize(sizeForTV15);
        ed_pass.setTextSize(sizeForTV15);
        TextView tv_forPass = getActivity().findViewById(R.id.tv_signIn_pass);
        tv_forPass.setTextSize(sizeForTV15);
        tv_forPass.setPadding(size30, size30, size30, 0);


        ViewGroup.MarginLayoutParams logParams = (ViewGroup.MarginLayoutParams) ed_data.getLayoutParams();
        logParams.setMargins(size30, size10, size30, 0);
        ed_data.requestLayout();

        ViewGroup.MarginLayoutParams passParams = (ViewGroup.MarginLayoutParams) ed_pass.getLayoutParams();
        passParams.setMargins(size30, size10, size30, 0);
        ed_pass.requestLayout();

        ViewGroup.MarginLayoutParams signInParams = (ViewGroup.MarginLayoutParams) btSignIn.getLayoutParams();
        signInParams.setMargins(size30,size70, size30, size10);
        btSignIn.requestLayout();

        ViewGroup.MarginLayoutParams regParams = (ViewGroup.MarginLayoutParams) btReg.getLayoutParams();
        regParams.setMargins(size30,size10, size30, size50);
        btReg.requestLayout();

        btSignIn.setTextSize( (float) data / 150);
        TextView tv_or = getActivity().findViewById(R.id.tv_signIn_or);
        tv_or.setTextSize(sizeForTV15);
        btReg.setTextSize( (float) data / 150);

        btReg.setOnClickListener((view1) -> {
            NavHostFragment.findNavController(SignInFragment.this).navigate(
                    R.id.action_signInFragment_to_regFragment);
        });
        btSignIn.performClick();
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String encodedHash = "";
                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    encodedHash = Arrays.toString(digest.digest(
                            ed_pass.getText().toString().getBytes(StandardCharsets.UTF_8)));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                if (ed_pass.getText().toString().isEmpty() || ed_data.getText().toString().isEmpty())
                    checking.setText("Не все поля заполнены");
                else if (encodedHash.equals(openHelper.findPassByLogin(ed_data.getText().toString())))
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("LOG", ed_data.getText().toString());
                    btSignIn.setOnClickListener((view1) -> {
                        NavHostFragment.findNavController(SignInFragment.this).navigate(
                                R.id.action_signInFragment_to_orgProfileFragment, bundle);
                    });
                    btSignIn.performClick();
                } else {
                    checking.setText("Логин или пароль не верны ");
                }
            }
        });

    }

}