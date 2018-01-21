package com.example.michl.communityboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SendMessages extends AppCompatActivity {

    private AutoCompleteTextView textBox;
    private int userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_messages);
        userID = getIntent().getIntExtra("userID",0);
        textBox = (AutoCompleteTextView) findViewById(R.id.TextBox);
        Button sendButton = (Button) findViewById(R.id.Send);
        Button backButton = (Button) findViewById(R.id.Back);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessages();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoBack();
            }
        });
        textBox.setHeight(300);
        textBox.setWidth(800);
        textBox.setX(140);
        textBox.setY(150);
        sendButton.setHeight(400);
        sendButton.setWidth(400);
        backButton.setWidth(400);
        backButton.setHeight(400);
        sendButton.setX(540);
        backButton.setX(140);
        sendButton.setY(650);
        backButton.setY(650);
    }
    public static void HttpRequest(String message, int userID){
        try {
            List<Pair> params = new ArrayList<Pair>();
            params.add(new Pair("task", message));
            params.add(new Pair("id", Integer.toString(userID)));
            URL url = new URL("http://83bb8b84.ngrok.io/omega12/GoogleCal/add?"+getQuery(params));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            InputStream is = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            System.out.println(in.read());
            /*OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            conn.disconnect();;*/
            //os.close();

            conn.connect();
        }catch(Exception e){
            System.out.println(e);
        }

    }

    private static String getQuery(List<Pair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Pair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode((String) pair.first, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode((String) pair.second, "UTF-8"));
        }

        return result.toString();
    }
    private void sendMessages(){
        textBox.setError(null);
        final String text = textBox.getText().toString();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpRequest(text, userID);
            }
        });
        thread.start();
        GoBack();
    }

    private void GoBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}