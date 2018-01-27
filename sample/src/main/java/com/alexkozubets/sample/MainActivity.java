package com.alexkozubets.sample;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.alexkozubets.samlpe.R;

public class MainActivity extends Activity implements MenuFragment.OnMenuItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            addFragment(new MenuFragment(), false);
        }
    }

    @Override
    public void onSimpleResizeSampleClicked() {
        addFragment(new SimpleResizeFragment(), true);
    }

    @Override
    public void onAnimatedResizeSampleClicked() {
        addFragment(new AnimatedResizeFragment(), true);
    }

    @Override
    public void onListItemsResizeSampleClicked() {
        // TODO: 9/23/17 implement
    }

    private void addFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getTag());
        }
        transaction.commitAllowingStateLoss();
    }
}
