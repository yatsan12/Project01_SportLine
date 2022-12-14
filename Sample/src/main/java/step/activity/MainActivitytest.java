package step.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.luseen.spacenavigationview.ActivityWithBadge;
import com.luseen.spacenavigationview.R;

import calendar_1.MainCalendarActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import step.step.UpdateUiCallBack;
import step.step.service.StepService;
import step.step.utils.SharedPreferencesUtils;
import step.view.StepArcView;
import ttu.mis.lr0816.ChatActivity;

/**
 */
public class MainActivitytest extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_data;
    private StepArcView cc;
    private TextView tv_set;
    TextView tv_isSupport;
    private EditText tv_step_number;
    private SharedPreferencesUtils sp;
    private String walk_qty;
    CheckBox cb_remind;
    String remind;
    TextView tv_remind_time;
    String achieveTime;
    ImageButton btn_save;
    private String uname;
    private ImageView step_pic;
    CircleImageView profile_image;

    private void assignViews() {
        tv_data = findViewById(R.id.tv_data);
        cc = findViewById(R.id.cc);
//        tv_set = findViewById(R.id.tv_set);
//        tv_isSupport = findViewById(R.id.tv_isSupport);
        tv_step_number = findViewById(R.id.tv_step_number);
        btn_save = findViewById(R.id.btn_save);
        step_pic = findViewById(R.id.step_pic);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maindylantest);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.home);
        uname = getIntent().getExtras().getString("name");
        getSupportActionBar().setTitle(uname);
        profile_image = new CircleImageView(this);


        if (uname.equals("jjLin")){
            profile_image.setImageResource(R.drawable.man1211);
        }else if (uname.equals("??????")){
            //Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
            profile_image.setImageResource(R.drawable.meow1211);
        }else if (uname.equals("?????????")){
            profile_image.setImageResource(R.drawable.joe0611);
        }else if (uname.equals("yatsan")){
            profile_image.setImageResource(R.drawable.yatsan1211);
        }else if (uname.equals("??????")){
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
                intent = new Intent(MainActivitytest.this, com.luseen.spacenavigationview.ActivityWithBadge.class);
                startActivity(intent);
            }
        });

//        if (uname.equals("jjLin")){
//            step_pic.setImageResource(R.drawable.jjlinnn);
//        }else if (uname.equals("??????")){
//            //Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
//            step_pic.setImageResource(R.drawable.abc00);
//        }else if (uname.equals("?????????")){
//            step_pic.setImageResource(R.drawable.asd00);
//        }else if (uname.equals("yatsan")){
//            step_pic.setImageResource(R.drawable.yatsan);
//        }else if (uname.equals("??????")){
//            step_pic.setImageResource(R.drawable.yaya09);
//        }else if (uname.equals("HDwang")){
//            step_pic.setImageResource(R.drawable.hdwang);
//        }else {
//            step_pic.setImageResource(R.drawable.ic_action_face2);
//        }



        assignViews();
        initData();
        addListener();
        SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        //spaceNavigationView.changeSpaceBackgroundColor(ContextCompat.getColor(this,R.color.quantum_pink));
        spaceNavigationView.addSpaceItem(new SpaceItem(R.id.navigation_first, "??????", R.drawable.book));
        spaceNavigationView.addSpaceItem(new SpaceItem(R.id.navigation_second, "??????", R.drawable.chatroom));
        spaceNavigationView.addSpaceItem(new SpaceItem(R.id.navigation_third, "??????", R.drawable.searchfield));
        spaceNavigationView.addSpaceItem(new SpaceItem(R.id.navigation_forth, "??????", R.drawable.clock));
        spaceNavigationView.shouldShowFullBadgeText(false);
        spaceNavigationView.setCentreButtonIcon(R.drawable.logo99);

        spaceNavigationView.setCentreButtonId(R.id.navigation_centre);

        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false);
        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {


            @Override
            public void onCentreButtonClick() {
                Log.d("onCentreButtonClick ", "onCentreButtonClick");
                Intent intent = new Intent(MainActivitytest.this, competition.CompetitionActivity.class);
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
                Toast.makeText(MainActivitytest.this, "onCentreButtonLongClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(int itemIndex, String itemName) {
                Toast.makeText(MainActivitytest.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });
        //setUpRecyclerView();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
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
                intent = new Intent(MainActivitytest.this, calendar_1.MainCalendarActivity.class);
                intent.putExtra("name", uname);
                break;
            case 1:
                intent = new Intent(MainActivitytest.this, ChatActivity.class);
                intent.putExtra("name", uname);
                break;
            default: // case 2:
                intent = new Intent(MainActivitytest.this, sportfieldsearch.MainActivity.class);
                intent.putExtra("name", uname);
                break;
            case 3:
                intent = new Intent(MainActivitytest.this, step.activity.MainActivitytest.class);
                intent.putExtra("name", uname);
                break;
        }
        startActivity(intent);
    }


/*
    private void initData() {
        sp = new SharedPreferencesUtils(this);
        //?????????????????????????????????????????????????????????????????????7000
        String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
        //?????????????????????0
        cc.setCurrentCount(Integer.parseInt(planWalk_QTY), 0);
        tv_isSupport.setText("?????????...");
        setupService();
    }
*/
    public void initData() {//??????????????????
        sp = new SharedPreferencesUtils(this);
        String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
        cc.setCurrentCount(Integer.parseInt(planWalk_QTY), 0);

        if (!planWalk_QTY.isEmpty()) {
            if ("0".equals(planWalk_QTY)) {
                tv_step_number.setText("7000");
            } else {
                tv_step_number.setText(planWalk_QTY);
            }
        }
        setupService();
    }

    private void setupService() {
        Intent intent = new Intent(this, step.step.service.StepService.class); //step.activity.SetPlanActivity.class);
        intent.putExtra("uname", uname);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    private void save() {
        walk_qty = tv_step_number.getText().toString().trim();
        if (walk_qty.isEmpty() || "0".equals(walk_qty)) {
            sp.setParam("planWalk_QTY", "7000");
        } else {
            sp.setParam("planWalk_QTY", walk_qty);
        }
    finish();
}

    private boolean isBind = false;

    /**
     * ??????????????????
     */
    /**
     * ???????????????????????????application Service?????????????????????interface???
     * ??????????????????????????????Service ??? context.bindService()???????????????
     * ?????????????????????????????????????????????ServiceConnection????????????????????????????????????????????????
     */
    ServiceConnection conn = new ServiceConnection() {
        /**
         * ???????????????Service???????????????????????????????????????Android?????????IBind?????????????????????????????????
         * @param name ?????????????????????Service????????????
         * @param service ????????????????????????IBind???????????????Service??????????????????
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //?????????????????????
            String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
            cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepService.getStepCount());

            //????????????????????????
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
                    cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepCount);
                }
            });
        }

        /**
         * ??????Service???????????????????????????????????????????????????
         * ???????????????????????????Service??????????????????????????????Kill??????????????????
         * ????????????????????????Service????????????????????????????????????????????????????????? onServiceConnected()???
         * @param name ???????????????????????????
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_data:
                intent = new Intent(MainActivitytest.this, HistoryActivity.class);
                intent.putExtra("name", uname);
                startActivity(intent);
                break;
            case R.id.btn_save:
                save();
                break;
        }
    }

    public void addListener() {
        btn_save.setOnClickListener(this);
        tv_data.setOnClickListener(this);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            this.unbindService(conn);
        }
    }
}
