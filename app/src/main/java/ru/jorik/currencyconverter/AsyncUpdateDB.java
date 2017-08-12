package ru.jorik.currencyconverter;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.simpleframework.xml.core.Persister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 111 on 12.08.2017.
 */

public class AsyncUpdateDB extends AsyncTask<MainActivity, Void, Context> {

    @Override
    protected void onPostExecute(Context context) {
        super.onPostExecute(context);
        ((MainActivity)context).valCurs.valuteList.size();
        ((MainActivity)context).valCurs.valuteList = ((MainActivity)context).vdb.getAllValutes();
        Toast.makeText(context, "База обновлена", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected Context doInBackground(MainActivity... params) {
        MainActivity mainActivity = params[0];
        ValCurs valCurs= mainActivity.valCurs;
        String request="";
        try {
            request = sendRequestCB();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //// Вот тут начинаются дикие костыли и танцы с бубнами. Устал
        request = request.replace(',', '.');
        Reader reader = new StringReader(request);
        Persister deserializer = new Persister();

        try {
            mainActivity.valCurs = deserializer.read(ValCurs.class, reader, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //добавление рубля, так как его нету в ответе сервера
        Valute rubValute = new Valute();
        rubValute.charCode = "RUB";
        rubValute.name = "Российский рубль";
        rubValute.nominal = 1;
        rubValute.id = "rubId";
        rubValute.numCode = 123;
        rubValute.value = 1;
        valCurs.valuteList.add(0, rubValute);

        for (Valute valute : valCurs.valuteList) {
            mainActivity.vdb.updateItem(valute);
        }

        return params[0];
    }

    private String sendRequestCB() throws IOException {
        URL cbURL = new URL(SimpleUtils.urlCB);
        HttpURLConnection connection = (HttpURLConnection) cbURL.openConnection();

        connection.setDoInput(true);
        connection.setRequestMethod("GET");


        InputStream inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuffer stringBuffer = new StringBuffer();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line).append("\n");
        }

        bufferedReader.close();
        connection.disconnect();

        return stringBuffer.toString();
    }
}
