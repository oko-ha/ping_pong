package com.ping_pong.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.ping_pong.android.R;
import com.ping_pong.android.databinding.FragmentIntro2Binding;

public class IntroFragment2 extends Fragment {
    private FragmentIntro2Binding binding;
    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_intro2, container, false);
        view = binding.getRoot();

        return view;
    }
}
