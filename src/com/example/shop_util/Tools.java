package com.example.shop_util;

import android.os.Environment;

public class Tools {
	public static boolean hasSdcard(){
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){
			return true ;
		}else{
			return false ;
		}
	}
}
