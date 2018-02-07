package com.alexkozubets.sample;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alexkozubets.android.utils.ViewParams;
import com.alexkozubets.samlpe.R;

public class SimpleResizeFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener {

    protected static final float MIN = 0.25f;

    protected static final float MAX = 0.75f;

    protected static final int MIN_WIDTH;

    protected static final int MIN_HEIGHT;

    protected static final int MAX_WIDTH;

    protected static final int MAX_HEIGHT;

    static {
        DisplayMetrics screen = Resources.getSystem().getDisplayMetrics();
        MIN_WIDTH = (int) (screen.widthPixels * MIN);
        MIN_HEIGHT = (int) (screen.heightPixels * MIN);
        MAX_WIDTH = (int) (screen.widthPixels * MAX);
        MAX_HEIGHT = (int) (screen.heightPixels * MAX);
    }

    private ImageView imgDroid;

    private TextView tvDroidSize;

    private SeekBar sbSize;

    private RadioGroup rgModes;

    private Mode mode;

    public SimpleResizeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simple_resize, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews();
        initViews();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_absolute: {
                mode = Mode.ABSOLUTE;
            } break;

            case R.id.rb_relative_to_parent: {
                mode = Mode.RELATIVE_TO_PARENT;
            } break;

            case R.id.rb_relative_to_screen: {
                mode = Mode.RELATIVE_TO_SCREEN;
            } break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            switch (mode) {
                case ABSOLUTE: {
//                    int w = (int) (MIN_WIDTH + percentToFloat(progress) * (MAX_WIDTH - MIN_WIDTH));
                    int h = (int) (MIN_HEIGHT + percentToFloat(progress) * (MAX_HEIGHT - MIN_HEIGHT));
                    ViewParams.of(imgDroid)
                            .height(h)
                            .widthToHeightRatio(1f)
                            .apply();
                } break;

                case RELATIVE_TO_PARENT: {
                    ViewParams.of(imgDroid)
                            .partOfParentHeight(percentToFloat(progress))
                            .widthToHeightRatio(1f)
                            .apply();
                } break;

                case RELATIVE_TO_SCREEN: {
                    ViewParams.of(imgDroid)
                            .partOfScreenHeight(percentToFloat(progress))
                            .widthToHeightRatio(1f)
                            .apply();
                } break;
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    protected void bindViews() {
        rgModes = findView(R.id.rg_modes);
        imgDroid = findView(R.id.img_droid);
        tvDroidSize = findView(R.id.tv_droid_size);
        sbSize = findView(R.id.sb_size);
    }

    protected void initViews() {
        rgModes.setOnCheckedChangeListener(this);
        rgModes.check(R.id.rb_absolute);
        sbSize.setOnSeekBarChangeListener(this);
        imgDroid.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                tvDroidSize.setText(String.format("W: %d; H: %d", v.getWidth(), v.getHeight()));
            }
        });
    }

    @FloatRange(from = 0f, to = 1f)
    protected float percentToFloat(@IntRange(from = 0, to = 100) int percent) {
        return percent / 100f;
    }

    protected <T extends View> T findView(@IdRes int id) {
        return getView() != null ? (T) getView().findViewById(id) : null;
    }

    private enum Mode {
        ABSOLUTE,
        RELATIVE_TO_PARENT,
        RELATIVE_TO_SCREEN,
    }
}
