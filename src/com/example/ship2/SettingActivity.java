package com.example.ship2;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingActivity extends Activity {
	private static final boolean log = false;
	private static final String TAG = "SettingActivity";
    private Vibrator vibrator=null;
	

	private static TextView 
//	btn_setting_ark_id,
	btn_setting_start,btn_setting_end,selectAll;
	private static LinearLayout 
//	ll_setting_ark_id,
	ll_setting_start,ll_setting_end;
	
	private static TextView talk_title;
	private static ImageButton title_bar_back,title_bar_gps;
	
//	private static RelativeLayout rl_setting_personal;
//	final String[] ArkNames = {"��̨118881","��̨12828 ","��ï81888 "};
	//set menu ID
	private static final int ITEM1 = Menu.FIRST ;
	private static final int ITEM2 = Menu.FIRST+1 ;
	private static final int ITEM3 = Menu.FIRST+2 ;
	private static final int ITEM4 = Menu.FIRST+3 ;
	private static final int ITEM5 = Menu.FIRST+4 ;
	private Calendar calendar = Calendar.getInstance();

    private ToggleButton TB_sound = null, TB_shake = null;

	private static boolean first_shake = false;

	private static final int SWIPE_MIN_DISTANCE = 120 ;
	private static final int SWIPE_MAX_OFF_PATH = 250 ;
	//pulling speed
	private static final int SWIPE_THRESHOLD_VELOCITY = 200 ;
	//
	private GestureDetector gestureDetector ;
	View.OnTouchListener gestureListener ;
	
	 @SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
	    	
	        super.onCreate(savedInstanceState);
	        
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

	        GlobalID globalID = ((GlobalID)getApplication());
			if(null != savedInstanceState){
				globalID.start();
			}
	        //no_title
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.setting);

	        vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
	        
	        setTitle();
//	        btn_setting_ark_id = (TextView)findViewById(R.id.btn_setting_ark_id);
	        btn_setting_start = (TextView)findViewById(R.id.btn_setting_start);
	        btn_setting_end = (TextView)findViewById(R.id.btn_setting_end);
//	        btn_setting_line = (TextView)findViewById(R.id.btn_setting_line);
	        first_shake = false;
//	        if(!globalID.getNow_ark().equals("-1")){
//	        	btn_setting_ark_id.setText("���� ("+globalID.getNow_ark()+")"+ArkNames[Integer.parseInt(globalID.getNow_ark())-1]);
//	         }
//	         else 
//	        	 btn_setting_ark_id.setText("û�л�ȡ����Ȩ��");
	        btn_setting_start.setText(globalID.getStartDate());
	        btn_setting_end.setText(globalID.getEndDate());
//	        btn_setting_line.setText("��ѯ "+globalID.getLine() + " ����¼");
	         
//	        ll_setting_ark_id = (LinearLayout)findViewById(R.id.ll_setting_ark_id);
	        ll_setting_start = (LinearLayout)findViewById(R.id.ll_setting_start);
	        ll_setting_end = (LinearLayout)findViewById(R.id.ll_setting_end);
//	        ll_setting_line = (LinearLayout)findViewById(R.id.ll_setting_line);
	        TB_sound = (ToggleButton)findViewById(R.id.TB_sound);
	        TB_shake = (ToggleButton)findViewById(R.id.TB_shake);

	        TB_sound.setOnCheckedChangeListener(listener);
	        TB_shake.setOnCheckedChangeListener(listener);
	        
	        
//	        rl_setting_personal = (RelativeLayout)findViewById(R.id.rl_setting_personal);
	        
	        OnClickListener clickListener = new OnClickListener(){
	 			public void onClick(View v) {
	 					try {
							SearchButtonProcess(v);
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.v("err1", "UnknownHostException: "+e);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.v("err2", "IOException: "+e);
						}
	 			}
	         };

//	         ll_setting_ark_id.setOnClickListener(clickListener);
	         ll_setting_start.setOnClickListener(clickListener);
	         ll_setting_end.setOnClickListener(clickListener);
