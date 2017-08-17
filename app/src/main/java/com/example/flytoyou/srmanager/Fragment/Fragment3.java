package com.example.flytoyou.srmanager.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.flytoyou.srmanager.Login;
import com.example.flytoyou.srmanager.MainActivity;
import com.example.flytoyou.srmanager.R;
import com.example.flytoyou.srmanager.SelectLogin;
import com.example.flytoyou.srmanager.Util.App;
import com.example.flytoyou.srmanager.Util.HttpUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by flytoyou on 2016/12/6.
 */

public class Fragment3 extends Fragment {

    private TextView money;
    private LinearLayout loginnum;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.faxian,null);

        money = (TextView) view.findViewById(R.id.te_money);
        loginnum = (LinearLayout) view.findViewById(R.id.loginnum);

        money.setText(App.user.getMoney()+"");

        loginnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SelectLogin.class));
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
