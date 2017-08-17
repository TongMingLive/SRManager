package com.example.flytoyou.srmanager.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flytoyou.srmanager.Index;
import com.example.flytoyou.srmanager.Login;
import com.example.flytoyou.srmanager.MainActivity;
import com.example.flytoyou.srmanager.MyMessage;
import com.example.flytoyou.srmanager.R;
import com.example.flytoyou.srmanager.Util.App;

/**
 * Created by flytoyou on 2016/12/6.
 */

public class Fragment4 extends Fragment {

    private TextView roomnum,name,id;
    private LinearLayout mymessage,outlogin;
    private SharedPreferences spf = null;
    private SharedPreferences.Editor editor = null;

    //自定义吐司时间
    public static void showToast(final Activity activity, final String word, final long time){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(activity, word, Toast.LENGTH_LONG);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        toast.cancel();
                    }
                }, time);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.wo,null);

        spf = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        roomnum = (TextView) view.findViewById(R.id.tex_roomnum);
        name = (TextView) view.findViewById(R.id.w_name);
        id = (TextView) view.findViewById(R.id.w_id);
        mymessage = (LinearLayout) view.findViewById(R.id.mymessage);
        outlogin = (LinearLayout) view.findViewById(R.id.outlogin);

        roomnum.setText(App.user.getRoom());
        name.setText(App.user.getUsername());
        id.setText("ID:"+App.user.getId()+"");

        mymessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyMessage.class));
            }
        });

        outlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = spf.edit();
                editor.clear().commit();
                showToast(getActivity(), "退出成功", 1000);
                startActivity(new Intent(getActivity(), Login.class));
                App.user = null;
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
