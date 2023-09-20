package com.sproductions.bgeraser;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

public class CursorView extends View {

    private Paint cursorPaint;
    private Paint handlePaint;
    private Point cursorPosition;
    private Point handlePosition;
    private int cursorRadius = 30;
    private int handleRadius = 10;
    private int handleOffset = 60;
    private SeekBar cursorSizeSeekBar;
    private SeekBar cursorOffsetSeekBar;

    public CursorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        cursorPaint = new Paint();
        cursorPaint.setColor(Color.RED);
        cursorPaint.setStyle(Paint.Style.STROKE);
        cursorPaint.setStrokeWidth(5);
        handlePaint = new Paint();
        handlePaint.setColor(Color.BLUE);
        cursorPosition = new Point();
        handlePosition = new Point();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(cursorPosition.x, cursorPosition.y, cursorRadius, cursorPaint);
        canvas.drawCircle(handlePosition.x, handlePosition.y, handleRadius, handlePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                cursorPosition.set((int)event.getX(), (int)event.getY());
                int dx = cursorPosition.x - handlePosition.x;
                int dy = cursorPosition.y - handlePosition.y;
                double dist = Math.sqrt(dx*dx + dy*dy);
                if (dist > handleOffset) {
                    double scale = handleOffset / dist;
                    handlePosition.set((int)(cursorPosition.x - dx*scale), (int)(cursorPosition.y - dy*scale));
                } else {
                    handlePosition.set(cursorPosition.x, cursorPosition.y);
                }
                invalidate();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    public void setCursorSizeSeekBar(SeekBar seekBar) {
        cursorSizeSeekBar = seekBar;
        cursorSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cursorRadius = progress;
                invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    public void setCursorOffsetSeekBar(SeekBar seekBar) {
        cursorOffsetSeekBar = seekBar;
        cursorOffsetSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                handleOffset = progress;
                invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
}

