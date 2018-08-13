package com.example.shraddha.handyman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class reviewComplaint extends AppCompatActivity implements View.OnClickListener {

    private TextView dateOfcomplaint, hostel, room, datePref1, timePref1, datePref2, timePref2, describe;
    DatabaseReference databaseReference, databaseNotification;
    String handymanId, student;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_complaint);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("complaint");
        databaseNotification = FirebaseDatabase.getInstance().getReference("notification");

        student = getIntent().getStringExtra("studentId");
        handymanId = getIntent().getStringExtra("handyman");

        dateOfcomplaint = (TextView)findViewById(R.id.dateTextView);
        hostel = (TextView)findViewById(R.id.hostelTextView);
        room = (TextView)findViewById(R.id.roomTextView);
        datePref1= (TextView)findViewById(R.id.datePrefTextView);
        timePref1 = (TextView)findViewById(R.id.timePrefTextView);
        datePref2 = (TextView)findViewById(R.id.datePrefTextView2);
        timePref2 = (TextView)findViewById(R.id.timePrefTextView2);
        describe = (TextView)findViewById(R.id.describe);

        dateOfcomplaint.setText(getIntent().getStringExtra("dateOfcomplaint"));
        hostel.setText("Hostel : " + getIntent().getStringExtra("hostel"));
        room.setText("Room Number : " + getIntent().getStringExtra("room"));
        datePref1.setText("Date : " + getIntent().getStringExtra("datePref1"));
        timePref1.setText("Time : " + getIntent().getStringExtra("timePref1"));
        datePref2.setText("Date : " + getIntent().getStringExtra("datePref2"));
        timePref2.setText("Time : " + getIntent().getStringExtra("timePref2"));
        describe.setText("Description : " + getIntent().getStringExtra("describe"));

        findViewById(R.id.resolvedButton).setOnClickListener(this);
        findViewById(R.id.notResolvedButton).setOnClickListener(this);
        findViewById(R.id.missedButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resolvedButton:
                resolved();
                break;
            case  R.id.notResolvedButton:
                String notificationNR = "Oops!! seems your complaint could not be resolved today due to reasons. We'll be at it asap ^_^";
                String id = databaseNotification.child(student).push().getKey();
                notificationInfo notify = new notificationInfo(student, handymanId, notificationNR);
                databaseNotification.child(student).child(id).setValue(notify);
                startActivity(new Intent(this, tracker.class));
                break;
            case R.id.missedButton:
                String notificationMissed = "We came at given time, but seems you were away. Update the Time and date if required, later :D";
                String id2 = databaseNotification.child(student).push().getKey();
                notificationInfo notifyStudent = new notificationInfo(student, handymanId, notificationMissed);
                databaseNotification.child(student).child(id2).setValue(notifyStudent);
                startActivity(new Intent(this, tracker.class));
                break;
        }
    }

    private void resolved(){
        databaseReference.child(getIntent().getStringExtra("handyman")).child(getIntent().getStringExtra("complaintId")).removeValue();
        startActivity(new Intent(this, tracker.class));
        Toast.makeText(this, "Good Job!! :)", Toast.LENGTH_SHORT).show();
    }
}
