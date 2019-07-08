package com.example.asynctaskdemo2;

import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    String strDataParse;
    AlertDialog dialog;
    private View.OnClickListener btn_click= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                MyTask process = new MyTask();
                process.execute();
        }
    };

    private void showProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Progress Dialog");
        builder.setView(R.layout.layout_progress);
        builder.setCancelable(false);
        dialog= builder.create();
        dialog.show();
    }

    public class MyTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //網址轉碼
                URL url = new URL("https://api.myjson.com/bins/a2j5r");
                //取得連線
                URLConnection conn = url.openConnection();
                //取得串流
                InputStream streamIn = conn.getInputStream();
                //準備開始解碼，首先，把剛剛的串流讀進來，製作一個串流讀取器(BufferReader)
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(streamIn));
                //做一個StringBuilder,接著不斷地去讀取串流，讀到他是NULL為止，在這之前則把每一行 append  到 StringBuilder 裡面
                StringBuilder html  = new StringBuilder();

                String line ;

                while ( (line = bufferedReader.readLine()) != null){
                    html.append(line);
                }

                String strJson = html.toString();

                JSONObject jsonObject = new JSONObject(strJson);

//          JSONArray ja = (JSONArray) jsonObject.opt("data");
                JSONArray ja  = jsonObject.getJSONArray("data");

                for(int i = 0 ; i<ja.length();i++){
                    JSONObject dataJo = ja.getJSONObject(i);
                    strDataParse = strDataParse+ dataJo.getString("mom_name");
                    Log.d(TAG,strDataParse);
                }

//           intTotal= (Integer) jsonObject.opt("total");
//            strDataParse = intTotal.toString();





            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            lbl.setText(strDataParse);
            dialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitialComponent();

    }

    private void InitialComponent() {
        lbl = findViewById(R.id.lbl);
        btn= findViewById(R.id.btn);
        btn.setOnClickListener(btn_click);
    }
    TextView lbl;
    Button btn;
}
