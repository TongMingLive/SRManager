package com.example.flytoyou.srmanager.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.flytoyou.srmanager.Index;
import com.example.flytoyou.srmanager.Login;
import com.example.flytoyou.srmanager.R;
import com.example.flytoyou.srmanager.ResPost;
import com.example.flytoyou.srmanager.Talk_All;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by flytoyou on 2016/12/6.
 */

public class Fragment2 extends Fragment {

    private ListView listView;

    private Button baoxiu;

    private List<Map<String,Object>> BaseList = new ArrayList();



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.baoxiu,null);

        baoxiu = (Button) view.findViewById(R.id.btn_baoxiu);

        baoxiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ResPost.class));
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
