package com.iceman.eodbficlient;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase1;
    private DatabaseReference mDatabase2;
    TextView textViewDate;
    TextView textViewProcess;
    ImageView imageViewBFI;
    TextView textViewStatus;
    String processname1 = null;
    String processname2 = null;

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
                processname1 = dataSnapshot.getValue().toString();
                textViewProcess.setText("Process Name : "+processname1);

                if(!processname1.equals(processname2))
                {
                    try {
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        r.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Notification();
                }

                processname2 = processname1;

                Log.d("Disini", processname1);
                if(processname1.equals("SP CONFIGURE EOD AFTER"))
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

    private void Notification()
    {
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(MainActivity.this)
                .setSmallIcon(android.R.drawable.stat_notify_error)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setContentTitle("EOD BFI")
                .setContentText("Running On Process : "+processname1);
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                notificationIntent, 0);
//        notification.setLatestEventInfo(MainActivity.this, pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
        notificationManager.notify(1, notificationBuilder.build());
    }

}
