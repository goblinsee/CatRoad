package com.example.android.catroad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class MainGame extends AppCompatActivity {
    public static final String EXTRA_MESSAGE
            = "com.example.android.catroad.extra.MESSAGE";
    private static int[] level_1 = {0, 3, 0, 1, 1, 1, 2, 0, 3, 0, 2, 3, 2, 2, 3, 2, 2, 4, 3, 4};
    private static int[] level_2 = {1, 1, 0, 3, 1, 3, 2, 3, 3, 3, 1, 0, 0, 1, 0, 2, 3, 1, 3, 2};
    private static int[] level_3 = {1, 0, 0, 0, 0, 3, 3, 0, 3, 3, 1, 2, 0, 2, 1, 3, 2, 3, 3, 2};
    private static int[] level_4 = {1, 1, 0, 0, 0, 2, 2, 3, 3, 1, 1, 0, 3, 0, 1, 3, 1, 4, 3, 3};
    private static int[] level_5 = {1, 1, 0, 1, 0, 3, 2, 3, 3, 3, 0, 0, 1, 3, 1, 4, 2, 0, 3, 0};
    private static int[] level_6 = {1, 0, 0, 2, 1, 3, 2, 3, 3, 2, 1, 2, 0, 0, 0, 1, 3, 0, 3, 1};
    private static int[] level_7 = {1, 0, 0, 1, 0, 3, 3, 1, 3, 3, 1, 4, 0, 0, 1, 3, 2 ,3 ,3, 0};
    private static int[] level_8 = {1, 2, 0, 0, 1, 0, 2, 0, 3, 0 ,0, 4, 0 ,3 ,2 ,4 ,3 ,3, 3, 4};
    private static int[] level_9 = {1, 0, 0 ,0 ,0 ,3 ,3 ,0, 3, 3, 1, 3, 0, 2, 1, 2, 2, 2, 3, 2};
    private static int[] level_10 = {0, 0, 0, 3, 1, 3, 2, 0, 3, 0, 0, 2, 2, 2, 2, 3, 3, 2, 3, 3};
    private static int[] level_11 = {1, 0, 0, 0, 3, 0, 0, 2, 3, 2, 1, 2, 0, 4, 3, 4, 1, 3, 2, 3};
    private TextView mLevelName;
    private TextView mCount, mTime;
    private ChessboardView mBoard;
    private int finalStep, level;
    private int midTime = 120;
    private boolean result = true;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_main);

        mLevelName = findViewById(R.id.level_name);
        mCount = findViewById(R.id.stepCount);
        mTime = findViewById(R.id.timeCount);
        //设置关卡
        Intent intent = getIntent();
        String message = intent.getStringExtra(LevelOption.EXTRA_MESSAGE);
        mLevelName.setText(message);
        mBoard = findViewById(R.id.chessboardView);
        switch (message) {
            case "猫猫不见了":
                mBoard.setLevelInfo(level_1);
                level = 1;
                break;
            case "移形换影":
                mBoard.setLevelInfo(level_2);
                level = 2;
                break;
            case "重蹈覆辙":
                mBoard.setLevelInfo(level_3);
                level = 3;
                break;
            case "甜心出击":
                mBoard.setLevelInfo(level_4);
                level = 4;
                break;
        }
        //初始化游戏
        setCount(0);
        mBoard.init();
        mBoard.findGameWin(new ChessboardView.winCallBack(){
            @Override
            public void isWin(int count) {
                gameWin(count);
                finalStep = count;
            }
            @Override
            public void showCount(int count) {
                setCount(count);
            }
        });
        //倒计时
        mTime.setText(getString(R.string.game_time) + midTime + 's');
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                midTime--;
                mTime.setText(getString(R.string.game_time) + midTime + 's');
                if(midTime > 0)
                    handler.postDelayed(this, 1000);
                else {
                    result = false;
                    gameWin(0);
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }
    private void showWinDialog(boolean r, int c) {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.game_win,null);
        customizeDialog.setView(dialogView);
        if(r) {
            customizeDialog.setMessage(getString(R.string.win_game) + c);
            customizeDialog.setPositiveButton("好的",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            certainWin();
                        }
                    });
        }
        else {
            customizeDialog.setMessage(getString(R.string.lose_game));
            customizeDialog.setPositiveButton("好的",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            certainLose();
                        }
                    });
        }
        customizeDialog.setCancelable(false);
        customizeDialog.show();
    }

    public void gameWin(int c) {
        showWinDialog(result, c);
    }
    public void setCount(int c) {
        mCount.setText(getString(R.string.game_step) + c);
    }
    public void certainWin() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MESSAGE, String.valueOf(finalStep));
        setResult(level, intent);
        finish();
    }
    public void certainLose() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MESSAGE, "0");
        setResult(0, intent);
        finish();
    }
    public void backToLevel(View v) {
        onBackPressed();
    }
    public void restartGame(View v) {
        setCount(0);
        midTime = 120;
        mTime.setText(getString(R.string.game_time) + midTime + 's');
        mBoard.reset();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MESSAGE, "0");
        setResult(0, intent);
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
}
