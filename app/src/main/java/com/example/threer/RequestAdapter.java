package com.example.threer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a3rs.RecycleOrder;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    ArrayList<RecycleOrder> recycleOrderArrayList;
    Context context;

    public RequestAdapter(Context context, ArrayList<RecycleOrder> recycleOrderArrayList ){
       this.context=context;
       this.recycleOrderArrayList=recycleOrderArrayList;
    }
    @NonNull
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.request_item, parent, false);
        return new RequestAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.ViewHolder holder, int position) {
        RecycleOrder order =recycleOrderArrayList.get(position);
        holder.name.setText(order.getName());
        holder.time.setText(order.getTime());
        holder.date.setText(order.getDate());
        holder.mobileNumber.setText(order.getPhone());
        holder.zipcode.setText(order.getZipcode());
        holder.city.setText(order.getCity());
        holder.address.setText(order.getAddress());
    }

    @Override
    public int getItemCount() {
        return recycleOrderArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,address,city,zipcode,mobileNumber,date,time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            address=itemView.findViewById(R.id.address);
            city=itemView.findViewById(R.id.city);
            zipcode=itemView.findViewById(R.id.zipcode);
            mobileNumber=itemView.findViewById(R.id.mobileNumber);
            date=itemView.findViewById(R.id.date);
            time=itemView.findViewById(R.id.time);
        }
    }
}
