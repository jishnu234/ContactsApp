package com.example.contactsapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements Filterable {

    private ArrayList<User> user;
    private ArrayList<User> userCopy;
    Context mcontext;
    Activity mactivity;

    public MyAdapter(ArrayList<User> user,Context context) {
        this.user=user;
        userCopy=new ArrayList<>(user);
        mcontext=context;
        mactivity=(Activity)context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final int positionnew=position;
        holder.text_name.setText(user.get(position).getName());
        holder.text_phone.setText(user.get(position).getContacts());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(mcontext, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(mactivity,new String[]{Manifest.permission.CALL_PHONE},2);
                }
                else
                {
                    Intent intent=new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+user.get(positionnew).getContacts().trim()));
                    mcontext.startActivity(intent);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return user.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<User> filteredList=new ArrayList<>();

            if(constraint==null || constraint.length()==0)
            {
                filteredList.addAll(userCopy);
            }
            else
            {
                String data=constraint.toString().toLowerCase().trim();

                for(User user:userCopy)
                {
                    if(user.getName().toLowerCase().contains(data))
                    {
                        filteredList.add(user);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            user.clear();
            user.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_name,text_phone;
        RelativeLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout=itemView.findViewById(R.id.layout);
            text_name=itemView.findViewById(R.id.custom_name);
            text_phone=itemView.findViewById(R.id.custom_phone);
        }
    }
}
