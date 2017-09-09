package com.example.archismansarkar.smsparseandiot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Archisman Sarkar on 9/8/2017.
 */

public class loginActivity extends Activity {

    Button enter;
    EditText phoneNumber;
    public int contextTrack = 0;
    SharedPreferences pref;
    String desired_number;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sender_address);

        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        enter = (Button) findViewById(R.id.button);
        pref = getApplicationContext().getSharedPreferences("Desired_Sender_Address", MODE_PRIVATE);

        desired_number = pref.getString("desired_address", null);
        if(desired_number!=null)
        phoneNumber.setText(desired_number, TextView.BufferType.EDITABLE);
        else phoneNumber.setText("", TextView.BufferType.EDITABLE);

        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                String str = phoneNumber.getText().toString();
                editor.putString("desired_address", str);
                editor.commit();

                Toast msg = Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG);
                msg.show();

                Intent activityIntent=new Intent(getApplicationContext(),MainActivity.class);
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getApplicationContext().startActivity(activityIntent);
            }
        });
    }
}
