package com.alexkozubets.sample;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexkozubets.android.utils.R;

public class MenuFragment extends Fragment implements View.OnClickListener {

    private OnMenuItemClickListener onMenuItemClickListener;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_simple_resize_sample).setOnClickListener(this);
        view.findViewById(R.id.btn_animated_resize_sample).setOnClickListener(this);
        view.findViewById(R.id.btn_list_items_resize_sample).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_simple_resize_sample: {
                onMenuItemClickListener.onSimpleResizeSampleClicked();
            } break;

            case R.id.btn_animated_resize_sample: {
                onMenuItemClickListener.onAnimatedResizeSampleClicked();
            } break;

            case R.id.btn_list_items_resize_sample: {
                onMenuItemClickListener.onListItemsResizeSampleClicked();
            } break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onMenuItemClickListener = (OnMenuItemClickListener) context;
        } catch (ClassCastException e) {
            throw new RuntimeException("Context object should implement OnMenuItemClickListener interface.");
        }
    }

    public interface OnMenuItemClickListener {
        void onSimpleResizeSampleClicked();
        void onAnimatedResizeSampleClicked();
        void onListItemsResizeSampleClicked();
    }
}
