package com.example.ibrah.to_do_list;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {


    ListView listView;
    private ArrayList<String> list;
    ArrayAdapter<String> listViewAdapter;

    public ListFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_list, container, false);

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        listView = view.findViewById(R.id.listView);

        list = new ArrayList<>();
        refresh();                              //ilk listViewAdapter atamasi

        //alttal, fonksiyon listedeki bir goreve uzun sure tiklyinca gorevi listeden silme islemine (remove) gonderiyor.
        AdapterView.OnItemLongClickListener onItemLongClickListener=new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                remove(position);
                return false;
            }
        };
        listView.setOnItemLongClickListener(onItemLongClickListener);       //olusturulan adapter'in onItemLongClickListener'a atanmasi

        return  view;
    }

    public void refresh()       //listedeki ekleme,silme,liste degisikligi gibi degismelerde listView'in guncellemesi
    {
       listViewAdapter = new ArrayAdapter<>(
                getActivity(),android.R.layout.simple_list_item_1,list
        );
        listView.setAdapter(listViewAdapter);
    }

    public  void remove(int position)       //listeden veri silme
    {
        list.remove(position);
        refresh();
    }

    public void add(String s)               //listeye veri ekleme
    {
        list.add(s);
        refresh();
    }

    public void setStrings(ArrayList<String> s)     //listeye baska bir gorev listesi atama, kalıcı dosyadan veri okuyup atamada kullanilacak
    {
        list = new ArrayList<>(s);
        refresh();
    }

    public ArrayList<String> getStrings()           //listeyi donduren fonksiyon, main'deki islemler icin gerekli
    {
        return list;
    }
}