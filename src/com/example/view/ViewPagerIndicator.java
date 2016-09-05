package com.example.view;


import java.util.List;

import com.example.viewpagerindicatordemo.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewPagerIndicator extends LinearLayout{
	private Paint mPaint;
	private Path mPath;
	private int mTriangleWidth;//三角形的宽度
	private int mTriangleHeight;//三角形的高度
	private static final float RADIO_TRIANGLE_WIDTH=1/6f;
	//三角形底边的最大宽度
	private final int DIMENSION_TRIANGLE_WIDTH_MAX=(int) (getScreenWidth()/3*RADIO_TRIANGLE_WIDTH);
	private int mInitTranslationX;//初始化的偏移位置
	private int mTranslationX;//三角形移动时的偏移位置
	
	private int mTabVisibleCount;
	
	private static final int COUNT_DEFAULT_TAB=4;
	
	private List<String> mTitles;
	
	private static final int COLOR_TEXT_NORMAL=0x77ffffff;
	private static final int COLOR_TEXT_HIGHLIGH=0xffffffff;//高亮文本颜色
//	private static final int COLOR_TEXT_NORMAL=0x77ffffff;
	
	private ViewPager mViewPager;

	public ViewPagerIndicator(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}
	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		//获取可见tab的数量
		TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.ViewPagerIndicator);
		mTabVisibleCount=a.getInt(R.styleable.ViewPagerIndicator_visible_tab_count,COUNT_DEFAULT_TAB);
//		
		if(mTabVisibleCount<0){
			mTabVisibleCount=COUNT_DEFAULT_TAB;
		}
		a.recycle();
		//初始化画笔
		mPaint=new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.parseColor("#ffffff"));
		mPaint.setStyle(Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.save();
		canvas.translate(mInitTranslationX+mTranslationX,getHeight()+10);
		canvas.drawPath(mPath,mPaint);
		
		
		canvas.restore();
		super.dispatchDraw(canvas);
	}
	
	/**设置三角形的大小     适用于控件宽高来设置控件*/
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		mTriangleWidth=(int)(w/mTabVisibleCount*RADIO_TRIANGLE_WIDTH);//三角形控件的底边宽度 ==textview的1/6
		mTriangleWidth=Math.min(mTriangleWidth,DIMENSION_TRIANGLE_WIDTH_MAX);
		mInitTranslationX=w/mTabVisibleCount/2-mTriangleWidth/2;//宽度除以3除以2得到每个textview的宽度的一半，减去三角形底边宽度的一半
		
		/**初始化三角形*/
		initTriangle();
	}
	
	/**设置子view*/
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		int cCount=getChildCount();
		if(cCount==0)
			return ;
		for(int i=0;i<cCount;i++){
			View view=getChildAt(i);
			LinearLayout.LayoutParams lp=(LayoutParams) view.getLayoutParams();
			lp.weight=0;
			lp.width=getScreenWidth()/mTabVisibleCount;
			view.setLayoutParams(lp);
		}
		 setItemClickEvent();
		
	}
	/**获得屏幕宽度*/
	private int getScreenWidth() {
		// TODO Auto-generated method stub
		WindowManager wm=(WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics=new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}
	private void initTriangle() {
		// TODO Auto-generated method stub
		
		mTriangleHeight=mTriangleWidth/2;//三角形的高度
		
		mPath=new Path();
		mPath.moveTo(0, 0);
		mPath.lineTo(mTriangleWidth,0);
		mPath.lineTo(mTriangleWidth/2,-mTriangleHeight);
	}
	
	/**
	 * 指示器跟随手指进行滚动
	 * @param position
	 * @param offset
	 */
	public void scroll(int position, float offset) {
		// TODO Auto-generated method stub
		int tabWidth=getWidth()/mTabVisibleCount;
		mTranslationX=(int) (tabWidth*(offset+position));
		
		//容器移动，当tab处于移动至最后时
		if(position>=(mTabVisibleCount-2)&&offset>0&&getChildCount()>mTabVisibleCount){
			if(mTabVisibleCount!=1){
				this.scrollTo((position-(mTabVisibleCount-2))*tabWidth+(int)(tabWidth*offset), 0);
			}else{
				this.scrollTo(position*tabWidth+(int)(tabWidth*offset),0);
			}
			
		}
		invalidate();
		
	}
	
	public void setTabItemTitles(List<String> titles){
		if(titles!=null&&titles.size()>0){
			this.removeAllViews();
			mTitles=titles;
			for(String title:mTitles){
				addView(generateTextView(title));
			}
		}
		 setItemClickEvent();
	}
	
	/**
	 * 设置可见的tab数量
	 * @param count
	 * 
	 * 需要在setTabItemTitles之前调用，因为在generateTextView使用了mTabVisibleCount
	 */
	public void setVisibleTabCount(int count){
		mTabVisibleCount=count;
	}
	/**
	 * 根据title创建tab
	 * @param title
	 * @return
	 */
	private View generateTextView(String title) {
		// TODO Auto-generated method stub
		TextView tv=new TextView(getContext());
		LinearLayout.LayoutParams lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		lp.width=getScreenWidth()/mTabVisibleCount;
		tv.setText(title);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
		tv.setTextColor(COLOR_TEXT_NORMAL);
		tv.setLayoutParams(lp);
		return tv;
	}
	
	/**
	 * 设置关联的ViewPager
	 * @param viewPager
	 * @param pos
	 */
	public void setViewPager(ViewPager viewPager,int pos){
		mViewPager=viewPager;
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

				if(mListener!=null){
					mListener.onPageSelected(arg0);
				}
				highLighTextView(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				//
				scroll(arg0, arg1);
				if(mListener!=null){
					mListener.onPageScrolled(arg0, arg1, arg2);
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				if(mListener!=null){
					mListener.onPageScrollStateChanged(arg0);
				}
			}
		});
		
		mViewPager.setCurrentItem(pos);
		highLighTextView(pos);
	}
	
	public interface PageOnChangeListener{
		public void onPageScrolled(int position,float positionOffset,int positionOffsetPixels);
		public void onPageSelected(int position);
		public void onPageScrollStateChanged(int state);
	}
	
	public PageOnChangeListener mListener;
	public void setOnPageChangeListener(PageOnChangeListener listener){
		this.mListener=listener;
	}
	/**
	 * 重置tab文本颜色
	 */
	private void resetTextColor(){
		for(int i=0;i<getChildCount();i++){
			View view=getChildAt(i);
			if(view instanceof TextView){
				((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
			}
		}
	}
	/**
	 * 高亮某个tab的文本
	 * @param pos
	 */
	private void highLighTextView(int pos){
		resetTextColor();
		View view=getChildAt(pos);
		if(view instanceof TextView){
			((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGH);
		}
		
	}
	
	/**
	 * 设置tab的点击事件
	 */
	private void setItemClickEvent(){
		int cCount=getChildCount();
		for(int i=0;i<cCount;i++){
			final int j=i;
			View view=getChildAt(i);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mViewPager.setCurrentItem(j);
				}
			});
		}
	}
}
