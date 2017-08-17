package com.example.flytoyou.srmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flytoyou.srmanager.Bean.User;
import com.example.flytoyou.srmanager.Util.App;
import com.example.flytoyou.srmanager.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by flytoyou on 2016/12/7.
 */

public class Login extends AppCompatActivity {

    private Button login,sign;
    private EditText name,pwd;
    private String qqname,qqpwd;
    private int userid;
    private long exitTime = 0;
    private SharedPreferences spf = null;
    private Editor editor = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        spf = this.getSharedPreferences("user",MODE_PRIVATE);
        editor = spf.edit();

        login = (Button) findViewById(R.id.btn_login);
        sign = (Button) findViewById(R.id.btn_sign);
        name = (EditText) findViewById(R.id.name);
        pwd = (EditText) findViewById(R.id.psw);

        //登陆按钮
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setText("登陆中...");

                //获得用户输入的内容
                qqname = name.getText().toString();
                qqpwd = pwd.getText().toString();

                if (name.length()==0||pwd.length()==0){
                    Toast.makeText(Login.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                    login.setText("立即登录");
                }
                else {
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            Map<String,Object> map = new HashMap<String, Object>();
                            map.put("username",qqname);
                            map.put("password",qqpwd);
                            String str = HttpUtil.doPost(HttpUtil.path+"LoginServlet",map);
                            if (str.equals("error")) {
                                handler.sendEmptyMessage(0x000);
                            }else {
                                try {
                                    JSONObject jsonObject = new JSONObject(str);
                                    if (jsonObject.getInt("id") > 0) {
                                        //创建实体类对象存储账号信息
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
                                        handler.sendEmptyMessage(0x124);
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

        //注册按钮
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Sign.class));
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            login.setText("立即登录");
            if (msg.what == 0x123){
                Toast.makeText(Login.this, "登陆成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, Index.class));
                finish();
            }else if (msg.what == 0x124){
                Toast.makeText(Login.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
            }else if (msg.what == 0x000){
                Toast.makeText(Login.this, "网络连接失败，请检查您的网络", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //再次返回键退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                    System.exit(0);
                }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
