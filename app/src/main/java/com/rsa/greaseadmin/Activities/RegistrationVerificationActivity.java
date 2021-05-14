package com.rsa.greaseadmin.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.ornach.nobobutton.NoboButton;
import com.rsa.greaseadmin.LoadingDialog;
import com.rsa.greaseadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationVerificationActivity extends AppCompatActivity {

    private String TAG = "TAG:";

    private NoboButton verifyBTN, approveBTN, declineBTN;
    private TextView gstin, bName, pan, address, nob, reg_date, dpt_code, st_pn_code, status, errorMessage;
    private LoadingDialog loadingDialog;
    private LinearLayout buttonLayout;
    private ScrollView detailsLayout;

    private FirebaseFirestore db;
    private CollectionReference registrationRef;
    private CollectionReference serviceRef;

    private RequestQueue requestQueue;

    private String regID, pinCode, district, state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_verification);

        regID = getIntent().getStringExtra("REGISTRATION ID");

        initValues();

        registrationRef.document(regID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    gstin.setText(task.getResult().getString("userGSTIN"));
                }
            }
        });

        verifyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDetails();
            }
        });

        approveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveRegistration();
            }
        });

        declineBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineRegistration();
            }
        });

    }

    private void declineRegistration() {
        loadingDialog.showLoadingDialog("Declining the Service...");
        Map<String, Object> registrationStatus = new HashMap<>();
        registrationStatus.put("userRegistrationStatus", "Declined");
        registrationRef.document(regID).
                update(registrationStatus)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loadingDialog.dismissLoadingDialog();
                            finish();
                        }
                    }
                });
    }

    private void approveRegistration() {
        loadingDialog.showLoadingDialog("Approving the Service...");

        registrationRef.document(regID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    Double lng = task.getResult().getDouble("userLongitude");
                    Double lat = task.getResult().getDouble("userLatitude");
                    Map<String, Object> registrationStatus = new HashMap<>();
                    registrationStatus.put("userRegistrationStatus", "Verified");

                    Map<String, Object> serviceProvider = new HashMap<>();
                    serviceProvider.put("serviceGSTIN", gstin.getText().toString());
                    serviceProvider.put("serviceBusinessName", bName.getText().toString());
                    serviceProvider.put("servicePAN", pan.getText().toString());
                    serviceProvider.put("serviceAddress", address.getText().toString());
                    serviceProvider.put("serviceDistrict", district);
                    serviceProvider.put("serviceNature", nob.getText().toString());
                    serviceProvider.put("serviceRegistrationDate", reg_date.getText().toString());
                    serviceProvider.put("serviceDeptCodeType", dpt_code.getText().toString());
                    serviceProvider.put("serviceState", state);
                    serviceProvider.put("servicePinCode", pinCode);
                    serviceProvider.put("serviceStatus", status.getText().toString());
                    serviceProvider.put("serviceLongitude", lng);
                    serviceProvider.put("serviceLatitude", lat);

                    WriteBatch batch = db.batch();

                    batch.set(serviceRef.document(regID), serviceProvider);

                    batch.update(registrationRef.document(regID), registrationStatus);

                    // Commit the batch
                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                loadingDialog.dismissLoadingDialog();
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }

    private void initValues() {
        verifyBTN = (NoboButton) findViewById(R.id.registration_verification_verify_btn);
        bName = (TextView) findViewById(R.id.registration_verification_bName);
        gstin = (TextView) findViewById(R.id.registration_verification_gstin);
        pan = (TextView) findViewById(R.id.registration_verification_pan);
        address = (TextView) findViewById(R.id.registration_verification_address);
        nob = (TextView) findViewById(R.id.registration_verification_nob);
        reg_date = (TextView) findViewById(R.id.registration_verification_reg_date);
        dpt_code = (TextView) findViewById(R.id.registration_verification_dept_code_type);
        st_pn_code = (TextView) findViewById(R.id.registration_verification_st_code);
        status = (TextView) findViewById(R.id.registration_verification_status);
        errorMessage = (TextView) findViewById(R.id.registration_verification_error_message);
        detailsLayout = (ScrollView) findViewById(R.id.registration_verification_gst_details);
        buttonLayout = (LinearLayout) findViewById(R.id.registration_verification_button_layout);
        approveBTN = (NoboButton) findViewById(R.id.registration_verification_approve_btn);
        declineBTN = (NoboButton) findViewById(R.id.registration_verification_decline_btn);

        db = FirebaseFirestore.getInstance();
        registrationRef = db.collection("Users");
        serviceRef = db.collection("Service Providers");

        loadingDialog = new LoadingDialog(this);
    }

    private void fetchDetails() {
        loadingDialog.showLoadingDialog("Fetching Data...");
        registrationRef.document(regID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String gstin = task.getResult().getString("userGSTIN");
                    Log.d(TAG, "onComplete: " + gstin);
                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                    String url = "https://appyflow.in/api/verifyGST?gstNo=" + gstin + "&key_secret=KR4QqNTMHpO9ChtMWzVojgTuXgo1";

                    // Request a string response from the provided URL.
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONObject taxpayerInfo = response.getJSONObject("taxpayerInfo");
                                        pan.setText(taxpayerInfo.getString("panNo"));
                                        reg_date.setText(taxpayerInfo.getString("rgdt"));
                                        dpt_code.setText(taxpayerInfo.getString("stj") + ", " + taxpayerInfo.getString("stjCd"));
                                        status.setText(taxpayerInfo.getString("sts"));
                                        bName.setText(taxpayerInfo.getString("tradeNam"));

                                        JSONArray nobArray = taxpayerInfo.getJSONArray("nba");
                                        String nobString = "";
                                        for (int i = 0; i < nobArray.length(); i++) {
                                            nobString += (nobArray.getString(i) + ", ");
                                        }
                                        nob.setText(nobString);

                                        JSONObject pradr = taxpayerInfo.getJSONObject("pradr");
                                        JSONObject addr = pradr.getJSONObject("addr");
                                        pinCode = addr.getString("pncd");
                                        state = addr.getString("stcd");
                                        district = addr.getString("dst");
                                        st_pn_code.setText(addr.getString("stcd") + ", " + addr.getString("pncd"));
                                        address.setText(addr.getString("bno") + ", " +
                                                addr.getString("st") + ", " +
                                                addr.getString("city") + ", " +
                                                addr.getString("dst"));

                                        detailsLayout.setVisibility(View.VISIBLE);
                                        errorMessage.setVisibility(View.GONE);
                                        verifyBTN.setVisibility(View.GONE);
                                        buttonLayout.setVisibility(View.VISIBLE);
                                        loadingDialog.dismissLoadingDialog();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        detailsLayout.setVisibility(View.GONE);
                                        errorMessage.setVisibility(View.VISIBLE);
                                        buttonLayout.setVisibility(View.VISIBLE);
                                        loadingDialog.dismissLoadingDialog();
                                    }

                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    bName.setText("Error: " + error.getMessage());
                                    loadingDialog.dismissLoadingDialog();
                                }
                            });

                    // Add the request to the RequestQueue.
                    requestQueue.add(jsonObjectRequest);
                }
            }
        });
    }
}