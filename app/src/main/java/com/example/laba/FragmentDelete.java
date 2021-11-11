package com.example.laba;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentDelete extends Fragment {
    ArrayList<String> data;
    public String type_store;
    IStore Store;
    ServiceDatabase servicedb;
    boolean isBound;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (type_store == "file") {
            Store = new File_Separate_Thread(getActivity());
            data = Store.getStudents();
        } else if (type_store == "db") {
            if (!isBound) {
                Toast.makeText(getActivity(), "Not", Toast.LENGTH_SHORT).show();
                return;
            }
            Store = servicedb.getIStore();
            data = servicedb.getListStudent();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_del, null);
        ListView list = view.findViewById(R.id.list);
        Store.PrintData(data, list);
        Button b = view.findViewById(R.id.buttonBack);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fM = getFragmentManager();
                fM.popBackStackImmediate();
            }
        });
        return view;
    }
}