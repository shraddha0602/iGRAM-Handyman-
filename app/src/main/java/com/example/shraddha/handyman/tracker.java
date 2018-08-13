package com.example.shraddha.handyman;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class tracker extends AppCompatActivity implements View.OnClickListener {
    private String handymanOccupation;
    DatabaseReference databaseReference, databaseReferenceUsers, databaseHandyman;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private complaintAdapter adapter;
    private List<newComplaint> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseReference = FirebaseDatabase.getInstance().getReference("complaint");
        databaseHandyman = FirebaseDatabase.getInstance().getReference("handymans").child(user.getUid());
        databaseHandyman.keepSynced(true);
        databaseReference.keepSynced(true);

        list = new ArrayList<>();
        adapter = new complaintAdapter(list, this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.confirmWorkButton).setOnClickListener(this);


        databaseHandyman.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                handymanInfo info = dataSnapshot.getValue(handymanInfo.class);
                handymanOccupation = info.getOccupation();

                Query query = databaseReference.child(handymanOccupation).orderByChild("forOrder");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list.clear();
                        Date date = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy");
                        String formattedDate = df.format(date);
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            newComplaint c = snapshot.getValue(newComplaint.class);
                            if(c.getDatepref1().equals(formattedDate) || c.getStatus().equals("Processing"))
                                list.add(c);
                            else
                                continue;
                        }
                        recyclerView.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(tracker.this, "Oops!! something went wrong.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(tracker.this, "Something went wrong :( ", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirmWorkButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(tracker.this);
                builder.setMessage("Process the complaints??")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseHandyman.addListenerForSingleValueEvent(new ValueEventListener(){
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        handymanInfo info = dataSnapshot.getValue(handymanInfo.class);
                                        handymanOccupation = info.getOccupation();
                                        changeStatus();
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        return;
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", null);
                builder.create().show();
                break;
        }
    }

    private void changeStatus(){
        databaseReference.child(handymanOccupation).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                for(DataSnapshot snapshot1 : dataSnapshot1.getChildren()){
                    if(snapshot1.getValue(newComplaint.class).getStatus().equals("Pending"))
                         snapshot1.getRef().child("status").setValue("Processing");
                }
                recyclerView.setVisibility(1);
                Toast.makeText(tracker.this, "Have a Good Day!! Stay Safe :)", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(tracker.this, "Something went wrong :( ", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}
