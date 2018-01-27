package com.alexkozubets.sample;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

}
