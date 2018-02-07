package com.alexkozubets.sample;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexkozubets.android.utils.ViewParams;
import com.alexkozubets.samlpe.R;

public class AnimatedResizeFragment extends Fragment {

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
        View v = findView(R.id.container);

        ViewParams.of(v).animate()
                .widthBy(200)
                .heightBy(200)
                .repeatInfinitely()
                .repeatModeReverse()
                .setDuration(2500)
                .start();
    }

    protected <T extends View> T findView(@IdRes int id) {
        return getView() != null ? (T) getView().findViewById(id) : null;
    }
}
