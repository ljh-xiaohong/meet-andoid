package com.yuejian.meet.activities.meritsurname;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.FamilyTree.AddRelationActivity;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.common.SelectMemberCityActivity;
import com.yuejian.meet.activities.common.SurnameActivity;
import com.yuejian.meet.adapters.AddSurnameOriginAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.RegionEntity;
import com.yuejian.meet.bean.SurnameOriginAllEntity;
import com.yuejian.meet.bean.SurnameOriginEntity;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * @author :
 * @time : 2018/11/13 16:59
 * @desc : 姓氏族人 添加好友
 * @version: V1.0
 * @update : 2018/11/13 16:59
 */

public class AddSurnameOriginActivity extends BaseActivity implements SpringView.OnFreshListener {

  @Bind(R.id.ib_back)
  ImageButton mIbBack;

  @Bind(R.id.ll_selected_location)
  LinearLayout mLlSelectedLocation;

  @Bind(R.id.ll_selected_surname)
  LinearLayout mLlSelectedSurname;

  @Bind(R.id.app_surname_origin_rv)
  RecyclerView mRecyclerView;

  @Bind(R.id.tv_address)
  TextView mTvAddress;

  @Bind(R.id.tv_surname)
  TextView mTvSurname;

  @Bind(R.id.app_surname_origin_sv)
  SpringView mSpringView;


  private int pageIndex = 1;
  public String surname;
  String province;
  String city = "";


  String district = "";
  String areaName = "";


