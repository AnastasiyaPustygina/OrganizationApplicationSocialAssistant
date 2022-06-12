package com.example.appfororg.fragment;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.appfororg.OpenHelper;
import com.example.appfororg.R;
import com.example.appfororg.domain.Chat;
import com.example.appfororg.domain.Message;
import com.example.appfororg.domain.Organization;
import com.example.appfororg.rest.AppApiVolley;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class RegFragment extends Fragment {

    private RadioButton hospice, nursingHome, orphanHome;
    private RadioGroup radioGroup;
    private EditText et_name, et_log, et_address, et_link, et_pass, et_checkPass;
    private AppCompatButton bt_reg_fr_reg;
    private TextView checking;
    private String type = "";
    private final int height  = Resources.getSystem().getDisplayMetrics().heightPixels;
    private final int width  = Resources.getSystem().getDisplayMetrics().widthPixels;
    private float scale = Resources.getSystem().getDisplayMetrics().density;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reg_fragment, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();

        radioGroup = getActivity().findViewById(R.id.rg_reg);
        hospice = getActivity().findViewById(R.id.rbt_reg_hospice);
        nursingHome = getActivity().findViewById(R.id.rbt_reg_nursingHome);
        orphanHome = getActivity().findViewById(R.id.rbt_reg_orphanHome);
        et_name = getActivity().findViewById(R.id.ed_reg_name);
        et_log = getActivity().findViewById(R.id.ed_reg_log);
        et_address = getActivity().findViewById(R.id.ed_reg_address);
        et_link = getActivity().findViewById(R.id.ed_reg_link);
        et_pass = getActivity().findViewById(R.id.ed_reg_pass1);
        et_checkPass = getActivity().findViewById(R.id.ed_reg_pass2);
        bt_reg_fr_reg = getActivity().findViewById(R.id.bt_reg_fr_reg);
        checking = getActivity().findViewById(R.id.checking);
        int data = Math.max(width, height);
        int size20 = (int) (scale * (data / 80) + 0.5f);
        int size10 = (int) (scale * (data / 140) + 0.5f);
        int size60 = (int) (scale * (data / 30) + 0.5f);
        int size30 = (int) (scale * (data / 70) + 0.5f);
        int size80 = (int) (scale * (data / 20) + 0.5f);
        float sizeForTV15 = (float) data / 160;

        TextView tv_header = getActivity().findViewById(R.id.tv_reg_header);
        tv_header.setTextSize((float) data / 85);
        tv_header.setPadding(size30, size60, 0, 0);
        TextView tv_forName= getActivity().findViewById(R.id.tv_reg_forName);
        tv_forName.setPadding(size20, size60, 0, 0);
        tv_forName.setTextSize(sizeForTV15);

        ViewGroup.MarginLayoutParams nameParams = (ViewGroup.MarginLayoutParams) et_name.getLayoutParams();
        nameParams.setMargins(size20, size10, size20, size30);
        et_name.requestLayout();

        ViewGroup.MarginLayoutParams logParams = (ViewGroup.MarginLayoutParams) et_log.getLayoutParams();
        logParams.setMargins(size20, size10, size20, size30);
        et_log.requestLayout();

        ViewGroup.MarginLayoutParams addressParams = (ViewGroup.MarginLayoutParams) et_address.getLayoutParams();
        addressParams.setMargins(size20, size10, size20, size30);
        et_address.requestLayout();

        ViewGroup.MarginLayoutParams linkParams = (ViewGroup.MarginLayoutParams) et_link.getLayoutParams();
        linkParams.setMargins(size20, size10, size20, size30);
        et_link.requestLayout();

        ViewGroup.MarginLayoutParams pass1Params = (ViewGroup.MarginLayoutParams) et_pass.getLayoutParams();
        pass1Params.setMargins(size20, size10, size20, size30);
        et_pass.requestLayout();

        ViewGroup.MarginLayoutParams pass2Params = (ViewGroup.MarginLayoutParams) et_checkPass.getLayoutParams();
        pass2Params.setMargins(size20, size10, size20, size30);
        et_checkPass.requestLayout();

        ViewGroup.MarginLayoutParams btParams = (ViewGroup.MarginLayoutParams) bt_reg_fr_reg.getLayoutParams();
        btParams.setMargins(size30, size60, size30, size80);
        bt_reg_fr_reg.requestLayout();

        et_name.setTextSize(sizeForTV15);
        TextView tv_forLog= getActivity().findViewById(R.id.tv_reg_forLog);
        tv_forLog.setPadding(size30, size10, 0, 0);
        tv_forLog.setTextSize(sizeForTV15);
        et_log.setTextSize(sizeForTV15);
        TextView tv_forType = getActivity().findViewById(R.id.tv_reg_forType);
        tv_forType.setTextSize(sizeForTV15);
        tv_forType.setPadding(size30, size20,0, 0);
        radioGroup.setPadding(size30, size10, 0, size20);
        RadioButton rb_hospice  = getActivity().findViewById(R.id.rbt_reg_hospice);
        rb_hospice.setTextSize(sizeForTV15);
        RadioButton rb_orphanHome  = getActivity().findViewById(R.id.rbt_reg_orphanHome);
        rb_orphanHome.setTextSize(sizeForTV15);
        RadioButton rb_nursingHome  = getActivity().findViewById(R.id.rbt_reg_nursingHome);
        rb_nursingHome.setTextSize(sizeForTV15);
        TextView tv_forAddress = getActivity().findViewById(R.id.tv_reg_forAddress);
        tv_forAddress.setTextSize(sizeForTV15);
        tv_forAddress.setPadding(size30, size20, 0, 0);
        et_address.setTextSize(sizeForTV15);
        TextView tv_forLink = getActivity().findViewById(R.id.tv_reg_forLink);
        tv_forLink.setTextSize(sizeForTV15);
        tv_forLink.setPadding(size30, size20, 0, 0);
        et_link.setTextSize(sizeForTV15);
        TextView tv_notNecessary = getActivity().findViewById(R.id.tv_reg_notNecessary);
        tv_notNecessary.setTextSize(sizeForTV15);
        tv_notNecessary.setPadding(size30, size20, 0, 0);
        TextView tv_forPass1 = getActivity().findViewById(R.id.tv_reg_forPass);
        tv_forPass1.setTextSize(sizeForTV15);
        tv_forPass1.setPadding(size30, size20, 0, 0);
        et_pass.setTextSize(sizeForTV15);
        TextView tv_forPass2 = getActivity().findViewById(R.id.tv_reg_forPass2);
        tv_forPass2.setTextSize(sizeForTV15);
        tv_forPass2.setPadding(size30, size20, 0, 0);
        et_checkPass.setTextSize(sizeForTV15);
        bt_reg_fr_reg.setTextSize((float) data / 150);



        try {
            radioGroup.setOnCheckedChangeListener(
                    new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            switch (i) {
                                case -1:
                                    checking.setText("Не выбран тип организации");
                                case R.id.rbt_reg_hospice:
                                    type = hospice.getText().toString();
                                    break;
                                case R.id.rbt_reg_orphanHome:
                                    type = orphanHome.getText().toString();
                                    break;
                                case R.id.rbt_reg_nursingHome:
                                    type = nursingHome.getText().toString();
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
            bt_reg_fr_reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OpenHelper openHelper = new OpenHelper(getContext(),
                            "op", null, OpenHelper.VERSION);
                    if (!et_pass.getText().toString().equals(et_checkPass.getText().toString()))
                        checking.setText("Пароли не совпадают");
                    else if(openHelper.findOrgByLogin(et_log.getText().toString()).getId() != -1
                            || openHelper.findOrgByAddress(et_address.getText().toString())
                            .getId() != -1) checking.setText("Такая организация уже существует");
                    else {
                        String name = et_name.getText().toString();
                        String pass = et_pass.getText().toString();
                        String login = et_log.getText().toString();
                        String address = et_address.getText().toString();
                        String linkToWebsite = et_link.getText().toString();
                        if (name.isEmpty() ||
                                type.isEmpty() ||
                                pass.isEmpty()
                                || login.isEmpty() ||
                                address.isEmpty())
                            checking.setText("Не все поля заполнены");
                        else {
                            if(linkToWebsite.isEmpty()) linkToWebsite = "(не указан)";
                            String encodedHash = null;
                            try {
                                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                                encodedHash = Arrays.toString(digest.digest(
                                        pass.getBytes(StandardCharsets.UTF_8)));
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            Organization organization = new Organization(
                                    name, login, type, null, "",
                                    address, "", linkToWebsite,  encodedHash);
                            openHelper.insertOrg(organization);
                            new AppApiVolley(getContext()).addOrganization(openHelper.findOrgByLogin(login));
                            bt_reg_fr_reg.setOnClickListener((view1) -> {
                                NavHostFragment.
                                        findNavController(RegFragment.this).navigate(
                                        R.id.action_regFragment_to_signInFragment);
                            });
                            bt_reg_fr_reg.performClick();
                        }
                    }
                }
            });
        }catch (Exception e) {
            Log.e("MY_LOG", e.getMessage());
        }
    }
}