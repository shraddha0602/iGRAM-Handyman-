package com.example.shraddha.handyman;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import java.util.List;

public class complaintAdapter extends RecyclerView.Adapter<complaintAdapter.ViewHolder> {

    private List<newComplaint> list;
    private Context context;

    public complaintAdapter(List<newComplaint> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public complaintAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint_list_layout, parent, false);
        return new ViewHolder(v, context, list);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        newComplaint listItem = list.get(position);
        holder.dateOfComplaint.setText(listItem.getDateOfComplaint());
        holder.timeTextView.setText(listItem.getTimepref1());
        holder.hostel.setText(listItem.getHostel());
        holder.room.setText(listItem.getRoom());

        if(listItem.getTimepref1().indexOf("AM")!= -1){
            holder.complaintListLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.dayBackground));
            holder.dateOfComplaint.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.timeTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.hostel.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.room.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        else if(listItem.getTimepref1().indexOf("PM")!= -1) {
            holder.complaintListLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.nightBackground));
            holder.dateOfComplaint.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.timeTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.hostel.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.room.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView dateOfComplaint, timeTextView, hostel, room;
        public RelativeLayout complaintListLayout;

        List<newComplaint> ncomplaint;
        Context ctx;

        public ViewHolder(View itemView, Context ctx, List<newComplaint> c) {
            super(itemView);
            this.ncomplaint=c;
            this.ctx=ctx;
            itemView.setOnClickListener(this);
            dateOfComplaint = (TextView)itemView.findViewById(R.id.dateOfComplaint);
            timeTextView = (TextView)itemView.findViewById(R.id.time);
            hostel = (TextView)itemView.findViewById(R.id.hostelTextView2);
            room = (TextView)itemView.findViewById(R.id.roomTextView);
            complaintListLayout = (RelativeLayout)itemView.findViewById(R.id.complaintListLayout);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            newComplaint c = this.ncomplaint.get(position);
            Intent intent = new Intent(this.ctx, reviewComplaint.class);
            intent.putExtra("hostel", c.getHostel());
            intent.putExtra("room", c.getRoom());
            intent.putExtra("dateOfcomplaint", c.getDateOfComplaint());
            intent.putExtra("datePref1", c.getDatepref1());
            intent.putExtra("timePref1", c.getTimepref1());
            intent.putExtra("datePref2", c.getDatepref2());
            intent.putExtra("timePref2", c.getTimepref2());
            intent.putExtra("describe", c.getDescription());
            intent.putExtra("complaintId", c.getComplaintId());
            intent.putExtra("handyman", c.getHandyman());
            intent.putExtra("studentId", c.getStudentId());

            this.ctx.startActivity(intent);
        }
    }
}