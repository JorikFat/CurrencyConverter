package ru.jorik.currencyconverter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.simpleframework.xml.core.Persister;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    String serverResponce;
    ValCurs valCurs;

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

            }
        });

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
        //// TODO: 12.08.2017 переделать разделитель double с "," на "."
        serverResponce = serverResponce.replace(',', '.');

        Reader reader = new StringReader(serverResponce);
        Persister deserializer = new Persister();

        try {
            valCurs = deserializer.read(ValCurs.class, reader, false);
//            Toast.makeText(this, "Десериализация", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] valuteArray;
        valuteArray = getCountryArray();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valuteArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ((Spinner)findViewById(R.id.spinner1)).setAdapter(arrayAdapter);
        ((Spinner)findViewById(R.id.spinner2)).setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String[] getCountryArray(){
        List<String> rList = new ArrayList<>();
        for (Valute v : valCurs.valuteList){
            rList.add(v.name);
        }
        String[] rArray = new String[rList.size()];
        rArray = rList.toArray(rArray);
        return rArray;
    }

}
