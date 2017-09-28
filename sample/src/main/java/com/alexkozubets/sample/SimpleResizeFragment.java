package com.alexkozubets.sample;


import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alexkozubets.android.utils.ViewParams;
import com.alexkozubets.samlpe.R;

public class SimpleResizeFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

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

    ImageView imgDroid;

    TextView tvDroidSize;

    SeekBar sbWidth;

    SeekBar sbHeight;

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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            int w = (int) (MIN_WIDTH + percentToFloat(progress) * (MAX_WIDTH - MIN_WIDTH));
            int h = (int) (MIN_HEIGHT + percentToFloat(progress) * (MAX_HEIGHT - MIN_HEIGHT));
            ViewParams.of(imgDroid).width(w).height(h).apply();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    protected void bindViews() {
        imgDroid = findView(R.id.img_droid);
        tvDroidSize = findView(R.id.tv_droid_size);
        sbWidth = findView(R.id.sb_width);
        sbHeight = findView(R.id.sb_height);
    }

    protected void initViews() {
        sbWidth.setOnSeekBarChangeListener(this);
        sbHeight.setOnSeekBarChangeListener(this);
    }

    @FloatRange(from = 0f, to = 1f)
    protected float percentToFloat(@IntRange(from = 0, to = 100) int percent) {
        return percent / 100f;
    }

    protected <T extends Comparable<T>> T clamp(T value, T min, T max) {
        return value.compareTo(min) < 0 ? min : (value.compareTo(max) > 0 ? max : value);
    }

    protected <T extends View> T findView(@IdRes int id) {
        return getView() != null ? (T) getView().findViewById(id) : null;
    }
}
