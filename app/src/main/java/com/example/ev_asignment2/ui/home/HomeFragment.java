package com.example.ev_asignment2.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ev_asignment2.R;
import com.example.ev_asignment2.adapters.HomeAdapter;
import com.example.ev_asignment2.models.HomeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth auth;
    RecyclerView stepRec;

    //step items
    HomeAdapter homeAdapter;
    List<HomeModel> homeModelsList;
    CircularProgressIndicator cpi;

     public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container,false);
        db = FirebaseFirestore.getInstance();
        cpi = root.findViewById(R.id.circularProgressBar);
        auth  = FirebaseAuth.getInstance();

        //recyclers
         stepRec = root.findViewById(R.id.step_rec);

         //step item
         stepRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false));
         homeModelsList = new ArrayList<>();
         homeAdapter = new HomeAdapter(getActivity(), homeModelsList);
         stepRec.setAdapter(homeAdapter);


         db.collection("daily_steps").whereEqualTo("userID", auth.getCurrentUser().getUid())
                 .get()
                 .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                     @SuppressLint("NotifyDataSetChanged")
                     @Override
                     public void onComplete(@NonNull Task<QuerySnapshot> task) {
                         if (task.isSuccessful()) {
                             for (QueryDocumentSnapshot document : task.getResult()) {
                                 HomeModel homeModel = document.toObject(HomeModel.class);
                                 homeModelsList.add(homeModel);
                                 homeAdapter.notifyDataSetChanged();

                             }
                         } else {
                             Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                         }
                     }
                 });


        return root;

    }


}