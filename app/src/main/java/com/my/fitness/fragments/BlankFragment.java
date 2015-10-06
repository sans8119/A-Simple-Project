package com.my.fitness.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.my.fitness.activity.SetValues;
import com.my.fitness.controller.Controller;


public class BlankFragment extends Fragment {

    private Controller controller;
    private String[] uiData;
    private int position;

    public void setPosition(int position){
        this.position=position;
    }

    public int getPosition(){
        return position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        controller=new Controller();
    }

    public Controller getController() {
        return controller;
    }

}
