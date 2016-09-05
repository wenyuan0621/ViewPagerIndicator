package com.example.viewpagerindicatordemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.view.ViewPagerIndicator;
import com.example.view.ViewPagerIndicator.PageOnChangeListener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class MainActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ViewPagerIndicator mIndicator;

	private List<String> mTitls = Arrays.asList("短信1", "收藏2", "推荐3", "短信4",
			"收藏5", "推荐6", "短信7", "收藏8", "推荐9");
	private List<VpSingleFragment> mContents = new ArrayList<VpSingleFragment>();
	private FragmentPagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initViews();
		initDatas();

		mIndicator.setVisibleTabCount(3);
		mIndicator.setTabItemTitles(mTitls);
		mViewPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mViewPager, 0);
		//如果需要对viewpager的滚动进行监听，可以通过这种方法
//		mIndicator.setOnPageChangeListener(new PageOnChangeListener() {
//			
//			@Override
//			public void onPageSelected(int position) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onPageScrolled(int position, float positionOffset,
//					int positionOffsetPixels) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onPageScrollStateChanged(int state) {
//				// TODO Auto-generated method stub
//				
//			}
//		});

	}

	private void initViews() {
		// TODO Auto-generated method stub
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mIndicator = (ViewPagerIndicator) findViewById(R.id.indicator);
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		for (String title : mTitls) {
			VpSingleFragment fragment = VpSingleFragment.newInstence(title);
			mContents.add(fragment);
		}
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mContents.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return mContents.get(arg0);
			}
		};
	}

}
