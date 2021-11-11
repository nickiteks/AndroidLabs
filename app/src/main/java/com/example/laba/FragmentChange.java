package com.example.laba;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class FragmentChange extends Fragment {
    private static String result = "";
    String textData = "";
    ArrayList<String> dataCh;
    public String type_store;
    IStore Store;
    ActivityInterractor activityInterractor;
    ServiceDatabase servicedb;
    boolean isBound;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activityInterractor = (ActivityInterractor) context;
        if (type_store.equals("file")) {
            Store = new File_Separate_Thread(getActivity());
            dataCh = Store.getStudents();
        } else if (type_store.equals("db")) {
            if (!isBound) {
                Toast.makeText(getActivity(), "Not", Toast.LENGTH_SHORT).show();
                return;
            }
            Store = servicedb.getIStore();
            dataCh = servicedb.getListStudent();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change, null);
        textData = dataCh.get((dataCh.size()) - 1);

        TextView text = view.findViewById(R.id.textData);
        ListView list = view.findViewById(R.id.list);
        text.setText(textData.split(";")[1]);
        int index = dataCh.indexOf(textData);

        TextInputEditText repText = view.findViewById(R.id.text);

        Button bSave = view.findViewById(R.id.buttonSave);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = repText.getText().toString();

                if (value.length() > 0) {
                    result = textData.split(";")[0] + ";" + value + ";" + textData.split(";")[2];
                    dataCh.remove(index);
                    dataCh.add(index, result);
                    Store.UpdateStudent(result);
                    Store.PrintData(dataCh, list);
                } else
                    Toast.makeText(getActivity(), "Ошибка", Toast.LENGTH_LONG).show();

            }
        });
        Button bBack = view.findViewById(R.id.buttonBack);
        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.myFragmentManager.beginTransaction().remove(FragmentChange.this).commit();
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        System.out.println("onDestroyView");
        super.onDestroyView();
        activityInterractor.onFragmentClosed();
    }
}