package com.rsa.greaseadmin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rsa.greaseadmin.Adapters.AdapterServiceProvider;
import com.rsa.greaseadmin.Models.ModelServiceProvider;
import com.rsa.greaseadmin.R;
import com.rsa.greaseadmin.Activities.ServiceProviderDetailsActivity;

import java.util.ArrayList;

public class FragmentServiceProviders extends Fragment implements AdapterServiceProvider.OnServiceProviderClickListener {

    private RecyclerView recyclerView;

    private FirebaseFirestore db;
    private CollectionReference serviceProviderRef;

    private ArrayList<ModelServiceProvider> serviceProviders = new ArrayList<>();
    private ArrayList<String> serviceProvidersID = new ArrayList<>();
    private AdapterServiceProvider sAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_providers, container, false);

        initValues(view);

        setUpRecycler();

        return view;
    }

    private void setUpRecycler() {
        serviceProviderRef.limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        String id = documentSnapshot.getId();
                        String name = documentSnapshot.getString("serviceBusinessName");
                        String dst = documentSnapshot.getString("serviceDistrict");
                        String st = documentSnapshot.getString("serviceState");
                        String pncd = documentSnapshot.getString("servicePinCode");

                        ModelServiceProvider serviceProvider = new ModelServiceProvider(name, dst, st, pncd);
                        serviceProviders.add(serviceProvider);
                        serviceProvidersID.add(id);

                        if (serviceProviders.size() == 0) {
                            recyclerView.setVisibility(View.GONE);
//                        noServiceProvidersLayout.setVisibility(View.VISIBLE);
                        } else {
                            sAdapter = new AdapterServiceProvider(getActivity(), serviceProviders, FragmentServiceProviders.this);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(sAdapter);
//                        noServiceProvidersLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    private void initValues(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_service_providers_recycler);

        db = FirebaseFirestore.getInstance();
        serviceProviderRef = db.collection("Service Providers");
    }

    @Override
    public void OnServiceProviderClick(int position, View view) {
        startActivity(new Intent(getContext(), ServiceProviderDetailsActivity.class)
                .putExtra("SERVICE PROVIDER ID", serviceProvidersID.get(position)));
    }
}
