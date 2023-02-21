package com.example.chat.adapter;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.Cont;
import com.example.chat.MessageModel;
import com.example.chat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    ArrayList<MessageModel> messageList;
    Context context;
    private int send=1;
    private int receive=2;
    private boolean status=false;
    private String senderUID;
    String recId;
    String type;
    TextView sendertv, receivertv;
    ImageView iv_sender,iv_receiver;


    public ChatAdapter(ArrayList<MessageModel> messageList, Context context)
    {
        this.messageList=messageList;
        this.context=context;
        senderUID= FirebaseAuth.getInstance().getUid();
    }

    public ChatAdapter(String recId) {
        this.recId = recId;
    }

    public ChatAdapter(Context context) {
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> messageList) {
        this.messageList = messageList;
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
        MessageModel messageModel = messageList.get(position);

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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderUID = FirebaseAuth.getInstance().getUid() + recId;
                                database.getReference().child("chats").child(senderUID)
                                        .child(messageModel.getMessageId())
                                        .setValue(null);

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                return false;
            }
        });

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

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView message, tvTime, tvDate;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            if (status) {
                message = itemView.findViewById(R.id.send_message_tv);
                tvTime = itemView.findViewById(R.id.tv_send_time);
                tvDate = itemView.findViewById(R.id.tv_send_date);

            } else {
                message = itemView.findViewById(R.id.receive_message_tv);
                tvTime = itemView.findViewById(R.id.tv_receive_time);
                tvDate = itemView.findViewById(R.id.tv_receive_date);
            }
        }

        /*public void setMessage(Application application, String message, String time, String date, String type, String senderuid, String receiveruid) {
            sendertv = itemView.findViewById(R.id.iv_sender);
            receivertv = itemView.findViewById(R.id.iv_receiver);

            iv_receiver = itemView.findViewById(R.id.iv_receiver);
            iv_sender = itemView.findViewById(R.id.iv_sender);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String currentUid = user.getUid();
            if (type.equals("Text")) {
                if (currentUid.equals(senderuid)) {
                    receivertv.setVisibility(View.GONE);
                    sendertv.setText(message);

                } else if (currentUid.equals(receiveruid)) {
                    sendertv.setVisibility(View.GONE);
                    receivertv.setText(message);

                }

            } else if (type.equals("iv")) {
                if (currentUid.equals(senderuid)) {
                    receivertv.setVisibility(View.GONE);
                    sendertv.setVisibility(View.GONE);
                    iv_sender.setVisibility(View.VISIBLE);
                    Picasso.get().load(message).into(iv_sender);


                } else if (currentUid.equals(receiveruid)) {
                    receivertv.setVisibility(View.GONE);
                    sendertv.setVisibility(View.GONE);
                    iv_receiver.setVisibility(View.VISIBLE);
                    Picasso.get().load(message).into(iv_receiver);


                } else {


                }

            }

        }*/
    }

}