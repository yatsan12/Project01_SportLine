package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.luseen.spacenavigationview.R;

import java.util.List;

import model.Chat;
import model.User;
import ttu.mis.lr0816.MessageActivity;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPE_RIGHT=1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;

    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Chat> mChat,String imageurl){
        this.mContext=mContext;
        this.mChat=mChat;
        this.imageurl=imageurl;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message=itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.profile_image);
            txt_seen =itemView.findViewById(R.id.txt_seen);
        }
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat=mChat.get(position);

        holder.show_message.setText(chat.getMessage());


        if (imageurl.equals("jjLin")){
            holder.profile_image.setImageResource(R.drawable.man121);
        }else if (imageurl.equals("琳琳")){
            //Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
            holder.profile_image.setImageResource(R.drawable.meow121);
        }else if (imageurl.equals("翁祥恩")){
            holder.profile_image.setImageResource(R.drawable.joe061);
        }else if (imageurl.equals("yatsan")){
            holder.profile_image.setImageResource(R.drawable.yatsan1211);
        }else if (imageurl.equals("雅琪")){
            holder.profile_image.setImageResource(R.drawable.yaya09011);
        }else if (imageurl.equals("HDwang")){
            holder.profile_image.setImageResource(R.drawable.hdwang101);
        }else {
            holder.profile_image.setImageResource(R.drawable.ic_action_face2);
        }


//        if (imageurl.equals("ywang")){
//            holder.profile_image.setImageResource(R.mipmap.ic_launcher_round);
//        } else if (imageurl.equals("yaya")) {
//            holder.profile_image.setImageResource(R.drawable.ic_action_face2);
//        }else if (imageurl.equals("yatsan")){
//            holder.profile_image.setImageResource(R.drawable.logo99);

       //     holder.profile_image.setImageResource(R.drawable.ic_action_face2);

        //check for last message
        if (position ==mChat.size()-1){
            if (chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            }else{
                holder.txt_seen.setText("delivered");
            }
        }else{
            holder.txt_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
