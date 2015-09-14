package com.hunk.autohide;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    View header;
    View content;
    int touchSlop = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        setupView();
    }
    
    private void  findView(){
        header = findViewById(R.id.header);
        content = findViewById(R.id.content);
    }


    private void setupView(){
        getSupportFragmentManager().beginTransaction().replace(R.id.content, Fragment.instantiate(this,ItemFragment.class.getName())).commit();
    }


    private boolean isHide = false ;


    AnimatorSet backAnimatorSet;

    private void animateBack() {
        if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
            hideAnimatorSet.cancel();
        }
        if (!isHide){
            return;
        }
        if (backAnimatorSet != null && backAnimatorSet.isRunning()) {

        } else {
            backAnimatorSet = new AnimatorSet();
//            TranslateAnimation animation = new TranslateAnimation();
//            ScaleAnimation animation = new ScaleAnimation();
            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(header, "translationY", header.getTranslationY(), 0f);
            ObjectAnimator contentAnimator = ObjectAnimator.ofFloat(content, "translationY", content.getTranslationY(), 0f);
            ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(content, "scaleY", 1f, (header.getHeight() + content.getHeight())/(content.getHeight()*1.0f), 1f);
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(headerAnimator);
            animators.add(contentAnimator);
            animators.add(scaleAnimator);
            backAnimatorSet.setDuration(300);
            backAnimatorSet.playTogether(animators);
            backAnimatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isHide = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            backAnimatorSet.start();
        }
    }

    AnimatorSet hideAnimatorSet;

    private void animateHide() {
        if (backAnimatorSet != null && backAnimatorSet.isRunning()) {
            backAnimatorSet.cancel();
        }
        if (isHide){
            return;
        }
        if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {

        } else {
            hideAnimatorSet = new AnimatorSet();
            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(header, "translationY", header.getTranslationY(), -header.getHeight()/2);
            ObjectAnimator contentAnimator = ObjectAnimator.ofFloat(content, "translationY", content.getTranslationY(), -header.getHeight()/2);
            ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(content, "scaleY", 1f, 1f,(header.getHeight() + content.getHeight())/(content.getHeight()*1.0f));
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(headerAnimator);
            animators.add(contentAnimator);
            animators.add(scaleAnimator);
            hideAnimatorSet.setDuration(200);
            hideAnimatorSet.playTogether(animators);
            hideAnimatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isHide = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            hideAnimatorSet.start();
        }
    }

    public View.OnTouchListener getOnToucherListener(final  AbsListView listView) {
        return new View.OnTouchListener() {


            float lastY = 0f;
            float currentY = 0f;
            int lastDirection = 0;
            int currentDirection = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastY = event.getY();
                        currentY = event.getY();
                        currentDirection = 0;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int position = listView.getFirstVisiblePosition();
                        position = 1;
                        if (position > 0) {
                            float tmpCurrentY = event.getY();
                            if (Math.abs(tmpCurrentY - lastY) > touchSlop) {
                                currentY = tmpCurrentY;
                                currentDirection = (int) (currentY - lastY);
                                if (lastDirection != currentDirection) {
                                    if (currentDirection < 0) {
                                        animateHide();
                                    } else {
                                        animateBack();
                                    }
                                }
                                lastY = currentY;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        currentDirection = 0;
                        break;
                }
                return false;
            }
        };

    }

    public AbsListView.OnScrollListener getOnScrollListener(){
        return onScrollListener;
    }

    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        int lastPosition = 0;
        int state = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            state = scrollState;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem == 0) {
                animateBack();
            }
            if (firstVisibleItem > 0) {
                if (firstVisibleItem > lastPosition && state == SCROLL_STATE_FLING) {
                    animateHide();
                }
            }
            lastPosition = firstVisibleItem;
        }
    };

}
