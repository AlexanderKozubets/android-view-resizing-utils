package com.alexkozubets.android.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.annotation.IntRange;
import android.support.annotation.Px;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;

import java.util.Collection;
import java.util.HashMap;

public class ViewParamsAnimator {

    private ViewParams viewParams;

    private HashMap<Object, ObjectAnimator> animations = new HashMap<>(6);

    private HashMap<ObjectAnimator, Runnable> pendingInit = new HashMap<>(6);

    private AnimatorSet animatorSet;

    private Integer repeatMode;

    private Integer repeatCount;

    ViewParamsAnimator(ViewParams viewParams) {
        this.viewParams = viewParams;
        this.animatorSet = new AnimatorSet();
    }

    public ViewParamsAnimator widthBy(int value) {
        ObjectAnimator animator = createAnimator(WIDTH);
        addPendingAnimation(WIDTH, animator);
        setupAnimation(animator, new OnWidthMeasured(viewParams.view, animator).changeBy(value));
        return this;
    }

    public ViewParamsAnimator toWidth(@Px int value) {
        ObjectAnimator animator = createAnimator(WIDTH);
        addPendingAnimation(WIDTH, animator);
        setupAnimation(animator, new OnWidthMeasured(viewParams.view, animator).changeTo(value));
        return this;
    }

    public ViewParamsAnimator heightBy(int value) {
        ObjectAnimator animator = createAnimator(HEIGHT);
        addPendingAnimation(HEIGHT, animator);
        setupAnimation(animator, new OnHeightMeasured(viewParams.view, animator).changeBy(value));
        return this;
    }

    public ViewParamsAnimator toHeight(int value) {
        ObjectAnimator animator = createAnimator(HEIGHT);
        addPendingAnimation(HEIGHT, animator);
        setupAnimation(animator, new OnHeightMeasured(viewParams.view, animator).changeTo(value));
        return this;
    }

    public ViewParamsAnimator setDuration(long duration) {
        animatorSet.setDuration(duration);
        return this;
    }

    public ViewParamsAnimator repeatModeRestart() {
        repeatMode = ValueAnimator.RESTART;
        return this;
    }

    public ViewParamsAnimator repeatModeReverse() {
        repeatMode = ValueAnimator.REVERSE;
        return this;
    }

    public ViewParamsAnimator setRepeatCount(@IntRange(from = 0, to = Integer.MAX_VALUE) int repeatCount) {
        this.repeatCount = repeatCount;
        return this;
    }

    public ViewParamsAnimator repeatInfinitely() {
        repeatCount = ValueAnimator.INFINITE;
        return this;
    }

    public ViewParamsAnimator setInterpolator(Interpolator interpolator) {
        this.animatorSet.setInterpolator(interpolator);
        return this;
    }

    public ViewParamsAnimator addAnimatorListener(Animator.AnimatorListener listener) {
        this.animatorSet.addListener(listener);
        return this;
    }

    public ViewParamsAnimator removeAnimatorListener(Animator.AnimatorListener listener) {
        this.animatorSet.removeListener(listener);
        return this;
    }

    public ViewParamsAnimator removeAllAnimatorListeners() {
        this.animatorSet.removeAllListeners();
        return this;
    }

