package com.example.task2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class layout_2 extends View {

    Paint black, stroke, blackText, blackScore;
    Paint[] rPaints = new Paint[4];
    Paint[] lPaints = new Paint[4];
    int row;
    int column;
    int nop;
    int radius;
    float x, y, start, rHeight, cWidth;
    byte turn;
    int[] scores;
    List<Character> times = new ArrayList<>();
    List<Character> rTimes = new ArrayList<>();
    List<Path> paths = new ArrayList<>();
    List<Point> startP = new ArrayList<>();
    List<Point> endP = new ArrayList<>();
    List<Rect> rectangles = new ArrayList<>();
    int[][] rectSides;


    //constructor
    public layout_2(Context context, int r, int c, int p) {
        super(context);
        row = r;
        column = c;
        nop = p;
        if (row == 5)
            radius = 23;
        else if (row == 6)
            radius = 21;
        else
            radius = 19;
        rectSides = new int[row + 1][row + 1];
        scores = new int[nop];
        turn = 1;
        for (int i = 0; i < nop; i++) {
            scores[i] = 0;
        }
        brushes();
        times.add((char) turn);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        start = (float) height * 0.32f;
        float end = (float) height * (float) (0.32 + 0.6);
        rHeight = (end - start) / (row);
        cWidth = (float) width / (3 + column);

        if (nop == 1) {
            canvas.drawText("Singleplayer", width * 0.36f, height * 0.08f, blackText);
            canvas.drawText("Player 1", width * 0.17f, height * 0.17f, rPaints[0]);
            canvas.drawText("Bot", width * 0.64f, height * 0.17f, rPaints[1]);
            for (int i = 0; i < nop; i++)
                canvas.drawText(String.valueOf(scores[i]), (float) (width * (0.27 + 0.42 * i)), height * 0.23f, blackScore);
        }
        else if (nop == 2) {
            canvas.drawText("1v1", width * 0.44f, height * 0.08f, blackText);
            canvas.drawText("Player 1", width * 0.17f, height * 0.17f, rPaints[0]);
            canvas.drawText("Player 2", width * 0.6f, height * 0.17f, rPaints[1]);
            for (int i = 0; i < nop; i++)
                canvas.drawText(String.valueOf(scores[i]), (float) (width * (0.27 + 0.42 * i)), height * 0.23f, blackScore);
        } else if (nop == 3) {
            canvas.drawText("Multiplayer", width * 0.36f, height * 0.08f, blackText);
            canvas.drawText("Player 1", width * 0.087f, height * 0.17f, rPaints[0]);
            canvas.drawText("Player 2", width * 0.39f, height * 0.17f, rPaints[1]);
            canvas.drawText("Player 3", width * 0.7f, height * 0.17f, rPaints[2]);
            for (int i = 0; i < nop; i++)
                canvas.drawText(String.valueOf(scores[i]), (float) (width * (0.19 + 0.32 * i)), height * 0.22f, blackScore);
        } else{
            canvas.drawText("Multiplayer", width * 0.36f, height * 0.068f, blackText);
            canvas.drawText("Player 1", width * 0.17f, height * 0.151f, rPaints[0]);
            canvas.drawText("Player 2", width * 0.6f, height * 0.151f, rPaints[1]);
            canvas.drawText("Player 3", width * 0.17f, height * 0.247f, rPaints[2]);
            canvas.drawText("Player 4", width * 0.6f, height * 0.247f, rPaints[3]);
            for (int i = 0; i < nop; i++) {
                canvas.drawText(String.valueOf(scores[i]), (float) (width * (0.27 + (0.42 * i))),
                        height * 0.196f, blackScore);
                if (i >= 2)
                    canvas.drawText(String.valueOf(scores[i]), (float) (width * (0.27 + (0.42 * (i-2)))),
                            height * 0.292f, blackScore);
            }
        }
        if (rectangles.size() < (row - 1) * column) {
            canvas.drawText("Player " + turn + "'s turn", width * 0.32f, height * 0.34f, blackScore);
        } else if (rectangles.size() == (row - 1) * column) {
            int k = largest();
            if (k == -1)
                canvas.drawText("Draw", width * 0.44f, height * 0.34f, blackText);
            else
                canvas.drawText("Player " + ++k + " Won", width * 0.35f, height * 0.34f, blackScore);
            finish();
        }

        int j = 0;
        for (Rect rect : rectangles) {
            canvas.drawRect(rect, rPaints[(int) (rTimes.get(j)) - 1]);
            j++;
        }
        int i = 0;
        for (Path path : paths) {
            canvas.drawPath(path, lPaints[(int) (times.get(i)) - 1]);
            i++;
        }

        for (i = 1; i <= row; i++) {                  //rHeight->y(column)  //cWidth->x(row)
            for (j = 1; j <= column + 1; j++)
                canvas.drawCircle(cWidth * i, start + (rHeight * j), radius, black);
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x = event.getRawX() - 2.35f;
            y = event.getRawY() - 277;
            if (check(rHeight, cWidth))
                invalidate();
        }

        return true;
    }

    public boolean check(float height, float width) {
        Point p, q;
        Path path = new Path();
        for (int i = 1; i <= column + 1; i++) {
            if (x >= (width * i) + radius + 20 && x <= (width * (i + 1)) - radius - 20) {
                for (int j = 1; j <= row - 1; j++) {
                    if (y >= (start + (height * j) - radius - 20) && y <= (start + (height * j) + radius + 20)) {
                        path.moveTo(width * i, start + (height * j));
                        path.lineTo(width * (i + 1), start + (height * j));
                        p = new Point((int) width * i, (int) (start + (height * j)));
                        q = new Point((int) width * (i + 1), (int) (start + (height * j)));
                        if (checkLine(p, q)) {
                            paths.add(path);
                            startP.add(p);
                            endP.add(q);
                            checkRectangle(i, j, 'h');
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        for (int j = 1; j <= row - 2; j++) {
            if (y >= start + (height * j) + radius + 20 && y <= start + (height * (j + 1)) - radius - 20) {
                for (int i = 1; i <= column + 2; i++) {
                    if (x >= (width * i - radius - 20) && x <= (width * i + radius + 20)) {
                        path.moveTo(width * i, start + (height * j));
                        path.lineTo(width * i, start + (height * (j + 1)));
                        p = new Point((int) width * i, (int) (start + (height * j)));
                        q = new Point((int) width * i, (int) (start + (height * (j + 1))));
                        if (checkLine(p, q)) {
                            paths.add(path);
                            startP.add(p);
                            endP.add(q);
                            checkRectangle(i, j, 'v');
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean checkLine(Point p, Point q) {
        int i = startP.indexOf(p);
        return i < 0 || !endP.get(i).equals(q.x, q.y);
    }

    public void checkRectangle(int i, int j, char s) {
        boolean b = true;
        if (s == 'h') {
            if (j == 1) {
                if (++rectSides[j][i] == 4) {
                    createRectangle(j, i);
                    b = false;
                }
            } else {
                if (++rectSides[j - 1][i] == 4) {
                    createRectangle(j - 1, i);
                    b = false;
                }
                if (++rectSides[j][i] == 4) {
                    createRectangle(j, i);
                    b = false;
                }
            }
        } else if (s == 'v') {
            if (i == 1) {
                if (++rectSides[j][i] == 4) {
                    createRectangle(j, i);
                    b = false;
                }
            } else {
                if (++rectSides[j][i] == 4) {
                    b = false;
                    createRectangle(j, i);
                }
                if (++rectSides[j][i - 1] == 4) {
                    createRectangle(j, i - 1);
                    b = false;
                }
            }
        }
        if (!b) {
            times.add((char) turn);
        } else {
            if (turn == nop)
                turn = 1;
            else
                turn++;
            times.add((char) turn);
        }
    }

    public void createRectangle(int i, int j) {
        Rect r = new Rect();
        r.set((int) (cWidth * j), (int) (start + rHeight * i), (int) (cWidth * (j + 1)), (int) (start + rHeight * (i + 1)));
        rectangles.add(r);
        ++scores[turn - 1];
        rTimes.add((char) turn);
    }

    public void brushes() {
        black = new Paint();
        black.setColor(Color.rgb(28, 28, 28));
        black.setStyle(Paint.Style.FILL);
        rPaints[0] = new Paint();
        rPaints[0].setColor(Color.rgb(255, 77, 77));
        rPaints[0].setStyle(Paint.Style.FILL);
        rPaints[0].setTextSize(100);
        rPaints[1] = new Paint();
        rPaints[1].setColor(Color.rgb(60, 208, 112));
        rPaints[1].setStyle(Paint.Style.FILL);
        rPaints[1].setTextSize(100);
        rPaints[2] = new Paint();
        rPaints[2].setColor(Color.rgb(125, 119, 237));
        rPaints[2].setStyle(Paint.Style.FILL);
        rPaints[2].setTextSize(100);
        rPaints[3] = new Paint();
        rPaints[3].setColor(Color.rgb(216, 153, 103));
        rPaints[3].setStyle(Paint.Style.FILL);
        rPaints[3].setTextSize(100);
        stroke = new Paint();
        stroke.setColor(Color.rgb(28, 28, 28));
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(10);
        blackScore = new Paint();
        blackScore.setColor(Color.rgb(28, 28, 28));
        blackScore.setStyle(Paint.Style.FILL);
        blackScore.setTextSize(80);
        blackText = new Paint();
        blackText.setColor(Color.rgb(28, 28, 28));
        blackText.setStyle(Paint.Style.FILL);
        blackText.setTextSize(100);
        lPaints[0] = new Paint();
        lPaints[0].setColor(Color.rgb(255, 20, 20));
        lPaints[0].setStyle(Paint.Style.STROKE);
        lPaints[0].setStrokeWidth(15);
        lPaints[1] = new Paint();
        lPaints[1].setColor(Color.rgb(30, 255, 70));
        lPaints[1].setStyle(Paint.Style.STROKE);
        lPaints[1].setStrokeWidth(15);
        lPaints[2] = new Paint();
        lPaints[2].setColor(Color.rgb(80, 90, 237));
        lPaints[2].setStyle(Paint.Style.STROKE);
        lPaints[2].setStrokeWidth(15);
        lPaints[3] = new Paint();
        lPaints[3].setColor(Color.rgb(216, 130, 80));
        lPaints[3].setStyle(Paint.Style.STROKE);
        lPaints[3].setStrokeWidth(15);
    }

    public int largest() {
        int index = 0, c = -1;
        for (int i = 1; i < nop; i++) {
            if (scores[i] > scores[index])
                index = i;
            else if (scores[i] == scores[index])
                c = i;
        }
        if (c > 0 && scores[index] == scores[c])
            return -1;
        else
            return index;
    }

    public void finish() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Activity activity = (Activity) getContext();
                activity.finishAffinity();
            }
        }, 2000);
    }

}