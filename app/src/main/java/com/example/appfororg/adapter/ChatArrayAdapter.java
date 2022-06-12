package com.example.appfororg.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfororg.fragment.ChatFragment;
import com.example.appfororg.OpenHelper;
import com.example.appfororg.R;
import com.example.appfororg.domain.Message;

import java.util.ArrayList;

public class ChatArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private final int height  = Resources.getSystem().getDisplayMetrics().heightPixels;
    private final int width  = Resources.getSystem().getDisplayMetrics().widthPixels;
    private float scale = Resources.getSystem().getDisplayMetrics().density;
    private LayoutInflater inflater;
    private ChatFragment fragment;
    private ArrayList<Message> allMsg = new ArrayList<>();

    public ChatArrayAdapter(Context context, ChatFragment fragment, int chatId) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.fragment = fragment;
        OpenHelper openHelper = new OpenHelper(context, "op", null, OpenHelper.VERSION);
        allMsg = openHelper.findMsgByChatId(chatId);
    }

    @Override
    public int getItemViewType(int position) {
        if(allMsg.get(position).getWhose().equals("org")) return 0;
        else return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View itemLayoutView;
        switch (viewType){
            case 0:
                itemLayoutView = inflater.inflate(R.layout.msg_my, parent, false);
                vh = new HolderZeroType(itemLayoutView);
                break;
            case 1:
                itemLayoutView = inflater.inflate(R.layout.msg_other, parent, false);
                vh = new HolderFirstType(itemLayoutView);
                break;
            default:
                itemLayoutView = inflater.inflate(R.layout.msg_other, parent, false);
                vh = new HolderFirstType(itemLayoutView);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (this.getItemViewType(position)){
            case 0:
                HolderZeroType zero = (HolderZeroType) holder;
                zero.main.setText(allMsg.get(position).getValues());
                zero.time.setText(allMsg.get(position).getTime());
                break;
            case 1:
                HolderFirstType first = (HolderFirstType) holder;
                first.main.setText(allMsg.get(position).getValues());
                first.time.setText(allMsg.get(position).getTime());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return allMsg.size();
    }

    public class HolderZeroType extends RecyclerView.ViewHolder{
        TextView main, time;
        public HolderZeroType(@NonNull View itemView) {
            super(itemView);
            main = itemView.findViewById(R.id.tv_myMsg);
            time = itemView.findViewById(R.id.tv_myMsg_time);
            main.setMaxWidth((int) (width * 0.7));
            if (width > height) {
                main.setTextSize((float) (width / 160));
                int size20 = (int) (scale * (width / 80) + 0.5f);
                int size15 = (int) (scale * (width / 110) + 0.5f);
                int size3 = (int) (scale * (width / 297) + 0.5f);

                main.setPadding(size20, 0, size20, size20);
                time.setPadding(size15, 0, size15,size3);
                time.setTextSize((float) (width / 182));
                main.requestLayout();
                time.requestLayout();
            } else {
                main.setTextSize((float) (height / 160));
                time.setTextSize((float) (height / 182));
                int size20 = (int) (scale * (height / 80) + 0.5f);
                int size15 = (int) (scale * (height / 110) + 0.5f);
                int size3 = (int) (scale * (height / 297) + 0.5f);

                main.setPadding(size20, size3, size20, size20);
                time.setPadding(size15, size3, size15,size3);
            }
        }
    }
    public class HolderFirstType extends RecyclerView.ViewHolder{
        TextView main, time;
        public HolderFirstType(@NonNull View itemView) {
            super(itemView);
            main = itemView.findViewById(R.id.tv_otherMsg);
            time = itemView.findViewById(R.id.tv_otherMsg_time);
            main.setMaxWidth((int) (width * 0.7));
            if (width > height) {
                main.setTextSize((float) (width / 160));
                int size20 = (int) (scale * (width / 80) + 0.5f);
                int size15 = (int) (scale * (width / 110) + 0.5f);
                int size3 = (int) (scale * (width / 297) + 0.5f);

                main.setPadding(size20, 0, size20, size20);
                time.setPadding(size15, 0, size15,size3);
                time.setTextSize((float) (width / 182));
                main.requestLayout();
                time.requestLayout();
            } else {
                main.setTextSize((float) (height / 160));
                time.setTextSize((float) (height / 182));
                int size20 = (int) (scale * (height / 80) + 0.5f);
                int size15 = (int) (scale * (height / 110) + 0.5f);
                int size3 = (int) (scale * (height / 297) + 0.5f);

                main.setPadding(size20, size3, size20, size20);
                time.setPadding(size15, size3, size15,size3);
            }
        }

    }
}