    public void start() {
        viewParams.setViewParamsAnimator(this);

        if (isInitialized()) {
            startInternal();
        } else {
            viewParams.view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    viewParams.view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    for (Runnable runnable : pendingInit.values()) {
                        runnable.run();
                    }
                    pendingInit.clear();
                    startInternal();
                }
            });
            viewParams.view.requestLayout();
        }
    }

    private void startInternal() {
        if (animations.isEmpty()) {
            //nothing to run
            return;
        }

        if (animatorSet.isRunning()) {
            animatorSet.end();
            Log.d("VPA","END!!!");
//            viewParams.view.post(new Runnable() {
//                @Override
//                public void run() {
//                    startInternal();
//                }
//            });
            return;
        }

        Collection<ObjectAnimator> animators = animations.values();
        for (ObjectAnimator animator : animators) {
            if (repeatMode != null) {
                animator.setRepeatMode(repeatMode);
            }
            if (repeatCount != null) {
                animator.setRepeatCount(repeatCount);
            }
        }

        Animator[] animatorsArray = new Animator[animators.size()];
        animatorSet.playTogether(animators.toArray(animatorsArray));
        animatorSet.start();
    }

    public boolean isRunning() {
        return animatorSet.isRunning() || !pendingInit.isEmpty();
    }

    public void cancel() {
        animations.clear();
        pendingInit.clear();
        animatorSet.cancel();
    }

    public void end() {
        if (isInitialized()) {
            animations.clear();
            animatorSet.end();
        } else {
            throw new RuntimeException("Trying to end animation that is not started yet!");
        }
    }

    private void addPendingAnimation(Object key, ObjectAnimator animator) {
        ObjectAnimator oldAnimator = animations.put(key, animator);
        if (oldAnimator != null) {
            Runnable runnable = pendingInit.get(oldAnimator);
            if (runnable != null) {
                pendingInit.remove(oldAnimator);
            }
        }
    }

    @Deprecated
    private boolean needsMeasuring() {
        return viewParams.getWidth() < 0 || viewParams.getHeight() < 0;
    }

    private boolean isInitialized() {
        return pendingInit.isEmpty();
    }

    private void setupAnimation(ObjectAnimator animator, SetupAnimationRunnable init) {
        ViewParamsAnimator currentAnimator = viewParams.getViewParamsAnimator();
        if (needsMeasuring() || (currentAnimator != null && currentAnimator.isRunning())) {
            pendingInit.put(animator, init);
        } else {
            init.run();
        }
    }

    private ObjectAnimator createAnimator(Property property) {
        ObjectAnimator animator = new ObjectAnimator();
        animator.setTarget(viewParams);
        animator.setProperty(property);
        return animator;
    }

    static final Property<ViewParams, Integer> WIDTH = new Property<ViewParams, Integer>(Integer.TYPE, "width") {
        @Override
        public void set(ViewParams object, Integer value) {
            object.width(value).apply();
        }

        @Override
        public Integer get(ViewParams viewParams) {
            return viewParams.getWidth();
        }
    };

    static final Property<ViewParams, Integer> HEIGHT = new Property<ViewParams, Integer>(Integer.TYPE, "height") {
        @Override
        public void set(ViewParams object, Integer value) {
            object.height(value).apply();
            Log.d("VPA", "Set height = " + value);
        }

        @Override
        public Integer get(ViewParams viewParams) {
            return viewParams.getHeight();
        }
    };

    static final Property<ViewParams, Integer> MARGIN_LEFT = new Property<ViewParams, Integer>(Integer.TYPE, "margin_left") {
        @Override
        public void set(ViewParams object, Integer value) {
            object.marginLeft(value).apply();
        }

        @Override
        public Integer get(ViewParams viewParams) {
            return viewParams.getLeftMargin();
        }
    };

    static final Property<ViewParams, Integer> MARGIN_RIGHT = new Property<ViewParams, Integer>(Integer.TYPE, "margin_right") {
        @Override
        public void set(ViewParams object, Integer value) {
            object.marginRight(value).apply();
        }

        @Override
        public Integer get(ViewParams viewParams) {
            return viewParams.getRightMargin();
        }
    };

    static final Property<ViewParams, Integer> MARGIN_TOP = new Property<ViewParams, Integer>(Integer.TYPE, "margin_top") {
        @Override
        public void set(ViewParams object, Integer value) {
            object.marginTop(value).apply();
        }

        @Override
        public Integer get(ViewParams viewParams) {
            return viewParams.getTopMargin();
        }
    };

    static final Property<ViewParams, Integer> MARGIN_BOTTOM = new Property<ViewParams, Integer>(Integer.TYPE, "margin_bottom") {
        @Override
        public void set(ViewParams object, Integer value) {
            object.marginBottom(value).apply();
        }

        @Override
        public Integer get(ViewParams viewParams) {
            return viewParams.getBottomMargin();
        }
    };

    private abstract static class SetupAnimationRunnable implements Runnable {

        private final ObjectAnimator animator;

        private Integer to;

        private Integer by;

        public SetupAnimationRunnable(ObjectAnimator animator) {
            this.animator = animator;
        }

        @Override
        public void run() {
            animator.setIntValues(getFromValue(), getToValue());
        }

        final SetupAnimationRunnable changeTo(int value) {
            this.to = value;
            this.by = null;
            return this;
        }

        final SetupAnimationRunnable changeBy(int value) {
            this.by = value;
            this.to = null;
            return this;
        }

        final int getToValue() {
            return to != null ? to : getFromValue() + by;
        }

        abstract int getFromValue();
    }

    private static class OnWidthMeasured extends SetupAnimationRunnable {

        private final View view;

        OnWidthMeasured(View view, ObjectAnimator animator) {
            super(animator);
            this.view = view;
        }

        @Override
        int getFromValue() {
            return view.getWidth();
        }
    }

    private static class OnHeightMeasured extends SetupAnimationRunnable {

        private final View view;

        public OnHeightMeasured(View view, ObjectAnimator animator) {
            super(animator);
            this.view = view;
        }

        @Override
        int getFromValue() {
            return view.getHeight();
        }
    }
}
