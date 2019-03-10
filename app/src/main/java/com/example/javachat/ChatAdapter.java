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
        public TextView text;
        public TextView user;

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
        //Log.d(" ","called");
        Text t = chat.get(i);

        if(t.isYou()){
            //Log.i("", "onBindViewHolder: " + t.getUser()+ " is You!!");
            holder.user.setGravity(Gravity.RIGHT);
            holder.text.setGravity(Gravity.RIGHT);
        }else{
            holder.user.setGravity(Gravity.LEFT);
            holder.text.setGravity(Gravity.LEFT);
        }
        holder.user.setText(t.getUser());
        holder.text.setText(t.getContent());
    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

}
