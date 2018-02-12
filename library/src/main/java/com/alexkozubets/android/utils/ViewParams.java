package com.alexkozubets.android.utils;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

@SuppressWarnings("unused")
public class ViewParams {

    protected final View view;

    protected final ViewGroup.LayoutParams params;

    protected final DisplayMetrics displayMetrics;

    private ApplyAfterMeasurementParams afterMeasurementParams;

    private OnAttachStateChangeListener onAttachStateChangeListener;

    public static ViewParams of(View v) {
        return new ViewParams(v);
    }

    private ViewParams(View v) {
        view = v;
        params = v.getLayoutParams();
        displayMetrics = v.getResources().getDisplayMetrics();
    }

    public ViewParams widthMatchScreen() {
        params.width = displayMetrics.widthPixels;
        return this;
    }

    public ViewParams heightMatchScreen() {
        params.height = displayMetrics.heightPixels;
        return this;
    }

    public ViewParams widthMatchScreen(float fraction) {
        params.width = (int) (displayMetrics.widthPixels * fraction);
        return this;
    }

    public ViewParams heightMatchScreen(float fraction) {
        params.height = (int) (displayMetrics.heightPixels * fraction);
        return this;
    }

    public ViewParams matchScreenSize() {
        widthMatchScreen();
        heightMatchScreen();
        return this;
    }

    public ViewParams widthWrapContent() {
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        return this;
    }

    public ViewParams heightWrapContent() {
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        return this;
    }

    // TODO: 2/12/18 add methods widthWrapContent(fraction) and heightWrapContent(fraction)

    public ViewParams widthMatchParent() {
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        return this;
    }

    public ViewParams heightMatchParent() {
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        return this;
    }

    public ViewParams widthMatchParent(float fraction) {
        if (!hasParent(view)) {
            throw new RuntimeException("View is not attached to parent!");
        }
        ensureAfterMeasurementParams();
        afterMeasurementParams.parentWidthFraction = fraction;
        return this;
    }

    public ViewParams heightMatchParent(float fraction) {
        if (!hasParent(view)) {
            throw new RuntimeException("View is not attached to parent!");
        }
        ensureAfterMeasurementParams();
        afterMeasurementParams.parentHeightFraction = fraction;
        return this;
    }

    public ViewParams widthToHeightRatio(float ratio) {
        ensureAfterMeasurementParams();
        afterMeasurementParams.widthToHeightRatio = ratio;
        return this;
    }

    public ViewParams heightToWidthRatio(float ratio) {
        ensureAfterMeasurementParams();
        afterMeasurementParams.heightToWidthRatio = ratio;
        return this;
    }

    public ViewParams width(@Px int w) {
        params.width = w;
        return this;
    }

    public ViewParams height(@Px int h) {
        params.height = h;
        return this;
    }

    public ViewParams margins(@Px int l, @Px int t, @Px int r, @Px int b) {
        marginParams(params).setMargins(l, t, r, b);
        return this;
    }

    public ViewParams marginLeft(@Px int left) {
        marginParams(params).leftMargin = left;
        return this;
    }

    public ViewParams marginRight(@Px int right) {
        marginParams(params).rightMargin = right;
        return this;
    }

    public ViewParams marginTop(@Px int top) {
        marginParams(params).topMargin = top;
        return this;
    }

    public ViewParams marginBottom(@Px int bottom) {
        marginParams(params).bottomMargin = bottom;
        return this;
    }

    public int getWidth() {
        return params.width;
    }

    public int getHeight() {
        return params.height;
    }

    public int getLeftMargin() {
        return marginParams(params).leftMargin;
    }

    public int getRightMargin() {
        return marginParams(params).rightMargin;
    }

    public int getTopMargin() {
        return marginParams(params).topMargin;
    }

    public int getBottomMargin() {
        return marginParams(params).bottomMargin;
    }

    boolean needsMeasuring() {
        return getWidth() < 0 || getHeight() < 0;
    }

    boolean hasParent(View view) {
        return view.getParent() != null;
    }

    @NonNull
    private ViewGroup.MarginLayoutParams marginParams(ViewGroup.LayoutParams params) {
        if (params instanceof ViewGroup.MarginLayoutParams) {
            return ((ViewGroup.MarginLayoutParams) params);

        } else {
            throw new RuntimeException("LayoutParams is not instance of MarginLayoutParams");
        }
    }

