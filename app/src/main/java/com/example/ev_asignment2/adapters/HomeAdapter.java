package com.example.ev_asignment2.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ev_asignment2.R;
import com.example.ev_asignment2.activities.StepsActivity;
import com.example.ev_asignment2.models.HomeModel;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements Serializable {

    private Context context;
    private List<HomeModel> list;
    private FirebaseAuth auth;

    public HomeAdapter(Context context, List<HomeModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.steps.setText(list.get(position).getSteps());
        holder.date.setText(list.get(position).getDate());
        auth  = FirebaseAuth.getInstance();
        // Get the common element for the transition in this activity


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StepsActivity.class);
                intent.putExtra("userID", auth.getCurrentUser().getUid());
                intent.putExtra("list",(Serializable) list);

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView steps, date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            steps = itemView.findViewById(R.id.tv_stepsTaken);
            date  = itemView.findViewById(R.id.tv_date);

        }
    }
}
