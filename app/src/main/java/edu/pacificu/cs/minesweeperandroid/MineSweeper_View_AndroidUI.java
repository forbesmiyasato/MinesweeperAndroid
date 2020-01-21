package edu.pacificu.cs.minesweeperandroid;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import edu.pacificu.cs.minesweeperjava.IMineSweeper_View;
import edu.pacificu.cs.minesweeperjava.MineSweeper_Model;
import edu.pacificu.cs.minesweeperjava.MineSweeper_Presenter;

public class MineSweeper_View_AndroidUI extends View implements IMineSweeper_View {
//    private final MainActivity mMainActivity;
    private String[][] mBoard;
    private MineSweeper_Presenter mcPresenter;
    private boolean mbLost = false;
    private boolean mbWon = false;
    private Paint mcBackground;
    private Paint mcDarkLines;
    private final int NUMBER_OF_RECTANGLES = 9;
    private float mRectangleHeight;
    private float mRectangleWidth;
    private Paint mcForeground;
    private MediaPlayer mcMediaPlayer;
    private int mSelectedRow;
    private int mSelectedColumn;

    public MineSweeper_View_AndroidUI(Context context) {
        super(context);
        MineSweeper_Model cModel = new MineSweeper_Model();
        mcPresenter = new MineSweeper_Presenter(this, cModel);
        mcBackground = new Paint();
        mcDarkLines = new Paint();
        mcForeground = new Paint(Paint.ANTI_ALIAS_FLAG);
        // Gives access to MainActivity resources
//        this.mMainActivity = (MainActivity) context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        Log.d("MinesweeperView", "Constructor Call");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int topHeight = 0;
        String cell = ".";
        mcDarkLines.setColor(getResources().getColor(R.color.cBlack));
        mRectangleHeight = ((float) getHeight() / (float) NUMBER_OF_RECTANGLES);
        mRectangleWidth = ((float) getWidth() / (float) NUMBER_OF_RECTANGLES);
        mcForeground.setColor(Color.BLACK);
        mcForeground.setStyle(Paint.Style.FILL);
        mcForeground.setTextSize(mRectangleHeight * 0.75f);
        mcForeground.setTextScaleX(mRectangleWidth / mRectangleHeight);
        mcForeground.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = mcForeground.getFontMetrics();
        float xFontCoord = mRectangleWidth / 2;
        float yFontCoord = mRectangleHeight / 2
                - (fontMetrics.ascent + fontMetrics.descent) / 2;
        mcDarkLines.setStrokeWidth(5);
        mcBackground.setColor(getResources().getColor(R.color.cGrass2));
        canvas.drawRect(0, 0, getWidth(), getHeight(), mcBackground);

        for (int i = 0; i < NUMBER_OF_RECTANGLES; i++) {

            for (int j = 0; j < NUMBER_OF_RECTANGLES; j++) {

                mcBackground.setColor(getResources().getColor(R.color.cGrass2));


                if (mBoard != null) {
                    if ((mBoard[i][j].equals("@") || mBoard[i][j].equals(".")) &&
                            !(mbLost && i == mSelectedRow && j == mSelectedColumn)) {
                        cell = "";
                    } else {
                        cell = mBoard[i][j];
                    }

                    if (!mBoard[i][j].equals("@") && !mBoard[i][j].equals(".")) {
                        mcBackground.setColor(getResources().getColor(R.color.cSand1));

                    }
                }

                canvas.drawRect(j * mRectangleWidth, topHeight + i * mRectangleHeight,
                        j * mRectangleWidth + mRectangleWidth,
                        topHeight + i * mRectangleHeight + mRectangleHeight, mcBackground);
                canvas.drawText(cell,
                        j * mRectangleWidth + xFontCoord,
                        i * mRectangleHeight + topHeight + yFontCoord,
                        mcForeground);

                if ((mbLost && i == mSelectedRow && j == mSelectedColumn)) {
                    Drawable d = getResources().getDrawable(R.drawable.bomb, null);
                    d.setBounds((int) (j * mRectangleWidth), (int) (i * mRectangleHeight),
                            (int) (j * mRectangleWidth + mRectangleWidth),
                            (int) (i * mRectangleHeight + mRectangleHeight));
                    d.draw(canvas);
                }
            }
        }

        for (int k = 0; k < NUMBER_OF_RECTANGLES; k++) {
            //             Draw Horizontal Line
            canvas.drawLine(0, topHeight + k * mRectangleHeight, getWidth(),
                    topHeight + k * mRectangleHeight, mcDarkLines);
            // Draw Vertical Line
            canvas.drawLine(k * mRectangleWidth, topHeight, k * mRectangleWidth,
                    getHeight(), mcDarkLines);
        }
    }

    /**
     * Main Loop for the game. Asks user to select difficulty and cells,
     * prints the board, and displays messages.
     */
    public void eventLoop() {

    }

    /**
     * Sets the game state to user lost
     */
    public void userLost() {
        mbLost = true;
        mcMediaPlayer = MediaPlayer.create(getContext(), R.raw.lose_sound);
        mcMediaPlayer.start();
    }

    /**
     * Sets the game state to user won
     */
    public void userWon() {
        mbWon = true;
        mcMediaPlayer = MediaPlayer.create(getContext(), R.raw.win_sound);
        mcMediaPlayer.start();
    }

    /**
     * Sets the TextUI's game board
     *
     * @param board The game board to set to
     */
    public void setBoard(String[][] board) {
        mBoard = board;
    }

    /**
     * Prints message to tell the user they selected an invalid position
     */
    public void invalidPosition() {
        System.out.println("Invalid Position! Pick again!");
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int topHeight = 0;
        int column = (int) (event.getX() / mRectangleWidth);
        int row = (int) ((event.getY() - topHeight) / mRectangleHeight);

        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }

        Log.d("Minesweeperview", "xTile = " + row
                + "yTile = " + column);
        mSelectedColumn = column;
        mSelectedRow = row;
        if (mbWon || mbLost)
        {
            mcPresenter.resetGame();
            mbWon = false;
            mbLost = false;
        }
        else {
            mcPresenter.UserSelectedCell(row, column);
        }

        this.invalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int viewWidth, int viewHeight,
                                 int oldViewWidth, int oldViewHeight) {
        mRectangleWidth = viewWidth / NUMBER_OF_RECTANGLES;
        mRectangleHeight = viewHeight / NUMBER_OF_RECTANGLES;
        super.onSizeChanged(viewWidth, viewHeight,
                oldViewWidth, oldViewHeight);
        Log.d("TicTacToeView", "new width = " + viewWidth +
                "new height = " + viewHeight);
    }

//    public MineSweeper_Presenter getPresenter() {
//        mbLost = false;
//        return mcPresenter;
//    }
    public void setDifficulty(int difficulty)
    {
        mbLost = false;
        mcPresenter.setDifficulty(difficulty);
    }
}
