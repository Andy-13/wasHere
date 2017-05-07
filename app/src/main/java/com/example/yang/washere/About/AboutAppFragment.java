package com.example.yang.washere.About;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yang.washere.R;

/**
 * Created by Yang on 2017/4/28.
 */

public class AboutAppFragment extends Fragment {
    private static Context mContext;

    public static AboutAppFragment newInstance(Context context) {

        Bundle args = new Bundle();
        mContext = context;
        AboutAppFragment fragment = new AboutAppFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about,container,false);
        return view;
    }
}
