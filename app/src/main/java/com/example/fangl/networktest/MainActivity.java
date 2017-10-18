package com.example.fangl.networktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.send_request);
        button.setOnClickListener(this);
        textView = (TextView)findViewById(R.id.responseText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.send_request:
//                sendRequestWithHttpUrlConnection();
                sendRequestWithOkHttp();
                break;
            default:


        }
    }


    private void sendRequestWithHttpUrlConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {

                    URL url = new URL("http://rmis.ideasoft.net.cn:7073/home/api");
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    showResponseText(response.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (reader!=null){
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection!=null)
                        connection.disconnect();
                }


            }
        }).start();
    }

    private void showResponseText(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(response);
            }
        });
    }

    private void sendRequestWithOkHttp(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                // 如果是post,需要创建一个requestBody对象
             /*   RequestBody requestBody = new FormBody.Builder()
                        .add("id","12345")
                        .add("name","hello")
                        .build();

                Request request = new Request.Builder()
                        .url("http://rmis.ideasoft.net.cn:7073/home/api")
                        .post(requestBody)
                        .build();*/

                Request request = new Request.Builder()
                        .url("http://rmis.ideasoft.net.cn:7073/home/api")
                        .build();
                try {
                   Response response =  client.newCall(request).execute();
                   String responseData = response.body().string();
                   showResponseText(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }


}
