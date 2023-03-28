package com.zds.gpt.tetris;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.activity.R;

import java.util.Arrays;

/**
 * @Author: ZDS
 * @Date:2023/3/28
 * @Desc:
 */

class TetrisView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    // 声明常量
    public static final int GRID_WIDTH = 10;
    public static final int GRID_HEIGHT = 20;
    // 声明成员变量
    private Thread mThread;
    private SurfaceHolder mHolder;
    private volatile boolean mIsRunning;
    private boolean[][] mGrid;
    private Tetromino mCurrentShape;
    private int mCurrentX;
    private int mCurrentY;
    private int mCurrentRotation;
    private Tetromino mNextShape;
    private boolean mIsPaused;
    private boolean mIsGameOver;
    private int mScore;
    private int mCurrentLevel;
    private int mDropInterval;
    // 声明资源
    private Bitmap mBitmapGrid;
    private Bitmap mBitmapBackground;
    private Bitmap[] mBitmapShapes;
    private Paint mPaint;

    public TetrisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.addCallback(this);

        // 加载资源
        mBitmapGrid = BitmapFactory.decodeResource(getResources(), R.drawable.grid);
        mBitmapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        mBitmapShapes = new Bitmap[Tetromino.values().length];
        mBitmapShapes[Tetromino.I.ordinal()] = BitmapFactory.decodeResource(getResources(), R.drawable.shape_i);
        mBitmapShapes[Tetromino.J.ordinal()] = BitmapFactory.decodeResource(getResources(), R.drawable.shape_j);
        mBitmapShapes[Tetromino.L.ordinal()] = BitmapFactory.decodeResource(getResources(), R.drawable.shape_l);
        mBitmapShapes[Tetromino.O.ordinal()] = BitmapFactory.decodeResource(getResources(), R.drawable.shape_o);
        mBitmapShapes[Tetromino.S.ordinal()] = BitmapFactory.decodeResource(getResources(), R.drawable.shape_s);
        mBitmapShapes[Tetromino.T.ordinal()] = BitmapFactory.decodeResource(getResources(), R.drawable.shape_t);
        mBitmapShapes[Tetromino.Z.ordinal()] = BitmapFactory.decodeResource(getResources(), R.drawable.shape_z);

        // 初始化常量
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mGrid = new boolean[GRID_HEIGHT][GRID_WIDTH];
        mDropInterval = 1000;
    }

    public void setGameState(int score, int level, Tetromino currentShape, int currentX, int currentY, int currentRotation, Tetromino nextShape, boolean[][] grid, boolean isPaused, boolean isGameOver) {
        mScore = score;
        mCurrentLevel = level;
        mCurrentShape = currentShape;
        mCurrentX = currentX;
        mCurrentY = currentY;
        mCurrentRotation = currentRotation;
        mNextShape = nextShape;
        mGrid = grid;
        mIsPaused = isPaused;
        mIsGameOver = isGameOver;
        updateDropInterval();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsRunning = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mIsPaused || mIsGameOver) {
                // 响应重新开始游戏
                startNewGame();
                return true;
            }
            // 响应移动方块
            float x = event.getX();
            float y = event.getY();
            if (y > getHeight() - mBitmapGrid.getHeight() && mCurrentShape != null) {
                if (x < getWidth() / 2) {
                    // 向左移动方块
                    moveShapeLeft();
                } else {
                    // 向右移动方块
                    moveShapeRight();
                }
            }
        }
        return true;
    }

    private void startNewGame() {
        // 初始化游戏状态
        mScore = 0;
        mCurrentLevel = 1;
        mGrid = new boolean[GRID_HEIGHT][GRID_WIDTH];
        mIsPaused = false;
        mIsGameOver = false;
        mCurrentShape = null;
        mNextShape = Tetromino.getRandomShape();
        updateDropInterval();
    }

    private void updateDropInterval() {
        // 根据当前关卡难度计算下落周期
        mDropInterval = 1000 / (1 + mCurrentLevel * 0.1f);
    }

    private void moveShapeLeft() {
        playMoveSound();
        if (canMoveShapeLeft()) {
            mCurrentX--;
        }
    }

    private boolean canMoveShapeLeft() {
        for (Point p : mCurrentShape.getBlocks(mCurrentX - 1, mCurrentY, mCurrentRotation)) {
            if (p.x < 0 || mGrid[p.y][p.x]) {
                return false;
            }
        }
        return true;
    }

    private void moveShapeRight() {
        playMoveSound();
        if (canMoveShapeRight()) {
            mCurrentX++;
        }
    }

    private boolean canMoveShapeRight() {
        for (Point p : mCurrentShape.getBlocks(mCurrentX + 1, mCurrentY, mCurrentRotation)) {
            if (p.x >= GRID_WIDTH || mGrid[p.y][p.x]) {
                return false;
            }
        }
        return true;
    }

    private void rotateShape() {
        playRotateSound();
        if (canRotateShape()) {
            mCurrentRotation = (mCurrentRotation + 1) % 4;
        }
    }

    private boolean canRotateShape() {
        for (Point p : mCurrentShape.getBlocks(mCurrentX, mCurrentY, (mCurrentRotation + 1) % 4)) {
            if (p.x < 0 || p.x >= GRID_WIDTH || mGrid[p.y][p.x]) {
                return false;
            }
        }
        return true;
    }

    private void dropShape() {
        if (canDropShape()) {
            mCurrentY++;
        } else {
            lockShape();
        }
    }

    private boolean canDropShape() {
        for (Point p : mCurrentShape.getBlocks(mCurrentX, mCurrentY + 1, mCurrentRotation)) {
            if (p.y >= GRID_HEIGHT || mGrid[p.y][p.x]) {
                return false;
            }
        }
        return true;
    }

    private void lockShape() {
        // 将当前方块固定在游戏区域并检查是否有行被消除
        for (Point p : mCurrentShape.getBlocks(mCurrentX, mCurrentY, mCurrentRotation)) {
            mGrid[p.y][p.x] = true;
        }
        int linesCleared = removeCompletedLines();
        if (linesCleared > 0) {
            playClearSound();
            increaseScore(linesCleared);
            if (mScore >= mCurrentLevel * 1000) {
                increaseLevel();
            }
        }
        spawnShape();
        if (!canSpawnShape()) {
            mIsGameOver = true;
        }
    }

    private int removeCompletedLines() {
        int linesCleared = 0;
        for (int i = GRID_HEIGHT - 1; i >= 0; i--) {
            boolean isLineCompleted = true;
            for (int j = 0; j < GRID_WIDTH; j++) {
                if (!mGrid[i][j]) {
                    isLineCompleted = false;
                    break;
                }
            }
            if (isLineCompleted) {
                // 将当前行上面的所有行向下移动一格
                for (int k = i; k > 0; k--) {
                    System.arraycopy(mGrid[k - 1], 0, mGrid[k], 0, GRID_WIDTH);
                }
                Arrays.fill(mGrid[0], false);
                linesCleared++;
                // 因为有一行被消除，所以 i 需要加 1 以保证不漏掉任何一行
                i++;
            }
        }
        return linesCleared;
    }

    private void increaseScore(int linesCleared) {
        ((TetrisGameActivity) getContext()).increaseScore(linesCleared);
    }

    private void increaseLevel() {
        ((TetrisGameActivity) getContext()).increaseLevel();
        updateDropInterval();
    }

    private void spawnShape() {
        mCurrentShape = mNextShape;
        mNextShape = Tetromino.getRandomShape();
        mCurrentX = (GRID_WIDTH - mCurrentShape.getWidth()) / 2;
        mCurrentY = 0;
        mCurrentRotation = 0;
    }

    private boolean canSpawnShape() {
        for (Point p : mCurrentShape.getBlocks(mCurrentX, mCurrentY, mCurrentRotation)) {
            if (mGrid[p.y][p.x]) {
                return false;
            }
        }
        return true;
    }

    private void updateGame() {
        if (mIsPaused) {
            return;
        }
        if (mCurrentShape == null) {
            spawnShape();
        } else {
            long now = System.currentTimeMillis();
            if (now - mLastDropTime > mDropInterval) {
                mLastDropTime = now;
                dropShape();
            }
        }
    }

    @Override
    public void run() {
        Canvas canvas = null;
        mLastDropTime = System.currentTimeMillis();
        while (mIsRunning) {
            try {
                synchronized (mHolder) {
                    canvas = mHolder.lockCanvas();
                    if (canvas == null) {
                        continue;
                    }
                    canvas.drawBitmap(mBitmapBackground, 0, 0, null);
                    drawGrid(canvas);
                    drawShape(canvas, mCurrentShape, mCurrentX, mCurrentY, mCurrentRotation);
                    drawShape(canvas, mNextShape, GRID_WIDTH + 1, 1, 0);
                    drawScore(canvas);
                    if (mIsPaused) {
                        drawPaused(canvas);
                    } else if (mIsGameOver) {
                        drawGameOver(canvas);
                    }
                    updateGame();
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    mHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void drawGrid(Canvas canvas) {
        canvas.drawBitmap(mBitmapGrid, 0, getHeight() - mBitmapGrid.getHeight(), null);
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                if (mGrid[i][j]) {
                    drawBlock(canvas, j, i, Tetromino.values()[0]);
                }
            }
        }
    }

    private void drawShape(Canvas canvas, Tetromino shape, int x, int y, int rotation) {
        if (shape == null) {
            return;
        }
        for (Point p : shape for (int j = 0; j < TetrisView.GRID_WIDTH; j++) {
            sb.append(grid[i][j] ? "1" : "0");
        }
    }
        editor.putString(KEY_GRID,sb.toString());
        editor.apply();
}

    private void loadGameState() {
        SharedPreferences prefs = getSharedPreferences("game", Context.MODE_PRIVATE);
        mScore = prefs.getInt(KEY_SCORE, 0);
        mCurrentLevel = prefs.getInt(KEY_LEVEL, 1);
        mIsPaused = prefs.getBoolean(KEY_PAUSED, false);
        mIsGameOver = prefs.getBoolean(KEY_GAME_OVER, false);
        // 加载当前方块
        Tetromino[] values = Tetromino.values();
        int currentShapeIndex = prefs.getInt(KEY_CURRENT_SHAPE, 0);
        Tetromino currentShape = values[currentShapeIndex];
        int currentX = prefs.getInt(KEY_CURRENT_X, 0);
        int currentY = prefs.getInt(KEY_CURRENT_Y, 0);
        int currentRotation = prefs.getInt(KEY_CURRENT_ROTATION, 0);
        // 加载下一个方块
        int nextShapeIndex = prefs.getInt(KEY_NEXT_SHAPE, 0);
        Tetromino nextShape = values[nextShapeIndex];
        // 加载游戏区域
        String gridString = prefs.getString(KEY_GRID, "");
        boolean[][] grid = new boolean[TetrisView.GRID_HEIGHT][TetrisView.GRID_WIDTH];
        if (gridString.length() == TetrisView.GRID_HEIGHT * TetrisView.GRID_WIDTH) {
            int index = 0;
            for (int i = 0; i < TetrisView.GRID_HEIGHT; i++) {
                for (int j = 0; j < TetrisView.GRID_WIDTH; j++) {
                    grid[i][j] = gridString.charAt(index) == '1';
                    index++;
                }
            }
        }
        // 加载游戏状态
        mTetrisView.setGameState(currentShape, currentX, currentY, currentRotation, nextShape, grid);
        updateScore();
        updateLevel();
    }

    // 其他方法，例如处理触摸事件等

}