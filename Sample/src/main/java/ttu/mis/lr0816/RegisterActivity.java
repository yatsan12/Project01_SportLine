package ttu.mis.lr0816;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.luseen.spacenavigationview.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //宣告
    EditText username,email,password;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {  //內建
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //利用findViewById找到xml框架中的的元件
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);

        auth = FirebaseAuth.getInstance();

        //監聽
        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {  //按下註冊按鍵後會做的動作:
                String txt_username = username.getText().toString();  //宣告一個變數txt_username儲存使用者輸入的資料
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(RegisterActivity.this, "不可有欄位是空白!!!", Toast.LENGTH_SHORT).show();
                }else if(txt_password.length() < 6 ){
                    Toast.makeText(RegisterActivity.this, "密碼至少要有6位數喔!!!", Toast.LENGTH_SHORT).show();
                }else{
                    register(txt_username,txt_email,txt_password); //register方法寫在下方
                }
            }
        });
    }

    private void register(final String txt_username, String email, String password){ //註冊三要件

        auth.createUserWithEmailAndPassword(email,password) //android 方法
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid); //txt_username);
                            Log.d("currentUid", userid);
                            //利用HashMap排序
                            HashMap <String, String> hashMap =new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username.getText().toString());
                            hashMap.put("imageURL", "default");
                            hashMap.put("status","offline");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity.this, "email無法註冊", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}