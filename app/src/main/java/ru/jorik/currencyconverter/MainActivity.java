package ru.jorik.currencyconverter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.simpleframework.xml.core.Persister;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    ValuteDateBase vdb;
    String serverResponce;
    ValCurs valCurs;
    Spinner spinner1;
    Spinner spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convert();
            }
        });

        vdb = new ValuteDateBase(this);
        if (vdb.getCount() == 0){
            formListValute();
            for (Valute valute : valCurs.valuteList){
                vdb.createItem(valute);
            }
        } else {
            valCurs = new ValCurs();
            valCurs.valuteList = vdb.getAllValutes();
            new AsyncUpdateDB().execute(this);
        }

        String[] valuteArray;
        valuteArray = getValuteArray();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valuteArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1 = ((Spinner)findViewById(R.id.spinner1));
        spinner2 = ((Spinner)findViewById(R.id.spinner2));
        spinner1.setAdapter(arrayAdapter);
        spinner2.setAdapter(arrayAdapter);
    }

    private String[] getValuteArray(){
        List<String> rList = new ArrayList<>();
        for (Valute v : valCurs.valuteList){
            rList.add(v.charCode + " " + v.name);
        }
        String[] rArray = new String[rList.size()];
        rArray = rList.toArray(rArray);
        return rArray;
    }

    private String[] getValuteItem(){
        List<String> rList = new ArrayList<>();
        for (Valute v : valCurs.valuteList){
            rList.add(v.charCode);
        }
        String[] rArray = new String[rList.size()];
        rArray = rList.toArray(rArray);
        return rArray;
    }

    private void convert(){
        Valute vf;
        Valute vt;

        vf = valCurs.valuteList.get(spinner1.getSelectedItemPosition());
        vt = valCurs.valuteList.get(spinner2.getSelectedItemPosition());
        double inputSum = Double.parseDouble(((EditText) findViewById(R.id.editText)).getText().toString());

        double coef1 = vf.value / vf.nominal;
        double coef2 = vt.value / vt.nominal;


        double tempValue = inputSum * coef1;//in Rub
        double result = tempValue / coef2;

        ((TextView)findViewById(R.id.textView)).setText(String.format("%.4f",result));
    }


    void formListValute(){
        try {
            serverResponce = new AsyncRequest().execute(this).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        /*
        * Костыль:
        * преобразуем все разделители вещественных чисел из запятых в точки
        * Я проверял ответ от сервера. Запятые выдается только в разделителях чисел,
        * больше они нигде не встречаются
        */
        //// TODO: 12.08.2017 переделать разделитель double с "," на "."      serverResponce = serverResponce.replace(',', '.');
        serverResponce = serverResponce.replace(',', '.');

        Reader reader = new StringReader(serverResponce);
        Persister deserializer = new Persister();

        try {
            valCurs = deserializer.read(ValCurs.class, reader, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //добавление рубля, так как его нету в ответе сервера
        Valute rubValute = new Valute();
        rubValute.charCode = "RUB";
        rubValute.id = "rubId";
        rubValute.numCode = 123;
        rubValute.name = "Российский рубль";
        rubValute.nominal = 1;
        rubValute.value = 1;
        valCurs.valuteList.add(0, rubValute);
    }
}
