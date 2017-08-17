package com.example.flytoyou.srmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.flytoyou.srmanager.Bean.User;
import com.example.flytoyou.srmanager.Util.App;
import com.example.flytoyou.srmanager.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences spf;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        //设置全屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        spf = this.getSharedPreferences("user",MODE_PRIVATE);
        //判断用户是否从未登陆过
        if (spf.getString("username",null) == null){
            //未登录过，直接跳入登录界面
            //定时器跳转
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                }
            },2000);
        }else {
            //登陆过，获取轻量级数据库中的账号密码直接登录
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            Map<String,Object> map = new HashMap<String, Object>();
                            map.put("username",spf.getString("username",null));
                            map.put("password",spf.getString("password",null));
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
            },2000);
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123){
                startActivity(new Intent(MainActivity.this, Index.class));
                finish();
            }else if (msg.what == 0x124){
                Toast.makeText(MainActivity.this, "登录超时，请重新登录", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,Login.class));
                finish();
            }else if (msg.what == 0x000){
                Toast.makeText(MainActivity.this, "网络连接失败，请检查您的网络", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,Login.class));
                finish();
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
