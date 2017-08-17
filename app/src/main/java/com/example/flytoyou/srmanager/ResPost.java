package com.example.flytoyou.srmanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flytoyou.srmanager.Util.App;
import com.example.flytoyou.srmanager.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flytoyou on 2017/2/28.
 */

public class ResPost extends AppCompatActivity {

    private EditText resname,respage,restime;
    private Button yes,back;
    private String name,page,str,time;
    private int userid;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respost);

        userid = App.user.getId();

        findview();

        //提交按钮
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = resname.getText().toString();
                page = respage.getText().toString();
                time = restime.getText().toString();

                if (name.length()==0 || page.length()==0||time.length()==0){
                    Toast.makeText(ResPost.this, "请填写完整物品信息", Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            Map<String,Object> map = new HashMap<String, Object>();
                            map.put("userId",userid);
                            map.put("resName",name);
                            map.put("resTime",time);
                            map.put("resBadMessger",page);
                            Log.e("userId",userid+"");
                            Log.e("resName",name);
                            Log.e("resTime",time);
                            Log.e("resBadMessger",page);
                            str = HttpUtil.doPost(HttpUtil.path+"InsertResServlet",map);
                            Log.e("str",str);
                                if (str.equals("true")){
                                    handler.sendEmptyMessage(0x123);
                                }else if (str.equals("false")){
                                    handler.sendEmptyMessage(0x124);
                                }else if (str.equals("error")){
                                    handler.sendEmptyMessage(0x000);
                                }
                        }
                    }.start();
                }
            }
        });

        //返回按钮
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void findview(){
        resname = (EditText) findViewById(R.id.resname);
        respage = (EditText) findViewById(R.id.respage);
        restime = (EditText) findViewById(R.id.restime);
        yes = (Button) findViewById(R.id.btn_yes);
        back = (Button) findViewById(R.id.btn_fh);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123){
                Toast.makeText(ResPost.this, "报修成功", Toast.LENGTH_SHORT).show();
                finish();
            }else if (msg.what == 0x124){
                Toast.makeText(ResPost.this, "报修失败，发生错误", Toast.LENGTH_SHORT).show();
            }else if (msg.what == 0x000){
                Toast.makeText(ResPost.this, "报修失败,请检查您的网络连接", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
