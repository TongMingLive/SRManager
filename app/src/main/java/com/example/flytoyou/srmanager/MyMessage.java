package com.example.flytoyou.srmanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
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
 * Created by flytoyou on 2017/3/1.
 */

public class MyMessage extends AppCompatActivity {

    private EditText edtname,edtroom;
    private Button yes;
    private String name,room,str,str1;
    private int id;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymessage);

        edtname = (EditText) findViewById(R.id.edt_name);
        edtroom = (EditText) findViewById(R.id.edt_roomnum);
        yes = (Button) findViewById(R.id.btn_mymessage);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = App.user.getId();
                name = edtname.getText().toString();
                room = edtroom.getText().toString();

                if (name.length()==0 || room.length()==0){
                    Toast.makeText(MyMessage.this, "请输入完整的信息", Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            Map<String,Object> map = new HashMap<String, Object>();
                            map.put("userid",id);
                            map.put("username",name);
                            map.put("room",room);
                            str = HttpUtil.doPost(HttpUtil.path+"SelectNameServlet",map);
                            JSONObject jsonObject = null;
                            if (str.equals("error")) {
                                handler.sendEmptyMessage(0x000);
                            }else {
                                try {
                                    jsonObject = new JSONObject(str);
                                    if (jsonObject.getInt("id") <= 0) {
                                        str1 = HttpUtil.doPost(HttpUtil.path + "UpdateUserServlet", map);
                                        if (str1.equals("true")) {
                                            handler.sendEmptyMessage(0x123);
                                        } else {
                                            handler.sendEmptyMessage(0x124);
                                        }
                                    } else {
                                        handler.sendEmptyMessage(0x125);
                                    }
                                } catch (JSONException e) {
                                    handler.sendEmptyMessage(0x000);
                                }
                            }
                        }
                    }.start();
                }
            }
        });

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123){
                Toast.makeText(MyMessage.this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            }else if (msg.what == 0x124){
                Toast.makeText(MyMessage.this, "修改失败,发生未知错误", Toast.LENGTH_SHORT).show();
            }else if (msg.what == 0x125){
                Toast.makeText(MyMessage.this, "修改失败,存在重复用户名", Toast.LENGTH_SHORT).show();
            }else if (msg.what == 0x000){
                Toast.makeText(MyMessage.this, "网络连接失败，请检查您的网络", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
