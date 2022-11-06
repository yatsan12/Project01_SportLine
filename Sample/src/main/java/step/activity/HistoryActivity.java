package step.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.luseen.spacenavigation.SpaceOnLongClickListener;
import com.luseen.spacenavigationview.R;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Text;

import java.util.List;


import calendar_1.MainCalendarActivity;
import calendar_1.MyCalendarView;
import de.hdodenhof.circleimageview.CircleImageView;
import step.adapter.CommonAdapter;
import step.adapter.CommonViewHolder;
import step.step.bean.StepData;
import step.step.utils.DbUtils;
import ttu.mis.lr0816.ChatActivity;


public class HistoryActivity extends AppCompatActivity {
    private LinearLayout layout_titlebar;
    private ImageView iv_left;
    private ImageView iv_right;
    private ListView lv;
    TextView text_ac;
    CircleImageView profile_image;
    private String uname;

    private void assignViews() {
        layout_titlebar = (LinearLayout) findViewById(R.id.layout_titlebar);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        lv = (ListView) findViewById(R.id.lv);
        text_ac = (TextView)findViewById(R.id.text_ac);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_history);
        assignViews();
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
                intent = new Intent(HistoryActivity.this, com.luseen.spacenavigationview.ActivityWithBadge.class);
                startActivity(intent);
            }
        });

        //iv_left.setOnClickListener(new View.OnClickListener() {
        //@Override
        //public void onClick(View v) {
        //   finish();
        //}
        // });
       /*
        text_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
*/

        initData();
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
                Intent intent = new Intent(HistoryActivity.this, competition.CompetitionActivity.class);
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
                Toast.makeText(HistoryActivity.this, "onCentreButtonLongClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(int itemIndex, String itemName) {
                Toast.makeText(HistoryActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
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
                intent = new Intent(HistoryActivity.this, calendar_1.MainCalendarActivity.class);
                intent.putExtra("name", uname);
                break;
            case 1:
                intent = new Intent(HistoryActivity.this, ChatActivity.class);
                intent.putExtra("name", uname);
                break;
            default: // case 2:
                intent = new Intent(HistoryActivity.this, sportfieldsearch.MainActivity.class);
                intent.putExtra("name", uname);
                break;
            case 3:
                intent = new Intent(HistoryActivity.this, step.activity.MainActivitytest.class);
                intent.putExtra("name", uname);
                break;
        }
        startActivity(intent);
    }

    private void initData() {
        setEmptyView(lv);
        if(DbUtils.getLiteOrm()==null){
            DbUtils.createDb(this, "jingzhi");
        }
        List<StepData> stepDatas =DbUtils.getQueryAll(StepData.class);
        Logger.d("stepDatas="+stepDatas);
        lv.setAdapter(new CommonAdapter<StepData>(this,stepDatas,R.layout.item) {
            @Override
            protected void convertView(View item, StepData stepData) {
                TextView tv_date= CommonViewHolder.get(item,R.id.tv_date);
                TextView tv_step= CommonViewHolder.get(item,R.id.tv_step);
                tv_date.setText(stepData.getToday());
                tv_step.setText(stepData.getStep()+"步");
            }
        });
    }

    protected <T extends View> T setEmptyView(ListView listView) {
        TextView emptyView = new TextView(this);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText("暫無數據！");
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
        return (T) emptyView;
    }
}
