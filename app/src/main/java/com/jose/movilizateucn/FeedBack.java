package com.jose.movilizateucn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class FeedBack extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
    }

    public void EnviarFeedBack(View view){
        Toast.makeText(this, "Mensaje Enviado", Toast.LENGTH_SHORT).show();
        FeedBack.this.finish();
    }


}
