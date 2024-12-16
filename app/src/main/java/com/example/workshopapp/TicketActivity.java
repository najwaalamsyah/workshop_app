package com.example.workshopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class TicketActivity extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> data;

    public TicketActivity(Context context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_ticket, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvPhone = convertView.findViewById(R.id.tvPhone);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);
        TextView tvTotalPerson = convertView.findViewById(R.id.tvTotalPerson);
        TextView tvAmount = convertView.findViewById(R.id.tvAmount);
        TextView tvPaymentMethod = convertView.findViewById(R.id.tvPaymentMethod);

        HashMap<String, String> ticket = data.get(position);

        tvName.setText("Name: " + ticket.get("name"));
        tvPhone.setText("Phone: " + ticket.get("phone"));
        tvDescription.setText("Event: " + ticket.get("description"));
        tvTotalPerson.setText("Num of Person: " + ticket.get("totalPerson"));
        tvAmount.setText("Amount: " + ticket.get("amount"));
        tvPaymentMethod.setText("Payment Method: " + ticket.get("paymentMethod"));

        return convertView;
        }
}
