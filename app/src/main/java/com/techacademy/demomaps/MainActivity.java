package com.techacademy.demomaps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void mapView(View view){
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }
    public void sendMessage(View view){
        TextView number = (TextView)findViewById(R.id.numberField);
        final String numberString = number.getText().toString();
        number.setText("");
        TextView body = (TextView)findViewById(R.id.messageField);
        final String bodyString = body.getText().toString();
        body.setText("");
        TextView delay = (TextView)findViewById(R.id.delayField);
        final String delayString = delay.getText().toString();
        delay.setText("");
        String s =delayString.equals("1")?"":"s";
        Toast.makeText(getApplicationContext(),"Text will be sent in "+delayString+" minute"+s, Toast.LENGTH_LONG).show();
        Thread one = new Thread()
        {
            public void run()
            {

                sendMessage(numberString, bodyString,delayString);
            }

        };
        one.start();
    }

    public static void sendMessage(String number, String message, String delay)
    {

        HttpURLConnection connection = null;

        try {
            //Create connection
            message = URLEncoder.encode(message, "UTF-8");
            URL url = new URL("https://a39a455b.ngrok.io/para?number=" + number + "&message=" +
                    message + "&delay=" + delay);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length",
                    Integer.toString("PLEASE PUT SOMEHTING HERE FOR THE LOVE OF GOD".getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            wr.writeBytes("PLEASE PUT SOMEHTING HERE FOR THE LOVE OF GOD");
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