  boolean isSelProvince = false;
  private AddSurnameOriginAdapter mAddSurnameOriginAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_surname_origin);
    initView();
    initData();
    initListener();
  }

  private void initView() {

    province = getString(R.string.nationwide);

    mTvAddress.setText("全国");

    if (user.getSurname() != null) {
      mTvSurname.setText(user.getSurname());
      surname = user.getSurname();

    } else {
      mTvSurname.setText("全部");
      surname = "全部";
    }
    mSpringView.setHeader(new DefaultHeader(this));
    mSpringView.setFooter(new DefaultFooter(this));
    LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    mRecyclerView.setLayoutManager(manager);
    mAddSurnameOriginAdapter = new AddSurnameOriginAdapter(this, null);
    mRecyclerView.setAdapter(mAddSurnameOriginAdapter);




  }

  private void initData() {
    if ("".equals(province)) {
      return;
    }
    /**
     * age_range  年龄段
     * surname 姓氏
     * pageIndex 页码
     * area_name 区间名 是 市 县 还是全国
     * province 市名 县名 区名 或者全国
     */
    final Map<String, Object> params = new HashMap<>();

    params.put("pageIndex", pageIndex + "");

    if (isSelProvince || province.equals(getString(R.string.nationwide)))
      params.put("province", province);
    if (district.equals(getString(R.string.surnames_all)) || district.equals(getString(R.string.nationwide))) {
      params.put("area_name", "");
    } else {
      params.put("area_name", areaName);
    }
    params.put("surname", surname == null ? "" : surname);

    params.put("age_range", "0,100");

    // TODO: 2018/11/20   新加用户id 判断好友部分

    params.put("customer_id", user.getCustomer_id());

    apiImp.getSurnameOrigin(params, this, new DataIdCallback<String>() {
      @Override
      public void onSuccess(String data, int id) {
        mSpringView.onFinishFreshAndLoad();
        SurnameOriginAllEntity membersAllEntity = JSON.parseObject(data, SurnameOriginAllEntity.class);
        List<SurnameOriginEntity> membersEntities = JSON.parseArray(membersAllEntity.getMembers(), SurnameOriginEntity.class);
        if (pageIndex == 1) {
          if (membersEntities == null || membersEntities.isEmpty()) {
            pageIndex = 1;
          } else {
            mAddSurnameOriginAdapter.setNewData(membersEntities);
          }
        } else {
          if (membersEntities == null || membersEntities.isEmpty()) {
            pageIndex--;
          } else {
            mAddSurnameOriginAdapter.addData(membersEntities);
          }
        }




      }

      @Override
      public void onFailed(String errCode, String errMsg, int id) {
        if (mSpringView != null) {
          mSpringView.onFinishFreshAndLoad();
        }
      }
    });

  }

  private void initListener() {
    mLlSelectedLocation.setOnClickListener(this);
    mLlSelectedSurname.setOnClickListener(this);
    mIbBack.setOnClickListener(this);
    mSpringView.setListener(this);

    mAddSurnameOriginAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
      @Override
      public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
          case R.id.item_surname_rank_iv_icon:
            // 点击头像 到个人资料

            long customer_id = mAddSurnameOriginAdapter.getData().get(position).getCustomer_id();
           AppUitls.goToPersonHome(AddSurnameOriginActivity.this,customer_id +"");


            break;
          case R.id.item_surname_rank_ll:
            if (mAddSurnameOriginAdapter.getData().get(position).getIs_friend() == 1) {
              // 已经是好友了
              return;
            } else {
              Intent intent = new Intent(AddSurnameOriginActivity.this, AddRelationActivity.class);
              intent.putExtra("userName", mAddSurnameOriginAdapter.getData().get(position).getSurname() + mAddSurnameOriginAdapter.getData().get(position).getName());
              intent.putExtra("op_customer_id", mAddSurnameOriginAdapter.getData().get(position).getCustomer_id() + "");
              startActivityForResult(intent, 01);
            }

            break;

        }
      }
    });

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.ll_selected_location:
        // 根据地址筛选
        Intent cityintent = new Intent(this, SelectMemberCityActivity.class);
        if (StringUtils.isAutonomy(AppConfig.province)) {
          cityintent.putExtra("city", AppConfig.province);
        } else {
          cityintent.putExtra("city", AppConfig.district);
        }
        startActivity(cityintent);

        break;

      case R.id.ll_selected_surname:
        // 根据姓氏筛选
        Intent intent = new Intent(this, SurnameActivity.class);
        if (user != null) {
          intent.putExtra("meSurname", user.getSurname());
        } else {
          intent.putExtra("meSurname", getString(R.string.txt_comment_all));
        }
        startActivity(intent);

        break;
      case R.id.ib_back:
        this.finish();
        break;
    }
    super.onClick(v);
  }

  @Override
  public void onBusCallback(BusCallEntity event) {
    super.onBusCallback(event);

    if (event.getCallType() == BusEnum.SURNAME_UPDATE) {
      // 更改姓名bus回调
      surname = event.getData();
      pageIndex = 1;
      mTvSurname.setText(surname);
      initData();
    } else if (event.getCallType() == BusEnum.district) {
      isSelProvince = false;
      if (AppConfig.district.equals(event.getData())) {
        // 选择定位自己所在的区域
        province = AppConfig.province;

        city = AppConfig.city;

        district = AppConfig.district;

        mTvAddress.setText(district.equals("") ? city : district);

        getAreaName(province, city, district);

        return;
      } else if (StringUtils.isAutonomy(event.getData())) {
        // 选择省市区
        province = event.getData();
        city = event.getData();
        district = event.getData();
      } else if (getString(R.string.surnames_all).equals(event.getData())) {
        // 选择全国
        province = getString(R.string.nationwide);
        district = getString(R.string.nationwide);
      } else {
        if (event.getStateType().equals("2")) {
          city = event.getData();
          district = "";
        } else {
          district = event.getData();
        }
      }
      areaName = event.getAreaName();
      mTvAddress.setText(district.equals("") ? city : district);
      pageIndex = 1;
      initData();
    } else if (event.getCallType() == BusEnum.CITY) {
      isSelProvince = false;
      city = event.getData();
    } else if (event.getCallType() == BusEnum.PROVINCE) {
      // 按省份选择 选择省份的话
      province = event.getData();
      pageIndex = 1;
      if (event.getSelProvince()) {
        isSelProvince = true;
        areaName = province;
        mTvAddress.setText(province);
        initData();
      }
    } else if (event.getCallType() == BusEnum.NETWOR_CONNECTION) {

    }
  }

  public void getAreaName(String province, final String city, String area) {
    Map<String, Object> params = new HashMap<>();
    params.put("province", province);
    params.put("city", city);
    params.put("area", area);
    apiImp.getAreaName(params, this, new DataIdCallback<String>() {
      @Override
      public void onSuccess(String data, int id) {
        RegionEntity entity = JSON.parseObject(data, RegionEntity.class);

        areaName = entity.getArea_name() == null ? "" : entity.getArea_name();
        try {
          district = areaName.substring(areaName.indexOf(" ") + 1, areaName.length());
          mTvSurname.setText(district.equals("") ? city : district);
        } catch (Exception e) {
        }
        pageIndex = 1;
        initData();

      }

      @Override
      public void onFailed(String errCode, String errMsg, int id) {

      }
    });
  }

  @Override
  public void onRefresh() {
    pageIndex = 1;
    initData();


  }

  @Override
  public void onLoadmore() {
    pageIndex++;
    initData();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode != RESULT_OK) {
      return;
    }
    if (requestCode == 01){
      pageIndex = 1;
      initData();
    }

  }
}
