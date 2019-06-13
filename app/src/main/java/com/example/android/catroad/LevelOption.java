package com.example.android.catroad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LevelOption extends AppCompatActivity {
    public static final String EXTRA_MESSAGE
            = "com.example.android.catroad.extra.MESSAGE";
    private Button mLevelName;
    private static String level_1, level_2, level_3, level_4;
    private TextView mStep_1, mStep_2, mStep_3, mStep_4;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_main);
        mStep_1 = findViewById(R.id.step_1);
        mStep_2 = findViewById(R.id.step_2);
        mStep_3 = findViewById(R.id.step_3);
        mStep_4 = findViewById(R.id.step_4);

        //获取SharedPreferences对象
        sp = this.getSharedPreferences("SP", MODE_PRIVATE);
        editor = sp.edit();
        level_1 = sp.getString("LEVEL_1", "null");
        level_2 = sp.getString("LEVEL_2", "null");
        level_3 = sp.getString("LEVEL_3", "null");
        level_4 = sp.getString("LEVEL_4", "null");
        if(!level_1.equals("null")) {
            mStep_1.setText(getString(R.string.is_win) + level_1);
        }
        if(!level_2.equals("null")) {
            mStep_2.setText(getString(R.string.is_win) + level_2);
        }
        if(!level_3.equals("null")) {
            mStep_3.setText(getString(R.string.is_win) + level_3);
        }
        if(!level_4.equals("null")) {
            mStep_4.setText(getString(R.string.is_win) + level_4);
        }
    }


    @Override
    protected void onActivityResult(int responseCode, int resultCode, Intent data){
        String info = data.getStringExtra(EXTRA_MESSAGE);
        switch (resultCode) {
            case 1:
                if(level_1.equals("null") || info.compareTo(level_1) < 0) {
                    level_1 = info;
                    mStep_1.setText(getString(R.string.is_win) + level_1);
                    //存入数据
                    editor.putString("LEVEL_1", level_1);
                    editor.commit();
                }
                break;
            case 2:
                if(level_2.equals("null") || info.compareTo(level_2) < 0) {
                    level_2 = info;
                    mStep_2.setText(getString(R.string.is_win) + level_2);
                    editor.putString("LEVEL_2", level_2);
                    editor.commit();
                }
                break;
            case 3:
                if(level_3.equals("null") || info.compareTo(level_3) < 0) {
                    level_3 = info;
                    mStep_3.setText(getString(R.string.is_win) + level_3);
                    editor.putString("LEVEL_3", level_3);
                    editor.commit();
                }
                break;
            case 4:
                if(level_4.equals("null") || info.compareTo(level_4) < 0) {
                    level_4 = info;
                    mStep_4.setText(getString(R.string.is_win) + level_4);
                    editor.putString("LEVEL_4", level_4);
                    editor.commit();
                }
                break;
            default:
                break;
        }
    }

    public void getLevel(View view) {
        Intent intent = new Intent(this, MainGame.class);
        mLevelName = findViewById(view.getId());
        String message = (String) mLevelName.getText();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivityForResult(intent, 10);
    }
}
