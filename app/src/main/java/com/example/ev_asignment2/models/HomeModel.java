package com.example.ev_asignment2.models;

import android.widget.ProgressBar;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.io.Serializable;

public class HomeModel implements Serializable {

    String steps;
    String date;

    public HomeModel() {
    }

    public HomeModel(String steps, String date) {
        this.steps = steps;
        this.date = date;

    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
