package com.example.android.catroad;

import java.util.Hashtable;

public class Fragment {
    private String name;
    private int length;
    private int height;
    private int xPos;
    private int yPos;
    private int value;
    private int mPicture;
    public static final int DIRECTION_DONTMOVE = 0;
    public static final int DIRECTION_UP = 1;
    public static final int DIRECTION_DOWN = 2;
    public static final int DIRECTION_LEFT = 3;
    public static final int DIRECTION_RIGHT = 4;

    public static Hashtable<Integer, Fragment> fragmentHashTable;
    public static MainRoad playBoard;

    public Fragment(String name, int value, int length, int height, int xPos, int yPos, int mPicture)
    {
        this.name = name;
        this.length = length;
        this.height = height;
        this.value = value;
        this.xPos = xPos;
        this.yPos = yPos;
        this.mPicture = mPicture;
    }
    // 在HashTable中添加方块
    public static boolean addFragment(Fragment fragment)
    {
        if(fragmentHashTable == null)
            fragmentHashTable = new Hashtable<Integer, Fragment>();
        if(fragmentHashTable.containsKey((Integer)fragment.getValue()))
            return false;
        if(!playBoard.PutFragment(fragment))
            return false;
        fragmentHashTable.put(fragment.getValue(), fragment);
        return true;
    }

    public static void setPlayBoard(MainRoad playBoard)
    {
        Fragment.playBoard = playBoard;
    }
    public static void reset()
    {
        if(fragmentHashTable != null)
            fragmentHashTable.clear();
    }
    // 更改方块坐标
    public Fragment move(int direction)
    {
        if(playBoard.isFragmentCanBeMoved(this, direction))
            switch(direction)
            {
                case Fragment.DIRECTION_UP:
                {
                    this.setyPos(this.getyPos() - 1);
                    break;
                }
                case Fragment.DIRECTION_DOWN:
                {
                    this.setyPos(this.getyPos() + 1);
                    break;
                }
                case Fragment.DIRECTION_LEFT:
                {
                    this.setxPos(this.getxPos() - 1);
                    break;
                }
                case Fragment.DIRECTION_RIGHT:
                {
                    this.setxPos(this.getxPos() + 1);
                    break;
                }
            }
        else return null;
        playBoard.moveFragment(this);
        return this;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public int getPicture() {
        return this.mPicture;
    }
}
