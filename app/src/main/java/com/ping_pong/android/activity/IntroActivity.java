package com.ping_pong.android.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;

import com.ping_pong.android.fragment.IntroFragment1;
import com.ping_pong.android.fragment.IntroFragment2;
import com.ping_pong.android.fragment.IntroFragment3;
import com.ping_pong.android.R;
import com.ping_pong.android.databinding.ActivityIntroBinding;

public class IntroActivity extends AppCompatActivity {
    private ActivityIntroBinding binding;
    private final int NUM_PAGES = 3;
    private FragmentStateAdapter introAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro);

        introAdapter = new IntroAdapter(this);
        binding.vpIntro.setAdapter(introAdapter);
        binding.indIntro.setViewPager2(binding.vpIntro);
    }

    private class IntroAdapter extends FragmentStateAdapter {

        public IntroAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new IntroFragment1();
                case 1:
                    return new IntroFragment2();
                default:
                    return new IntroFragment3();
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}