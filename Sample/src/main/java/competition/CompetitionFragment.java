package competition;

import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.common.util.DataUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luseen.spacenavigationview.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import competition.MyItemRecyclerViewAdapter;
import competition.UserStep;
import model.User;

/**
 * A fragment representing a list of Items.
 */
public class CompetitionFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private RecyclerView recyclerView;
    List<UserStep> mUser = new ArrayList<>();
    private TextView myidTv;
    private TextView mystepTv;
    private ImageView mypic;
    private String uname;
    private TextView rank;
    private ImageView winnerImage;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CompetitionFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CompetitionFragment newInstance(int columnCount) {
        CompetitionFragment fragment = new CompetitionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competition_list, container, false);
        recyclerView = view.findViewById(R.id.list);
        myidTv = view.findViewById(R.id.uid);
        mystepTv = view.findViewById(R.id.mystep);
        rank = view.findViewById(R.id.rank);
        winnerImage = view.findViewById(R.id.winnerImage);
        mypic = view.findViewById(R.id.mypic);

        uname = getActivity().getIntent().getExtras().getString("name");

        // Set the adapter
        if (recyclerView != null) { // view instanceof RecyclerView) {
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            //Firebase
            // FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();  //獲取"當前登入"的用戶
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Steps");

            reference.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUser.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String uid = snapshot.getKey(); //getkey名字支線
                        Log.d("steps", uid);
                        long step = (long) snapshot.child("step").getValue(); //.child 得分支-步數
                        Log.d("steps", "" + step);

                        mUser.add(new UserStep("0", uid, step));
                        if (uid.equals(uname)) {
                            myidTv.setText(uid);
                            mystepTv.setText(String.valueOf(step));
                            if (uid.equals("jjLin")) {
                                mypic.setImageResource(R.drawable.man121);
                            } else if (uid.equals("琳琳")) {
                                //Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
                                mypic.setImageResource(R.drawable.meow121);
                            } else if (uid.equals("翁祥恩")) {
                                mypic.setImageResource(R.drawable.joe061);
                            } else if (uid.equals("yatsan")) {
                                mypic.setImageResource(R.drawable.yatsan121);
                            } else if (uid.equals("雅琪")) {
                                mypic.setImageResource(R.drawable.yaya09011);
                            } else if (uid.equals("HDwang")) {
                                mypic.setImageResource(R.drawable.hdwang101);
                            } else {
                                mypic.setImageResource(R.drawable.ic_action_face2);
                            }
                        }
                    }
                    // myItemRecyclerViewAdapter.notifyDataSetChanged();
                    Log.d("steps", mUser.toString());

                    Log.d("mUser", mUser.toString());
                    //Collections.sort(mUser);
                    mUser.sort(Collections.<UserStep>reverseOrder());
                    Log.d("mUser", mUser.toString());

                    //先由大到小排序再逐一印12345 最大1 第二大2 ...
                    for (int i = 0; i < mUser.size(); i++) {
                        mUser.get(i).setRank(String.valueOf(i + 1));
                    }

                    //mUser(0)=第一名 抓她的id是誰就設誰的drawable
                    int bigPic = R.drawable.ic_action_face2;
                    switch (mUser.get(0).getUid()) {
                        case "琳琳":
                            bigPic = R.drawable.meow_len;
                            break;
                        case "翁祥恩":
                            bigPic = R.drawable.joe_len;
                            break;
                        case "yatsan":
                            bigPic = R.drawable.yatsan_len;
                            break;
                        case "雅琪":
                            bigPic = R.drawable.yaya_len;
                            break;
                        case "HDwang":
                            bigPic = R.drawable.hdwang_len;
                            break;
                        case "jjLin":
                            bigPic = R.drawable.man_len;
                            break;
                    }
                    winnerImage.setImageResource(bigPic);
                    // winnerImage = mUser.get(0).setPicture(R.drawable.logo99);

                    myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(mUser);
                    recyclerView.setAdapter(myItemRecyclerViewAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        return view;
    }

    //讀取User
    private void listUser() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();  //獲取"當前登入"的用戶
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Steps");

        //final List<String> names = new ArrayList<>();
        //final List<Integer> steps = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey();
                    Log.d("steps", uid);
                    long step = (long) snapshot.child("step").getValue();
                    Log.d("steps", "" + step);
                    // userStep = snapshot.getValue(UserStep.class);
                    //UserStep userStep = snapshot.getValue(UserStep.class);
//                        int step = snapshot.child(name).getValue(Integer.class);
                    // if (!userStep.getUid().equals("yourname")) { // userStep.getUid().equals(firebaseUser.getUid())) {
                    //names.add(name);
                    //steps.add(step);
                    // mUser.add(new UserStep(userStep.getUid(), R.drawable.ic_action_face, userStep.getStep()));
                    //mUser.add(new UserStep(rank,uid, R.drawable.ic_action_face, step));
                    //}
                }
                // myItemRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}