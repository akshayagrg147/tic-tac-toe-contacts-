package com.games.tictocgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void playTwoPGame(View view) {
        Intent intent = new Intent(this, PlayerName.class);
        if (intent.resolveActivity(getPackageManager()) != null) {


            startActivity(intent);
        }

    }

    public void playSinglePGame(View view) {
        Intent intent = new Intent(this, PlayerNameWithComputer.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }



}



