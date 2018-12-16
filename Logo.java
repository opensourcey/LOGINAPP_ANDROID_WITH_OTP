package io.githube.opensourcey.coinectus;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Logo extends AppCompatActivity {
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mUser != null) {
                    Intent intent = new Intent(Logo.this, Home.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Logo.this, GetNumber.class);
                    startActivity(intent);
                    finish();
                }
            }
        },3000);
    }
}
