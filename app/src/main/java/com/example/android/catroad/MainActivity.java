package com.example.android.catroad;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView title;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        //开场动画
        PropertyValuesHolder a1 = PropertyValuesHolder.ofFloat("alpha", 0.2f, 1f);
        PropertyValuesHolder a2 = PropertyValuesHolder.ofFloat("translationY", -150, 0);
        PropertyValuesHolder a3 = PropertyValuesHolder.ofFloat("scaleX", 0.4f, 1.0f);
        PropertyValuesHolder a4 = PropertyValuesHolder.ofFloat("scaleY", 0.4f, 1.0f);
        ObjectAnimator a = ObjectAnimator.ofPropertyValuesHolder(title, a1, a2, a3, a4);
        a.setDuration(1600);
        a.setInterpolator(new OvershootInterpolator());
        a.start();
        //设置BGM
        mp = MediaPlayer.create(this, R.raw.bgm);
        mp.setLooping(true);
        mp.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.release();
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, LevelOption.class);
        startActivity(intent);
    }

    public void quitGame(View view) {
        System.exit(0);
    }
}
