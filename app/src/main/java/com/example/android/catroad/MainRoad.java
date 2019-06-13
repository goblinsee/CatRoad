package com.example.android.catroad;

public class MainRoad {

    private int[][] playArea;
    private int count = 0;

    public MainRoad()
    {
        playArea = new int[4][5];
        //Initialize playArea.
        for(int i = 0; i < playArea.length; i++)
            for(int j = 0; j < playArea[i].length; j++)
                playArea[i][j] = 0;
    }

    public int getBoardValue(int x, int y)
    {
        return playArea[x][y];
    }

    public int getCount() {
        return count;
    }

    public void addCount() {
        this.count++;
    }

    // It means the fragment can't be put into the play board if return false.
    // 在playArea上放置新方块
    public boolean PutFragment(Fragment fragment)
    {
        for(int j = 0; j < fragment.getLength(); j++)
        {
            for(int k = 0; k < fragment.getHeight(); k++)
            {
                if(playArea[fragment.getxPos() + j][fragment.getyPos() + k] == 0)
                    playArea[fragment.getxPos() + j][fragment.getyPos() + k] = fragment.getValue();
                else
                    return false;
            }
        }
        return true;
    }
    // 判断方块是否可朝指定方向移动
    public boolean isFragmentCanBeMoved(Fragment fragment, int direction)
    {
        switch(direction)
        {
            case Fragment.DIRECTION_UP:
            {
                for (int i = 0; i < fragment.getLength(); i++)
                    if(playArea[fragment.getxPos() + i][fragment.getyPos() - 1] != 0)
                        return false;
                break;
            }
            case Fragment.DIRECTION_DOWN:
            {
                for (int i = 0; i < fragment.getLength(); i++)
                    if(playArea[fragment.getxPos() + i][fragment.getyPos() + fragment.getHeight()] != 0)
                        return false;
                break;
            }
            case Fragment.DIRECTION_LEFT:
            {
                for (int i = 0; i < fragment.getHeight(); i++)
                    if(playArea[fragment.getxPos() - 1][fragment.getyPos() + i] != 0)
                        return false;
                break;
            }
            case Fragment.DIRECTION_RIGHT:
            {
                for (int i = 0; i < fragment.getHeight(); i ++)
                    if(playArea[fragment.getxPos() + fragment.getLength()][fragment.getyPos() + i] != 0)
                        return false;
                break;
            }
        }
        return true;
    }
    // 更新playArea
    public void moveFragment(Fragment fragment)
    {
        // 在playArea上清除旧方块
        for (int i = 0; i < playArea.length; i++)
            for (int j = 0; j < playArea[i].length; j++)
                if(playArea[i][j] == fragment.getValue())
                    playArea[i][j] = 0;
        // 放置新方块
        PutFragment(fragment);
    }
}
