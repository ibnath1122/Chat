package com.example.chat.adapter;


import static com.google.android.material.internal.ContextUtils.getActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.ChatActivity;
import com.example.chat.MainActivity;
import com.example.chat.R;
import com.example.chat.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<UserModel>userArrayList;
    private Context context;
    public static final String SENDER_UID_KEY="sender_id";
    public static final String SENDER_NAME_KEY="sender_name";
    public static final String SENDER_IMAGE_URL_KEY="sender_image_url";

    public UserAdapter(ArrayList<UserModel> userArrayList, Context context)
    {
        this.userArrayList=userArrayList;
        this.context=context;
    }


    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_single_layout, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {

        UserModel model = userArrayList.get(position);
        Picasso
                .get()
                .load(model.getImageUri())
                .error(R.drawable.ic_baseline_account_circle_24)
                .into(holder.userImageView);

        holder.userName.setText(model.getName());
        holder.userBio.setText(model.getBio());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChatActivity.class) ; // context
                intent.putExtra(SENDER_UID_KEY,model.getId());
                intent.putExtra(SENDER_NAME_KEY, model.getName());
                intent.putExtra(SENDER_IMAGE_URL_KEY, model.getImageUri());
                context.startActivity(intent);

            }
        });

        if(model.isStatus())
        {
            holder.userStatusIV.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_active_user_12));
        }
        else
        {
            holder.userStatusIV.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_de_active_user_12));
        }

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView userImageView;
        TextView userName, userBio;
        CardView cardView;
        ImageView userStatusIV;

        public UserViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userImageView=itemView.findViewById(R.id.user_image_view);
            userName=itemView.findViewById(R.id.text_view_user_name);
            userBio=itemView.findViewById(R.id.user_bio_text_view);
            cardView=itemView.findViewById(R.id.card_view);
            userStatusIV=itemView.findViewById(R.id.iv_user_status);

        }
    }


}