package com.example.android0027.MyPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.android0027.R;

public class Fragment1 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Fragment 1의 레이아웃 inflate
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        // Fragment 1의 UI 설정 및 로직 구현


        return view;
    }

}
