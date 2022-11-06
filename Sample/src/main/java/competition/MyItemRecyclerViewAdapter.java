package competition;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luseen.spacenavigationview.R;

import java.util.List;

import competition.UserStep;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private Context mcontext;
    private final List<UserStep> mUser;

    public MyItemRecyclerViewAdapter(List<UserStep> mUser) {
        this.mUser = mUser;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview, parent, false);
        return new ViewHolder(view);
    }

    //視圖持有者的位置
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        UserStep userStep=mUser.get(position);
        holder.rank.setText((CharSequence) userStep.getRank());
        holder.uid.setText(userStep.getUid());
        holder.mystep.setText(String.valueOf(userStep.getStep()));

        if (userStep.getUid().equals("jjLin")){
            holder.pic.setImageResource(R.drawable.man121);
        }else if (userStep.getUid().equals("琳琳")){
            //Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
            holder.pic.setImageResource(R.drawable.meow121);
        }else if (userStep.getUid().equals("翁祥恩")){
            holder.pic.setImageResource(R.drawable.joe061);
        }else if (userStep.getUid().equals("yatsan")){
            holder.pic.setImageResource(R.drawable.yatsan121);
        }else if (userStep.getUid().equals("雅琪")){
            holder.pic.setImageResource(R.drawable.yaya09011);
        }else if (userStep.getUid().equals("HDwang")){
            holder.pic.setImageResource(R.drawable.hdwang101);
        }else {
            holder.pic.setImageResource(R.drawable.ic_action_face2);
        }
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public  TextView rank;
        public TextView uid;
        public TextView mystep;
        public ImageView pic;

        //每个視圖持有者負責顯示一個带有視圖的項
        public ViewHolder(View view) {
            super(view);
            mView = view;
            rank = view.findViewById(R.id.rank);
            uid = view.findViewById(R.id.uid);
            mystep =view.findViewById(R.id.mystep);
            pic = view.findViewById(R.id.pic);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + uid.getText() + "'";
        }
    }
}