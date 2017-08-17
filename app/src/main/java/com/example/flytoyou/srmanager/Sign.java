package com.example.flytoyou.srmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flytoyou.srmanager.Bean.User;
import com.example.flytoyou.srmanager.Util.App;
import com.example.flytoyou.srmanager.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flytoyou on 2016/12/7.
 */

public class Sign extends AppCompatActivity {

    private EditText qqname,qqpsw;
    private String name,pwd,str,str1;
    private Button tj;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);

        spf = this.getSharedPreferences("user",MODE_PRIVATE);
        editor = spf.edit();

        qqname = (EditText) findViewById(R.id.QQname);
        qqpsw = (EditText) findViewById(R.id.qqpsw);
        tj = (Button) findViewById(R.id.btn_tj);

        tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tj.setText("注册中...");

                //获取用户输入的内容
                name = qqname.getText().toString();
                pwd = qqpsw.getText().toString();
                if (reguser(v)&&regpwd(v)){
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            Map<String,Object> map = new HashMap<String, Object>();
                            map.put("username",name);
                            map.put("password",pwd);
                            str = HttpUtil.doPost(HttpUtil.path+"SelectNameServlet",map);
                            if (str.equals("error")) {
                                handler.sendEmptyMessage(0x000);
                            }else {
                                try {
                                    JSONObject jsonObject1 = new JSONObject(str);
                                    if (jsonObject1.getInt("id") > 0) {
                                        handler.sendEmptyMessage(0x124);
                                    } else {
                                        str1 = HttpUtil.doPost(HttpUtil.path + "RegeistServlet", map);
                                        if (str1.equals("true")) {
                                            String str2 = HttpUtil.doPost(HttpUtil.path + "LoginServlet", map);
                                            JSONObject jsonObject = new JSONObject(str2);
                                            User user = new User();
                                            user.setId(jsonObject.getInt("id"));
                                            user.setUsername(jsonObject.getString("username"));
                                            user.setPasssword(jsonObject.getString("password"));
                                            user.setMoney(jsonObject.getDouble("money"));
                                            user.setRoom(jsonObject.getString("room"));
                                            App.user = user;
                                            //将用户信息放入轻量级数据库
                                            editor.putString("username",jsonObject.getString("username"));
                                            editor.putString("password",jsonObject.getString("password"));
                                            editor.commit();

                                            handler.sendEmptyMessage(0x123);
                                        } else {
                                            handler.sendEmptyMessage(0x125);
                                        }
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

    //验证用户名
    public boolean reguser(View view){
        if (TextUtils.isEmpty(qqname.getText().toString())){
            showToast(Sign.this, "请输入账号", 1000);
            return false;
        }else{
            return true;
        }
    }

    //验证密码
    public boolean regpwd(View view){
        if (TextUtils.isEmpty(qqpsw.getText().toString())){
            showToast(Sign.this, "请输入密码", 1000);
            return false;
        }else if (!qqpsw.getText().toString().trim().matches("[0-9]*")) {
            showToast(Sign.this,"密码只能输入数字",1000);
            return false;
        }else{
            return true;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tj.setText("完成注册");
            if (msg.what == 0x123){
                showToast(Sign.this,"注册成功", 1000);
                startActivity(new Intent(Sign.this, Index.class));
            }else if (msg.what == 0x124){
                showToast(Sign.this, "存在重复用户名", 1000);
            }else if (msg.what == 0x125){
                showToast(Sign.this, "注册失败,发生未知错误", 1000);
            }else if (msg.what == 0x000){
                Toast.makeText(Sign.this, "网络连接失败，请检查您的网络", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
