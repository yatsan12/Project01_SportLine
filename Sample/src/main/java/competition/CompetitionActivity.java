package competition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.luseen.spacenavigation.SpaceOnLongClickListener;
import com.luseen.spacenavigationview.R;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import calendar_1.MainCalendarActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import model.User;
import ttu.mis.lr0816.ChatActivity;

public class CompetitionActivity extends AppCompatActivity {

    //FirebaseUser firebaseUser;
    //DatabaseReference reference;
    CircleImageView profile_image;
    List<competition.UserStep> mUser;
    List<competition.UserStep> mystep;
    private competition.MyItemRecyclerViewAdapter MyItemRecyclerViewAdapter;
    private String uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.home);
        uname = getIntent().getExtras().getString("name");
        getSupportActionBar().setTitle(uname);
        profile_image = new CircleImageView(this);


        if (uname.equals("jjLin")){
            profile_image.setImageResource(R.drawable.man1211);
        }else if (uname.equals("琳琳")){
            //Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
            profile_image.setImageResource(R.drawable.meow1211);
        }else if (uname.equals("翁祥恩")){
            profile_image.setImageResource(R.drawable.joe0611);
        }else if (uname.equals("yatsan")){
            profile_image.setImageResource(R.drawable.yatsan1211);
        }else if (uname.equals("雅琪")){
            profile_image.setImageResource(R.drawable.yaya090111);
        }else if (uname.equals("HDwang")){
            profile_image.setImageResource(R.drawable.hdwang1011);
        }else {
            profile_image.setImageResource(R.drawable.ic_action_face2);
        }
        toolbar.setLogo(profile_image.getDrawable());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            Intent intent;

            @Override
            public void onClick(View view) {
                intent = new Intent(CompetitionActivity.this, com.luseen.spacenavigationview.ActivityWithBadge.class);
                startActivity(intent);
            }
        });

        mUser=new ArrayList<>();
        mystep=new ArrayList<>();


        SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        //spaceNavigationView.changeSpaceBackgroundColor(ContextCompat.getColor(this,R.color.quantum_pink));
        spaceNavigationView.addSpaceItem(new SpaceItem(R.id.navigation_first, "日記", R.drawable.book));
        spaceNavigationView.addSpaceItem(new SpaceItem(R.id.navigation_second, "聊天", R.drawable.chatroom));
        spaceNavigationView.addSpaceItem(new SpaceItem(R.id.navigation_third, "場所", R.drawable.searchfield));
        spaceNavigationView.addSpaceItem(new SpaceItem(R.id.navigation_forth, "計步", R.drawable.clock));
        spaceNavigationView.shouldShowFullBadgeText(false);
        spaceNavigationView.setCentreButtonIcon(R.drawable.logo99);

        spaceNavigationView.setCentreButtonId(R.id.navigation_centre);

        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false);
        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {

            @Override
            public void onCentreButtonClick() {
                Log.d("onCentreButtonClick ", "onCentreButtonClick");
                Intent intent = new Intent(CompetitionActivity.this, competition.CompetitionActivity.class);
                intent.putExtra("name", uname);
                startActivity(intent);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                Log.d("onItemClick", "" + itemIndex);
                itemAction(itemIndex, itemName);
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Log.d("onItemReselected ", "" + itemIndex + " " + itemName);
                itemAction(itemIndex, itemName);
            }
        });


        spaceNavigationView.setSpaceOnLongClickListener(new SpaceOnLongClickListener() {
            @Override
            public void onCentreButtonLongClick() {
                Toast.makeText(CompetitionActivity.this, "onCentreButtonLongClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(int itemIndex, String itemName) {
                Toast.makeText(CompetitionActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });
        //setUpRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                //finish();
                startActivity(new Intent(this, ttu.mis.lr0816.LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;
            case R.id.profile:
                Intent it = new Intent(this, ttu.mis.lr0816.ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                it.putExtra("name", uname);
                startActivity(it);
                return true;
        }
        return false;
    }


    public void itemAction(int itemIndex, String itemName) {
        Intent intent;
        switch (itemIndex) {
            case 0:
                intent = new Intent(CompetitionActivity.this, calendar_1.MainCalendarActivity.class);
                intent.putExtra("name", uname);
                break;
            case 1:
                intent = new Intent(CompetitionActivity.this, ChatActivity.class);
                intent.putExtra("name", uname);
                break;
            default: // case 2:
                intent = new Intent(CompetitionActivity.this, sportfieldsearch.MainActivity.class);
                intent.putExtra("name", uname);
                break;
            case 3:
                intent = new Intent(CompetitionActivity.this, step.activity.MainActivitytest.class);
                intent.putExtra("name", uname);
                break;
        }
        startActivity(intent);
    }

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        uname = getIntent().getExtras().getString("name");
//        getSupportActionBar().setTitle(uname);
//        toolbar.setLogo(R.drawable.ic_action_face2);

        //firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //reference= FirebaseDatabase.getInstance().getReference("Steps").child(firebaseUser.getUid());



    }

