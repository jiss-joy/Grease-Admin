package com.rsa.greaseadmin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.rsa.greaseadmin.Adapters.AdapterRegistration;
import com.rsa.greaseadmin.Models.ModelRegistration;
import com.rsa.greaseadmin.R;
import com.rsa.greaseadmin.Activities.RegistrationVerificationActivity;

import java.util.ArrayList;

public class FragmentRegistrations extends Fragment implements AdapterRegistration.OnRegistrationClickListener {

    private RecyclerView registrationRecycler;
    private TextView noRegistrationsLayout;

    private FirebaseFirestore db;
    private CollectionReference registrationRef;
    private ArrayList<ModelRegistration> registrations = new ArrayList<>();
    private ArrayList<String> registrationID = new ArrayList<>();
    private AdapterRegistration rAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrations, container, false);

        initValues(view);

        setUpRecycler();

        return view;
    }

    private void setUpRecycler() {
        registrationRef.whereEqualTo("userType", "Mechanic")
                .whereEqualTo("userRegistrationStatus", "Pending")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        registrations.clear();
                        registrationID.clear();
                        for (DocumentSnapshot document : value.getDocuments()) {
                            String id = document.getId();
                            String gstin = document.getString("userGSTIN");

                            ModelRegistration registration = new ModelRegistration(gstin);
                            registrations.add(registration);
                            registrationID.add(id);

                            if (registrations.size() == 0) {
                                registrationRecycler.setVisibility(View.GONE);
                                noRegistrationsLayout.setVisibility(View.VISIBLE);
                            } else {
                                noRegistrationsLayout.setVisibility(View.VISIBLE);
                                registrationRecycler.setVisibility(View.GONE);
                            }
                            rAdapter = new AdapterRegistration(getActivity(), registrations, FragmentRegistrations.this);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                            registrationRecycler.setLayoutManager(linearLayoutManager);
                            registrationRecycler.setAdapter(rAdapter);
                            noRegistrationsLayout.setVisibility(View.GONE);
                            registrationRecycler.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void initValues(View view) {
        registrationRecycler = (RecyclerView) view.findViewById(R.id.registrations_recycler);
        noRegistrationsLayout = (TextView) view.findViewById(R.id.registrations_no_registrations);

        db = FirebaseFirestore.getInstance();
        registrationRef = db.collection("Users");
    }

    @Override
    public void OnRegistrationClick(int position, View view) {
        String id = registrationID.get(position);
        startActivity(new Intent(getActivity(), RegistrationVerificationActivity.class).putExtra("REGISTRATION ID", id));
    }
}