    public void apply() {
        if (afterMeasurementParams != null) {
            final ApplyAfterMeasurementParams params = afterMeasurementParams;
            afterMeasurementParams = null;

            SelfRemoveGlobalLayoutListener.addTo(view, new SelfRemoveGlobalLayoutListener.Listener() {
                @Override
                public void onGlobalLayout(View view) {
                    applyAfterMeasurement(view, params);
                }
            });
        }
        view.requestLayout();
    }

    private void ensureAfterMeasurementParams() {
        if (afterMeasurementParams == null) {
            afterMeasurementParams = new ApplyAfterMeasurementParams();
        }
    }

    private void applyAfterMeasurement(View view, ApplyAfterMeasurementParams params) {
        final View parent = (View) view.getParent();
        Integer w = view.getWidth(), h = view.getHeight();

        if (params.parentWidthFraction != null) {
            w = (int) (parent.getWidth() * params.parentWidthFraction);
        }

        if (params.parentHeightFraction != null) {
            h = (int) (parent.getHeight() * params.parentHeightFraction);
        }

        if (params.widthToHeightRatio != null) {
            w = (int) (h * params.widthToHeightRatio);
        }

        if (params.heightToWidthRatio != null) {
            h = (int) (w * params.heightToWidthRatio);
        }

        width(w);
        height(h);

        view.requestLayout();
    }

    private static class ApplyAfterMeasurementParams {

        Float heightToWidthRatio;

        Float widthToHeightRatio;

        Float parentWidthFraction;

        Float parentHeightFraction;

        private void clear() {
            heightToWidthRatio = null;
            widthToHeightRatio = null;
            parentWidthFraction = null;
            parentHeightFraction = null;
        }
    }

    private static class SelfRemoveGlobalLayoutListener implements OnGlobalLayoutListener {

        private View view;

        private Listener listener;

        public static void addTo(View view, Listener listener) {
            view.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new SelfRemoveGlobalLayoutListener(view, listener));
        }

        @Override
        public void onGlobalLayout() {
            removeOnGlobalLayoutListener(view, this);
            listener.onGlobalLayout(view);
        }

        private SelfRemoveGlobalLayoutListener(View view, Listener listener) {
            this.view = view;
            this.listener = listener;
        }

        private static void removeOnGlobalLayoutListener(View v, OnGlobalLayoutListener listener) {
            ViewTreeObserver observer = v.getViewTreeObserver();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                observer.removeGlobalOnLayoutListener(listener);
            } else {
                observer.removeOnGlobalLayoutListener(listener);
            }
        }

        private interface Listener {
            void onGlobalLayout(View view);
        }
    }

    @NonNull
    public ViewParamsAnimator animate() {
        ViewParamsAnimator animator = getViewParamsAnimator();
        if (animator == null) {
            animator = new ViewParamsAnimator(this);
            setViewParamsAnimator(animator);
        }
        return animator;
    }

    @Nullable
    protected ViewParamsAnimator getViewParamsAnimator() {
        Object animator = view.getTag(R.id.view_params_animator);
        ViewParamsAnimator viewParamsAnimator;
        if (animator instanceof ViewParamsAnimator) {
            return (ViewParamsAnimator) animator;
        } else {
            return null;
        }
    }

    private void setViewParamsAnimator(@NonNull ViewParamsAnimator viewParamsAnimator) {
        ViewParamsAnimator oldAnimator = getViewParamsAnimator();
        if (oldAnimator != null && oldAnimator.isRunning()) {
            setOnAttachStateChangeListener(null);
            oldAnimator.cancel();
        }
        setOnAttachStateChangeListener(new CancelAnimationOnDetachFromWindow(viewParamsAnimator));
        view.setTag(R.id.view_params_animator, viewParamsAnimator);
    }

    private void setOnAttachStateChangeListener(@Nullable OnAttachStateChangeListener listener) {
        if (this.onAttachStateChangeListener != null) {
            view.removeOnAttachStateChangeListener(this.onAttachStateChangeListener);
            this.onAttachStateChangeListener = null;
        }

        if (listener != null) {
            this.onAttachStateChangeListener = listener;
            view.addOnAttachStateChangeListener(listener);
        }
    }

    private static class CancelAnimationOnDetachFromWindow implements OnAttachStateChangeListener {

        private final ViewParamsAnimator animator;

        public CancelAnimationOnDetachFromWindow(ViewParamsAnimator animator) {
            this.animator = animator;
        }

        @Override
        public void onViewAttachedToWindow(View view) {

        }

        @Override
        public void onViewDetachedFromWindow(View view) {
            view.removeOnAttachStateChangeListener(this);
            animator.cancel();
        }
    }
}
