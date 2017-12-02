package com.example.yangjiyu.myapplication;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by yangjiyu on 2017/12/1.
 */

public class SceneWall {

    private final static String TAG = SceneWall.class.getSimpleName();
    private final static int MAX_SUPPORT_CELL = 12;
    private final static int MAX_SUPPORT_WALL = 1;

    public final static int sVideoCellGap = 2;

    public final static int sSuccess = 0;
    public final static int sFailure = -1;

    private static SceneWall sceneWall;
    private static ArrayList<SingleSceneCell> singleSceneCells;

    private static int mWallWidth;
    private static int mWallHeight;
    private static  SingleSceneCell singleSceneCell;

    public static SceneWall newInstance(int wallWidth, int wallHeight){
        if(sceneWall != null){
            if( wallWidth * wallHeight <= 0 ){
                return null;
            }else{
                return sceneWall;
            }
        }else{
            sceneWall= new SceneWall(wallWidth, wallHeight);
            return sceneWall;
        }
    }


    private SceneWall(int width, int height) {
        if( height * width <= 0 ) {
            Log.e(TAG, "VideoWall size: " + width + " x " + height);
        }
        else {
            mWallWidth  = width;
            mWallHeight = height;
        }
    }

    public int getWallWidth() {
        return mWallWidth;
    }
    public int getWallHeight() {
        return mWallHeight;
    }

    public static ArrayList<SingleSceneCell> getEchSceneCellCollections(int rows, int columns) {
        int VideoCellNumber = rows * columns;

        if( VideoCellNumber <= 0 ||
                VideoCellNumber > MAX_SUPPORT_CELL){
            //Log.e(TAG,"VideoCell number more than the max support number: " + VideoCellNumber);
            return null;
        }else {
            //Log.i(TAG,"Begin to layout: rows = " + rows + ", columns = " + columns);
        }

        int VideoCellWidth = (int) Math.floor((sceneWall.getWallWidth() - sVideoCellGap * (columns  - 1 )) / columns);
        int VideoCellHeight = (int) Math.floor((sceneWall.getWallHeight() - sVideoCellGap * (rows  - 1 )) / rows);


        //ToDo: takes time to modify the shape of video shape
        //int VideoCellWidthTmp = (int) Math.floor(VideoCellHeight * sVideoCellSizeWidthToHeightRatioSmall);
        //int VideoCellHeightTmp = (int) Math.floor(VideoCellWidth / sVideoCellSizeWidthToHeightRatioSmall);
        singleSceneCells = new ArrayList<>();
        int posX = 0;
        int posY = 0;
        for( int i = 0; i < rows; i++ ){
            posY = i * (VideoCellHeight + sVideoCellGap);

            for( int j = 0; j < columns; j++){
                singleSceneCell = new SingleSceneCell();
                posX = j * (VideoCellWidth + sVideoCellGap);
                singleSceneCell.setM_startX(posX);
                singleSceneCell.setM_startY(posY);
                singleSceneCell.setM_endX(posX+VideoCellWidth);
                singleSceneCell.setM_endY(posY+VideoCellHeight);
                singleSceneCells.add(singleSceneCell);
                //Log.i(TAG,"Add scene cell: x = " + posX + ", y = " + posY);
            }

        }
        return singleSceneCells;
    }

    public  static ArrayList<SingleSceneCell> getH2PartSceneCellCollections(int rows,int cols)
    {
        //int VideoCellWidth = (int) Math.floor((sVideoWall.getWallWidth() - sVideoCellGap * (cols  - 1 )) / cols);
        int VideoCellHeight = (int) Math.floor((sceneWall.getWallHeight() - sVideoCellGap * (rows  - 1 )) / rows);

        int upNum = (int)Math.floor(rows/2);
        int downNum=rows-upNum;
        //ToDo: takes time to modify the shape of video shape
        //int VideoCellWidthTmp = (int) Math.floor(VideoCellHeight * sVideoCellSizeWidthToHeightRatioSmall);
        //int VideoCellHeightTmp = (int) Math.floor(VideoCellWidth / sVideoCellSizeWidthToHeightRatioSmall);
        singleSceneCells = new ArrayList<>();
        int posX = 0;
        int posY = 0;

        singleSceneCell = new SingleSceneCell();
        posX=sceneWall.getWallWidth();
        posY=(upNum * (VideoCellHeight + sVideoCellGap)-sVideoCellGap);
        singleSceneCell.setM_startX(0);
        singleSceneCell.setM_startY(0);
        singleSceneCell.setM_endX(posX);
        singleSceneCell.setM_endY(posY);
        singleSceneCells.add(singleSceneCell);

        posY = upNum * (VideoCellHeight + sVideoCellGap);
        singleSceneCell = new SingleSceneCell();
        singleSceneCell.setM_startX(0);
        singleSceneCell.setM_startY(posY);
        singleSceneCell.setM_endX(posX);
        singleSceneCell.setM_endY(sceneWall.getWallHeight());
        singleSceneCells.add(singleSceneCell);


        return singleSceneCells;
    }

    public  static ArrayList<SingleSceneCell> getV2PartSceneCellCollections(int rows,int cols)
    {
        int VideoCellWidth = (int) Math.floor((sceneWall.getWallWidth() - sVideoCellGap * (cols  - 1 )) / cols);
        //int VideoCellHeight = (int) Math.floor((sVideoWall.getWallHeight() - sVideoCellGap * (rows  - 1 )) / rows);

        int leftNum = (int)Math.floor(cols/2);
        int rightNum=rows-leftNum;
        //ToDo: takes time to modify the shape of video shape
        //int VideoCellWidthTmp = (int) Math.floor(VideoCellHeight * sVideoCellSizeWidthToHeightRatioSmall);
        //int VideoCellHeightTmp = (int) Math.floor(VideoCellWidth / sVideoCellSizeWidthToHeightRatioSmall);
        singleSceneCells = new ArrayList<>();
        int posX = (leftNum * (VideoCellWidth + sVideoCellGap)-sVideoCellGap);
        int posY = sceneWall.getWallHeight();

        singleSceneCell = new SingleSceneCell();
        singleSceneCell.setM_startX(0);
        singleSceneCell.setM_startY(0);
        singleSceneCell.setM_endX(posX);
        singleSceneCell.setM_endY(posY);
        singleSceneCells.add(singleSceneCell);

        posX = leftNum * (VideoCellWidth + sVideoCellGap);
        singleSceneCell = new SingleSceneCell();
        singleSceneCell.setM_startX(posX);
        singleSceneCell.setM_startY(0);
        singleSceneCell.setM_endX(sceneWall.getWallWidth());
        singleSceneCell.setM_endY(posY);
        singleSceneCells.add(singleSceneCell);


        return singleSceneCells;
    }

}
