package com.zds.gpt.tetris;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.activity.R;

import java.util.Arrays;

/**
 * @Author: ZDS
 * @Date:2023/3/28
 * @Desc:
 */
public class TetrisGameActivity extends Activity {
    // 声明变量
    private int mScore;
    private int mCurrentLevel;
    private boolean mIsPaused;
    private boolean mIsGameOver;
    private TetrisView mTetrisView;
    private TextView mScoreView;
    private TextView mLevelView;
    // 声明资源
    private SoundPool mSoundPool;
    private int mMoveSound;
    private int mRotateSound;
    private int mClearSound;
    // 声明游戏存档
    private static final String KEY_SCORE = "score";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_PAUSED = "paused";
    private static final String KEY_GAME_OVER = "game_over";
    private static final String KEY_CURRENT_SHAPE = "current_shape";
    private static final String KEY_CURRENT_X = "current_x";
    private static final String KEY_CURRENT_Y = "current_y";
    private static final String KEY_CURRENT_ROTATION = "current_rotation";
    private static final String KEY_NEXT_SHAPE = "next_shape";
    private static final String KEY_GRID = "grid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris_game);

        // 初始化变量
        mScore = 0;
        mCurrentLevel = 1;
        mIsPaused = false;
        mIsGameOver = false;
        mTetrisView = findViewById(R.id.tetris_view);
        mScoreView = findViewById(R.id.score_view);
        mLevelView = findViewById(R.id.level_view);

        // 加载音效
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        mMoveSound = mSoundPool.load(this, R.raw.move, 1);
        mRotateSound = mSoundPool.load(this, R.raw.rotate, 1);
        mClearSound = mSoundPool.load(this, R.raw.clear, 1);

        // 设置界面文字
        updateScore();
        updateLevel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveGameState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGameState();
    }

    private void updateScore() {
        mScoreView.setText(getString(R.string.score_format, mScore));
    }

    private void updateLevel() {
        mLevelView.setText(getString(R.string.level_format, mCurrentLevel));
    }

    public void increaseScore(int linesCleared) {
        // 根据消除的行数计算分数
        mScore += linesCleared * 100 * mCurrentLevel;
        updateScore();
    }

    public void increaseLevel() {
        // 增加关卡难度
        mCurrentLevel++;
        updateLevel();
    }

    public void playMoveSound() {
        mSoundPool.play(mMoveSound, 1f, 1f, 0, 0, 1f);
    }

    public void playRotateSound() {
        mSoundPool.play(mRotateSound, 1f, 1f, 0, 0, 1f);
    }

    public void playClearSound() {
        mSoundPool.play(mClearSound, 1f, 1f, 0, 0, 1f);
    }

    private void saveGameState() {
        SharedPreferences prefs = getSharedPreferences("game", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_SCORE, mScore);
        editor.putInt(KEY_LEVEL, mCurrentLevel);
        editor.putBoolean(KEY_PAUSED, mIsPaused);
        editor.putBoolean(KEY_GAME_OVER, mIsGameOver);
        // 记录当前方块
        editor.putInt(KEY_CURRENT_SHAPE, mTetrisView.getCurrentShape().ordinal());
        editor.putInt(KEY_CURRENT_X, mTetrisView.getCurrentX());
        editor.putInt(KEY_CURRENT_Y, mTetrisView.getCurrentY());
        editor.putInt(KEY_CURRENT_ROTATION, mTetrisView.getCurrentRotation());
        // 记录下一个方块
        editor.putInt(KEY_NEXT_SHAPE, mTetrisView.getNextShape().ordinal());
        // 记录游戏区域
        boolean[][] grid = mTetrisView.getGrid();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TetrisView.GRID_HEIGHT; i++) {
            for (int j = 0; j < TetrisView.GRID_WIDTH; j++) {
                sb.append(grid[i][j] ? "1" : "0");
            }
        }
        editor.putString(KEY_GRID, sb.toString());
        editor.apply();
    }

    private void loadGameState() {
        SharedPreferences prefs = getSharedPreferences("game", Context.MODE_PRIVATE);
        mScore = prefs.getInt(KEY_SCORE, 0);
        mCurrentLevel = prefs.getInt(KEY_LEVEL, 1);
        mIsPaused = prefs.getBoolean(KEY_PAUSED, false);
        mIsGameOver = prefs.getBoolean(KEY_GAME_OVER, false);
        // 恢复当前方块
        int currentShapeOrdinal = prefs.getInt(KEY_CURRENT_SHAPE, 0);
        int currentX = prefs.getInt(KEY_CURRENT_X, 0);
        int currentY = prefs.getInt(KEY_CURRENT_Y, 0);
        int currentRotation = prefs.getInt(KEY_CURRENT_ROTATION, 0);
        Tetromino currentShape = Tetromino.values()[currentShapeOrdinal];
        // 恢复下一个方块
        int nextShapeOrdinal = prefs.getInt(KEY_NEXT_SHAPE, 0);
        Tetromino nextShape = Tetromino.values()[nextShapeOrdinal];
        // 恢复游戏区域
        String gridStr = prefs.getString(KEY_GRID, "");
        boolean[][] grid = new boolean[TetrisView.GRID_HEIGHT][TetrisView.GRID_WIDTH];
        if (gridStr.length() == TetrisView.GRID_HEIGHT * TetrisView.GRID_WIDTH) {
            for (int i = 0; i < TetrisView.GRID_HEIGHT; i++) {
                for (int j = 0; j < TetrisView.GRID_WIDTH; j++) {
                    grid[i][j] = gridStr.charAt(i * TetrisView.GRID_WIDTH + j) == '1';
                }
            }
        }
        // 更新界面以恢复游戏状态
        mTetrisView.setGameState(mScore, mCurrentLevel, currentShape, currentX, currentY, currentRotation,
                nextShape, grid, mIsPaused, mIsGameOver);
        updateScore();
        updateLevel();
    }
}
