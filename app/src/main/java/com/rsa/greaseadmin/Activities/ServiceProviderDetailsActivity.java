package com.rsa.greaseadmin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ornach.nobobutton.NoboButton;
import com.rsa.greaseadmin.LoadingDialog;
import com.rsa.greaseadmin.R;

public class ServiceProviderDetailsActivity extends AppCompatActivity {

    private NoboButton viewStaffBTN;
    private TextView gstin, bName, pan, address, nob, reg_date, dpt_code, st_pn_code, status;
    private LoadingDialog loadingDialog;

    private FirebaseFirestore db;
    private CollectionReference serviceRef;

    private String spID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_details);

        spID = getIntent().getStringExtra("SERVICE PROVIDER ID");

        initValues();

        loadingDialog.showLoadingDialog("Loading details...");
        loadDetails();

        viewStaffBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ServiceProviderDetailsActivity.this, StaffListActivity.class).putExtra("ID", spID));
            }
        });
    }

    private void loadDetails() {
        serviceRef.document(spID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    gstin.setText(documentSnapshot.getString("serviceGSTIN"));
                    bName.setText(documentSnapshot.getString("serviceBusinessName"));
                    pan.setText(documentSnapshot.getString("servicePAN"));
                    address.setText(documentSnapshot.getString("serviceAddress"));
                    nob.setText(documentSnapshot.getString("serviceNature"));
                    reg_date.setText(documentSnapshot.getString("serviceRegistrationDate"));
                    dpt_code.setText(documentSnapshot.getString("serviceDeptCodeType"));
                    st_pn_code.setText(documentSnapshot.getString("serviceState") + ", " + documentSnapshot.getString("servicePinCode"));
                    status.setText(documentSnapshot.getString("serviceStatus"));

                    loadingDialog.dismissLoadingDialog();
                }
            }
        });
    }

    private void initValues() {
        viewStaffBTN = (NoboButton) findViewById(R.id.service_provider_view_staff_btn);
        bName = (TextView) findViewById(R.id.service_provider_bName);
        gstin = (TextView) findViewById(R.id.service_provider_gstin);
        pan = (TextView) findViewById(R.id.service_provider_pan);
        address = (TextView) findViewById(R.id.service_provider_address);
        nob = (TextView) findViewById(R.id.service_provider_nob);
        reg_date = (TextView) findViewById(R.id.service_provider_reg_date);
        dpt_code = (TextView) findViewById(R.id.service_provider_dept_code_type);
        st_pn_code = (TextView) findViewById(R.id.service_provider_st_code);
        status = (TextView) findViewById(R.id.service_provider_status);
        loadingDialog = new LoadingDialog(this);

        db = FirebaseFirestore.getInstance();
        serviceRef = db.collection("Service Providers");
    }
}