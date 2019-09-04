package com.yuejian.meet.activities.mine;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.ViewInject;

import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

public class LanguageSettingActivity extends BaseActivity{
    @Bind(R.id.txt_titlebar_save)
    TextView txt_titlebar_save;
    @Bind(R.id.select_img_1)
    ImageView select_img_1;
    @Bind(R.id.select_img_2)
    ImageView select_img_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_setting);
        initView();
    }
    public void initView(){
        txt_titlebar_save.setVisibility(View.VISIBLE);
        if (PreferencesUtil.get(this,PreferencesUtil.Langguage,"1").equals("1")){
            select_img_1.setVisibility(View.VISIBLE);
        }else {
            select_img_2.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.language_layout_1,R.id.language_layout_2,R.id.txt_titlebar_save})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.txt_titlebar_save:
                if (select_img_1.getVisibility()==View.VISIBLE){
                    PreferencesUtil.put(this,PreferencesUtil.Langguage,"1");
                    changeAppLanguage("1");
                }else {
                    PreferencesUtil.put(this,PreferencesUtil.Langguage,"2");
                    changeAppLanguage("2");
                }

                break;
            case R.id.language_layout_1:
                select_img_1.setVisibility(View.VISIBLE);
                select_img_2.setVisibility(View.GONE);

                break;
            case R.id.language_layout_2:
                select_img_1.setVisibility(View.GONE);
                select_img_2.setVisibility(View.VISIBLE);
                break;
        }
    }
    public void changeAppLanguage(String name) {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
// 应用用户选择语言
        if (name.contains("1")){
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }else {
            config.locale = Locale.TRADITIONAL_CHINESE;
        }
        resources.updateConfiguration(config, dm);
        BusCallEntity busCallEntity=new BusCallEntity();
        busCallEntity.setCallType(BusEnum.Language);
        Bus.getDefault().post(busCallEntity);
        AppConfig.language=name;
//        ViewInject.toast(this,"设置到哪里");
        finish();
    }
}
