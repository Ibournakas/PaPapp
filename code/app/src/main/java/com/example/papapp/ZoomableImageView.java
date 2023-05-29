package com.example.papapp;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

public class ZoomableImageView extends AppCompatImageView {
    private Matrix matrix;
    private float scaleFactor = 1.0f;
    private GestureDetector gestureDetector;
    private MoveGestureListener moveGestureListener;

    public ZoomableImageView(Context context) {
        super(context);
        initialize();
    }

    public ZoomableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ZoomableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        matrix = new Matrix();
        setScaleType(ScaleType.MATRIX);

        moveGestureListener = new MoveGestureListener();
        gestureDetector = new GestureDetector(getContext(), moveGestureListener);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    @Override
    public void setImageMatrix(Matrix matrix) {
        super.setImageMatrix(matrix);
        this.matrix.set(matrix);

        float[] matrixValues = new float[9];
        matrix.getValues(matrixValues);
        float currentScale = matrixValues[Matrix.MSCALE_X];
        float imageWidth = getDrawable() != null ? getDrawable().getIntrinsicWidth() : 0;
        float imageHeight = getDrawable() != null ? getDrawable().getIntrinsicHeight() : 0;

        moveGestureListener.setImageSize(imageWidth, imageHeight);
        moveGestureListener.setViewSize(getWidth(), getHeight());
    }

    private class MoveGestureListener extends GestureDetector.SimpleOnGestureListener {
        private float imageWidth;
        private float imageHeight;
        private float viewWidth;
        private float viewHeight;

        public void setImageSize(float imageWidth, float imageHeight) {
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
        }

        public void setViewSize(float viewWidth, float viewHeight) {
            this.viewWidth = viewWidth;
            this.viewHeight = viewHeight;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float[] matrixValues = new float[9];
            matrix.getValues(matrixValues);
            float currentX = matrixValues[Matrix.MTRANS_X];
            float currentY = matrixValues[Matrix.MTRANS_Y];
            float currentScale = matrixValues[Matrix.MSCALE_X];

            float dx = -distanceX / currentScale;
            float dy = -distanceY / currentScale;
            float newX = currentX + dx;
            float newY = currentY + dy;

            float scaledImageWidth = imageWidth * currentScale;
            float scaledImageHeight = imageHeight * currentScale;

            float minX = Math.min(0, viewWidth - scaledImageWidth);
            float minY = Math.min(0, viewHeight - scaledImageHeight);
            float maxX = Math.max(0, viewWidth - scaledImageWidth);
            float maxY = Math.max(0, viewHeight - scaledImageHeight);

            if (newX < minX) {
                dx = minX - currentX;
            } else if (newX > maxX) {
                dx = maxX - currentX;
            }

            if (newY < minY) {
                dy = minY - currentY;
            } else if (newY > maxY) {
                dy = maxY - currentY;
            }

            matrix.postTranslate(dx, dy);
            setImageMatrix(matrix);
            return true;
        }
    }
}


