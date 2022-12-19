package com.example.ev_asignment2.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.example.ev_asignment2.R;
import com.example.ev_asignment2.models.HomeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class StepsActivity extends AppCompatActivity implements SensorEventListener{

    FirebaseFirestore db;
    private TextView steps;
    private TextView disDate;
    private ListView lv;
    int stepCount;
    int previousSteps = 0;
    SensorManager sensorManager = null;
    boolean running;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private String userID;


    SharedPreferences sharedPreferences;
    //CircularProgressIndicator progressBar;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        userID = getIntent().getStringExtra("userID");
        steps = findViewById(R.id.detail_stepsTaken);
        disDate = findViewById(R.id.dis_date);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //progressBar = findViewById(R.id.circularProgressBar);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = dateFormat.format(calendar.getTime());

        db = FirebaseFirestore.getInstance();


        saveData();
        loadData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        running = true;

        Sensor stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        // Register a listener for the sensor.
        if(stepCounterSensor !=null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI);
        }
        else{
            Toast.makeText(this, "Sensor not working", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;

        /*SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(this);*/
    }


        @Override
        public void onSensorChanged(SensorEvent event) {
            if(running) {

                stepCount = (int) event.values[0]-previousSteps;
                steps.setText(String.valueOf(stepCount));

                //progressBar.setProgress(stepCount);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void saveData() {
            Map<String, Object> dSteps = new HashMap<>();
            dSteps.put("steps", String.valueOf(stepCount));
            dSteps.put("date", String.valueOf(date));
            dSteps.put("userID", userID);

            db.collection("daily_steps").whereEqualTo("userID", userID).whereEqualTo("date", date)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot document2 = task.getResult();
                                    if (document2.isEmpty()) {
                                        db.collection("daily_steps")
                                                .add(dSteps)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("TAG", "Error adding document", e);
                                                    }
                                                });
                                        Log.d("val","false");
                                    }
                                    else {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            db.collection("daily_steps")
                                                    .document(document.getId())
                                                    .set(dSteps);
                                        }
                                    }
                            }else {
                                Log.d("TAG", "Failed with: ", task.getException());
                            }
                        }
                    });
        }



        public void loadData() {

            List<HomeModel> list = (List<HomeModel>) getIntent().getSerializableExtra("list");
            for (int i = 0; i < list.size(); i++) {
                if (Objects.equals(list.get(i).getDate(), date)){
                    disDate.setText(list.get(i).getDate());
                }

            }
        }

}

