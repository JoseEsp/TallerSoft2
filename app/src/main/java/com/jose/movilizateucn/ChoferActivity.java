package com.jose.movilizateucn;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        updateScore();
        test(); //método para probar solamente (modifico el starBar y el lblScore
    }

    private void configureStarBar(){
        starBar = (RatingBar) findViewById(R.id.ratingBarChofer);
        lblScore = (TextView) findViewById(R.id.lblStarScoreChofer);
        lblScore.setTextColor(Color.BLACK);
    }

    private void updateScore(){
        float actualScore = starBar.getRating();
        if (actualScore < 2f){
            lblScore.setTextColor(Color.RED);
        }else if (actualScore >= 2f && actualScore < 4.0f){
            lblScore.setTextColor(Color.BLACK);
        }else{
            lblScore.setTextColor(Color.BLUE);
        }
    }

    private void test(){
        Button btnConfRuta = (Button) findViewById(R.id.btnConfigurarRuta);
        btnConfRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float actualScore = (float) (Math.random() * (float) starBar.getNumStars());
                String strScore = String.format("%.1f/%d", actualScore, starBar.getNumStars());
                lblScore.setText(strScore);
                starBar.setRating(actualScore);
                updateScore();
            }
        });
    }

}
