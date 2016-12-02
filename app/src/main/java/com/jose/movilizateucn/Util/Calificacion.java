package com.jose.movilizateucn.Util;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Actualiza el RatingBar (Estrellitas) y el Label text del Score
 */

public class Calificacion {

    public static void updateScore(RatingBar starBar, TextView lblScore){
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
}
