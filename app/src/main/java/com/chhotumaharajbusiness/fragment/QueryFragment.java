package com.chhotumaharajbusiness.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.chhotumaharajbusiness.R;

public class QueryFragment extends DialogFragment {
    public static View fragment;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.activity_query, container, false);



         return fragment;
     }
    }
