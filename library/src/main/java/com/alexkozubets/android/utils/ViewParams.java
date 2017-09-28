package com.alexkozubets.android.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

@SuppressWarnings("unused")
public class ViewParams {

    protected final View view;

    protected final ViewGroup.LayoutParams params;

    protected final DisplayMetrics displayMetrics;

    private ViewParams(View v) {
        view = v;
        params = v.getLayoutParams();
        displayMetrics = v.getResources().getDisplayMetrics();
    }

    public ViewParams fitScreenWidth() {
        params.width = displayMetrics.widthPixels;
        return this;
    }

    public ViewParams fitScreenHeight() {
        params.height = displayMetrics.heightPixels;
        return this;
    }

    public ViewParams partOfScreenWidth(float f) {
        params.width = (int) (displayMetrics.widthPixels * f);
        return this;
    }

    public ViewParams partOfScreenHeight(float f) {
        params.height = (int) (displayMetrics.heightPixels * f);
        return this;
    }

    public ViewParams fitScreen() {
        fitScreenWidth();
        fitScreenHeight();
        return this;
    }

    public ViewParams wrapContentWidth() {
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        return this;
    }

    public ViewParams wrapContentHeight() {
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        return this;
    }

    public ViewParams matchParentWidth() {
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        return this;
    }

    public ViewParams matchParentHeight() {
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
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

    @NonNull
    private ViewGroup.MarginLayoutParams marginParams(ViewGroup.LayoutParams params) {
        if (params instanceof ViewGroup.MarginLayoutParams) {
            return ((ViewGroup.MarginLayoutParams) params);

        } else {
            throw new RuntimeException("LayoutParams is not instance of MarginLayoutParams");
        }
    }

    public void apply() {
        view.requestLayout();
    }

    public static ViewParams of(View v) {
        return new ViewParams(v);
    }
}
