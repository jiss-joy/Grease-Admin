package com.rsa.greaseadmin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rsa.greaseadmin.Adapters.AdapterStaff;
import com.rsa.greaseadmin.Models.ModelStaff;
import com.rsa.greaseadmin.R;

import java.util.ArrayList;

public class StaffListActivity extends AppCompatActivity implements AdapterStaff.OnStaffClickListener {

    private RecyclerView recyclerView;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private CollectionReference staffRef;
    private ArrayList<ModelStaff> staffs = new ArrayList<>();
    private ArrayList<String> staffID = new ArrayList<>();
    private AdapterStaff sAdapter;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_list);

        id = getIntent().getStringExtra("ID");

        initValues();

        setUpRecycler();
    }

    private void setUpRecycler() {
        staffRef.orderBy("staffExperience", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                staffs.clear();
                staffID.clear();
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    String id = document.getId();
                    String image = document.getString("staffImage");
                    String name = document.getString("staffName");
                    String dob = document.getString("staffDOB");
                    String experience = document.getString("staffExperience");

                    ModelStaff staff = new ModelStaff(image, name, dob, experience);
                    staffs.add(staff);
                    staffID.add(id);

                    if (staffs.size() == 0) {
                        recyclerView.setVisibility(View.GONE);
//                        noStaffsLayout.setVisibility(View.VISIBLE);
                    } else {
                        sAdapter = new AdapterStaff(StaffListActivity.this, staffs, StaffListActivity.this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(StaffListActivity.this);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(sAdapter);
//                        noStaffsLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
    }

    private void initValues() {
        recyclerView = (RecyclerView) findViewById(R.id.staff_list_recycler);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        staffRef = db.collection("Service Providers").document(id).collection("Staff");
    }

    @Override
    public void OnStaffClick(int position, View view) {
        startActivity(new Intent(StaffListActivity.this, StaffDetailsActivity.class)
                .putExtra("STAFF ID", staffID.get(position))
                .putExtra("SERVICE PROVIDER ID", id));
    }
}