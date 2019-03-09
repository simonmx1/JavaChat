package com.example.javachat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<Text> chat;

    public ChatAdapter(Context context, ArrayList<Text> chat) {
        this.context = context;
        this.chat = chat;

    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public static TextView text;
        public static TextView user;

        public MyViewHolder(View v) {
            super(v);
            user = v.findViewById(R.id.v_user);
            text = v.findViewById(R.id.v_text);
        }


    }





    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Log.d(" ","called");
        if(chat.get(i).isYou()){
            MyViewHolder.user.setGravity(Gravity.RIGHT);
            MyViewHolder.text.setGravity(Gravity.RIGHT);
        }
        MyViewHolder.user.setText(chat.get(i).getUser());
        MyViewHolder.text.setText(chat.get(i).getContent());
    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

}
