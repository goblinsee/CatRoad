package com.example.android.catroad;

import java.io.InputStream;
import java.util.Enumeration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class ChessboardView extends View
{
    private static final String TAG = "hrd";
    private MainRoad playBoard;
    private int gridSize;
    private int[] levelInfo;
    private winCallBack winBack;
    private static SoundPool sp;
    private Context context;

    public ChessboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ChessboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public ChessboardView(Context context) {
        super(context);
        this.context = context;
    }

    public void setLevelInfo(int[] levelInfo) {
        this.levelInfo = levelInfo;
    }

    public void init () {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        gridSize = (int) (dm.density * 80 + 0.5f);
        playBoard = new MainRoad();
        Fragment.reset();
        Fragment.setPlayBoard(playBoard);
        setChess();
        //初始化音效
        if (sp == null) {
            // 5.0 及 之后
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes audioAttributes = null;
                audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build();

                sp = new SoundPool.Builder()
                        .setMaxStreams(16)
                        .setAudioAttributes(audioAttributes)
                        .build();
            } else { // 5.0 以前
                sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);  // 创建SoundPool
            }
        }
        final int cat1 = sp.load(context, R.raw.cat1, 1);
        final int cat2 = sp.load(context, R.raw.cat2, 1);

        this.setOnTouchListener(new OnTouchListener()
        {
            private int xPos = 0;
            private int yPos = 0;
            private int selectedValue = 0;
            // private int mScreenWidth, mScreenHeight;//屏幕宽高
            private int start_x, start_y;

            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // mScreenWidth = v.getResources().getDisplayMetrics().widthPixels;
                        // mScreenHeight = v.getResources().getDisplayMetrics().heightPixels;
                        //得到当前方块
                        start_x = (int)event.getX()/gridSize;
                        start_y = (int)event.getY()/gridSize;
                        selectedValue = playBoard.getBoardValue(start_x, start_y);
                        if(selectedValue == 1) {
                            sp.play(cat1, 1, 1, 1, 0, 1);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        xPos = (int)event.getX()/gridSize;
                        yPos = (int)event.getY()/gridSize;
                        // 选取空白或者超出边界时不反应
                        if(selectedValue == 0 || xPos > 3 || yPos > 4 || xPos < 0 || yPos < 0)
                            break;
                        //在长滑块上滑动时
                        int x = xPos, y = yPos;
                        if(!(x == start_x && y == start_y) && playBoard.getBoardValue(x, y) == selectedValue) {
                            if(x == start_x) y = y > start_y ? y+1 : y-1;
                            else if(y == start_y) x = x > start_x ? x+1 : x-1;
                        }
                        if(x > 3 || y > 4 || x < 0 || y < 0)
                            break;
                        int direction = decideDirection(x, y, (Fragment)Fragment.fragmentHashTable.get(selectedValue));
                        if(direction != Fragment.DIRECTION_DONTMOVE)
                        {
                            // 判断是否可移动
                            Fragment newFragment = Fragment.fragmentHashTable.get(selectedValue).move(direction);
                            if(newFragment != null){
                                // 移动并重绘
                                Fragment.fragmentHashTable.put(selectedValue, newFragment);
                                start_x = xPos;
                                start_y = yPos;
                                playBoard.addCount();
                                winBack.showCount(playBoard.getCount());
                                v.invalidate();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        // 判断胜利条件
                        if(playBoard.getBoardValue(1, 4)==1 && playBoard.getBoardValue(2, 4)==1) {
                            winBack.isWin(playBoard.getCount());
                            sp.play(cat2, 1, 1, 1, 0, 1);
                        }
                        break;
                }
                return false;
            }
            // 比较鼠标位置与当前位置得到移动方向
            private int decideDirection(int xPos, int yPos, Fragment fragment)
            {
                int x = fragment.getxPos();
                int y = fragment.getyPos();
                int l = fragment.getLength();
                int h = fragment.getHeight();
                if((xPos == x - 1) && (yPos >= y && yPos <= y + h - 1))
                    return Fragment.DIRECTION_LEFT;
                if((xPos == x + l) && (yPos >= y && yPos <= y + h - 1))
                    return Fragment.DIRECTION_RIGHT;
                if((xPos >= x && xPos <= x+ l - 1) && (yPos == y - 1))
                    return Fragment.DIRECTION_UP;
                if((xPos >= x && xPos <= x + l - 1) && (yPos == y + h))
                    return Fragment.DIRECTION_DOWN;
                return Fragment.DIRECTION_DONTMOVE;
            }
        });
    }
    public void reset () {
        playBoard = null;
        init();
        this.invalidate();
    }
    public void findGameWin(winCallBack winBack){
        this.winBack = winBack;
    }
    public interface winCallBack {
        void isWin(int count);
        void showCount(int count);
    }

    // 设置所有方块
    public void setChess()
    {
        Fragment.addFragment(new Fragment("Cao Cao", 1, 2, 2, levelInfo[0], levelInfo[1], R.drawable.cat));
        Fragment.addFragment(new Fragment("Zhang Fei", 2, 1, 2, levelInfo[2], levelInfo[3], R.drawable.like1));
        Fragment.addFragment(new Fragment("Huang Zhong", 3, 1, 2, levelInfo[4], levelInfo[5], R.drawable.like1));
        Fragment.addFragment(new Fragment("Ma Chao", 4, 1, 2, levelInfo[6], levelInfo[7], R.drawable.like2));
        Fragment.addFragment(new Fragment("Zhao Yun", 5 , 1, 2, levelInfo[8], levelInfo[9], R.drawable.like2));
        Fragment.addFragment(new Fragment("Guan Yu", 6, 2, 1, levelInfo[10], levelInfo[11], R.drawable.like3));
        Fragment.addFragment(new Fragment("Soldier1", 7, 1, 1, levelInfo[12], levelInfo[13], R.drawable.ball1));
        Fragment.addFragment(new Fragment("Soldier2", 8, 1, 1, levelInfo[14], levelInfo[15], R.drawable.ball2));
        Fragment.addFragment(new Fragment("Soldier3", 9, 1, 1, levelInfo[16], levelInfo[17], R.drawable.ball1));
        Fragment.addFragment(new Fragment("Soldier4", 10, 1, 1, levelInfo[18], levelInfo[19], R.drawable.ball2));
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        Enumeration<Fragment> enumeration = Fragment.fragmentHashTable.elements();
        while(enumeration.hasMoreElements())
        {
            Fragment fragment = enumeration.nextElement();
            int color = fragment.getValue();
            drawFragment(canvas, fragment, color);
        }
    }
    // 根据方块大小画出图像
    private void drawFragment(Canvas canvas, Fragment fragment, int color)
    {
        Paint paint = new Paint();

        Rect rect = new Rect();
        rect.left = fragment.getxPos() * gridSize;
        rect.top = fragment.getyPos() * gridSize;
        rect.right = (fragment.getxPos() + fragment.getLength()) * gridSize;
        rect.bottom = (fragment.getyPos() + fragment.getHeight()) * gridSize;


        InputStream is = this.getContext().getResources().openRawResource(fragment.getPicture());
        @SuppressWarnings("deprecation")
        BitmapDrawable bmpDraw = new BitmapDrawable(is);
        Bitmap mPic = bmpDraw.getBitmap();
        canvas.drawBitmap(mPic, null, rect, paint);
    }

}
