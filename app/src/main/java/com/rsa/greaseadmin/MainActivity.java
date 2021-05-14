package com.rsa.greaseadmin;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rsa.greaseadmin.Activities.NotificationsActivity;
import com.rsa.greaseadmin.Authentication.AuthenticationActivity;
import com.rsa.greaseadmin.Fragments.FragmentProfile;
import com.rsa.greaseadmin.Fragments.FragmentRegistrations;
import com.rsa.greaseadmin.Fragments.FragmentServiceProviders;
import com.rsa.greaseadmin.Network.ConnectivityReceiver;
import com.rsa.greaseadmin.Network.MyApp;
import com.rsa.greaseadmin.Network.NoNetworkActivity;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    public static final int REQUEST_CHECK_SETTING = 1001;

    private ImageButton notificationBTN;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private Fragment selectedFragment;


    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private int fragmentFlag = 1;

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.nav_registrations:
                            selectedFragment = new FragmentRegistrations();
                            fragmentFlag = 1;
                            break;
                        case R.id.nav_providers:
                            selectedFragment = new FragmentServiceProviders();
                            fragmentFlag = 2;
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new FragmentProfile();
                            fragmentFlag = 4;
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initValues();
        setSupportActionBar(toolbar);

        bottomNavigationView.setSelectedItemId(R.id.nav_registrations);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentRegistrations()).commit();

        notificationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NotificationsActivity.class));
            }
        });
    }


    private void initValues() {
        notificationBTN = (ImageButton) findViewById(R.id.main_notification_btn);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Database
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser == null) {
            finish();
            startActivity(new Intent(MainActivity.this, AuthenticationActivity.class));
        } else {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
        registerReceiver(connectivityReceiver, intentFilter);

        MyApp.getInstance().setConnectivityListener(this);
    }

    private void exit() {
        this.finishAffinity();
    }

    @Override
    public void onBackPressed() {
        if (fragmentFlag == 1) {
            exit();
        } else {
            Fragment selectedFragment = new FragmentRegistrations();
            bottomNavigationView.setSelectedItemId(R.id.nav_registrations);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            startActivity(new Intent(this, NoNetworkActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTING) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    break;
                case Activity.RESULT_CANCELED:
            }
        }
    }
}