package com.yuejian.meet.widgets;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import java.util.List;

public class GiftPagerAdapter
  extends PagerAdapter
{
  private List<GridView> gvs;
  
  public GiftPagerAdapter(List<GridView> paramList)
  {
    this.gvs = paramList;
  }
  
  public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
  {
    ((ViewPager)paramViewGroup).removeView((View)this.gvs.get(paramInt));
  }
  
  public int getCount()
  {
    return this.gvs.size();
  }
  
  public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
  {
    ((ViewPager)paramViewGroup).addView((View)this.gvs.get(paramInt));
    return this.gvs.get(paramInt);
  }
  
  public boolean isViewFromObject(View paramView, Object paramObject)
  {
    return paramView == paramObject;
  }
}
