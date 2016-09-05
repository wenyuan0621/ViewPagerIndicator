package com.example.viewpagerindicatordemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VpSingleFragment extends Fragment{
	private String mTitle;
	
	private static final String BUNDLE_TITLE="title";
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle bundle=getArguments();
		if(bundle!=null){
			mTitle=bundle.getString(BUNDLE_TITLE);
		}
		
		TextView tv=new TextView(getActivity());
		tv.setText(mTitle);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}
	public static VpSingleFragment newInstence(String title){
		Bundle bundle=new Bundle();
		bundle.putString(BUNDLE_TITLE,title);
		VpSingleFragment fragment=new VpSingleFragment();
		fragment.setArguments(bundle);
		return fragment;
	}
	
	

}
