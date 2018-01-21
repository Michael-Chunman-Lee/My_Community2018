package com.example.michl.communityboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textBox1;
    private TextView textBox2;
    private TextView textBox3;
    private int userID;
    public String[] texts;
    private int total;
    private int cur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userID = getIntent().getIntExtra("userID", 0);
        Button mRegisterButton = (Button) findViewById(R.id.refresh);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToRefresh();
            }
        });
        Button mSendButton = (Button) findViewById(R.id.send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToSend();
            }
        });
        Button mNext = (Button) findViewById(R.id.nextButton);
        Button mPrev = (Button) findViewById(R.id.prevButton);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPage();
            }
        });
        mPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevPage();
            }
        });
        textBox1 = (TextView) findViewById(R.id.TextBox1);
        textBox2 = (TextView) findViewById(R.id.TextBox2);
        textBox3 = (TextView) findViewById(R.id.TextBox3);
        textBox1.setHeight(250);
        textBox1.setWidth(800);
        textBox2.setHeight(250);
        textBox2.setWidth(800);
        textBox3.setHeight(250);
        textBox3.setWidth(800);
        textBox1.setX(140);
        textBox2.setX(140);
        textBox3.setX(140);
        textBox1.setY(100);
        textBox2.setY(550);
        textBox3.setY(1000);
        mRegisterButton.setHeight(400);
        mSendButton.setWidth(400);
        mRegisterButton.setWidth(400);
        mSendButton.setHeight(400);
        mRegisterButton.setY(1400);
        mSendButton.setY(1400);
        mRegisterButton.setX(140);
        mSendButton.setX(540);
        mPrev.setHeight(200);
        mNext.setHeight(200);
        mPrev.setWidth(400);
        mNext.setWidth(400);
        mPrev.setX(140);
        mNext.setX(540);
        mPrev.setY(1650);
        mNext.setY(1650);
        total = 0;
        cur = 0;
        ToRefresh();
    }

    private void ToRefresh(){
        final MainActivity activity = this;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpRequest(userID, activity);
            }
        });
        thread.start();
        curText();
    }



    private void curText(){
        System.out.println(cur);
        System.out.println(total);
        textBox1.setText("");
        textBox2.setText("");
        textBox3.setText("");
        if(cur >= 1)
            textBox1.setText(texts[cur - 1]);
        if (cur >= 2)
            textBox2.setText(texts[cur - 2]);
        if (cur >= 3)
            textBox3.setText(texts[cur - 3]);
    }
    private void nextPage(){
        if(cur >= 4){
            cur -= 3;
        }
        curText();
    }

    private void prevPage(){
        if(cur < total)
            cur += 3;
        curText();
    }

    public static void HttpRequest(int userID, MainActivity activity){
        try {
            List<Pair> params = new ArrayList<Pair>();
            params.add(new Pair("id", Integer.toString(userID)));
            URL url = new URL("http://83bb8b84.ngrok.io/omega12/GoogleCal/getAllMessage?"+getQuery(params));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            InputStream is = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String input = in.readLine();
            String[] all = input.split("=");
            System.out.println(input);
            /*OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            conn.disconnect();;*/
            //os.close();
            activity.texts = all;
            activity.total = all.length;
            activity.cur = all.length;
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



    private void ToSend(){
        setContentView(R.layout.activity_send_messages);
        Intent intent = new Intent(this, SendMessages.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

}