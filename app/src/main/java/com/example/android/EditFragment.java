package com.example.android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class EditFragment extends Fragment {

    String editText;
    public EditFragment(String str) {
        editText = str;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit,
                container, false);
        EditText editTextFrame = (EditText) view.findViewById(R.id.editText);
        Button btnEdit = (Button) view.findViewById(R.id.btnFinalEdit);
        editTextFrame.setText(editText);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() != null) {
                    String data = editTextFrame.getText().toString();
                    ((MainActivity)getActivity()).dataFromFragment(data);
                }
            }
        });
        return view;
    }

}