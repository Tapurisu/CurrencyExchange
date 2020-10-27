package com.example.currencyexchange;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class MainActivity2 extends AppCompatActivity {
    EditText edit_dollar, edit_euro, edit_won;
    float dollar_rate,euro_rate,won_rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        //intent.getFloatExtra(名称，取不到时值);
        Bundle bundle = intent.getExtras();
        dollar_rate = bundle.getFloat("dollar_rate",0.0f);
        euro_rate = bundle.getFloat("euro_rate",0.0f);
        won_rate = bundle.getFloat("won_rate",0.0f);

        edit_dollar= findViewById(R.id.edit_dollar_rate);
        edit_euro= findViewById(R.id.edit_euro_rate);
        edit_won= findViewById(R.id.edit_won_rate);
        //SharedPreferences sharedPreferences = getSharedPreferences("settings", Activity.MODE_PRIVATE);

        edit_dollar.setText(dollar_rate+"");
        edit_euro.setText(euro_rate+"");
        edit_won.setText(won_rate+"");

    }
    public void returnFather(View v){
        edit_dollar= findViewById(R.id.edit_dollar_rate);
        edit_euro= findViewById(R.id.edit_euro_rate);
        edit_won= findViewById(R.id.edit_won_rate);

        dollar_rate = Float.parseFloat(edit_dollar.getText().toString());
        euro_rate = Float.parseFloat(edit_euro.getText().toString());
        won_rate = Float.parseFloat(edit_won.getText().toString());

        Intent intent = getIntent();
        Bundle bdl = new Bundle();
        bdl.putFloat("dollar_rate", dollar_rate);
        bdl.putFloat("euro_rate", euro_rate);
        bdl.putFloat("won_rate", won_rate);
        intent.putExtras(bdl);

        setResult(2, intent);
        finish();
    }
}