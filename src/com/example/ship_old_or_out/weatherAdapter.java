package com.example.ship_old_or_out;
//package com.example.ship_Adapter;
//
//import java.util.List;
//
//import com.example.ship2.R;
//import com.example.ship2.R.drawable;
//import com.example.ship2.R.id;
//import com.example.ship2.R.layout;
//import com.example.ship_Entity.weatherEntity;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//public class weatherAdapter extends BaseAdapter{
//
//	//define data
//	private List<weatherEntity> coll;
//	private LayoutInflater mInflater;
//	public weatherAdapter(Context context , List<weatherEntity> coll){
//		//get current page link data array
//		this.coll = coll;
//		
//		//get current page layout
//		mInflater = LayoutInflater.from(context);
//	}
//	
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return coll.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return coll.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent){
//		
//		weatherEntity entity = coll.get(position);
//		
//		ViewHolder viewHolder = null;
//		
//		if(convertView == null){
//			
//			//set current Adapter layout
//			convertView = mInflater.inflate(R.layout.weather_list,null);
//			
//			//save item id in here for easy using
//			viewHolder = new ViewHolder();
//			
//			viewHolder.weather_picture = (ImageView)convertView.findViewById(R.id.iv_weather_list_picture);
//			viewHolder.Type = (TextView)convertView.findViewById(R.id.tv_weather_list_type);
//			viewHolder.Title = (TextView)convertView.findViewById(R.id.tv_weather_list_title);
//			viewHolder.Msg = (TextView)convertView.findViewById(R.id.tv_weather_list_msg);
//			viewHolder.PostTime = (TextView)convertView.findViewById(R.id.tv_weather_postTime);
//			viewHolder.ll_weather_list = (LinearLayout)convertView.findViewById(R.id.ll_weather_list);
//			
//			//set Tag for current layout
//			convertView.setTag(viewHolder);
//		}
//		else{
//			viewHolder = (ViewHolder)convertView.getTag();
//		}
//
//		viewHolder.weather_picture.setImageBitmap(entity.getWeather_picture());
//		viewHolder.Type.setText(entity.getType());
//		viewHolder.Title.setText(entity.getTitle());
//		viewHolder.Msg.setText(entity.getMsg());
//		viewHolder.PostTime.setText(entity.getPostTime());
//		viewHolder.ll_weather_list.setBackgroundResource(R.drawable.bus_content_new);
//		
//		return convertView;
//	}
//	
//	static class ViewHolder{
//		
//		public ImageView weather_picture;
//		
//		public TextView Type;
//		
//		public TextView Title;
//		public TextView Msg;
//		public TextView PostTime;
//
//		public LinearLayout ll_weather_list;
//	}
//}
