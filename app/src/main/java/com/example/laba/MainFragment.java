package com.example.laba;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    ArrayList<String> dataCh;
    ActivityInterractor activityInterractor;
    int index = 1;
    public String type_store;
    IStore Store;
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
        if (dataCh == null) {
            Toast.makeText(getActivity(), "Data null", Toast.LENGTH_SHORT).show();
            System.out.println("Data null");
        }
        if (dataCh.size() == 0) {
            index = 1;
        } else {
            for (int i = 0; i < dataCh.size(); i++) {
                if (Integer.parseInt(dataCh.get(i).split(";")[0]) > index)
                    index = Integer.parseInt(dataCh.get(i).split(";")[0]) + 1;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);

        ListView list = view.findViewById(R.id.list);
        FragmentManager fM = getFragmentManager();
        TextInputEditText repText = view.findViewById(R.id.text);
        TextInputEditText countText = view.findViewById(R.id.count);

        Button bAdd = view.findViewById(R.id.buttonAdd);

        if (dataCh.size() > 0)
            PrintData(dataCh, list);

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = repText.getText().toString();
                String count = countText.getText().toString();
                if (value.length() > 0 && count.length() > 0) {
                    String student = String.valueOf(index) + ";" + value + ";" + count;
                    dataCh.add(student);
                    index++;
                    PrintData(dataCh, list);
                    repText.setText("");
                    countText.setText("");
                } else
                    Toast.makeText(getActivity(), "Ошибка", Toast.LENGTH_LONG).show();

            }
        });
        Button bBack = view.findViewById(R.id.buttonBack);
        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.myFragmentManager.beginTransaction().remove(MainFragment.this).commit();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        System.out.println("onDestroyView");
        super.onDestroyView();
        Store.InsertStudents(dataCh);
        activityInterractor.onFragmentClosed();
    }

    public void PrintData(ArrayList<String> dataCh, ListView list) {
        // создаем адаптер
        ArrayList<String> buf = new ArrayList<String>();
        for (int i = 0; i < dataCh.size(); i++) {
            System.out.println("PRINT: " + dataCh.get(i));
            buf.add(
                    "              " + dataCh.get(i).split(";")[1]
                            + "              " + dataCh.get(i).split(";")[2]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.simple_list, buf);
        list.setAdapter(adapter);
        //устанавливаем множественный выбор
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
}