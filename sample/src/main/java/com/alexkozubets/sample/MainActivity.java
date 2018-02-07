package com.alexkozubets.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.alexkozubets.samlpe.R;

public class MainActivity extends AppCompatActivity implements MenuFragment.OnMenuItemClickListener {

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
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getTag());
        }
        transaction.commitAllowingStateLoss();
    }
}
