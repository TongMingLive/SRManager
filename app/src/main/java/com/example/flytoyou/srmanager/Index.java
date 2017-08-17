package com.example.flytoyou.srmanager;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flytoyou.srmanager.Fragment.Fragment1;
import com.example.flytoyou.srmanager.Fragment.Fragment2;
import com.example.flytoyou.srmanager.Fragment.Fragment3;
import com.example.flytoyou.srmanager.Fragment.Fragment4;

import java.util.ArrayList;
import java.util.List;

public class Index extends FragmentActivity implements OnClickListener {

    private LinearLayout L1,L2,L3,L4;
    private TextView weinxintv,txltv,faxiantv,metv,toptitle;
    private ImageView weinxiniv,txliv,faxianiv,meiv;
    private ViewPager viewPager;
    private Button topimg;
    private String userid;
    private long exitTime = 0;

    private int pagenum = 1;//页数

    private List<LinearLayout> LinList = new ArrayList<>();
    private List<Fragment> FgList = new ArrayList<>();
    private List<ImageView> ImgList = new ArrayList<>();
    private List<TextView> TexList = new ArrayList<>();

    private FragmentManager fragmentManager;

    private PopupWindow mPopupWindow;

    private FragmentTransaction fragmentTransaction;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        FindView();

        //获取fragment管理者对象
        fragmentManager = getSupportFragmentManager();

        //将碎片添加到fglist内
        Fragment1 fragment1 = new Fragment1();
        Fragment2 fragment2 = new Fragment2();
        Fragment3 fragment3 = new Fragment3();
        Fragment4 fragment4 = new Fragment4();
        FgList.add(fragment1);FgList.add(fragment2);FgList.add(fragment3);FgList.add(fragment4);

        //设置适配器
        viewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return FgList.get(position);
            }

            @Override
            public int getCount() {
                //返回item个数
                return FgList.size();
            }
        });

        //ViewPage监听事件
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onClick(LinList.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    //FindView
    private void FindView(){
        L1 = (LinearLayout) findViewById(R.id.l_weixin);
        L2 = (LinearLayout) findViewById(R.id.l_txl);
        L3 = (LinearLayout) findViewById(R.id.l_faxian);
        L4 = (LinearLayout) findViewById(R.id.l_me);
        LinList.add(L1);LinList.add(L2);LinList.add(L3);LinList.add(L4);

        weinxiniv = (ImageView) findViewById(R.id.wexiniv);
        txliv = (ImageView) findViewById(R.id.txliv);
        faxianiv = (ImageView) findViewById(R.id.faxianiv);
        meiv = (ImageView) findViewById(R.id.meiv);
        ImgList.add(weinxiniv);ImgList.add(txliv);ImgList.add(faxianiv);ImgList.add(meiv);

        weinxintv = (TextView) findViewById(R.id.weixintv);
        txltv = (TextView) findViewById(R.id.txltv);
        faxiantv = (TextView) findViewById(R.id.faxiantv);
        metv = (TextView) findViewById(R.id.metv);
        toptitle = (TextView) findViewById(R.id.top_title);
        TexList.add(weinxintv);TexList.add(txltv);TexList.add(faxiantv);TexList.add(metv);

        topimg = (Button) findViewById(R.id.top_img);

        viewPager = (ViewPager) findViewById(R.id.i_vp);

        //注册点击事件
        L1.setOnClickListener(this);L2.setOnClickListener(this);L3.setOnClickListener(this);L4.setOnClickListener(this);
        topimg.setOnClickListener(this);

    }

    //公告页面更改
    public void changeweixin(){
        viewPager.setCurrentItem(0,false);
        weinxiniv.setImageResource(R.mipmap.chats_green);
        weinxintv.setTextColor(Color.rgb(59,183,21));
        toptitle.setText("公告");
        topimg.setBackgroundResource(R.mipmap.chuifengji);
        topimg.setVisibility(viewPager.VISIBLE);
        pagenum = 1;
    }

    //报修页面更改
    public void changetxl(){
        viewPager.setCurrentItem(1,false);
        txliv.setImageResource(R.mipmap.contacts_green);
        txltv.setTextColor(Color.rgb(59,183,21));
        toptitle.setText("报修");
        topimg.setBackgroundResource(R.mipmap.baoxiu);
        topimg.setVisibility(viewPager.VISIBLE);
        pagenum = 2;
    }

    //查询页面更改
    public void changefaxian(){
        viewPager.setCurrentItem(2,false);
        faxianiv.setImageResource(R.mipmap.discover_green);
        faxiantv.setTextColor(Color.rgb(59,183,21));
        toptitle.setText("查询");
        topimg.setVisibility(viewPager.INVISIBLE);
        pagenum = 3;
    }

    //我页面更改
    public void changeme(){
        viewPager.setCurrentItem(3,false);
        meiv.setImageResource(R.mipmap.about_me_green);
        metv.setTextColor(Color.rgb(59,183,21));
        toptitle.setText("我");
        topimg.setVisibility(viewPager.INVISIBLE);
        pagenum = 4;
    }

    //DP转PX像素单位
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //公告顶部按钮
    public void weixinbtn(){
        changeweixin();
        if (null == mPopupWindow || !mPopupWindow.isShowing()) {
            LayoutInflater mLayoutInflater = (LayoutInflater) Index.this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View music_popunwindwow = mLayoutInflater.inflate(R.layout.mypopwindow, null);
           /* LinearLayout help = (LinearLayout)music_popunwindwow.findViewById(R.id.pop_window_help);
            LinearLayout get = (LinearLayout)music_popunwindwow.findViewById(R.id.pop_window_get);
            help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mPopupWindow.dismiss();
                }
            });
            get.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Index.this, "暂无验证请求", Toast.LENGTH_SHORT).show();
                    mPopupWindow.dismiss();
                }
            });*/
            mPopupWindow = new PopupWindow(
                    music_popunwindwow, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            mPopupWindow.showAtLocation(findViewById(R.id.main), Gravity.RIGHT | Gravity.TOP, 0,dip2px(getApplicationContext(),70));
        }else if (null != mPopupWindow && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            if (null == mPopupWindow) {
                Log.e("MainActivity", "null == mPopupWindow");
            }
        }
    }

    //报修顶部按钮
    public void txlbtn(){
        changetxl();
    }

    //初始化底部导航栏
    private void restart(){
        weinxiniv.setImageResource(R.mipmap.chats);
        txliv.setImageResource(R.mipmap.contacts);
        faxianiv.setImageResource(R.mipmap.discover);
        meiv.setImageResource(R.mipmap.about_me);
        weinxintv.setTextColor(Color.BLACK);
        txltv.setTextColor(Color.BLACK);
        faxiantv.setTextColor(Color.BLACK);
        metv.setTextColor(Color.BLACK);
    }

    //点击事件
    @Override
    public void onClick(View v) {
        restart();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()){
            case R.id.l_weixin:
                changeweixin();
                break;
            case R.id.l_txl:
                changetxl();
                break;
            case R.id.l_faxian:
                changefaxian();
                break;
            case R.id.l_me:
                changeme();
                break;
            case R.id.top_img:
                if (pagenum == 1){
                    //weixinbtn();
                }else if (pagenum == 2){
                    //txlbtn();
                }
                break;
        }
    }

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
