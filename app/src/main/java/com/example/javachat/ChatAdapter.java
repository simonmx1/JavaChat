package com.example.javachat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder>{
    private Context context;

    public ChatAdapter(Context context) {
        this.context = context;

    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{


        public MyViewHolder(View v) {
            super(v);
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


    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
