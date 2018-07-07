package com.example.vadim.daltonicgame;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<Record> mRecords;

    DataAdapter(Context context, List<Record> records){
        this.mRecords = records;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.ViewHolder holder, int position) {
        Record record = mRecords.get(position);
        holder.mNameTextView.setText(record.getName());
        holder.mDateTextView.setText(record.getDate());
        holder.mResultTextView.setText(String.valueOf(record.getResult()));
    }

    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mNameTextView;
        final TextView mDateTextView;
        final TextView mResultTextView;
        ViewHolder(View view){
            super(view);
            mNameTextView = view.findViewById(R.id.recordNameTextView);
            mDateTextView = view.findViewById(R.id.recordDateTextView);
            mResultTextView = view.findViewById(R.id.recordResultTextView);
        }
    }
}
