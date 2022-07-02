package com.example.appfororg.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfororg.OpenHelper;
import com.example.appfororg.R;
import com.example.appfororg.fragment.ListOfChatsFragment;
import com.example.appfororg.domain.Person;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatListArrayAdapter extends RecyclerView.Adapter<ChatListArrayAdapter.ViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    private ListOfChatsFragment fragment;
    private String log;
    private final int width  = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final int height  = Resources.getSystem().getDisplayMetrics().heightPixels;
    private float scale = Resources.getSystem().getDisplayMetrics().density;
    private Map<Integer, String> mapListChatIdAndMsg;
    private ArrayList<Integer> arrayListChatId;
    private OpenHelper openHelper;
    private ArrayList<Person> arrayListLastPer;

    public ChatListArrayAdapter(Context context, ListOfChatsFragment fragment, String log) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.fragment = fragment;
        this.log = log;
        openHelper = new OpenHelper(context, "op", null, OpenHelper.VERSION);
        try {
            mapListChatIdAndMsg = openHelper.findLastChatId(log);
        }catch (CursorIndexOutOfBoundsException e){
            mapListChatIdAndMsg = new HashMap<>();
        }
        arrayListChatId = new ArrayList<>();
        for (Integer key:
                mapListChatIdAndMsg.keySet()) {
            arrayListChatId.add(key);
        }
        arrayListLastPer = new ArrayList<>();
        for (int i = 0; i < mapListChatIdAndMsg.size(); i++) {
            arrayListLastPer.add(openHelper.findPersonByChatId(arrayListChatId.get(i)));
        }
        Collections.reverse(arrayListChatId);
        Collections.reverse(arrayListLastPer);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.small_chat_window, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try{
                Picasso.get().load(arrayListLastPer.get(position).getPhotoPer()).into(holder.ivPerAva);
            }catch (Exception e){
                holder.ivPerAva.setImageDrawable(context.getResources().getDrawable(R.drawable.ava_for_project));
            }
            holder.lastMsg.setText(mapListChatIdAndMsg.get(arrayListChatId.get(position)));
            holder.tvNamePer.setText(arrayListLastPer.get(position).getName());
            Bundle bundle = new Bundle();
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    bundle.putString("LOG", log);
                    bundle.putString("NamePer", holder.tvNamePer.getText().toString());

                    holder.itemView.setOnClickListener((view1) -> {
                        NavHostFragment.
                                findNavController(fragment).navigate(
                                R.id.action_listOfChatsFragment_to_chatFragment, bundle);
                    });
                    holder.itemView.performClick();
                }
            });
    }

    @Override
    public int getItemCount() {
        OpenHelper openHelper = new OpenHelper(context, "op", null, OpenHelper.VERSION);
        try{
            return openHelper.findLastMsgValuesByOrgLog(log).size();
        }catch (CursorIndexOutOfBoundsException e){return 0;}
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivPerAva;
        TextView tvNamePer, lastMsg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPerAva = itemView.findViewById(R.id.iv_SmallChatWin_avaPer);
            tvNamePer = itemView.findViewById(R.id.tv_SmallChatWin_nameOfPer);
            lastMsg = itemView.findViewById(R.id.tv_SmallChatWin_lastMsg);
            int data = Math.max(width, height);
            int size20 = (int) (scale * (data / 80) + 0.5f);
            int size10 = (int) (scale * (data / 140) + 0.5f);
            tvNamePer.setTextSize((float) data / 140);
            lastMsg.setTextSize((float) data / 170);
            tvNamePer.setPadding(size10, size20, size10, size10);
            lastMsg.setPadding(size10, 0, size10, size10);
        }
    }
}