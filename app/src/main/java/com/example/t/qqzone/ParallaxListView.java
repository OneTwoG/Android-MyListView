package com.example.t.qqzone;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by T on 2016/8/8.
 */

public class ParallaxListView extends ListView {

    private static final String TAG = "DEBUG";
    private ImageView imageView;
    private int mImageViewHeight = 0;
    private SimpleDraweeView headerImageView;
    private int rotateAngle = 0;

    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mImageViewHeight = (int) context.getResources().getDimension(R.dimen.size_default_height);
    }

    /**
     * 设置那个图片需要放大，将需要放大的图片传进来
     *
     * @param imageView
     */
    public void setZoomImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setIconImageView(ImageView imageView) {
        this.headerImageView = (SimpleDraweeView) imageView;
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        /*监听ListView(ScrollView)的滑动过度
        * deltaX deltaY 增量
        * - ：代表下拉过度
        * + ：代表上拉过度
        * */
        if (deltaY < 0) {      //下拉的时候
            imageView.getLayoutParams().height = imageView.getHeight() - deltaY;
            imageView.requestLayout();
//          设置头像旋转
            headerImageView.setRotation(rotateAngle -= 10);
            Log.d(TAG, "overScrollBy: " + rotateAngle);
        }else {
            //缩小
            if (imageView.getHeight() > mImageViewHeight){
                imageView.getLayoutParams().height = imageView.getHeight() - deltaY;
                imageView.requestLayout();
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        /*上滑时 让图片恢复正常大小*/
        View header = (View) imageView.getParent();     //拿到父容器

        //header.getTop()头部滑出去的高度
        if (header.getTop() < 0 && imageView.getHeight() > mImageViewHeight) {
            imageView.getLayoutParams().height = imageView.getHeight() + header.getTop();
            header.layout(header.getLeft(), 0, header.getRight(), 0);
            imageView.requestLayout();
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        /*监听松开手*/
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP){
            ResetAnimation animation = new ResetAnimation(imageView, mImageViewHeight);
            animation.setDuration(1000);
            imageView.startAnimation(animation);
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 自定义动画
     */
    public class ResetAnimation extends Animation{

        private final ImageView mIv;
        private final int targetHeight;
        private final int originalHeight;
        private final int extraHeight;

        public ResetAnimation(ImageView iv, int targetHeight){
            this.mIv = iv;
            this.targetHeight = targetHeight;   //最终恢复高度
            this.originalHeight = mIv.getHeight();  //现在的高度
            this.extraHeight = originalHeight - targetHeight;   //高度差
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            // interpolatedTime (0.0 - 1.0) 执行的百分比
            //减小ImageView的高度
            if (imageView.getHeight() > mImageViewHeight){
                headerImageView.setRotation(rotateAngle / interpolatedTime );
            }
            //现在高度减去高度差
            imageView.getLayoutParams().height = (int) (originalHeight - extraHeight * interpolatedTime);
            imageView.requestLayout();
            super.applyTransformation(interpolatedTime, t);
        }
    }
}
