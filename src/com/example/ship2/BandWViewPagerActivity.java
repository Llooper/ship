/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.example.ship2;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import com.example.ship_Entity.BandWEntity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class BandWViewPagerActivity extends Activity {

	private final static boolean log = false;
	private final static String TAG = "BnWViewPager";
	
	private List<BandWEntity> ALL_List = new ArrayList<BandWEntity>();
	private List<BandWEntity> IMG_List = new ArrayList<BandWEntity>();
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ViewPager mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		setContentView(mViewPager);

		GlobalID globalID = (GlobalID)getApplication();
		
		if(null != savedInstanceState){
			globalID.start();
			this.finish();
		}
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		int index = data.getInt("i");
		int item = 0;
		
		ALL_List = globalID.BandWArrays;
		for(int i = 0;i<ALL_List.size();i++){
			BandWEntity entity = ALL_List.get(i);
			if(!entity.getList_type()){
				IMG_List.add(entity);
				if(i == index)item = IMG_List.size()-1;
			}
		}
		mViewPager.setAdapter(new SamplePagerAdapter(BandWViewPagerActivity.this, IMG_List));
		mViewPager.setCurrentItem(item);
	}

	static class SamplePagerAdapter extends PagerAdapter {
		
		private Context context;
		private List<BandWEntity> coll;

		@Override
		public int getCount() {
			return coll.size();
		}
		
		public SamplePagerAdapter(Context context , List<BandWEntity> coll){
			this.coll = coll;
			this.context = context;
		}
		
		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(context);
			photoView.setImageBitmap(coll.get(position).getPic());

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
	

	
	 @Override
	 public void onResume(){
		 super.onResume();
		 GlobalID globalID = ((GlobalID)getApplication());
		 globalID.cancel_notification();
//		 globalID.un_stop = false;
	}

	 @Override 
	 public void onSaveInstanceState(Bundle savedInstanceState) {  
		 // Save away the original text, so we still have it if the activity   
		 // needs to be killed while paused.
		super.onSaveInstanceState(savedInstanceState);
		GlobalID globalID = ((GlobalID)getApplication());
//		globalID.clear();
		globalID.create_notification("后台接收数据", "后台运行", "船客户端", false, false, false
				,BandWViewPagerActivity.class.getName());
	    if(globalID.toast != null)globalID.toast.cancel();
   	}


	 @Override  
	 public void onRestoreInstanceState(Bundle savedInstanceState) {  
	    super.onRestoreInstanceState(savedInstanceState);
//		GlobalID globalID = ((GlobalID)getApplication());
//	    String StrTest = savedInstanceState.getString("BandWArrays"); 
	    if(log)Log.e(TAG, "onRestoreInstanceState");
		GlobalID globalID = ((GlobalID)getApplication());
	    globalID.un_stop = false;
	}  
}
