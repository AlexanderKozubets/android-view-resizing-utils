package com.alexkozubets.sample;


import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.alexkozubets.android.utils.ViewParams;
import com.alexkozubets.samlpe.R;

import static com.alexkozubets.sample.DimenUtil.dpToPx;

public class AnimatedResizeFragment extends Fragment {

    private final int DURATION_MS = 2500;

    public AnimatedResizeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_animated_resize, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final View v = view.findViewById(R.id.container);

        RadioGroup modes = view.findViewById(R.id.rg_modes);
        modes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_none: {
                        resetState(v);
                    } break;

                    case R.id.rb_resize: {
                        resetState(v);
                        resizeBy(v, dpToPx(75));
                    } break;

                    case R.id.rb_scale: {
                        resetState(v);
                        scaleBy(v, 0.5f);
                    } break;
                }
            }
        });

        modes.check(R.id.rb_resize);
    }

    private void resetState(View v) {
        v.animate().cancel();
        v.setScaleX(1.0f);
        v.setScaleY(1.0f);

        ViewParams params = ViewParams.of(v);
        params.animate().cancel();
        params.width(dpToPx(150)).heightToWidthRatio(1f).apply();
    }

    private void resizeBy(View v, int value) {
        ViewParams.of(v).animate()
                .widthBy(value)
                .heightBy(value)
                .repeatInfinitely()
                .repeatModeReverse()
                .setDuration(DURATION_MS)
                .start();
    }

    private void scaleBy(final View v, final float value) {
        v.animate()
                .scaleXBy(value)
                .scaleYBy(value)
                .setDuration(DURATION_MS)
                .setListener(new DefaultAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        animation.removeListener(this);
                        if (!isCanceled()) {
                            scaleBy(v, -value);
                        }
                    }
                })
                .start();
    }

    private class DefaultAnimatorListener implements Animator.AnimatorListener {

        private boolean isCanceled;

        public boolean isCanceled() {
            return isCanceled;
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {
            isCanceled = true;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

        @Override
        public void onAnimationStart(Animator animation, boolean isReverse) {
            isCanceled = false;
        }

        @Override
        public void onAnimationEnd(Animator animation, boolean isReverse) {

        }
    }
}
