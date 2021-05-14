package com.rsa.greaseadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rsa.greaseadmin.Models.ModelRegistration;
import com.rsa.greaseadmin.R;

import java.util.ArrayList;

public class AdapterRegistration extends RecyclerView.Adapter<AdapterRegistration.MyViewHolder> {

    private Context mContext;
    private ArrayList<ModelRegistration> registrations;
    private OnRegistrationClickListener onRegistrationClickListener;

    public AdapterRegistration(Context mContext, ArrayList<ModelRegistration> registrations, OnRegistrationClickListener onRegistrationClickListener) {
        this.mContext = mContext;
        this.registrations = registrations;
        this.onRegistrationClickListener = onRegistrationClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterRegistration.MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_registration, parent, false), onRegistrationClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.gstin.setText(registrations.get(position).getUserGSTIN());
    }

    @Override
    public int getItemCount() {
        return registrations.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView gstin;

        public MyViewHolder(@NonNull View itemView, OnRegistrationClickListener onRegistrationClickListener) {
            super(itemView);
            gstin = itemView.findViewById(R.id.item_registration_gstin);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRegistrationClickListener.OnRegistrationClick(getAdapterPosition(), v);
        }
    }

    public interface OnRegistrationClickListener {
        void OnRegistrationClick(int position, View view);
    }
}
