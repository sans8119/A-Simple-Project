package com.my.fitness.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.my.fitness.controller.Controller;

public class BlankFragment extends Fragment {

    private Controller controller;
    private String[] uiData;
    private int position;

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public Controller getController() {
        return controller;
    }

}
