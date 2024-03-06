package com.example.memorizationgame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class GameActivity extends AppCompatActivity {


    Chronometer ch;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //receiving user and displaying user information
        UerAccount user = (UerAccount) getIntent().getSerializableExtra("user");
        TextView scoreTextview = (TextView) findViewById(R.id.score);
        scoreTextview.setText("Your Score: \n" + "     " + user.gettotalPoints());

        //Displaying shapes
        Game bronze = new Game();
        int[] answer = bronze.getAnswer();
        ImageView shape1 = findViewById(R.id.shape1View);
        shape1.setImageResource(bronze.shapes[answer[0]]);
        ImageView shape2 = findViewById(R.id.shape2View);
        shape2.setImageResource(bronze.shapes[answer[1]]);
        ImageView shape3 = findViewById(R.id.shape3View);
        shape3.setImageResource(bronze.shapes[answer[2]]);

        //setting music
        final Intent serviceIntent = new Intent(GameActivity.this,MusicService.class);
        ImageButton musicPlayer = (ImageButton)findViewById(R.id.sound);
        if(MusicService.isplay == false){
            musicPlayer.setImageResource(R.drawable.start);
        }
        musicPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MusicService.isplay == false){
                    startService(serviceIntent);
                    musicPlayer.setImageResource(R.drawable.stop);
                }else {
                    stopService(serviceIntent);
                    musicPlayer.setImageResource(R.drawable.start);
                }
            }
        });

        //setting cutdown
        ch = (Chronometer) findViewById(R.id.cutdown);
        ch.setBase(SystemClock.elapsedRealtime() + 5000);
        ch.setCountDown(true);
        ch.setFormat("%s");
        ch.start();
        ch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                ch.setText(ch.getText().toString().substring(1));

                if (SystemClock.elapsedRealtime() - ch.getBase() >= 0) {
                    ch.stop();
                    Intent intent = new Intent(GameActivity.this, AnswerActivity.class);
                    //delivering data
                    intent.putExtra("game", bronze);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }
        });

        //setting exit button
        Button exitButton = (Button) findViewById(R.id.exitbutton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameActivity.this, AccountActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });



    }
}