package ru.jorik.currencyconverter;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 111 on 12.08.2017.
 */

public class AsyncRequest extends AsyncTask<Context, Void, String> {

    Context context;

    @Override
    protected void onPostExecute(String s) {
//        Toast.makeText(context, "Данные пришли", Toast.LENGTH_SHORT).show();
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(Context... params) {
        context = params[0];
        String request="";
        try {
            request = sendRequestCB();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return request;

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
