package com.example.appfororg.fragment;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfororg.OpenHelper;
import com.example.appfororg.adapter.ChatListArrayAdapter;
import com.example.appfororg.R;
import com.example.appfororg.rest.AppApiVolley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ListOfChatsFragment extends Fragment {

    private MyThread myThread;
    private RecyclerView recyclerView;
    private final int height  = Resources.getSystem().getDisplayMetrics().heightPixels;
    private final int width  = Resources.getSystem().getDisplayMetrics().widthPixels;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_of_chats_fragment, container, false);

        ImageView bt_listOfChats = view.findViewById(R.id.bt_listOfChats_chat);
        ConstraintLayout constraintLayout = view.findViewById(R.id.cl_listOfChats);
        TextView tv_chats = view.findViewById(R.id.tv_listOfChats_chats);
        ImageView bt_prof = view.findViewById(R.id.bt_listOfChats_profile);
        recyclerView = view.findViewById(R.id.rec_listOfChats);

        if (width > height) {
            tv_chats.setTextSize((float) (width / 80));
        } else {
            tv_chats.setTextSize((float) (height / 80));
        }
        String curTime = new SimpleDateFormat(
                "HH:mm:ss:mm", Locale.getDefault()).format(new Date());
        ChatListArrayAdapter chatListArrayAdaptor = new ChatListArrayAdapter(getContext(),
                ListOfChatsFragment.this, getArguments().getString("LOG"));
        recyclerView.setAdapter(chatListArrayAdaptor);
        float scale = Resources.getSystem().getDisplayMetrics().density;
        int data = Math.max(width, height);
        int size10 = (int) (scale * (data / 140) + 0.5f);
        int size5 = (int) (scale * (data / 320) + 0.5f);
        bt_prof.setPadding(0, size5, 0, size5);
        int size50 = (int) (scale * (data / 37) + 0.5f);
        constraintLayout.setMinHeight(size50);
        bt_listOfChats.setPadding(0, size10, 0, size5);
        Bundle bundleLog = new Bundle();
        bundleLog.putString("LOG", getArguments().getString("LOG"));

        bt_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_prof.setOnClickListener((view1) -> {
                    NavHostFragment.
                            findNavController(ListOfChatsFragment.this).navigate(
                            R.id.action_listOfChatsFragment_to_orgProfileFragment, bundleLog);
                });
                bt_prof.performClick();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        myThread = new MyThread(getContext());
        myThread.start();
    }

    public void updateAdapter(){
        ChatListArrayAdapter chatListArrayAdaptor = new ChatListArrayAdapter(getContext(),
                ListOfChatsFragment.this, getArguments().getString("LOG"));
        recyclerView.setAdapter(chatListArrayAdaptor);
    }

    class MyThread extends Thread {
        private Context context;
        private OpenHelper openHelper;
        private boolean b = true;

        public MyThread(Context context) {
            this.context = context;
            openHelper = new OpenHelper(context, "op", null, OpenHelper.VERSION);
        }

        @Override
        public void run() {
            try {
                while (b) {
                    new AppApiVolley(context).checkNewChat();
                    try {
                        sleep(3 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(!b) break;
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                recyclerView.getLayoutManager().onRestoreInstanceState(
                                        recyclerView.getLayoutManager().onSaveInstanceState());
                                updateAdapter();
                            }catch (Exception e){
                                Log.e("UPDATE_ADAPTER", e.getMessage());
                            }
                        }
                    });
                }
            }catch (Exception e){
                Log.e("CHAT_THREAD", e.getMessage());
            }
        }

        public void changeBool() {
            b = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        myThread.changeBool();
    }

}
