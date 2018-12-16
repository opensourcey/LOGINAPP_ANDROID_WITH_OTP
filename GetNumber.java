package io.githube.opensourcey.coinectus;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class GetNumber extends AppCompatActivity {
    private String mNumberString;
    private EditText mNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_number);

        //Local Variable
        EditText mCode;
        ImageButton mNext;

        // Initialize Local Variable
        mCode = findViewById(R.id.counteryCode_editText_getNumber);
        mNumber = findViewById(R.id.number_editText_getNumber);
        mNext = findViewById(R.id.next_imageButton_getNumber);

        //Disable Code EditText
        mCode.setEnabled(false);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get Valid Number
                mNumberString = mNumber.getText().toString();

                if ( TextUtils.isEmpty(mNumberString)  ||  mNumberString.length() < 10  ||  mNumberString.length() > 10) {
                    mNumber.setError("Enter valid mobile number");
                    return;
                }

                //Dialog Box
                AlertDialog.Builder builder;
                AlertDialog alertDialog;

                //Initialize
                builder = new AlertDialog.Builder(GetNumber.this);

                //Set Data
                builder.setMessage("Verify mobile number is \n"+
                        "+91 "+mNumberString +
                        "\n\nDo you want continue");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Send mNumber To other Activity
                        Intent intent = new Intent(GetNumber.this, Verify.class);
                        intent.putExtra("Number", mNumberString);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.transition.enter, R.transition.exit);
                    }
                });

                builder.setNegativeButton("EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                //Set Dialog
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    //         on BackPressed For EXIT From app
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mExitPress = new Intent(Intent.ACTION_MAIN);
        mExitPress.addCategory(Intent.CATEGORY_HOME);
        mExitPress.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mExitPress);
    }
}
