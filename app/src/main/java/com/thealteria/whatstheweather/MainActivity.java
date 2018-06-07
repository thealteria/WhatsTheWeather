package com.thealteria.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
    TextView result;

    public void findWeather(View v) {

        String cityName1 = cityName.getText().toString();

        Log.i("City: ", cityName.getText().toString());

        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(cityName.getWindowToken(), 0);

        try {
            String encodedCname = URLEncoder.encode(cityName.getText().toString(), "UTF-8"); //if theres space in name
            DownloadTask task = new DownloadTask();
            task.execute("http://samples.openweathermap.org/data/2.5/weather?q=" + encodedCname
                    + "&appid=b6907d289e10d714a6e88b30761fae22");


        } catch (UnsupportedEncodingException e) {
            Toast.makeText(getApplicationContext(), "Could not find weather." + "\n"
                    + "Enter valid location", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (EditText) findViewById(R.id.cityName);
        result = (TextView) findViewById(R.id.result);


    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection connection = null;

            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();

                InputStream stream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(stream);

                int data = reader.read();
                while (data != -1) {

                    char addRes = (char) data;
                    result += addRes;

                    data = reader.read();
                }

                return result;


            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Could not find weather." + "\n"
                        + "Enter valid location", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                String message = "";

                JSONObject object = new JSONObject(s);
                String weatherInfo = object.getString("weather");

                Log.i("Result: ", weatherInfo);

                JSONArray array = new JSONArray(weatherInfo);

                for (int i = 0; i < array.length(); i++) {

                    JSONObject object1 = array.getJSONObject(i);

                    String main = "";
                    String description = "";

                    main = object1.getString("main");
                    description = object1.getString("description");

                    if (main != "" && description != "") {

                        message += main + ": " + description + "\r\n";
                    }

                }

                if (message != "") {

                    result.setText(message);
                }

                else {

                    Toast.makeText(getApplicationContext(), "Could not find weather." + "\n"
                            + "Enter valid location", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Could not find weather." + "\n"
                        + "Enter valid location", Toast.LENGTH_LONG).show();
            }

        }
    }
}
