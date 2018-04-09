package com.example.ibrah.to_do_list;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends Activity {

    EditText editText;
    Button button;
    ListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        listFragment = new ListFragment();
        //fragmentTransaction.replace(R.id.fragment_container,f1);             //alt komuttaki .add komutu yerine replace de calisiyor ama vardir bir farki??
        fragmentTransaction.add(R.id.fragment_container,listFragment);         //fragment maindeki linear layoutun icine yerlesecek
        fragmentTransaction.commit();

        button = findViewById(R.id.button);                                   //widgetları ekliyoruz
        editText = findViewById(R.id.editText);
    }

    @Override
    public void onResume() {                                      //onCreate de fragmente erişim hata veriyor, onResume de dosya okuma yapiyoruz
        super.onResume();
        readFromFile();
    }

    @Override
    protected void onPause() {                                  //listeden görev silme fragmentde olguğu icin ve fragmentde dosya yazma kutuphaneleri
        super.onPause();                                        //calismadigi icin onPause da tekrar yazma yapiyoruz.
        writeToFile();
    }

    public void add_onClick(View view) {                        //her gorev eklendiginde fragmentdaki listeye ekleyip kalıcı bir dosyaya yazıyoruz

        if(editText.getText().length() != 0)                    //bosluk yapılacak bir gorev degil, edittexte yazi girilmesi bekleniyor
        {
            if(!listFragment.getStrings().contains(editText.getText().toString()))  //girilen gorev listede yok ise ekle
            {
                listFragment.add(editText.getText().toString());                    //fragmentdaki (listFragmet clasindaki) listeye ekleme islemi
                writeToFile();                         //dosyaya yazma islemi
                editText.setText("");                                               //editTexti silme
            }else
                Toast.makeText(this,"Bu görev zaten listenizde mevcut!",Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(this,"Lütfen geçerli bir görev giriniz!",Toast.LENGTH_SHORT).show();
    }

    private void writeToFile() {                            //dosyaya yazma islemi,

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        Gson gson = new Gson();

        prefsEditor.putString("list",gson.toJson(listFragment.getStrings())); //gson ile objeleri string formatında yaziyoruz
        prefsEditor.apply();
    }

    private void readFromFile() {                                   //dosyadan okuma islemi,

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        Gson gson = new Gson();
        String json = prefs.getString("list", "");

        if(!json.equals(""))                                        //json "" donerse "list" tagında herhangi bir kayıt yok demek, kayıt var ise okuyoruz
        {
            ArrayList<String> s = gson.fromJson(json, new TypeToken<ArrayList<String>>(){}.getType());      //gson ile kayitli listeyi okuma islemi

            listFragment.setStrings(s);                             //okunan veriyi listfragment sınıfındaki listeye atiyoruz
        }
    }

/*  Alternatif dosya okuma yazma fonksiyonları

    private void readFromFile() {

        ArrayList<String> s = new ArrayList<>();
        Scanner scan = null;
        try {
            scan = new Scanner(openFileInput("filename.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"Dosya okuma hatası",Toast.LENGTH_SHORT).show();
        }

        while (scan.hasNextLine()) {
            s.add(scan.nextLine());}

        listFragment.setStrings(s);
    }

    private void writeToFile(String s) {

        PrintStream out = null;
        try {
            out = new
                    PrintStream(openFileOutput("filename.txt",MODE_PRIVATE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"Dosya yazma hatası",Toast.LENGTH_SHORT).show();
        }

        for (String ss:listFragment.getStrings()
             ) {
            out.println(ss);
        }
        out.close();
    }
*/

//  InstanceState olmasa da veriler korunuyor...
/*
    //telefonu yan cevirince girilen veriler kaybolmasin diye verileri kayit ediyoruz.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArrayList("list",listFragment.getStrings());
    }

    //telefonu yan cevirince saveInstanceState de yedekledigimiz verileri geri dolduruyoruz
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        listFragment.setStrings(savedInstanceState.getStringArrayList("list"));
    }
*/
}
