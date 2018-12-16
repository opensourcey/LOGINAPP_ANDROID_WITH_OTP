package io.githube.opensourcey.coinectus;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Verify extends AppCompatActivity {
    private String mNumber;
    private String mVerificationID;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();

        //Get Number
        Intent intent = getIntent();
        String mNumber = intent.getStringExtra("Number");

        //Set Title
        setTitle("Verify +91 "+mNumber);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        //mAuth
        mAuth = FirebaseAuth.getInstance();

        //Wrong Number
        TextView mWrongNumber = findViewById(R.id.wrong_textView_verify);

        mWrongNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Verify.this, GetNumber.class);
                startActivity(intent);
                finish();
                overridePendingTransition( R.transition.left_to_right, R.transition.right_to_left);
            }
        });

        //OTP Resend
        TextView mResend = findViewById(R.id.resend_textView_verify);

        mResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpSend();
            }
        });

        //OTP SEND
        Intent intent1 = getIntent();
        mNumber = intent1.getStringExtra("Number");
        if ( TextUtils.isEmpty(mNumber) ) {
            Intent intent = new Intent( Verify.this, GetNumber.class);
            Toast.makeText(this, "Enter valid number", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
            overridePendingTransition( R.transition.left_to_right, R.transition.right_to_left);
        } else {
            otpSend();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent( this, GetNumber.class);
        startActivity(intent);
        finish();
        overridePendingTransition( R.transition.left_to_right, R.transition.right_to_left);
    }

    // OTP SEND
    private void otpSend() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+mNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks
        );
    }

    //mCallbacks
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String mOTP = phoneAuthCredential.getSmsCode();

            if (mOTP != null) {
                EditText mOTPEditText = findViewById(R.id.otp_editText_verify);
                mOTPEditText.setText(mOTP);
                verifyOTP( mOTP);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Verify.this, "Somthing is wrong..", Toast.LENGTH_SHORT).show();
            Log.d("ErrotOnOTPSend", e.getMessage());
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationID = s;
        }
    };

    //OTP Verify
    private void verifyOTP( String mOTP) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationID, mOTP);
        login(credential);
    }

    private void login(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(Verify.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful() ) {
                    Intent intent = new Intent(Verify.this, Home.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.transition.enter, R.transition.exit);
                } else {
                    Toast.makeText( Verify.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
