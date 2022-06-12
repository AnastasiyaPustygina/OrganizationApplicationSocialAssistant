package com.example.appfororg.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfororg.OpenHelper;
import com.example.appfororg.R;
import com.example.appfororg.adapter.ChatArrayAdapter;
import com.example.appfororg.domain.Message;
import com.example.appfororg.domain.Organization;
import com.example.appfororg.domain.Person;
import com.example.appfororg.rest.AppApiVolley;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatFragment extends Fragment {

    private ImageView imOrg, ivMicro;
    private TextView namePer;
    private EditText et_msg;
    private ImageView bt_arrow_back;
    private AppCompatButton bt_update;
    private final int width  = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final int height  = Resources.getSystem().getDisplayMetrics().heightPixels;
    private float scale = Resources.getSystem().getDisplayMetrics().density;
    private MyChatThread myChatThread;



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        bt_arrow_back = getActivity().findViewById(R.id.bt_chat_arrowBack);
        et_msg = getActivity().findViewById(R.id.et_chat_msg);
        imOrg = getActivity().findViewById(R.id.iv_ch_imPer);
        ivMicro = getActivity().findViewById(R.id.iv_chat_micro);
        namePer = getActivity().findViewById(R.id.tv_ch_namePer);
        int data = Math.max(width, height);
        int size20 = (int) (scale * (data / 80) + 0.5f);
        int size10 = (int) (scale * (data / 140) + 0.5f);
        int size15 = (int) (scale * (data / 100) + 0.5f);
        int size5 = (int) (scale * (data / 320) + 0.5f);
        et_msg.setTextSize((float) data / 160);
        et_msg.setPadding(size15, size15, size5, size15);
        ConstraintLayout clForPhoto = getActivity().findViewById(R.id.cl_chat_forPhotoAndArrow);
        clForPhoto.setPadding(size10, 0, size20, 0);
        ConstraintLayout clForMicro = getActivity().findViewById(R.id.cl_chat_microAndClip);
        clForMicro.setPadding(size10, 0, size20, 0);
        clForMicro.setMinHeight(size20);
        imOrg.setPadding(size15, 0, 0, 0);
        namePer.setPadding(size15, size15, size15, size15);
        namePer.setTextSize((float) data / 160);
        ivMicro.setPadding(size5, 0, 0, 0);


        OpenHelper openHelper = new OpenHelper(getContext(), "op", null, OpenHelper.VERSION);
        int orgId = openHelper.findOrgByLogin(
                getArguments().getString("LOG")).getId();
        Person per = openHelper.findPersonByLogin(openHelper.findPersonByLogin(
                getArguments().getString("NamePer")).getName());
        ChatArrayAdapter recyclerAdapter;
        RecyclerView rec = getActivity().findViewById(R.id.rec_chat);
        try {
            recyclerAdapter = new ChatArrayAdapter(getContext(),
                    ChatFragment.this, openHelper.findChatIdByOrgIdAndPerId(orgId, per.getId()));
            rec.setAdapter(recyclerAdapter);
            rec.scrollToPosition(openHelper.findMsgByChatId(
                    openHelper.findChatIdByOrgIdAndPerId(per.getId(),orgId)).size() - 1);
        }catch (CursorIndexOutOfBoundsException e){
            Log.e("CHAT_FRAGMENT", e.getMessage());}
        bt_update = new AppCompatButton(getContext());
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenHelper openHelper = new OpenHelper(getContext(), "op", null,
                        OpenHelper.VERSION);
                ChatArrayAdapter recyclerAdapter;
                RecyclerView rec = getActivity().findViewById(R.id.rec_chat);
                int perId = openHelper.findPersonByLogin(
                        getArguments().getString("NamePer")).getId();
                Organization org = openHelper.findOrgByLogin(getArguments().getString("LOG"));
                recyclerAdapter = new ChatArrayAdapter(getContext(),
                        ChatFragment.this, openHelper.findChatIdByOrgIdAndPerId(org.getId(), perId));
                try {
                    rec.setAdapter(recyclerAdapter);
                    rec.scrollToPosition(openHelper.findMsgByChatId(
                            openHelper.findChatIdByOrgIdAndPerId(org.getId(), perId)).size() - 1);
                }catch (Exception e){
                    Log.e("UPDATE_ADAPTER", e.getMessage());
                }
            }
        });
        myChatThread = new MyChatThread(getContext());
        myChatThread.start();
        imOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myChatThread.changeBool();
                Bundle bundleForFullDesc = new Bundle();
                bundleForFullDesc.putString("LOG", getArguments().getString("LOG"));
                bundleForFullDesc.putString("NamePer", getArguments().getString("NamePer"));
                imOrg.setOnClickListener((view1) -> {
                    NavHostFragment.
                            findNavController(ChatFragment.this).navigate(
                            R.id.action_chatFragment_to_personProfileFragment, bundleForFullDesc);
                });
                imOrg.performClick();

            }
        });
        bt_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myChatThread.changeBool();
                Bundle bundleLog = new Bundle();
                bundleLog.putString("LOG", getArguments().getString("LOG"));
                bt_arrow_back.setOnClickListener((view1) -> {
                    NavHostFragment.
                            findNavController(ChatFragment.this).navigate(
                            R.id.action_chatFragment_to_listOfChatsFragment, bundleLog);
                });
                bt_arrow_back.performClick();
            }
        });

        try{
            if(per.getPhotoPer() == null)
                imOrg.setImageDrawable(getResources().getDrawable(R.drawable.ava_for_project));
            else Picasso.get().load(per.getPhotoPer()).into(imOrg);
        }catch (Exception e){
            imOrg.setImageDrawable(getResources().getDrawable(R.drawable.ava_for_project));
        }

        namePer.setText(per.getName());



        et_msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ivMicro.setImageDrawable(getResources().getDrawable(R.drawable.bt_send_msg));
                ivMicro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String curTime = new SimpleDateFormat(
                                "HH:mm", Locale.getDefault()).format(new Date());
                        Message myMsg = new Message("org",
                                openHelper.findChatIdByOrgIdAndPerId(orgId, per.getId()), et_msg.getText().toString(),
                                curTime);

                        openHelper.insertMsg(myMsg);
                        new AppApiVolley(getContext()).addMessages(
                                openHelper.findLastMessageByChatId(
                                        openHelper.findChatIdByOrgIdAndPerId(orgId, per.getId())));
                        ChatArrayAdapter recyclerAdapter1 = new ChatArrayAdapter(getContext(),
                                ChatFragment.this, openHelper.
                                findChatIdByOrgIdAndPerId(orgId, per.getId()));
                        rec.setAdapter(recyclerAdapter1);
                        rec.scrollToPosition(openHelper.findMsgByChatId(
                                openHelper.findChatIdByOrgIdAndPerId(orgId, per.getId())).size() - 1);
                        et_msg.setText("");
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et_msg.getText().toString().isEmpty()){
                    ivMicro.setImageDrawable(getResources().getDrawable(R.drawable.microphone));
                    ivMicro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
            }
        });

    }
    class MyChatThread extends Thread {
        private Context context;
        private OpenHelper openHelper;
        private boolean b = true;

        public MyChatThread(Context context) {
            this.context = context;
            openHelper = new OpenHelper(context, "OpenHelder", null, OpenHelper.VERSION);
        }

        @Override
        public void run() {
            while (b) {
                new AppApiVolley(context).checkNewMsg();
                try {
                    sleep(3 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!b) break;
                try {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                bt_update.performClick();
                            } catch (Exception e) {
                                Log.e("UPDATE_ADAPTER", e.getMessage());
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e("UPDATE_ADAPTER", e.getMessage());
                }
            }
        }

        public void changeBool() {
            b = !b;
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if(myChatThread.b) myChatThread.changeBool();
    }
}