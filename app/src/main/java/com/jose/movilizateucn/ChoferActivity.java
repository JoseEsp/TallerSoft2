package com.jose.movilizateucn;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class ChoferActivity extends AppCompatActivity {

    private RatingBar starBar;
    private TextView lblScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chofer);
        configureStarBar();
    }

    private void configureStarBar(){
        starBar = (RatingBar) findViewById(R.id.ratingBarChofer);
        lblScore = (TextView) findViewById(R.id.lblStarScoreChofer);
        updateScore();
        test(); //método para probar solamente (modifico el starBar y el lblScore
    }

    private void updateScore(){
        float actualScore = starBar.getRating();
        String strScore = String.format("%.1f/%d", actualScore, starBar.getNumStars());
        Spannable colorText = new SpannableString(strScore);
        ForegroundColorSpan color = null;
        if (actualScore < 2.5f){
            color = new ForegroundColorSpan(Color.RED);
        }else if (actualScore >= 2.5f && actualScore < 4f){
            color = new ForegroundColorSpan(Color.BLACK);
        }else{
            color = new ForegroundColorSpan(Color.BLUE);
        }
        //Colorea antes del '/'
        colorText.setSpan(color, 0, strScore.indexOf("/"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //Colorea después del '/'
        colorText.setSpan(new ForegroundColorSpan(Color.BLACK), strScore.indexOf("/"),
                          strScore.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        lblScore.setText(colorText);
    }

    private void test(){
        Button btnConfRuta = (Button) findViewById(R.id.btnConfigurarRuta);
        btnConfRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float actualScore = (float) (Math.random() * (float) starBar.getNumStars());
                starBar.setRating(actualScore);
                updateScore();
            }
        });
    }

}