//	         ll_setting_line.setOnClickListener(clickListener);
//	         rl_setting_personal.setOnClickListener(clickListener);
	         
	         gestureDetector = new GestureDetector(new MyGestureDetector());
	         gestureListener = new View.OnTouchListener() {
	 			
	 			@Override
	 			public boolean onTouch(View v, MotionEvent event) {
	 				// TODO Auto-generated method stub
	 				if(gestureDetector.onTouchEvent(event)){
	 					return true ;
	 				}
	 				return false;
	 			}
	 		}; 
	        
	 }
	 
	 private void setTitle() {
		// TODO Auto-generated method stub
				// TODO Auto-generated method stub
		talk_title = (TextView)findViewById(R.id.talk_title);
	    talk_title.setText("����");
	    title_bar_back = (ImageButton)findViewById(R.id.title_bar_back);
	    title_bar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	    title_bar_gps = (ImageButton)findViewById(R.id.title_bar_gps);
	    title_bar_gps.setVisibility(View.INVISIBLE);
	}

	@SuppressWarnings("deprecation")
	void SearchButtonProcess(View v) throws UnknownHostException, IOException {
//			if (ll_setting_ark_id.equals(v)) {
//				selectArk_id();
//			}
//			else 
				if (ll_setting_start.equals(v)) {
				showDialog(0);
			}
			else if (ll_setting_end.equals(v)) {
				showDialog(1);
			}
//			else if (ll_setting_line.equals(v)) {
//				selectLine();
//			}
//			else if (rl_setting_personal.equals(v)) {
//				Intent intent = new Intent(SettingActivity.this,register2.class);
//				startActivity(intent);
//			}
		}
	 
	 protected void onResume(){
			super.onResume();
			GlobalID globalID = ((GlobalID)getApplication());
	        TB_sound.setChecked(globalID.isSound());
	        TB_shake.setChecked(globalID.isShake());
//			if(!globalID1.getNow_ark().equals("-1")){
//				btn_setting_ark_id.setText("���� ("+globalID1.getNow_ark()+")"+ArkNames[Integer.parseInt(globalID1.getNow_ark())-1]);
//	        }
//	        else 
//	        	btn_setting_ark_id.setText("û�л�ȡ����Ȩ��");
	        btn_setting_start.setText(globalID.getStartDate());
	        btn_setting_end.setText(globalID.getEndDate());
//	        btn_setting_line.setText("��ѯ "+globalID1.getLine() + " ����¼");
			first_shake = true;
			return;
	    }
		
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
//			getMenuInflater().inflate(R.menu.main, menu);
			
			//add menu item
//			menu.add(0,ITEM1,0,"��ȡ������Ϣ");
//			menu.add(0,ITEM2,0,"GPS��Ϣ");
			menu.add(0,ITEM3,0,"��ʱ������");
//			menu.add(0,ITEM4,0,"����������");
			menu.add(0,ITEM5,0,"����");
			
			return true;
		}
	    
	    public boolean onOptionsItemSelected(MenuItem item) {
			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.login, menu);
			switch(item.getItemId()){
			//�˵���1��ѡ��
			case ITEM1 :
//				selectLine();
				break;
			case ITEM2 :
				break;
			//case 2 change a select date
			case ITEM3 :
				selectDate();
				break;
			//case 3 change a select ark_id
			case ITEM4 :
				break;
				//case 4 for setting
			case ITEM5 :
				break;
			}
			return true;
		}
	    
	    private void selectDate(){
	    	final GlobalID globalID = ((GlobalID)getApplication());
	    	AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
	        LayoutInflater factory = LayoutInflater.from(SettingActivity.this);
	        final View textEntryView = factory.inflate(R.layout.inquire, null);
//	            builder.setIcon(R.drawable.ic_launcher);
	            builder.setTitle("ѡ���ѯ��Χ");
	            builder.setView(textEntryView);
	            
	            selectAll = (Button) textEntryView.findViewById(R.id.btn_inquire_selectAll);
	            
	            btn_setting_start = (Button) textEntryView.findViewById(R.id.btn_inquire_startTime); 
	            btn_setting_end = (Button) textEntryView.findViewById(R.id.btn_inquire_endTime);
	            
	            btn_setting_start.setText(globalID.getStartDate());
	            btn_setting_end.setText(globalID.getEndDate());
	            
	            selectAll.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						btn_setting_start.setText("0-0-0");
						Calendar eDate = Calendar.getInstance();
						btn_setting_end.setText(eDate.get(Calendar.YEAR)+"-"+(eDate.get(Calendar.MONTH)+1)+"-"+eDate.get(Calendar.DATE));
					}
				});
	            
	            btn_setting_start.setOnClickListener(new OnClickListener() {
					
					@SuppressWarnings("deprecation")
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						showDialog(0);
					}
				});
	            
	            btn_setting_end.setOnClickListener(new OnClickListener() {
					
					@SuppressWarnings("deprecation")
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						showDialog(1);
					}
				});
	            
	            
	            builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
