package com.example.archismansarkar.smsparseandiot;

/**
 * Created by Archisman Sarkar on 9/7/2017.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ListActivity{

    String senderAddress = "";
    String desiredAddress;
    String smsBody = "";
    String smsBodyParts[];
    List<SMSData> smsList = new ArrayList<SMSData>();

    String date = "";
    int track = 0;
    String identity = "";
    String identity1= "sms";
    String identity2= "login";
    SharedPreferences pref;

    private Boolean exit = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent sms_intent=getIntent();
        Bundle b=sms_intent.getExtras();

        pref = getApplicationContext().getSharedPreferences("Desired_Sender_Address", MODE_PRIVATE);
        if(b!=null) {
            Toast.makeText(this, "Address: " + b.getString("sms_address") + "\n Message: " + b.getString("sms_body"), Toast.LENGTH_LONG).show();
            SMSData sms = new SMSData();
            senderAddress = b.getString("sms_address");

            desiredAddress = pref.getString("desired_address", "+919800644706");
            if (desiredAddress.equals(senderAddress)) {
                smsBody = b.getString("sms_body");
                smsBody = smsBody.replace("\n", "=");
                smsBodyParts = smsBody.split("=");
                date = b.getString("sms_date");
                if (smsBodyParts.length >= 12)
                    smsBody = "VR: " + smsBodyParts[1] + "\n" + " VY: " + smsBodyParts[3] + "\n" + " VB: " + smsBodyParts[5] + "\n" + "CR: " + smsBodyParts[7] + "\n" + " CY: " + smsBodyParts[9] + "\n" + " CB: " + smsBodyParts[11] + " Time: " + date;

                sms.setBody(smsBody);
                sms.setNumber(senderAddress);
                smsList.add(sms);
                setListAdapter(new ListAdapter(this, smsList));
            }
        }
            if (track == 0) {
                desiredAddress = pref.getString("desired_address", "+919800644706");
                uiUpdate();
                track = 1;
            }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        SMSData sms = (SMSData)getListAdapter().getItem(position);

        Toast.makeText(getApplicationContext(), sms.getBody(), Toast.LENGTH_LONG).show();

    }


    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
            System.exit(0);
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }


    public void uiUpdate(){
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor c= getContentResolver().query(uri, null, null ,null,null);

        if(c.moveToFirst()) {
            for(int i=0; i < c.getCount(); i++) {
                SMSData sms = new SMSData();

                senderAddress = c.getString(c.getColumnIndexOrThrow("address")).toString();
                if(desiredAddress.equals(senderAddress)) {
                    smsBody = c.getString(c.getColumnIndexOrThrow("body")).toString();
                    smsBody = smsBody.replace("\n","=");
                    smsBodyParts = smsBody.split("=");

                    date =  c.getString(c.getColumnIndexOrThrow("date")).toString();
                    if(smsBodyParts.length>=12)
                    smsBody = "VR: "+smsBodyParts[1] + "\n" +" VY: "+smsBodyParts[3] + "\n" +" VB: "+smsBodyParts[5]+"\n"+"CR: "+smsBodyParts[7] + "\n" +" CY: "+smsBodyParts[9] + "\n" +" CB: "+smsBodyParts[11]+" Time: "+date;

                    sms.setBody(smsBody);
                    sms.setNumber(senderAddress);
                    smsList.add(sms);
                }
                c.moveToNext();
            }
        }
        c.close();

        setListAdapter(new ListAdapter(this, smsList));
    }

}