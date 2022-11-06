package sportfieldsearch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.luseen.spacenavigationview.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class InfoActivity extends AppCompatActivity {
    String strName,strPhoto,strAddress,strOperationTel,strGymFuncList,strLandAttrName,strRentState,strLatitude,strLongitude;
    TextView name,address,tel,list,attr,state;
    ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        name = findViewById(R.id.name);
        photo = findViewById(R.id.photo);
        address = findViewById(R.id.address);
        tel = findViewById(R.id.tel);
        list = findViewById(R.id.list);
        attr = findViewById(R.id.attr);
        state = findViewById(R.id.state);

        strName= getIntent().getExtras().getString("Name");
        strPhoto=getIntent().getExtras().getString("Photo");
        strAddress= getIntent().getExtras().getString("Address");
        strOperationTel= getIntent().getExtras().getString("OperationTel");
        strGymFuncList= getIntent().getExtras().getString("GymFuncList");
        strLandAttrName= getIntent().getExtras().getString("LandAttrName");
        strRentState= getIntent().getExtras().getString("RentState");
        strLatitude=getIntent().getExtras().getString("Latitude");
        strLongitude=getIntent().getExtras().getString("Longitude");
        Log.d("StrPhoto", strPhoto);

        name.setText("【" + strName + "】");
        address.setText("【" + strAddress + "】");
        tel.setText("【" + strOperationTel + "】");
        list.setText("【" + strGymFuncList + "】");
        attr.setText("【" + strLandAttrName + "】");
        state.setText("【" + strRentState + "】");

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap mBitmap = getBitmapFromURL(strPhoto);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        photo.setImageBitmap(mBitmap);
                    }
                });
            }}).start();
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            InputStream input = conn.getInputStream();
            Bitmap mBitmap = BitmapFactory.decodeStream(input);
            return mBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void myMap(View view) {
        Intent intent = new Intent(this, sportfieldsearch.MapsActivity.class);
        intent.putExtra("lat",strLatitude);
        intent.putExtra("lng", strLongitude);
        intent.putExtra("addr", strAddress);
        intent.putExtra("name", strName);
        startActivity(intent);
    }
}
