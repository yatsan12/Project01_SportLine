package sportfieldsearch;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.luseen.spacenavigation.SpaceOnLongClickListener;
import com.luseen.spacenavigationview.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import calendar_1.MainCalendarActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import step.activity.MainActivitytest;
import ttu.mis.lr0816.ChatActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    CircleImageView profile_image;
    private String uname;
    private Spinner spinnerCounty, spinnerCity, spinnerType;
    private String[][] cityArea = {
            {"士林區", "中正區", "大同區", "大安區", "中山區", "內湖區", "文山區", "北投區", "松山區", "信義區", "南港區", "萬華區"}, // 臺北市
            {"七堵區", "中山區", "中正區", "仁愛區", "安樂區", "信義區", "暖暖區"}, // 基隆市
            {"八里區", "三芝區", "三重區", "三峽區", "土城區", "中和區", "五股區", "平溪區", "永和區", "石門區", "石碇區", "汐止區",
                    "坪林區", "板橋區", "林口區", "金山區", "泰山區", "烏來區", "貢寮區", "淡水區", "深坑區", "新店區", "新莊區", "瑞芳區", "萬里區", "樹林區", "蘆洲區"}, // 新北市
            {"八德區", "大園區", "大溪區", "中壢區", "平鎮區", "桃園區", "復興區", "新屋區", "楊梅區", "龍潭區", "龜山區", "蘆竹區", "觀音區"}, // 桃園市
            {"北區", "東區", "香山區"}, //新竹市
            {"三義鄉", "三灣鄉", "大湖鄉", "公館鄉", "竹南鎮", "西湖鄉", "卓蘭鎮", "南庄鄉", "後龍鎮", "苑裡鎮", "苗栗市", "泰安鄉", "通霄鎮", "造橋鄉", "獅潭鄉", "銅鑼鄉", "頭份市", "頭屋鄉"}, // 苗栗縣
            {"大甲區", "大安區", "大肚區", "大里區", "大雅區", "中區", "太平區", "北屯區", "北區", "外埔區", "石岡區", "后里區", "西屯區",
                    "西區", "沙鹿區", "和平區", "東區", "東勢區", "南屯區", "南區", "烏日區", "神岡區", "梧棲區", "清水區", "新社區", "潭子區", "龍井區", "豐原區", "霧峰區"}, // 台中市
            {"二水鄉", "二林鎮", "大村鄉", "大城鄉", "北斗鎮", "永靖鄉", "田中鎮", "田尾鄉", "竹塘鄉", "秀水鄉", "和美鎮", "社頭鄉", "員林市", "彰化市", "溪湖鎮"}, // 彰化縣
            {"中寮鄉", "仁愛鄉", "水里鄉", "名間鄉", "竹山鎮", "信義鄉", "南投市", "埔里鎮", "草屯鎮", "國姓鄉", "魚池鄉", "鹿谷鄉", "集集鎮"}, // 南投縣
            {"二崙鄉", "口湖鄉", "土庫鎮", "大埤鄉", "元長鄉", "斗六市", "斗南鎮", "水林鄉", "北港鎮", "古坑鄉", "四湖鄉", "西螺鎮", "東勢鄉", "林內鄉", "虎尾鎮", "崙背鄉", "麥寮鄉"}, // 雲林縣
            {"大林鎮", "大埔鄉", "中埔鄉", "六腳鄉", "太保市", "水上鄉", "布袋鎮", "民雄鄉", "竹崎鄉", "東石鄉", "阿里山鄉", "梅山鄉", "鹿草鄉", "番路鄉", "新港鄉", "溪口鄉", "義竹鄉"}, // 嘉義縣
            {"西區", "東區"}, // 嘉義市
            {"七股區", "下營區", "大內區", "山上區", "中西區", "仁德區", "六甲區", "北門區", "北區", "左鎮區", "永康區", "玉井區", "白河區", "安平區", "安定區", "安南區", "西港區", "佳里區",
                    "官田區", "東山區", "東區", "南化區", "南區", "後壁區", "柳營區", "將軍區", "麻豆區", "善化區", "新市區", "新營區"}, // 臺南市
            {"三民區", "大社區", "大寮區", "大樹區", "小港區", "仁武區", "內門區", "六龜區", "左營區", "永安區", "田寮區", "甲仙區", "杉林區", "那瑪夏區", "岡山區",
                    "前鎮區", "美濃區", "苓雅區", "桃源區", "楠梓區", "路竹區", "鼓山區", "旗山區", "旗津區", "鳳山區", "橋頭區", "燕巢區", "鹽埕區"}, // 高雄市
            {"九如鄉", "三地門鄉", "內埔鄉", "竹田鄉", "牡丹鄉", "車城鄉", "里港鄉", "佳冬鄉", "來義鄉", "東港鎮", "枋寮鄉", "林邊鄉", "南州鄉", "屏東市"
                    , "恆春鎮", "春日鄉", "琉球鎮"}, // 屏東縣
            {"三星鄉", "大同鄉", "五結鄉", "冬山鄉", "壯圍鄉", "宜蘭市", "南澳鄉", "員山鄉", "頭城鎮", "礁溪鄉", "羅東鎮", "蘇澳鎮"}, // 宜蘭縣
            {"玉里鎮", "光復鄉", "吉安鄉", "秀林鄉", "卓溪鄉", "花蓮市", "富里鄉", "新城鄉", "瑞穗鄉", "萬榮鄉", "壽豐鄉", "鳳林鎮", "豐濱鄉"}, // 花蓮縣
            {"大武鄉", "太麻里鄉", "成功鎮", "池上鄉", "卑南鄉", "延平鄉", "東河鄉", "金峰鄉", "長濱鄉", "海端鄉", "鹿野鄉", "綠島鄉", "關山鎮", "蘭嶼鄉"}, // 台東縣
            {"七美鄉", "白沙鄉", "西嶼鄉", "馬公市", "望安鄉", "湖西鄉"}, // 澎湖縣
            {"金沙鎮", "金城鎮", "金湖鎮", "金寧鄉", "烈嶼鄉", "烏坵鄉"}, // 金門縣
            {"北竿鄉", "東引鄉", "南竿鄉", "莒光鄉"} // 連江縣馬祖
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1017);
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
                intent = new Intent(MainActivity.this, com.luseen.spacenavigationview.ActivityWithBadge.class);
                startActivity(intent);
            }
        });

        spinnerCity = findViewById(R.id.spinnerCounty); // 縣市
        spinnerCounty = findViewById(R.id.spinnerCity); // 區鄉鎮
        spinnerType = findViewById(R.id.spinnerType);
        ImageButton btnSearch = findViewById(R.id.btnSearch);
        populateSpinnerCounty();
        //populateSpinnerCity();
        populateSpinnerType();
        spinnerCity.setOnItemSelectedListener(this);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String county = spinnerCounty.getSelectedItem().toString();
                String city = spinnerCity.getSelectedItem().toString();
                String type = spinnerType.getSelectedItem().toString();
                Toast.makeText(MainActivity.this, city + "," + county + "," + type, Toast.LENGTH_SHORT).show();
                search(city, county, type);
            }
        });
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
                Intent intent = new Intent(MainActivity.this, competition.CompetitionActivity.class);
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
                Toast.makeText(MainActivity.this, "onCentreButtonLongClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(int itemIndex, String itemName) {
                Toast.makeText(MainActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
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
                intent = new Intent(MainActivity.this, calendar_1.MainCalendarActivity.class);
                intent.putExtra("name", uname);
                break;
            case 1:
                intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("name", uname);
                break;
            default: // case 2:
                intent = new Intent(MainActivity.this, sportfieldsearch.MainActivity.class);
                intent.putExtra("name", uname);
                break;
            case 3:
                intent = new Intent(MainActivity.this, step.activity.MainActivitytest.class);
                intent.putExtra("name", uname);
                break;
        }
        startActivity(intent);
    }


    private void search(String city, String county, String type) {
        new NukeSSLCerts().nuke(); // 避免下述 com.android.volley.NoConnectionError

        RequestQueue mQueue = Volley.newRequestQueue(this);
        final String url = "https://iplay.sa.gov.tw/api/GymSearchAllList?$format=application/json;odata.metadata=none&City=" + city + "&Country=" + county + "&GymType=" + type;
        Log.d("URL", url);

        // Request a JSONObject response from the provided URL.
        // JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() { // JSONObject>() {
                    @Override
                    public void onResponse(JSONArray res) { // JSONObject response) {
                        try {
                            Log.d("Response: ", res.toString());
                            // Log.d("Response2: ", response.getJSONArray("value").toString());
                            // JSONArray jArr = response.getJSONArray("value");
                            JSONObject jObj;
                            final String[] name = new String[res.length()];
                            final String[] photo=new String[res.length()];
                            final String[] addr = new String[res.length()];
                            final String[] tel = new String[res.length()];
                            final String[] list = new String[res.length()];
                            final String[] attr = new String[res.length()];
                            final String[] state = new String[res.length()];
                            final String[] lat = new String[res.length()];
                            final String[] lng = new String[res.length()];
                            for (int i = 0; i < res.length(); i++) {
                                jObj = (JSONObject) res.get(i);
                                name[i] = jObj.getString("Name");
                                photo[i]=jObj.getString("Photo1");
                                addr[i] = jObj.getString("Address");
                                tel[i] = jObj.getString("OperationTel");
                                list[i] = jObj.getString("GymFuncList");
                                attr[i] = jObj.getString("LandAttrName");
                                state[i] = jObj.getString("RentState");
                                String[] tokens = jObj.getString("LatLng").split(",");
                                lat[i] = tokens[0];
                                lng[i] = tokens[1];

                                Log.d("NameArr", name[i]);
                                Log.d("PhotoArr",photo[i]);
                                Log.d("AddrArr", addr[i]);
                                Log.d("TelArr", tel[i]);
                                Log.d("ListArr", list[i]);
                                Log.d("AttrArr", attr[i]);
                                Log.d("RentArr", state[i]);
                                Log.d("LatArr",lat[i]);
                                Log.d("LngArr",lng[i]);
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(sportfieldsearch.MainActivity.this);
                            if(res.length()!=0) {

                                builder.setTitle(R.string.searchRes)
                                        .setItems(name, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int pos) {
                                                Intent intent = new Intent(sportfieldsearch.MainActivity.this, sportfieldsearch.InfoActivity.class);
                                                intent.putExtra("Name", name[pos]); //名稱
                                                intent.putExtra("Photo", photo[pos]); //照片
                                                intent.putExtra("Address", addr[pos]); //地址
                                                intent.putExtra("OperationTel", tel[pos]); //連絡電話
                                                intent.putExtra("GymFuncList", list[pos]); //設施清單
                                                intent.putExtra("LandAttrName", attr[pos]); //設施屬性
                                                intent.putExtra("RentState", state[pos]);//租借狀態
                                                intent.putExtra("Latitude", lat[pos]); //緯度
                                                intent.putExtra("Longitude", lng[pos]);//經度
                                                startActivity(intent);
                                            }
                                        }).show();
                            }else { builder.setTitle(R.string.searchRes)
                            .setMessage("此區域無符合搜尋條件場所").show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VolleyError: ", error.toString());
                        // com.android.volley.NoConnectionError: javax.net.ssl.SSLHandshakeException: java.security.cert.CertPathValidatorException: Trust anchor for certification path not found
                    }
                });

        // Add the request to the RequestQueue.
        mQueue.add(jsonArrayRequest);
    }

    private void populateSpinnerType() {
        String[] Type = {"籃球", "足球", "游泳", "羽球", "排球", "網球", "棒球", "桌球", "健身", "跑步", "高爾夫球", "其他"};
        ArrayAdapter<String> TypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Type);
        TypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(TypeAdapter);
    }
    private void populateSpinnerCounty() {
        String[] city = getResources().getStringArray(R.array.spinner_city);
        ArrayAdapter<String> CountyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, city);
        CountyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(CountyAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] tempSet = cityArea[position];

        ArrayAdapter<String> City = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tempSet);
        City.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCounty.setAdapter(City);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    // https://newfivefour.com/android-trust-all-ssl-certificates.html
    // Android: Trust all SSL certificates in Volley
    public static class NukeSSLCerts {
        protected static final String TAG = "NukeSSLCerts";

        public static void nuke() {
            try {
                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() {
                                X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                                return myTrustedAnchors;
                            }

                            @Override
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            }
                        }
                };

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });
            } catch (Exception e) {
            }
        }
    }



}