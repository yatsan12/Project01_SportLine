package ttu.mis.lr0816;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.luseen.spacenavigationview.R;
import de.hdodenhof.circleimageview.CircleImageView;
import model.User;


public class ProfileActivity extends AppCompatActivity {

    CircleImageView image_profile;
    TextView username;
    private String uname;
    DatabaseReference reference;
    FirebaseUser fuser;

    StorageReference storageReference;
    private static final int IMAGE_REQUEST =1;
    private Uri imageURL;
    private StorageTask uploadTask; //上傳

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        image_profile=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);

        ////

        final String uname = getIntent().getExtras().getString("name");
        username.setText(uname);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        storageReference= FirebaseStorage.getInstance().getReference("upload");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               User user = dataSnapshot.getValue(User.class);
                if (uname.equals("jjLin")){
                    image_profile.setImageResource(R.drawable.man121);
                }else if (uname.equals("琳琳")){
                    //Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
                    image_profile.setImageResource(R.drawable.meow121);
                }else if (uname.equals("翁祥恩")){
                    image_profile.setImageResource(R.drawable.joe061);
                }else if (uname.equals("yatsan")){
                    image_profile.setImageResource(R.drawable.yatsan121);
                }else if (uname.equals("雅琪")){
                    image_profile.setImageResource(R.drawable.yaya09011);
                }else if (uname.equals("HDwang")){
                    image_profile.setImageResource(R.drawable.hdwang101);
                }else {
                    image_profile.setImageResource(R.drawable.ic_action_face2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}