package com.example.android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class DeleteFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete,
                container, false);
        Button btnYes = (Button) view.findViewById(R.id.btnDeleteYes);
        Button btnNo = (Button) view.findViewById(R.id.btnDeleteNo);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() != null) {
                    ((MainActivity)getActivity()).dataFromDelFragment(Boolean.TRUE);
                }

            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() != null) {
                    ((MainActivity)getActivity()).dataFromDelFragment(Boolean.FALSE);
                }
            }
        });
        return view;
    }
}