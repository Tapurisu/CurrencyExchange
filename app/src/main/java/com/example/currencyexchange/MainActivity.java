package com.example.currencyexchange;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements Runnable{
    float dollar_rate,euro_rate,won_rate;
    private static final String TAG = "MainActivity";
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 5){
                Bundle bdl = (Bundle) msg.obj;
                dollar_rate = bdl.getFloat("dollar_rate");
                euro_rate = bdl.getFloat("euro_rate");
                won_rate = bdl.getFloat("won_rate");

                Log.i(TAG, "handleMessage: dollarRate:" + dollar_rate);
                Log.i(TAG, "handleMessage: euroRate:" + euro_rate);
                Log.i(TAG, "handleMessage: wonRate:" + won_rate);
                Toast.makeText(getApplicationContext(),"汇率获取成功",Toast.LENGTH_LONG).show();
            }
            super.handleMessage(msg);
        }
    };
    @Override
    public void run() {
        Log.i(TAG, "run: run()......");
        Bundle bdl1 = new Bundle();
        List<String> list = new ArrayList<String>();
        try {
            String url = "https://www.usd-cny.com/bankofchina.htm";
            Document doc = Jsoup.connect(url).get();
            Elements tables = doc.getElementsByTag("tr");
            for(int i=1; i<tables.size(); i++){
                Element td = tables.get(i);
                Elements tds = td.getElementsByTag("td");
                Element td1 = tds.get(0);
                Element td2 = tds.get(5);

                String str1 = td1.text();
                String val = td2.text();

                float v = 100 / Float.parseFloat(val);
                if("美元".equals(str1)){
                    bdl1.putFloat("dollar_rate", v);
                }else if("欧元".equals(str1)){
                    bdl1.putFloat("euro_rate", v);
                }else if("韩国元".equals(str1)){
                    bdl1.putFloat("won_rate", v);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message msg5 = handler.obtainMessage(5);
        msg5.obj = bdl1;
        handler.sendMessage(msg5);

        SharedPreferences sp = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("dollar_rate_rate",dollar_rate);
        editor.putFloat("euro_rate_rate",euro_rate);
        editor.putFloat("won_rate_rate",won_rate);
        //获取月份日期放入
        SimpleDateFormat formatter= new SimpleDateFormat("MM-dd");
        Date date = new Date(System.currentTimeMillis());
        editor.putString("last_update_date", formatter.format(date));
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Activity.MODE_PRIVATE);//用sharedPreferences将设置保存到settings.xml文件中，初始若不存在此文件则赋默认值
        //获取当前日期，同上次更新日期相比较
        SimpleDateFormat formatter= new SimpleDateFormat("MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String date_of_now = formatter.format(date), last_update_date = sharedPreferences.getString("last_update_date","null");
        if(!date_of_now.equals(last_update_date)){
            //如果相等，则不必再更新，不等的话则至少经过了一天了，故进行一次更新
            Thread t = new Thread(this);
            t.start();
        }
        dollar_rate = sharedPreferences.getFloat("dollar_rate",0.1456f);
        euro_rate = sharedPreferences.getFloat("euro_rate",0.1260f);
        won_rate = sharedPreferences.getFloat("won_rate",100.0f);

    }

    public void currencyexchange(View v) {
        EditText currency = findViewById(R.id.editText);
        Float re = Float.parseFloat(currency.getText().toString());
        switch(v.getId()){
            case R.id.btn_dollar:
                currency.setText(String.valueOf(re * dollar_rate));
                break;
            case R.id.btn_euro:
                currency.setText(String.valueOf(re * euro_rate));
                break;
            case R.id.btn_won:
                currency.setText(String.valueOf(re * won_rate));
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == 1 && resultCode == 2) {
            Bundle bundle = data.getExtras();
            dollar_rate = bundle.getFloat("dollar_rate",0.0f);
            euro_rate = bundle.getFloat("euro_rate",0.0f);
            won_rate = bundle.getFloat("won_rate",0.0f);

            Log.i(TAG, "onActivityResult: dollar_rate=" + dollar_rate);
            Log.i(TAG, "onActivityResult: euro_rate=" + euro_rate);
            Log.i(TAG, "onActivityResult: won_rate=" + won_rate);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void config(View v){     //CONFIG按钮所实现的功能：转向Activity2
        Bundle bdl = new Bundle();
        bdl.putFloat("dollar_rate", dollar_rate);
        bdl.putFloat("euro_rate", euro_rate);
        bdl.putFloat("won_rate", won_rate);
        Intent second = new Intent(this, MainActivity2.class);
        second.putExtras(bdl);
        startActivityForResult(second, 1);
    }

    public void rectify(View v){
        Thread t = new Thread(this);
        t.start();
    }

    public void showlist(View v){
        Intent second = new Intent(this, RateListActivity.class);
        startActivityForResult(second, 1);
    }
}