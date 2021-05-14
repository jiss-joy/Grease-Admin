package com.rsa.greaseadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rsa.greaseadmin.Models.ModelServiceProvider;
import com.rsa.greaseadmin.R;

import java.util.ArrayList;

public class AdapterServiceProvider extends RecyclerView.Adapter<AdapterServiceProvider.MyViewHolder> {

    private Context mContext;
    private ArrayList<ModelServiceProvider> serviceProviders;
    private OnServiceProviderClickListener onServiceProviderClickListener;

    public AdapterServiceProvider(Context mContext, ArrayList<ModelServiceProvider> serviceProviders, OnServiceProviderClickListener onServiceProviderClickListener) {
        this.mContext = mContext;
        this.serviceProviders = serviceProviders;
        this.onServiceProviderClickListener = onServiceProviderClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterServiceProvider.MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_mini_service_provider_card, parent, false), onServiceProviderClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(serviceProviders.get(position).getServiceBusinessName());
        holder.dst.setText(serviceProviders.get(position).getServiceDistrict());
        holder.st.setText(serviceProviders.get(position).getServiceState());
        holder.pncd.setText(serviceProviders.get(position).getServicePinCode());
    }

    @Override
    public int getItemCount() {
        return serviceProviders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name, dst, st, pncd;

        public MyViewHolder(@NonNull View itemView, OnServiceProviderClickListener onServiceProviderClickListener) {
            super(itemView);

            name = itemView.findViewById(R.id.item_mini_service_provider_card_name);
            dst = itemView.findViewById(R.id.item_mini_service_provider_card_district);
            st = itemView.findViewById(R.id.item_mini_service_provider_card_state);
            pncd = itemView.findViewById(R.id.item_mini_service_provider_card_pncd);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onServiceProviderClickListener.OnServiceProviderClick(getAdapterPosition(), v);
        }
    }

    public interface OnServiceProviderClickListener {
        void OnServiceProviderClick(int position, View view);
    }
}
