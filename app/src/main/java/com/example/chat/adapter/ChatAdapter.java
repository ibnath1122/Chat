package com.example.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.Cont;
import com.example.chat.MessageModel;
import com.example.chat.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private ArrayList<MessageModel> messageList;
    private Context context;
    private int send=1;
    private int receive=2;
    private boolean status=false;
    private String senderUID;

    public ChatAdapter(ArrayList<MessageModel> messageList, Context context)
    {
        this.messageList=messageList;
        this.context=context;
        senderUID= FirebaseAuth.getInstance().getUid();
    }


    @NonNull
    @Override

    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = null;
        if(viewType==send)
        {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.send_singl_layout, parent, false);
        }
        else if(viewType==receive)
        {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.receive_singl_layout, parent, false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position)
    {

        holder.message.setText(messageList.get(position).getMessage());
        holder.tvTime.setText(Cont.getTime(messageList.get(position).getMsTime()));

        holder.tvDate.setText(Cont.getDate(messageList.get(position).getMsTime()));
        if(position==0)
        {
            holder.tvDate.setVisibility(View.VISIBLE);
        }
        else
        {
            String date= Cont.getDate(messageList.get(position).getMsTime());
            String previousDate= Cont.getDate(messageList.get(position).getMsTime());
            if(!date.equals(previousDate))
            {
                holder.tvDate.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.tvDate.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        //return 0;
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        if(messageList.get(position).getFrom().equals(senderUID))
        {
            status=true;
            return send;
        }
        else
        {
            status=false;
            return receive;
        }

    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{

        TextView message, tvTime, tvDate;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            if(status)
            {
                message=itemView.findViewById(R.id.send_message_tv);
                tvTime=itemView.findViewById(R.id.tv_send_time);
                tvDate=itemView.findViewById(R.id.tv_send_date);
            }
            else
            {
                message=itemView.findViewById(R.id.receive_message_tv);
                tvTime=itemView.findViewById(R.id.tv_receive_time);
                tvDate=itemView.findViewById(R.id.tv_receive_date);
            }
        }
    }

}