package com.example.bhaum.eduapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bhaum.eduapp.R;
import com.example.bhaum.eduapp.data.Student;

import java.util.ArrayList;

public class AttendanceRecyclerViewAdapter extends RecyclerView.Adapter<AttendanceRecyclerViewAdapter.ViewHolder> {


    private static final String TAG = "AttendanceRecyclerViewA";
    private ArrayList<Student> studentList = new ArrayList<>();

    public ArrayList<Student> getStudentList() {
        return studentList;
    }

    private Context mcontext;


    public AttendanceRecyclerViewAdapter(Context mcontext,ArrayList<Student> studentList) {
        this.studentList = studentList;
        this.mcontext = mcontext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendace_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindHolder");

        Student s = studentList.get(position);
        holder.roll_no.setText(Integer.toString(s.getRoll_no()));
        holder.name.setText(s.getName());
        holder.status.setChecked(true);
        Log.d(TAG, s.getName());

        holder.status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Student s1 = studentList.get(position);
                if(isChecked==true){
                    s1.setAttend_status(true);
                }
                else {
                    s1.setAttend_status(false);
                }
                studentList.set(position, s1);

            }
        });
    }
    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout parentLayout;
        TextView roll_no, name;
        CheckBox status;

        public ViewHolder(View itemView) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.parent_layout);
            roll_no = itemView.findViewById(R.id.roll_no);
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);

        }
    }
}
