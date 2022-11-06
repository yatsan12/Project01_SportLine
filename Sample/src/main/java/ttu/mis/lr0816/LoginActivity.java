package ttu.mis.lr0816;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luseen.spacenavigationview.ActivityWithBadge;
import com.luseen.spacenavigationview.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import sportfieldsearch.MainActivity;

public class LoginActivity extends AppCompatActivity {

    //宣告
    EditText email, password;
    Button btn_login;
    TextView enterregister;
    FirebaseAuth auth;
    TextView forgot_psw;
    private String[] name, addr, lat, lng,gymFunc;
    private String[] name1, addr1, lat1, lng1,gymFunc1;
    private String[] name2, addr2, lat2, lng2,gymFunc2;
    private String[] name3, addr3, lat3, lng3,gymFunc3;
    private String[] name5, addr5, lat5, lng5,gymFunc5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {  //內建
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        //利用findViewById找到xml框架中的的元件
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        forgot_psw = findViewById(R.id.forgot_psw);
        enterregister = findViewById(R.id.enterregister);

        forgot_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ResetPswActivity.class));
            }
        });


        enterregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });


        //監聽login按鍵
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //按下登入鍵會做的動作
                String txt_email = email.getText().toString();  //宣告一個變數txt_email儲存使用者輸入的資料
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) ){
                    Toast.makeText(LoginActivity.this, "不可有欄位是空白的喔!!!", Toast.LENGTH_SHORT).show();
                }else{
                    auth.signInWithEmailAndPassword(txt_email, txt_password)  //方法
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        final Intent intent = new Intent(LoginActivity.this, ActivityWithBadge.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("lat", lat);
                                        intent.putExtra("lng", lng);
                                        intent.putExtra("addr", addr);
                                        intent.putExtra("sname", name);
                                        intent.putExtra("gymFunc",gymFunc);

                                        intent.putExtra("lat1", lat1);
                                        intent.putExtra("lng1", lng1);
                                        intent.putExtra("addr1", addr1);
                                        intent.putExtra("sname1", name1);
                                        intent.putExtra("gymFunc1",gymFunc1);

                                        intent.putExtra("lat2", lat2);
                                        intent.putExtra("lng2", lng2);
                                        intent.putExtra("addr2", addr2);
                                        intent.putExtra("sname2", name2);
                                        intent.putExtra("gymFunc2",gymFunc2);

                                        intent.putExtra("lat3", lat3);
                                        intent.putExtra("lng3", lng3);
                                        intent.putExtra("addr3", addr3);
                                        intent.putExtra("sname3", name3);
                                        intent.putExtra("gymFunc3",gymFunc3);

                                        intent.putExtra("lat5", lat5);
                                        intent.putExtra("lng5", lng5);
                                        intent.putExtra("addr5", addr5);
                                        intent.putExtra("sname5", name5);
                                        intent.putExtra("gymFunc5",gymFunc5);
                                        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users");
                                        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                String user_name = dataSnapshot.child(auth.getCurrentUser().getUid()).child("username").getValue(String.class);
                                                intent.putExtra("name", user_name);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toast.makeText(LoginActivity.this, "Network Error?", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else{
                                        Toast.makeText(LoginActivity.this, "登入失敗", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        new MainActivity.NukeSSLCerts().nuke();
        RequestQueue bQueue = Volley.newRequestQueue(this);
        final String url1 = "https://iplay.sa.gov.tw/api/GymSearchAllList?$format=application/json;odata.metadata=none&City=臺北市&GymType=籃球";
        Log.d("URL1", url1);
        // Request a JSONObject response from the provided URL.
        // JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
        JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest
                (Request.Method.GET, url1, null, new Response.Listener<JSONArray>() { // JSONObject>() {
                    @Override
                    public void onResponse(JSONArray res) { // JSONObject response) {
                        try {
                            Log.d("Response: ", res.toString());
                            // Log.d("Response2: ", response.getJSONArray("value").toString());
                            // JSONArray jArr = response.getJSONArray("value");
                            JSONObject jObj;
                            name = new String[res.length()];
                            addr = new String[res.length()];
                            lat = new String[res.length()];
                            lng = new String[res.length()];
                            gymFunc = new String[res.length()];

                            for (int i = 0; i < res.length(); i++) {
                                jObj = (JSONObject) res.get(i);
                                name[i] = jObj.getString("Name");
                                addr[i] = jObj.getString("Address");
                                String[] tokens = jObj.getString("LatLng").split(",");
                                lat[i] = tokens[0];//緯度
                                lng[i] = tokens[1];//經度
                                gymFunc[i] = jObj.getString("GymFuncList");//場地類型
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
        bQueue.add(jsonArrayRequest1);

        new MainActivity.NukeSSLCerts().nuke();
        RequestQueue sQueue = Volley.newRequestQueue(this);
        final String url2 = "https://iplay.sa.gov.tw/api/GymSearchAllList?$format=application/json;odata.metadata=none&City=臺北市&GymType=游泳";
        Log.d("URL2", url2);
        // Request a JSONObject response from the provided URL.
        // JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
        JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONArray>() { // JSONObject>() {
                    @Override
                    public void onResponse(JSONArray res) { // JSONObject response) {
                        try {
                            Log.d("Response: ", res.toString());
                            // Log.d("Response2: ", response.getJSONArray("value").toString());
                            // JSONArray jArr = response.getJSONArray("value");
                            JSONObject jObj;
                            name1 = new String[res.length()];
                            addr1 = new String[res.length()];
                            lat1 = new String[res.length()];
                            lng1 = new String[res.length()];
                            gymFunc1 = new String[res.length()];

                            for (int i = 0; i < res.length(); i++) {
                                jObj = (JSONObject) res.get(i);
                                name1[i] = jObj.getString("Name");
                                addr1[i] = jObj.getString("Address");
                                String[] tokens = jObj.getString("LatLng").split(",");
                                lat1[i] = tokens[0];//緯度
                                lng1[i] = tokens[1];//經度
                                gymFunc1[i] = jObj.getString("GymFuncList");//場地類型
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
        sQueue.add(jsonArrayRequest2);

        new MainActivity.NukeSSLCerts().nuke();
        RequestQueue tQueue = Volley.newRequestQueue(this);
        final String url3 = "https://iplay.sa.gov.tw/api/GymSearchAllList?$format=application/json;odata.metadata=none&City=臺北市&GymType=健身";
        Log.d("URL3", url3);
        // Request a JSONObject response from the provided URL.
        // JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
        JsonArrayRequest jsonArrayRequest3 = new JsonArrayRequest
                (Request.Method.GET, url3, null, new Response.Listener<JSONArray>() { // JSONObject>() {
                    @Override
                    public void onResponse(JSONArray res) { // JSONObject response) {
                        try {
                            Log.d("Response: ", res.toString());
                            // Log.d("Response2: ", response.getJSONArray("value").toString());
                            // JSONArray jArr = response.getJSONArray("value");
                            JSONObject jObj;
                            name2 = new String[res.length()];
                            addr2 = new String[res.length()];
                            lat2 = new String[res.length()];
                            lng2 = new String[res.length()];
                            gymFunc2 = new String[res.length()];

                            for (int i = 0; i < res.length(); i++) {
                                jObj = (JSONObject) res.get(i);
                                name2[i] = jObj.getString("Name");
                                addr2[i] = jObj.getString("Address");
                                String[] tokens = jObj.getString("LatLng").split(",");
                                lat2[i] = tokens[0];//緯度
                                lng2[i] = tokens[1];//經度
                                gymFunc2[i] = jObj.getString("GymFuncList");//場地類型
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
        tQueue.add(jsonArrayRequest3);

        new MainActivity.NukeSSLCerts().nuke();
        RequestQueue wQueue = Volley.newRequestQueue(this);
        final String url4 = "https://iplay.sa.gov.tw/api/GymSearchAllList?$format=application/json;odata.metadata=none&City=臺北市&GymType=排球";
        Log.d("URL4", url4);
        // Request a JSONObject response from the provided URL.
        // JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
        JsonArrayRequest jsonArrayRequest4 = new JsonArrayRequest
                (Request.Method.GET, url4, null, new Response.Listener<JSONArray>() { // JSONObject>() {
                    @Override
                    public void onResponse(JSONArray res) { // JSONObject response) {
                        try {
                            Log.d("Response: ", res.toString());
                            // Log.d("Response2: ", response.getJSONArray("value").toString());
                            // JSONArray jArr = response.getJSONArray("value");
                            JSONObject jObj;
                            name3 = new String[res.length()];
                            addr3 = new String[res.length()];
                            lat3 = new String[res.length()];
                            lng3 = new String[res.length()];
                            gymFunc3 = new String[res.length()];

                            for (int i = 0; i < res.length(); i++) {
                                jObj = (JSONObject) res.get(i);
                                name3[i] = jObj.getString("Name");
                                addr3[i] = jObj.getString("Address");
                                String[] tokens = jObj.getString("LatLng").split(",");
                                lat3[i] = tokens[0];//緯度
                                lng3[i] = tokens[1];//經度
                                gymFunc3[i] = jObj.getString("GymFuncList");//場地類型
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
        wQueue.add(jsonArrayRequest4);

        new MainActivity.NukeSSLCerts().nuke();
        RequestQueue allQueue = Volley.newRequestQueue(this);
        final String urlall = "https://iplay.sa.gov.tw/api/GymSearchAllList?$format=application/json;odata.metadata=none&City=臺北市";
        Log.d("URLALL", urlall);
        // Request a JSONObject response from the provided URL.
        // JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
        JsonArrayRequest jsonArrayRequest5 = new JsonArrayRequest
                (Request.Method.GET, url1, null, new Response.Listener<JSONArray>() { // JSONObject>() {
                    @Override
                    public void onResponse(JSONArray res) { // JSONObject response) {
                        try {
                            Log.d("Response: ", res.toString());
                            // Log.d("Response2: ", response.getJSONArray("value").toString());
                            // JSONArray jArr = response.getJSONArray("value");
                            JSONObject jObj;
                            name5 = new String[res.length()];
                            addr5 = new String[res.length()];
                            lat5 = new String[res.length()];
                            lng5 = new String[res.length()];
                            gymFunc5 = new String[res.length()];

                            for (int i = 0; i < res.length(); i++) {
                                jObj = (JSONObject) res.get(i);
                                name5[i] = jObj.getString("Name5");
                                addr5[i] = jObj.getString("Address5");
                                String[] tokens = jObj.getString("LatLng5").split(",");
                                lat5[i] = tokens[0];//緯度
                                lng5[i] = tokens[1];//經度
                                gymFunc5[i] = jObj.getString("GymFuncList5");//場地類型
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
        allQueue.add(jsonArrayRequest5);

    }
}