package ttu.mis.lr0816;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.luseen.spacenavigationview.R;

public class MainActivity extends AppCompatActivity {
    private String uname;
    FirebaseDatabase database;
    DatabaseReference users;
    Button login,register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        login =findViewById(R.id.login);
        register = findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent L = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(L);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent R = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(R);
            }
        });
    }
}
