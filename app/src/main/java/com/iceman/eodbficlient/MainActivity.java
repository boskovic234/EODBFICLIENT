package com.iceman.eodbficlient;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase1;
    private DatabaseReference mDatabase2;
    TextView textViewDate;
    TextView textViewProcess;
    ImageView imageViewBFI;
    TextView textViewStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("EODDate");
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("ProcessName");
        textViewDate = (TextView) findViewById(R.id.TVDate);
        textViewProcess = (TextView) findViewById(R.id.TVProcess);
        imageViewBFI = (ImageView) findViewById(R.id.IVBFI);
        imageViewBFI.setImageResource(R.drawable.bfi);
        textViewStatus = (TextView) findViewById(R.id.TVStatus);

        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String date = dataSnapshot.getValue().toString();
                textViewDate.setText("EODDate : "+date);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String processname = dataSnapshot.getValue().toString();
                textViewProcess.setText("Process Name : "+processname);
                Log.d("Disini", processname);
                if(processname.equals("SP CONFIGURE EOD AFTER"))
                {
                    textViewStatus.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left));
                    textViewStatus.setText("EOD Completed");
                    textViewStatus.setTextColor(Color.GREEN);
                }
                else
                {
                    textViewStatus.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left));
                    textViewStatus.setText("EOD Still Running");
                    textViewStatus.setTextColor(Color.BLUE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}