//	                	TV_sign.setText(et_asSign.getText().toString());
//	                TV_sign.setText(et_asSign.getText().toString());
	                	globalID.setStartDate(btn_setting_start.getText().toString());
	        			globalID.setEndDate(btn_setting_end.getText().toString());
	        			dialog.dismiss();
	        	        btn_setting_start = (TextView)findViewById(R.id.btn_setting_start);
	        	        btn_setting_end = (TextView)findViewById(R.id.btn_setting_end);
	        	        btn_setting_start.setText(globalID.getStartDate());
	        	        btn_setting_end.setText(globalID.getEndDate());
	                }
	            });

	            builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	dialog.dismiss();
	        	        btn_setting_start = (TextView)findViewById(R.id.btn_setting_start);
	        	        btn_setting_end = (TextView)findViewById(R.id.btn_setting_end);
	        	        btn_setting_start.setText(globalID.getStartDate());
	        	        btn_setting_end.setText(globalID.getEndDate());
	                }
	            });
	            builder.create().show();
	    }
	    
		
		/******************creat dialog for time selecting*******************************/
	    @Override
	    protected Dialog onCreateDialog(int id) {
	    	Dialog dialog = null;
	    	switch (id) {
	    	case 0:
	    		dialog = new DatePickerDialog(
	            this,
	            new DatePickerDialog.OnDateSetListener() {
	                public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
	                	if(month<9){
	                    	if(dayOfMonth<10){
	                    		btn_setting_start.setText(year + "-0" + (month+1) + "-0" + dayOfMonth);
	                    	}
	                    	else btn_setting_start.setText(year + "-0" + (month+1) + "-" + dayOfMonth);
	                	}
	                	else btn_setting_start.setText(year + "-" + (month+1) + "-" + dayOfMonth);
	                }
	            }, 
	            calendar.get(Calendar.YEAR), // �������
	            calendar.get(Calendar.MONTH), // �����·�
	            calendar.get(Calendar.DAY_OF_MONTH) // ��������
	        );
	    		dialog.setTitle("ѡ��ʼ����");
	        break;
	    case 1:
	        dialog = new DatePickerDialog(
	            this,
	            new DatePickerDialog.OnDateSetListener() {
	                public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
	                	if(month<9){
	                    	if(dayOfMonth<10){
	                    		btn_setting_end.setText(year + "-0" + (month+1) + "-0" + dayOfMonth);
	                    	}
	                    	else btn_setting_end.setText(year + "-0" + (month+1) + "-" + dayOfMonth);
	                	}
	                	else btn_setting_end.setText(year + "-" + (month+1) + "-" + dayOfMonth);
	                }
	            }, 
	            calendar.get(Calendar.YEAR), // �������
	            calendar.get(Calendar.MONTH), // �����·�
	            calendar.get(Calendar.DAY_OF_MONTH) // ��������
	        );
	        dialog.setTitle("ѡ���������");
	        break;
	    }
	    return dialog;
	}
	    /******************creat dialog for time selecting*******************************/
	    
	    OnCheckedChangeListener listener=new OnCheckedChangeListener(){

	        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	            ToggleButton toggleButton=(ToggleButton)buttonView;
	            GlobalID globalID = ((GlobalID)getApplication());
	            switch (toggleButton.getId()) {
	            case R.id.TB_sound:
	                if(isChecked){
	                    //����ָ����ģʽ������
	                    //��һ���������������е�һ��Ԫ���ǵȴ��೤��ʱ��������𶯣�
	                    //֮�󽫻��ǿ����͹ر��𶯵ĳ���ʱ�䣬��λΪ����
	                    //�ڶ����������ظ���ʱ��pattern�е��������������Ϊ-1���ʾ���ظ���
//	                    vibrator.vibrate(new long[]{1000,50,50,100,50}, -1);
//	                    tv1.setText("��������");
	                	Log.v(TAG, "sound true");
	                	globalID.setSound(true);
	                }else {
	                    //�ر���
	                	Log.v(TAG, "sound false");
	                	globalID.setSound(false);
	                }
	                break;
	            case R.id.TB_shake:
	                if(isChecked){
	                    //�����𶯣�������ָ����ʱ��
//	                    vibrator.vibrate(3500);
//	                    tv2.setText("��������");
	                	Log.v(TAG, "shake true");
	                	globalID.setShake(true);
	                	if(first_shake)vibrator.vibrate(2000);
	                	else first_shake = true;
	                }else {
	                    //�ر�����
	                	Log.v(TAG, "shake false");
	                	globalID.setShake(false);
	                	vibrator.cancel();
	                }
	                break;
	            }
	        }
	        
	    };
	    
	    @Override
		public void finish(){
			super.finish();
			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
		}
	    
	    /**********************MotionEvent*************************/
		class MyGestureDetector extends SimpleOnGestureListener{
			@Override
			public boolean onFling(MotionEvent e1 , MotionEvent e2 , float velocityX , 
					float velocityY){
				if(Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH){
					return false;
				}

				//��������
				if(e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY ){
					SettingActivity.this.finish();
					return true;
					}
				return false;
				}
		}
		
		public boolean dispatchTouchEvent(MotionEvent event){
			if(gestureDetector.onTouchEvent(event)){
				event.setAction(MotionEvent.ACTION_CANCEL);
			}
			return super.dispatchTouchEvent(event);
		}
		/**********************MotionEvent*************************/
		
		@Override 
	    public void onSaveInstanceState(Bundle savedInstanceState) {  
	    	// Save away the original text, so we still have it if the activity   
	    	// needs to be killed while paused.
	    	super.onSaveInstanceState(savedInstanceState);
	    	GlobalID globalID = ((GlobalID)getApplication());
	    	globalID.create_notification("��̨��������", "��̨����", "���ͻ���", false, false, false,SettingActivity.class.getName());
		    if(globalID.toast != null)globalID.toast.cancel();
	    	}
		
		@Override  
		public void onRestoreInstanceState(Bundle savedInstanceState) {  
		    super.onRestoreInstanceState(savedInstanceState);
//			GlobalID globalID = ((GlobalID)getApplication());
//		    String StrTest = savedInstanceState.getString("BandWArrays"); 
		    if(log)Log.e(TAG, "onRestoreInstanceState");
			GlobalID globalID = ((GlobalID)getApplication());
		    globalID.un_stop = false;
		} 

}
