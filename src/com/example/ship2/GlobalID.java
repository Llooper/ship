package com.example.ship2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.ship_Adapter.TalkAdapter;
import com.example.ship_Entity.BandWEntity;
import com.example.ship_Entity.TalkEntity;
import com.example.shop_util.FuntionUtil;
import com.example.shop_util.LogHelper;
import com.example.shop_util.SpUtil;
import com.example.shop_util.StringUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

//all global data change and set in here
public class GlobalID extends Application{
	
	private static final boolean log = true;
	private static final String TAG = "GlobalID";
	SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String ID = "";
	private String urlServer = "192.168.1.10";
	public final static String URL = "192.168.1.10";
	public static int SERVERPORT = 8300;
	private int TIMEOUT = 4*1000;
	private int m_rate = 100;

	Calendar sDate = Calendar.getInstance();
	Calendar eDate = Calendar.getInstance();
	private String startDate = "2013-01-01 00:00:00";
	private String endDate = s.format(eDate.getTime());
	
	public Toast toast = null;
	
	public Socket socket = new Socket();
	
	//code for all request just request
	private boolean login_code = false;
	private boolean break_code = false;
	private boolean heart_code = false;
	private boolean send_code = false;
	
	/**********old data*******************/
//	private boolean bus_code = false;
//	private boolean wea_code = false;
//	private boolean msg_code  = false;
//	private boolean img_code = false;
//	
//	private boolean bus_push = false;
//	private boolean wea_push = false;
//	private boolean msg_push  = false;
//	private boolean img_push = false;
//
//	private boolean bus_push_UnGet = false;
//	private boolean wea_push_UnGet = false;
//	private boolean msg_push_UnGet = false;
//	private boolean img_push_UnGet = false;
//	
//	private boolean bus_change = true;
//	private boolean wea_change = true;
//	private boolean msg_change  = true;
//	private boolean img_change = true;
//	
//	public List<bussinessEntity> BusDataArrays = new ArrayList<bussinessEntity>();
//	public List<bussinessEntity> BusDeleteArrays = new ArrayList<bussinessEntity>();
//	public List<weatherEntity> WeaDataArrays = new ArrayList<weatherEntity>();
//	public List<weatherEntity> WeaDeleteArrays = new ArrayList<weatherEntity>();
//	public List<TestEntity> MsgDataArrays = new ArrayList<TestEntity>();
//	public List<TestEntity> MsgDeleteArrays = new ArrayList<TestEntity>();
//	public List<imageEntity> ImgDataArrays = new ArrayList<imageEntity>();
//	public List<imageEntity> ImgDeleteArrays = new ArrayList<imageEntity>();
//	
//	private int BusUpdateLines = 0;
//	private int WeaUpdateLines = 0;
//	private int MsgUpdateLines = 0;
//	private int ImgUpdateLines = 0;
	/**********old data*******************/

	/**********new code*******************/
	private boolean BandW_code = false;
	private boolean Talk_code = false;

	private boolean BandW_push = false;
	private boolean Talk_push = false;

	private int BandW_push_UnGet = 0;
	private int Talk_push_UnGet = 0;

	private boolean BandW_change = false;
	private boolean Talk_change = false;
	
	public List<BandWEntity> BandWArrays = new ArrayList<BandWEntity>();
	public List<TalkEntity> TalkList = new ArrayList<TalkEntity>();
	
	private int BandWUpdateLines = 0;
	private int TalkUpdateLines = 0;
	/**********new data*******************/
	
	
	
	//设置接收推送
	private boolean check_push = true;
	private boolean talk_check_push = true;
	
	//Connect code
	public boolean isConnect = false;
	
	//waitting dialog
	private ProgressDialog pd = null;
	

	private boolean sound = false;
	private boolean shake = true;
	
	private ClipboardManager clipboard = null;
	
	private int current_code = -1;
	private int push = 0;
	
	//to set properties files
	Properties prop = new Properties();

	int notificationRef = 1;
	public Notification notification = null;
	NotificationManager notificationManager = null;
	// Text to display in the status bar when the notification is launched
	String tickerText = "后台接收数据";
	// Text to display in the extended status window
	String expandedText = "后台运行"; 
	// Title for the expanded status
	String expandedTitle = "船上客户端";
	String svcName = Context.NOTIFICATION_SERVICE;
	Intent intent = new Intent();
	int icon = R.drawable.shop;
	//
	static int cnt = 0;

	//is main going to stop
	public boolean un_stop = false;
	//recieve and heart thread
	Thread rec = new Thread(new recieve());
	Thread hea = new Thread(new heart());
	

	Button sTime = null; 
	Button eTime = null;
	Button selectAll = null;
	
	//is something sended
	public boolean is_sended = false;
	//pagerAdapter
	public com.example.ship2.NewMainActivity.MyPagerAdapter mpAdapter;
//	public MyPagerAdapter mpAdapter;
	
	@SuppressWarnings("deprecation")
	public void create_notification(String tickerText ,String expandedText,String expandedTitle
			,boolean light,boolean is_shake,boolean is_sound,String className){
		notificationManager = (NotificationManager)getSystemService(svcName);
		notificationManager.cancel(notificationRef);
		// Choose a drawable to display as the status bar icon 

		// The extended status bar orders notification in time order
		long when = System.currentTimeMillis();
		notification = new Notification(icon, tickerText, when);
		Context context = getApplicationContext();

		Log.v("notification","className = "+className);
		// Intent to launch an activity when the extended text is clicked
		try {
			intent = new Intent(getApplicationContext(),

			        Class.forName(className));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PendingIntent launchIntent = PendingIntent.getActivity(context, 0, intent, 0);
		notification.setLatestEventInfo(context, expandedTitle,expandedText,launchIntent); 
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		if(light)notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		if(is_shake && shake)notification.defaults |= Notification.DEFAULT_VIBRATE;
		if(is_sound && sound)notification.defaults |= Notification.DEFAULT_SOUND;
		notificationManager.notify(notificationRef, notification);
	}
	
	public void cancel_notification(){
		notificationManager = (NotificationManager)getSystemService(svcName);
		if(notification != null)notificationManager.cancel(notificationRef);
		notification = null;
		push = 0;
	}
	
	public void PD(Context context,String title,String msg){
		if(pd == null)pd = ProgressDialog.show(context, title, msg);
		if(log)Log.v("global","PD");
	}
	
	public void cancelPD(){
		if(pd != null)pd.dismiss();
		pd = null;
		if(log)Log.v("global","cancelPD");
	}
	
	public void dialog(Context context,String title,String msg){
		cancelPD();
		new AlertDialog.Builder(context).  
		setTitle(title).setMessage(msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				}  
			}).show();
	}
	
//	public void time_change(){
//		bus_change = true;
//		wea_change = true;
//		msg_change = true;
//		img_change = true;
//	}

	public Socket getSocket() {
//		if(log)Log.v("getSocket","here0");
		
		//if socket is connected just return the socket
		if(isConnect){
//			if(log)Log.v("getSocket","here???");
			return socket;
		}
		else{
			for(int un_work = 0 ; un_work < 2 ; un_work++){
//				if(log)Log.v("getSocket","here");
				setSocket();
//				if(log)Log.v("getSocket","here2");
//				for(long exitTime = 0;exitTime < TIMEOUT;exitTime += m_rate){
//					try {
//						Thread.sleep(m_rate);
//						if(socket.isConnected()){
//							if(log)Log.v("getSocket","socket.connected");
//							un_work = 2;
//							break;
//						}
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						if(log)Log.e("getSocket", "InterruptedException: "+e);
//					}
//				}
				if(socket.isConnected())break;
			}
			
			if(socket.isConnected()){
//				if(log)Log.v("getSocket","here3");
				isConnect = true;
				//start recieve and heart thread if not alive

				if(!rec.isAlive()){
//					if(log)Log.v("global","rec.die()");
					rec = new Thread(new recieve());
					rec.start();
				}
				else if(log)Log.v("global","rec.isAlive()");
				if(!hea.isAlive()){
//					if(log)Log.v("global","hea.die()");
					hea = new Thread(new heart());
					hea.start();
				}
				else if(log)Log.v("global","hea.isAlive()");				
				
				if(this.ID.equals("")){
					start();
					if(log)Log.v("getSocket","after start() id = " + ID);
				}
				try {
			        try {
			        	sendID();
                       	
                       	for(long exitTime = 0; exitTime < getTIMEOUT() ;exitTime += getM_rate()){
//				        	if(log)Log.v("login","for");
	                       	try {
								Thread.sleep(getM_rate());
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
				                if(log)Log.e("send id", "InterruptedException: " + e);
								continue;
							}
	                       	if(isLogin_code()){
	                       		break;
	                       	}
                       	}
            			
                       	//socket connected
                       	if(isLogin_code()){
	        				return socket;
                       	}
                       	else{
                       		return null;
                       	}
			        } catch(SocketTimeoutException e) {
		                if(log)Log.e("send id", "SocketTimeoutException: " + e);
		                return null;
		        }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
	                if(log)Log.e("getSocket", "IOException: " + e);
	                isConnect = false;
	                return null;
				}
			}
			else return null;
		}
	}

	private void sendID() throws IOException {
		// TODO Auto-generated method stub
    	int Uid_length = 20;
//    	int Uid_length = 4;
    	int pwd_length = 20;
    	int ver_length = 4;
    	int flag_length = 1;
    	int reserv_length = 8;
    	
    	int Len_total = StringUtil.HEAD_LENGTH
                   +Uid_length
                   +pwd_length
                   +ver_length
                   +flag_length
                   +reserv_length;
//    	if(log)Log.v("Len_total",":"+String.valueOf(Len_total));
    	int Cmd_id = 0x00040001;
    	int Seq_id = 0;
    	String Uid = new String(ID.getBytes("GB2312"),"8859_1");
    	
    	//no password now 2013-12-03
    	String pwd = new String(ID.getBytes("GB2312"),"8859_1");
    	int ver = 0;
    	char flag = '\0';
    	String reserv = "";
    	
    	int length = 0;
    	
    	byte[] send = new byte[Len_total];
    	length = FuntionUtil.send_head(length, send
    			, StringUtil.Len_total_length, Len_total
    			, StringUtil.Cmd_id_length, Cmd_id
    			, StringUtil.Seq_id_length, Seq_id);
    	
//    	if(log)Log.v("length","3:"+String.valueOf(length));
        for(int i = 0;i<Uid_length;i++){
        	if(i<Uid.length())
        		send[length+i] = (byte)(Uid.charAt(i));
//    			send[length+i] = (byte)(0 >> 24-i*8);
        }
        length += Uid_length;
//    	if(log)Log.v("length","4:"+String.valueOf(length));
        for(int i = 0;i<pwd_length;i++){
        	if(i<pwd.length())
        		send[length+i] = (byte)(pwd.charAt(i));
        }
        length += pwd_length;
//    	if(log)Log.v("length","5:"+String.valueOf(length));
        for(int i = 0;i<ver_length;i++){
            send[length+i] = (byte)(ver >> 24-i*8);
        }
        length += ver_length;
//    	if(log)Log.v("length","6:"+String.valueOf(length));
        for(int i = 0;i<flag_length;i++){
            send[length+i] = (byte)(flag);
        }
        length += flag_length;
//    	if(log)Log.v("length","7:"+String.valueOf(length));
        for(int i = 0;i<reserv_length;i++){
        	if(i<reserv.length())
            send[length+i] = (byte)(reserv.charAt(i));
        }
        length += reserv_length;
//    	if(log)Log.v("length","8:"+String.valueOf(length));
        
        OutputStream os = socket.getOutputStream();
       	os.write(send);
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	//a thread or just a funtion will be better??
	public void setSocket() {
		
		//no use 2013-12-04
//		Thread setsocket = new Thread(){
//			public void run(){
		FuntionUtil.doSth();
		
		try {
			InetSocketAddress socketAdd = new InetSocketAddress(urlServer, SERVERPORT);
			if(socket.isClosed()){
				if(log)Log.v("setSocket","new socket()");
				socket = new Socket();
			}
			socket.connect(socketAdd, TIMEOUT);
//			if(log)Log.v("setSocket","try connect ?");
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(log)Log.e("setSocket", "UnknownHostException: "+e);
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(log)Log.e("setSocket", "IOException: "+e);
			return;
		}
//			}
//		};
//		setsocket.start();
	}

//	public int getLine() {
//		return line;
//	}
//
//	public void setLine(int line) {
//		this.line = line;
//	}
//
//	public int getLine_gps() {
//		return line_gps;
//	}
//
//	public void setLine_gps(int line_gps) {
//		this.line_gps = line_gps;
//	}

	public void closeSocket(){
		if(log)Log.v(TAG,"closeSocket");
		isConnect = false;
		try {
			if(socket.isConnected()){
	        	int Uid_length = 20;
	        	int reserv_length = 8;
	        	
	        	int Len_total = StringUtil.HEAD_LENGTH
	                       +Uid_length
	                       +reserv_length;
	        	
	        	if(log)Log.v("Len_total",":"+String.valueOf(Len_total));
	        	int Cmd_id = 0x00040002;
	        	int Seq_id = 0;
	        	String Uid = new String(ID.getBytes("GB2312"),"8859_1");
	        	String reserv = "";
	
	        	int length = 0;
	        	byte[] send = new byte[Len_total];
	        	length = FuntionUtil.send_head(length, send, StringUtil.Len_total_length, Len_total
	        			, StringUtil.Cmd_id_length, Cmd_id, StringUtil.Seq_id_length, Seq_id);
	        	
	        	for(int i = 0;i<Uid_length;i++){
	            	if(i<Uid.length())
	            		send[length+i] = (byte)(Uid.charAt(i));
	            }
	            length += Uid_length;
	            
	            for(int i = 0;i<reserv_length;i++){
	            	if(i<reserv.length())
	            		send[length+i] = (byte)(reserv.charAt(i));
	            }
	            length += reserv_length;
	            
	            OutputStream os = socket.getOutputStream();
	            os.write(send);
	            
	            for( long exitTime = 0;exitTime < TIMEOUT/2 ;exitTime += m_rate){
					try {
						Thread.sleep(m_rate);
						if(log)Log.v("close", "sleep");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			       	if(break_code){
						if(log)Log.v("close", "break_code");
			       		break;
			       	}
				}
			}
            
			if(socket != null)socket.close();
			if(socket.isClosed()){
				if(log)Log.v("close", "isClosed");
				socket = new Socket();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(log)Log.e("socket close","IOException: "+e);
		}
	}
	
	//to set startDate and endDate maybe we use sqlite later
	public void setDate(){
		//set endDate to current date and startDate 
		this.setEndDate();
		this.setStartDate();
		/******
		 * 这里是把开始的时间定为当前时间的前一天
		 * 但是，会导致，查询不到信息，因为最近一天没有发，所以暂时去掉
		 * ****/
//		sDate.add(Calendar.DATE,-1);
//		this.startDate = s.format(sDate.getTime());
	}
	
	public void logout(){
		
		setDate();
		closeSocket();
		clear();
//        BusDataArrays.clear();
//        BusDeleteArrays.clear();
//        WeaDataArrays.clear();
//        WeaDeleteArrays.clear();
//        MsgDataArrays.clear();
//        MsgDeleteArrays.clear();
//        ImgDataArrays.clear();
//        ImgDeleteArrays.clear();
//        time_change();
	}
	
	public void clear() {
		//create file in externalStorageDirectory
		File fil = Environment.getExternalStorageDirectory();
		String url = createFolder(fil.getParent()+ File.separator +fil.getName(),"wingsofark");
		try {
			//调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
            //强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            OutputStream fos = new FileOutputStream(url+File.separator+"ship.properties");
            if(this.getID()!=null)prop.setProperty("ID", this.getID());
            prop.setProperty("sound", String.valueOf(this.isSound()));
            prop.setProperty("shake", String.valueOf(this.isShake()));
            if(this.getStartDate()!=null)prop.setProperty("startDate", this.getStartDate());
            if(this.getEndDate()!=null)prop.setProperty("endDate", this.getEndDate());
            
            if(this.getUrlServer()!=null)prop.setProperty("urlServer", this.getUrlServer());
            prop.setProperty("SERVERPORT", String.valueOf(this.getSERVERPORT()));
            prop.setProperty("TIMEOUT", String.valueOf(this.getTIMEOUT()));
            prop.setProperty("current_code", String.valueOf(this.getCurrent_code()));
            
            //以适合使用 load 方法加载到 Properties 表中的格式，
            //将此 Properties 表中的属性列表（键和元素对）写入输出流
            prop.store(fos, "");
            } catch (IOException e) {
            	e.printStackTrace();
    			if(log)Log.v("global", "out IOException" + e);
            }
	}
	
	public void start(){
		if(Build.VERSION.SDK_INT >= 11){
	        clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
	        }
        
		File fil = Environment.getExternalStorageDirectory();
		File file = new File(fil.getParent()+ File.separator +fil.getName()+"/wingsofark/ship.properties");
		
		//if file not exists create it here (in else)
		if(file.exists()){
			try {
				FileInputStream fin = new FileInputStream(file);
				prop.load(fin);
				if(prop.getProperty("ID")!=null)this.setID(prop.getProperty("ID"));
				if(prop.getProperty("sound")!=null)this.setSound(Boolean.parseBoolean(prop.getProperty("sound")));
				if(prop.getProperty("shake")!=null)this.setShake(Boolean.parseBoolean(prop.getProperty("shake")));
				if(prop.getProperty("startDate")!=null)this.setStartDate(prop.getProperty("startDate"));
				if(prop.getProperty("endDate")!=null)this.setEndDate(prop.getProperty("endDate"));
				
				if(prop.getProperty("urlServer")!=null)this.setUrlServer(prop.getProperty("urlServer"));
				if(prop.getProperty("SERVERPORT")!=null)this.setSERVERPORT(Integer.parseInt(prop.getProperty("SERVERPORT")));
				if(prop.getProperty("TIMEOUT")!=null)this.setTIMEOUT(Integer.parseInt(prop.getProperty("TIMEOUT")));
				if(prop.getProperty("current_code")!=null)this.setCurrent_code(Integer.parseInt(prop.getProperty("current_code")));
				
				try {
					Date date = s.parse(startDate);
					sDate.setTime(date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(log)Log.e("start()","pase Date err: " + e);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(log)Log.v("global", "in FileNotFoundException"+ e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(log)Log.v("global", "in IOException"+ e);
			}
		}
		
		else{
			String url = createFolder(fil.getParent()+ File.separator +fil.getName(),"wingsofark");
			try {
				//调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
	            //强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
	            OutputStream fos = new FileOutputStream(url+File.separator+"ship.properties");
	            if(this.getID()!=null)prop.setProperty("ID", this.getID());
	            prop.setProperty("sound", String.valueOf(this.isSound()));
	            prop.setProperty("shake", String.valueOf(this.isShake()));
	            if(this.getStartDate()!=null)prop.setProperty("startDate", this.getStartDate());
	            if(this.getEndDate()!=null)prop.setProperty("endDate", this.getEndDate());
	            
	            if(this.getUrlServer()!=null)prop.setProperty("urlServer", this.getUrlServer());
	            prop.setProperty("SERVERPORT", String.valueOf(this.getSERVERPORT()));
	            prop.setProperty("TIMEOUT", String.valueOf(this.getTIMEOUT()));
	            prop.setProperty("current_code", String.valueOf(this.getCurrent_code()));
	            //以适合使用 load 方法加载到 perties 表中的格式，
	            //将此 perties 表中的属性列表（键和元素对）写入输出流
	            prop.store(fos, "");
	            } catch (IOException e) {
	            	e.printStackTrace();
	    			if(log)Log.v("global", "out IOException" + e);
	            }
		}
	}
	
	public void start(Context context){
		String TAG = "start";
		
		if(Build.VERSION.SDK_INT >= 11){
	        clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
	        }
        
		try{
			ID = SpUtil.read(context,"ID");
		}catch(Exception e){
			e.printStackTrace();
			Log.v(TAG, "Exception "+ e);
		}
		
		try{
			sound = SpUtil.readBoolean(context,"sound");
		}catch(Exception e){
			e.printStackTrace();
			Log.v(TAG, "Exception "+ e);
		}

		try{
			shake = SpUtil.readBoolean(context,"shake");
		}catch(Exception e){
			e.printStackTrace();
			Log.v(TAG, "Exception "+ e);
		}

		try{
			getUrlServer(context);
		}catch(Exception e){
			e.printStackTrace();
			Log.v(TAG, "Exception "+ e);
		}
		try{
			String SERVERPORT = SpUtil.read(context,"SERVERPORT");
			if(SERVERPORT.equals("")){
				SERVERPORT = String.valueOf(StringUtil.SERVERPORT);
				SpUtil.write(context, SERVERPORT, "SERVERPORT");
			}
			this.SERVERPORT = Integer.parseInt(SERVERPORT);
		}catch(Exception e){
			e.printStackTrace();
			Log.v(TAG, "Exception "+ e);
		}

		try{
			String TIMEOUT = SpUtil.read(context,"TIMEOUT");
			if(TIMEOUT.equals("")){
				TIMEOUT = String.valueOf(StringUtil.TIMEOUT);
				SpUtil.write(context, TIMEOUT, "TIMEOUT");
			}
			this.TIMEOUT = Integer.parseInt(TIMEOUT);
		}catch(Exception e){
			e.printStackTrace();
			Log.v(TAG, "Exception "+ e);
		}
	}
	
	/*****************createFolder******************/
	public static String createFolder(String createUrl,String name) {

        String createFileROOT3 = createUrl + File.separator + name;
        // 创建文件
        File file = new File(createUrl);
        if (file.exists()) {
        	File fileROOT3 = new File(createFileROOT3);
        	fileROOT3.mkdirs();
        }
        System.out.println("Create documents directory success.");
        return createFileROOT3;
    }
	/*****************createFolder******************/
	

	/*****************recieve thread******************/
	public class recieve implements Runnable {
		
		private static final int AU_CONN_RESP = 0x80040001;
		private static final int AU_DISCONN_RESP = 0x80040002;
		private static final int AU_HB_RESP = 0x80040003;
		private static final int AU_MO_CUST_DATA_RESP = 0x80040004;
		private static final int AU_MT_CUST_DATA = 0x00040005;
		private static final int AU_SQL_RESP = 0x80040006;
		private static final int AU_SQL_RESULT = 0x00040007;
		private static final int AU_PUSH_TYPE = 0x00040008;
		
		
		private static final byte BandW = StringUtil.BandW;
		private static final byte Talk = StringUtil.Talk;
		private static final int BandW_PUSH = StringUtil.BandW_PUSH;
		private static final int Talk_PUSH = StringUtil.Talk_PUSH;
//		private static final byte text = 0x02;
//		private static final byte image = 0x03;
		

		@Override
		public void run() {
			// TODO Auto-generated method stub
//			if(log)Log.v("recieve", "start");
			byte[] line = new byte[32];
			int length;
			int Cmd_id;
	    	int Seq_id;
			int cnt = 10;
			int un_work = 0;
			long un_work_time = 0;
	    	
			while(true){
//				if(log)Log.v("recieve","unwork:"+String.valueOf(un_work));
				if(un_work>3){
					if(log)Log.v("recieve","unwork >3  "+String.valueOf(un_work));
					closeSocket();
					break;
				}
				if(socket.isClosed()){
					if(log)Log.v("recieve","socket isClosed");
					break;
				}
				if(socket.isConnected()){
					//自动推送测试
//					Talk_push = true;
//					BandW_push = true;
					
//					if(log)Log.v("recieve","socket isConnected");
					try{
						socket.setSoTimeout(TIMEOUT*30);
						InputStream is = socket.getInputStream();
						if(log)Log.v("recieve", "recieving");
						length = 0;
						Cmd_id = 0;
						Seq_id = 0;
						is.read(line,0,4);
						length = byte2int(line);
						if(log)Log.v("recieve", "length = "+String.valueOf(length));
						is.read(line,0,4);
						Cmd_id = byte2int(line);
//						if(log)Log.v("recieve", "Cmd_id = "+String.valueOf(Integer.toHexString(Cmd_id)));
						is.read(line,0,4);
						Seq_id = byte2int(line);
						
						if(length < StringUtil.HEAD_LENGTH){
							if(log)Log.v("recieve", "HEAD_LENGTH err");
							Thread.sleep(cnt*m_rate);
							cnt += 10;
							un_work++;
							continue;
						}
						if(cnt>0)cnt--;
						switch(Cmd_id){
						
						//connect resp
						case AU_CONN_RESP:{
							if(length == 16){
								is.read(line,0,4);
								int login_code = byte2int(line);
								if( login_code == 0 )setLogin_code(true);
								if(log)Log.v("recieve", "AU_CONN_RESP setLogin_code = "+String.valueOf(byte2int(line)));
							}
							else{
								if(log)Log.v("recieve", "AU_CONN_RESP error length = "+String.valueOf(length));
							}
						}break;
						
						//close socket resp
						case AU_DISCONN_RESP:{
							if(length == 16){
								is.read(line,0,4);
								int break_code = byte2int(line);
								if( break_code == 0 )setBreak_code(true);
								if(log)Log.v("recieve", "AU_DISCONN_RESP setBreak_code = "+String.valueOf(byte2int(line)));
							}
							else{
								if(log)Log.v("recieve", "AU_DISCONN_RESP error length = "+String.valueOf(length));
							}
						}break;
						
						//heart beat resp
						case AU_HB_RESP:{
							if(length == 16){
								is.read(line,0,4);
								int heart_code = byte2int(line);
								if( heart_code == 0 )setHeart_code(true);
								if(log)Log.v("recieve", "AU_HB_RESP setHeart_code = "+String.valueOf(heart_code));
							}
							else{
								if(log)Log.v("recieve", "AU_HB_RESP error length = "+String.valueOf(length));
							}
						}break;

						//send resp
						case AU_MO_CUST_DATA_RESP:{
							if(length == 16){
								is.read(line,0,4);
								int send_code = byte2int(line);
								if( send_code == 0 )setSend_code(true);
								if(log)Log.v("recieve", "AU_MO_CUST_DATA_RESP setSend_code = "+String.valueOf(byte2int(line)));
							}
							else{
								if(log)Log.v("recieve", "AU_MO_CUST_DATA_RESP error Send_code = "+String.valueOf(send_code));
							}
						}break;
						
						//recieve push
						case AU_MT_CUST_DATA:{
							if(length > 12){
								if(log)Log.v("recieve", "AU_MT_CUST_DATA");
//									is.read(line,0,4);
							}
							else{
								if(log)Log.v("recieve", "AU_MT_CUST_DATA length = "+String.valueOf(length));
							}
						}break;
						
						//SQL resp
						case AU_SQL_RESP:{
							if(length > 12){
								if(log)Log.v("recieve", "AU_SQL_RESP");
								is.read(line,0,4);
								int code = byte2int(line);
								if(code == 0)break;
								if(log)Log.e("AU_SQL_RESP", "err code : " + String.valueOf(code));
							}
						}break;
						
						//SQL recieve
						case AU_SQL_RESULT:{
							if(length > 12){
								if(log)Log.v("recieve", "AU_SQL_RESULT");
								is.read(line,0,1);
								byte type = (byte) line[0];
//								if(log)Log.v("recieve", "type = "+String.valueOf(type));
								int data_length = length - StringUtil.HEAD_LENGTH - 1;
								byte[] line1 = new byte[data_length];
								if(log)Log.v("AU_SQL_RESULT","data_length:"+String.valueOf(data_length));
								boolean Insert_Head = false;
								
								byte[] tmp = new byte[data_length];
				        		int start = 0;
				        		
				        		//no use 2013-12-03
//									while(true){
//					        			 if(start+32>data_length)break;
//					        			 is.read(line,0,32);
//					        			 copy(line,tmp,start,32);
//					        			 start += 32;
//									}
//									if(log)Log.v("AU_SQL_RESULT","start:"+String.valueOf(start));
//									if(log)Log.v("AU_SQL_RESULT","data_length%32:"+String.valueOf(data_length%32));
//					        		is.read(line, 0, data_length%32);
//					        		copy(line,tmp,start,data_length%32);
				        		
				        		//read all date
				        		int readcnt = 0;
				        		while(data_length>0){
				        			readcnt = is.read(line1,0,data_length);
				        			if(readcnt > 0){
					        			copy(line1,tmp,start,readcnt);
					        			data_length -= readcnt;
					        			start += readcnt;
				        			}
				        		}

				        		String reader = new String (tmp,"GB2312");
				        		if(log)Log.v("AU_SQL_RESULT","reader: "+reader);
				        		
				        		//use seq_id for insert_head select
				        		if(Seq_id == 0) Insert_Head = true;
								
				        		//i don't know what's synchronized and notify()				        		
								switch(type){
								
								//get bussiness
								case BandW:{
					        		if(log)Log.v("AU_SQL_RESULT","BandW: ");
//					        		synchronized(BusDataArrays){
//						        		BusJson(reader,Insert_Head,BusDataArrays,BusDeleteArrays);
//						        		BusDataArrays.notify();
//					        		}
					        		
					        		BandWJson(reader, Insert_Head, BandWArrays);
					        		setBandW_code(true);
								}break;

								case Talk:{
					        		if(log)Log.v("AU_SQL_RESULT","Talk: ");
//					        		synchronized(BusDataArrays){
//						        		BusJson(reader,Insert_Head,BusDataArrays,BusDeleteArrays);
//						        		BusDataArrays.notify();
//					        		}
					        		
					        		TalkJson(reader, Insert_Head, TalkList);
					        		setTalk_code(true);
								}break;
								
//								//get weather
//								case weather:{
//					        		if(log)Log.v("AU_SQL_RESULT","weather: ");
//					        		synchronized(WeaDataArrays){
//						        		WeaJson(reader,Insert_Head,WeaDataArrays,WeaDeleteArrays);
//						        		WeaDataArrays.notify();
//					        		}
//					        		setWea_code(true);
//								}break;
//								
//								//get text
//								case text:{
//					        		if(log)Log.v("AU_SQL_RESULT","text: ");
//					        		synchronized (MsgDataArrays) {
//						        		MsgJson(reader,Insert_Head,MsgDataArrays,MsgDeleteArrays);
//						        		MsgDataArrays.notify();
//									}
//					        		setMsg_code(true);
//								}break;
//								
//								//get image
//								case image:{
//					        		if(log)Log.v("AU_SQL_RESULT","image: ");
//					        		synchronized (ImgDataArrays) {
//						        		ImgJson(reader,Insert_Head,ImgDataArrays,ImgDeleteArrays);
//						        		ImgDataArrays.notify();
//					        		}
//					        		setImg_code(true);
//								}break;
								
								default:{
									if(log)Log.v("AU_SQL_RESP", "type error "+String.valueOf(type));
									
								}break;
								}
							}
							else{
								if(log)Log.v("recieve", "AU_SQL_RESP length error "+String.valueOf(length));
							}
						}break;
						case AU_PUSH_TYPE:{
							if(length > 12){
//								if(log)Log.v("recieve", "AU_PUSH_TYPE");
//									byte type = (byte) line[0];
								
								is.read(line, 0, 4);
								int type = byte2int(line);
								switch(type){
								//get bussiness
//								case 0:{
//					        		if(log)Log.v("AU_PUSH_TYPE","bussiness");
//					        		if(notification != null){
//					        			setBus_push_UnGet(true);
//						        		current_code = 0;
//					        			push++;
//					        			create_notification("1条新闻新信息", String.valueOf(push) + "条新信息", expandedTitle, true, true, true);
//					        		}
//					        		else {
//						        		setBus_push(true);
//					        		}
//								}break;
//								
//								//get weather
//								case 1:{
//					        		if(log)Log.v("AU_PUSH_TYPE","weather");
//					        		if(notification != null){
//					        			setWea_push_UnGet(true);
//						        		current_code = 1;
//					        			push++;
//					        			create_notification("1条天气新信息", String.valueOf(push) + "条新信息", expandedTitle, true, true, true);
//					        		}
//					        		else {
//						        		setWea_push(true);
//					        		}
//								}break;
//								
//								//get text
//								case 2:{
//					        		if(log)Log.v("AU_PUSH_TYPE","text");
//					        		if(notification != null){
//						        		if(log)Log.v("text_push","background");
//					        			setMsg_push_UnGet(true);
//						        		current_code = 2;
//					        			push++;
//					        			create_notification("1条文字新信息", String.valueOf(push) + "条新信息", expandedTitle, true, true, true);
//					        		}
//					        		else {
//						        		if(log)Log.v("text_push","no_background");
//						        		setMsg_push(true);
//					        		}
//								}break;
//								
//								//get image
//								case 3:{
//					        		if(log)Log.v("AU_PUSH_TYPE","image");
//					        		if(notification != null){
//						        		if(log)Log.v("img_push","background");
//					        			setImg_push_UnGet(true);
//						        		current_code = 3;
//					        			push++;
//					        			create_notification("1条图片新信息", String.valueOf(push) + "条新信息", expandedTitle, true, true, true);
//					        		}
//					        		else {
//						        		if(log)Log.v("img_push","no_background");
//						        		setImg_push(true);
//					        		}
//								}break;
								
								case BandW_PUSH:{
									if(log)Log.v("AU_PUSH_TYPE","BandW_PUSH");
									if(notification != null){
						        		if(log)Log.v("BandW_PUSH","background");
					        			setBandW_push_UnGet(BandW_push_UnGet++);
//						        		current_code = 3;
					        			push++;
					        			create_notification("1条新闻信息", String.valueOf(push) + "条新信息", expandedTitle, true, true, true
					        					,NewMainActivity.class.getName());
					        		}
					        		else {
						        		if(log)Log.v("BandW_PUSH","no_background");
						        		setBandW_push(true);
					        		}
								}break;
								
								case Talk_PUSH:{
									if(log)Log.v("AU_PUSH_TYPE","Talk_PUSH");
									if(notification != null){
						        		if(log)Log.v("Talk_PUSH","background");
					        			setTalk_push_UnGet(Talk_push_UnGet++);
//						        		current_code = 3;
					        			push++;
					        			create_notification("1条聊天新信息", String.valueOf(push) + "条新信息", expandedTitle, true, true, true
					        					,NewMainActivity.class.getName());
					        		}
					        		else {
						        		if(log)Log.v("Talk_PUSH","no_background");
						        		setBandW_push(true);
					        		}
								}break;
								
								default:{
									if(log)Log.v("AU_PUSH_TYPE", "type error "+String.valueOf(type));
									
								}break;
								}
								
//									switch(type){
//									//get bussiness
//									case bussiness:{
//						        		if(log)Log.v("AU_PUSH_TYPE","bussiness");
//						        		setBus_push(true);
//									}break;
//									
//									//get weather
//									case weather:{
//						        		if(log)Log.v("AU_PUSH_TYPE","weather");
//						        		setWea_push(true);
//									}break;
//									
//									//get text
//									case text:{
//						        		if(log)Log.v("AU_PUSH_TYPE","text");
//						        		setMsg_push(true);
//									}break;
//									
//									//get image
//									case image:{
//						        		if(log)Log.v("AU_PUSH_TYPE","image");
//						        		setImg_push(true);
//									}break;
//									
//									default:{
//										if(log)Log.v("AU_PUSH_TYPE", "type error "+String.valueOf(type));
//										
//									}break;
//									}
							}
						}break;
						
						default:{
							if(log)Log.v("recieve", "Cmd_id error "+String.valueOf(Cmd_id));
							is.read(line);
							try {
								Thread.sleep(cnt*m_rate);
								cnt += 10;
								un_work ++;
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							}
						}
					}catch(SocketException e){
						e.printStackTrace();
						if(log)Log.v("recieve", "SocketException err " + e);
						try {
							Thread.sleep(cnt*m_rate);
							cnt += 10;
							if((System.currentTimeMillis()-un_work_time) < 2*TIMEOUT){
								un_work_time = System.currentTimeMillis();
								un_work++;
								if(un_work>3){
									if(log)Log.v("recieve", "un_work >3 a");
									closeSocket();
									return;
								}
							}
							else{
								un_work_time = System.currentTimeMillis();
								un_work = 0;
							}
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							if(log)Log.v("recieve", "InterruptedException err " + e1);
							
							if((System.currentTimeMillis()-un_work_time) < 2*TIMEOUT){
								un_work_time = System.currentTimeMillis();
								un_work++;
								if(un_work>3){
									if(log)Log.v("recieve", "un_work >3 b");
									closeSocket();
									return;
								}
							}
							else{
								un_work_time = System.currentTimeMillis();
								un_work = 0;
							}
						}
						continue;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						if(log)Log.v("recieve", "IOException err " + e);
						
						if((System.currentTimeMillis()-un_work_time) < 2*TIMEOUT){
							un_work_time = System.currentTimeMillis();
							un_work++;
							if(un_work>3){
								if(log)Log.v("recieve", "un_work >3 c");
								closeSocket();
								return;
							}
						}
						else{
							un_work_time = System.currentTimeMillis();
							un_work = 0;
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						if(log)Log.v("recieve", "InterruptedException err " + e);
						
						if((System.currentTimeMillis()-un_work_time) < 2*TIMEOUT){
							un_work_time = System.currentTimeMillis();
							un_work++;
							if(un_work>3){
								if(log)Log.v("recieve", "un_work >3 c");
								closeSocket();
								return;
							}
						}
						else{
							un_work_time = System.currentTimeMillis();
							un_work = 0;
						}
					}
				}
				else break;
			}
		}
	}
	/***********Json for BandW list******************/
	public boolean BandWJson(String resultObj,boolean Insert_Head,List<BandWEntity> mDataArrays){
		boolean insert = false;
		BandWUpdateLines = 0;
		
		JSONArray jsonArray = new JSONArray();
		try {
			jsonArray = new JSONArray(resultObj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(jsonArray.length()==0){
			return false;
			}
		else{
			for(int i = jsonArray.length()-1 ; i > -1  ; i--){
				insert = false;
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject = jsonArray.getJSONObject(i);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				
				BandWEntity entity = new BandWEntity(
						//表示消息类型，1表示新闻，0表示天气
						jsonObject.optString("list_type").equals("1")
						, jsonObject.optString("Id")
						, jsonObject.optString("Title")
						, jsonObject.optString("Typeid")
						, Html.fromHtml(jsonObject.optString("Detail")).toString()
						, jsonObject.optString("Pic")
						
						//这里读取的字段名是"Time"但是在数据库里面的字段名其实是"PostTime"
						, jsonObject.optString("Time"));
				if(!entity.getList_type()){
					/************获取图片！*************/
					String pic = entity.getPicName();
//					if(log)Log.v("pic","http://"+ getDBurl() +"/user/images" + pic);
					byte[] pic_line = new byte[pic.length()];
					for(int j = 0;j<pic.length();j++){
						pic_line[j] = (byte)(pic.charAt(j));
		            }
					
					byte[] pic_line2 = new byte[pic_line.length/2];
	     			  for(int k = 0;k<pic_line2.length;k++){
	     				  pic_line2[k] = (byte) (pic_line[2*k]-(pic_line[2*k]<'A'?'0':pic_line[2*k]<'a'?'A'-10:'a'-10));

	     				  pic_line2[k] = (byte) ((pic_line2[k] << 4) & 0xF0);
	     				  pic_line2[k] += pic_line[2*k+1]-(pic_line[2*k+1]<'A'?'0':pic_line[2*k+1]<'a'?'A'-10:'a'-10);
	     			}
	  				if(getBitmapFromByte(pic_line2)!=null){
	  					entity.setPic(getBitmapFromByte(pic_line2));
	  				}
	  				else{
	  		 			Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.weather_preview);
	  					entity.setPic(good);
	  				}
					/************获取图片！*************/
				}
  					
				for(int j = BandWArrays.size()-1 ; j>-1 ; j--){
					if(entity.getId().equals(BandWArrays.get(j).getId())
							&& entity.getList_type() == BandWArrays.get(j).getList_type()){
						insert = true;
						break;
					}
				}
				
				if(insert)continue;
				
				Message msg = new Message();
				msg.obj = entity;
				if(Insert_Head)msg.what = 0;
				else msg.what = 1;
				add_BandW_handler.sendMessage(msg);
				
				BandWUpdateLines++;
			}
			return true;
		}
	}
	final Handler add_BandW_handler = new Handler(){
		@Override
		public void handleMessage (Message msg){
			if(msg.what==1)
				BandWArrays.add((BandWEntity) msg.obj);
			else 
				BandWArrays.add(0,(BandWEntity) msg.obj);
		}
	};
	
	public boolean TalkJson(String resultObj,boolean Insert_Head,List<TalkEntity> mDataArrays){
		boolean insert = false;
		TalkUpdateLines = 0;
		
		JSONArray jsonArray = new JSONArray();
		try {
			jsonArray = new JSONArray(resultObj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(jsonArray.length()==0){
			return false;
			}
		else{
			for(int i = 0; i < jsonArray.length()  ; i++){
				insert = false;
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject = jsonArray.getJSONObject(i);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				TalkEntity entity = new TalkEntity(
						//表示船消息类型，0表示文字，1表示图片
						jsonObject.optString("msg_type").equals("0")
						//表示船消息方向，0表示船往岸发数据，1表示岸往船发数据
						, jsonObject.optString("type").equals("1")
						
						//从html转换为string
						, Html.fromHtml(jsonObject.optString("msg")).toString()
						, jsonObject.optString("datetime_send")
//						, jsonObject.optString("ark_id")
						, jsonObject.optString("user_id")
						, jsonObject.optString("id"));
				if(log)Log.v(TAG,"TalkJson jsonObject.optString(type).equals(1)"+jsonObject.optString("type"));
				if(!entity.getIsMsg()){
					/************获取图片！*************/
					String pic = entity.getDetail();
//					if(log)Log.v("pic","http://"+ getDBurl() +"/user/images" + pic);
					byte[] pic_line = new byte[pic.length()];
					for(int j = 0;j<pic.length();j++){
						pic_line[j] = (byte)(pic.charAt(j));
		            }
					
					byte[] pic_line2 = new byte[pic_line.length/2];
	     			  for(int k = 0;k<pic_line2.length;k++){
	     				  pic_line2[k] = (byte) (pic_line[2*k]-(pic_line[2*k]<'A'?'0':pic_line[2*k]<'a'?'A'-10:'a'-10));

	     				  pic_line2[k] = (byte) ((pic_line2[k] << 4) & 0xF0);
	     				  pic_line2[k] += pic_line[2*k+1]-(pic_line[2*k+1]<'A'?'0':pic_line[2*k+1]<'a'?'A'-10:'a'-10);
	     			}
	  				if(getBitmapFromByte(pic_line2)!=null){
	  					entity.setImage_picture(getBitmapFromByte(pic_line2));
	  				}
	  				else{
	  		 			Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.shop);
	  					entity.setImage_picture(good);
	  				}
					/************获取图片！*************/
				}
  					
				for(int j = TalkList.size()-1 ; j>-1 ; j--){
					if(entity.getMsg_id().equals(TalkList.get(j).getMsg_id())
							&& entity.getIsMsg() == TalkList.get(j).getIsMsg()){
						insert = true;
						break;
					}
				}
				
				if(insert)continue;
				
				Message msg = new Message();
				msg.obj = entity;
				if(Insert_Head)msg.what = 1;
				else msg.what = 0;
				add_Talk_handler.sendMessage(msg);
				
				TalkUpdateLines++;
			}
			return true;
		}
	}
	final Handler add_Talk_handler = new Handler(){
		@Override
		public void handleMessage (Message msg){
			if(log)Log.v(TAG,"add_Talk_handler");
			if(msg.what==1)
				TalkList.add((TalkEntity) msg.obj);
			else 
				TalkList.add(0,(TalkEntity) msg.obj);
		}
	};
		
//	    private boolean BusJson(String resultObj,boolean Insert_Head,List<bussinessEntity> mDataArrays,List<bussinessEntity> mDataArrays2){
//	    	boolean insert = false;
//	    	BusUpdateLines = 0;
////	    	if(log)Log.v("bus json", ": "+ resultObj);
//			try{
//				JSONArray jsonArray = new JSONArray(resultObj);
//				if(jsonArray.length()==0){
//					return false;
//					}
//				else{
//						for(int i = 0 ; i < jsonArray.length() ; i++){
//							insert = false;
//							bussinessEntity entity = new bussinessEntity();
//							JSONObject jsonObject = jsonArray.getJSONObject(i);
//							entity.setId(jsonObject.optString("Id"));
//							entity.setBussiness_Title(jsonObject.optString("Title"));
//							entity.setBussiness_detail(jsonObject.optString("Content"));
//							entity.setBussiness_PostTime(jsonObject.optString("PostTime"));
//							entity.setBussiness_Poster(jsonObject.optString("Poster"));
//
//							for(int j = mDataArrays.size()-1 ; j>-1 ; j--){
//								if(entity.getId().equals(mDataArrays.get(j).getId())){
//									insert = true;
//									break;
//								}
//							}
//							for(int j = mDataArrays2.size()-1 ; j>-1 ; j--){
//								if(log)Log.v("bus", "delete " + String.valueOf(j));
//								if(entity.getId().equals(mDataArrays2.get(j).getId())){
//									insert = true;
//									break;
//								}
//							}
//							if(insert)continue;
//							if(Insert_Head)mDataArrays.add(0,entity);
//							else mDataArrays.add(entity);
//							BusUpdateLines++;
////							if(log)Log.v("bus json", "updateLines " + String.valueOf(BusUpdateLines));
//					}
//				}
//			}catch(JSONException e){
//				e.printStackTrace();
//				if(log)Log.v("bus json","JSONException " + e);
//				return false;
//			}
//			//set current time for next refresh
////			startTime = mDataArrays.get(mDataArrays.size()-1).getBussiness_PostTime();
//			if(log)Log.v("bus json", "done");
//			return true;
//		}
//	    /***********Json for bussiness list******************/
//	    
//	    /***********Json for weathersiness list******************/
//	    private boolean WeaJson(String resultObj,boolean Insert_Head,List<weatherEntity> mDataArrays,List<weatherEntity> mDataArrays2){
//	    	boolean insert = false;
//	    	WeaUpdateLines = 0;
//			try{
//				JSONArray jsonArray = new JSONArray(resultObj);
//				if(jsonArray.length()==0){
//					return false;
//					}
//				else{
//					for(int i =  0; i < jsonArray.length() ; i++){
//					insert = false;
//					weatherEntity entity = new weatherEntity();
//					JSONObject jsonObject = jsonArray.getJSONObject(i);
//					entity.setId(jsonObject.optString("Id"));
//					entity.setType(jsonObject.optString("Type"));
//					entity.setTitle(jsonObject.optString("Title"));
//					entity.setMsg(jsonObject.optString("Msg"));
//					entity.setPostTime(jsonObject.optString("PostTime"));
//					String img = jsonObject.optString("Imagefile");
//					byte[] img_line = new byte[img.length()];
//					for(int j = 0;j<img.length();j++){
////		            	if(i<Uid.length())
//						img_line[j] = (byte)(img.charAt(j));
//		            }
////					if(log)Log.v("line","length: "+img_line.length);
//					byte[] img_line2 = new byte[img_line.length/2];
//	     			  for(int k = 0;k<img_line2.length;k++){
//	     				  img_line2[k] = (byte) (img_line[2*k]-(img_line[2*k]<'A'?'0':img_line[2*k]<'a'?'A'-10:'a'-10));
//
//	     				  img_line2[k] = (byte) ((img_line2[k] << 4) & 0xF0);
//	     				  img_line2[k] += img_line[2*k+1]-(img_line[2*k+1]<'A'?'0':img_line[2*k+1]<'a'?'A'-10:'a'-10);
//	     			  }
//	  					if(getBitmapFromByte(img_line2)!=null){
//	  						entity.setWeather_picture(getBitmapFromByte(img_line2));
//	  					}
//	  	  		     		   else{
//	 	  		 				Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
//	 	  						entity.setWeather_picture(good);
//	 	  						}
////					if(log)Log.v("weather","json "+ entity.getPostTime());
//					
//					for(int j = mDataArrays.size()-1 ; j>-1 ; j--){
//						if(entity.getId().equals(mDataArrays.get(j).getId())){
//							insert = true;
//							break;
//						}
//					}
//					for(int j = mDataArrays2.size()-1 ; j>-1 ; j--){
//						if(entity.getId().equals(mDataArrays2.get(j).getId())){
//							insert = true;
//							break;
//						}
//					}
//					if(insert)continue;
//					if(Insert_Head)mDataArrays.add(0,entity);
//					else mDataArrays.add(entity);
//					WeaUpdateLines++;
//					}
//				}
//			}catch(JSONException e){
//				e.printStackTrace();
//				return false;
//			}
//			return true;
//		}
//	    /***********Json for weathersiness list******************/
//	    
//	    /***********Json******************/
//	    private boolean MsgJson(String resultObj,boolean Insert_Head,List<TestEntity> mDataArrays,List<TestEntity> mDataArrays2){
//	    	boolean insert = false;
//	    	MsgUpdateLines = 0;
//			try{
//				JSONArray jsonArray = new JSONArray(resultObj);
//				if(jsonArray.length()==0){
//					return false;
//					}
//				else{
//					for(int i = 0 ; i < jsonArray.length()  ; i++){
//						insert = false;
//						TestEntity entity = new TestEntity();
//						JSONObject jsonObject = jsonArray.getJSONObject(i);
//						entity.setMsg_id(jsonObject.optString("msg_id"));
//						entity.setTest_detail(jsonObject.optString("msg"));
//						entity.setTest_PostTime(jsonObject.optString("datetime_send"));
//						entity.setTest_ark_id(jsonObject.optString("ark_id"));
//						entity.setSend_user_id(jsonObject.optString("user_id"));
//						if(log)Log.v("msg_id","msg_id: "+ jsonObject.optString("msg_id"));
//						for(int j = mDataArrays.size()-1 ; j>-1 ; j--){
//							if(entity.getMsg_id().equals(mDataArrays.get(j).getMsg_id())){
//								insert = true;
//								break;
//							}
//						}
//						for(int j = mDataArrays2.size()-1 ; j>-1 ; j--){
//							if(entity.getMsg_id().equals(mDataArrays2.get(j).getMsg_id())){
//								insert = true;
//								break;
//							}
//						}
//						if(insert)continue;
//						
//						if(Insert_Head)mDataArrays.add(0,entity);
//						else mDataArrays.add(entity);
//						MsgUpdateLines++;
//					}
//				}
//				
//			}catch(JSONException e){
//				e.printStackTrace();
//			}
//			return true;
//		}
//	    /***********Json******************/
	    
//	    /***********Json for image list******************/
//	    private boolean ImgJson(String resultObj,boolean Insert_Head,List<imageEntity> mDataArrays,List<imageEntity> mDataArrays2){
//	    	boolean insert = false;
//	    	ImgUpdateLines = 0;
//			try{
//				JSONArray jsonArray = new JSONArray(resultObj);
//				if(jsonArray.length()==0){
//					return false;
//					}
//				else{
//					for(int i = 0 ; i < jsonArray.length()  ; i++){
//						insert = false;
//						imageEntity entity = new imageEntity();
//						JSONObject jsonObject = jsonArray.getJSONObject(i);
//						entity.setImg_id(jsonObject.optString("img_id"));
//						entity.setImage_datetime_send(jsonObject.optString("datetime_send"));
//						
//						//set where to download picture
//						String pic = jsonObject.optString("img_name");
////						if(log)Log.v("pic","http://"+ getDBurl() +"/user/images" + pic);
//						byte[] pic_line = new byte[pic.length()];
//						for(int j = 0;j<pic.length();j++){
//							pic_line[j] = (byte)(pic.charAt(j));
//			            }
//						
//						byte[] pic_line2 = new byte[pic_line.length/2];
//		     			  for(int k = 0;k<pic_line2.length;k++){
//		     				  pic_line2[k] = (byte) (pic_line[2*k]-(pic_line[2*k]<'A'?'0':pic_line[2*k]<'a'?'A'-10:'a'-10));
//
//		     				  pic_line2[k] = (byte) ((pic_line2[k] << 4) & 0xF0);
//		     				  pic_line2[k] += pic_line[2*k+1]-(pic_line[2*k+1]<'A'?'0':pic_line[2*k+1]<'a'?'A'-10:'a'-10);
//		     			  }
//		  					if(getBitmapFromByte(pic_line2)!=null){
//		  						entity.setImage_picture(getBitmapFromByte(pic_line2));
//		  					}
//		  	  		     		   else{
//		 	  		 				Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
//		 	  						entity.setImage_picture(good);
//		 	  						}
//
//						entity.setImage_ark_id(jsonObject.optString("ark_id"));
//						entity.setSend_user_id(jsonObject.optString("user_id"));
//						
//						for(int j = mDataArrays.size()-1 ; j>-1 ; j--){
//							if(entity.getImg_id().equals(mDataArrays.get(j).getImg_id())){
//								insert = true;
//								break;
//							}
//						}
//						for(int j = mDataArrays2.size()-1 ; j>-1 ; j--){
//							if(entity.getImg_id().equals(mDataArrays2.get(j).getImg_id())){
//								insert = true;
//								break;
//							}
//						}
//						if(insert)continue;
//						if(Insert_Head)mDataArrays.add(0,entity);
//						else mDataArrays.add(entity);
//						ImgUpdateLines++;
//						}
//					}
//			}catch(JSONException e){
//				e.printStackTrace();
//			}
//			return true;
//		}
//	    /***********Json for image list******************/

	/*****************recieve thread******************/

	public String BandW2String() {
		// TODO Auto-generated method stub
		BandWEntity entity = new BandWEntity();
    	String encode = "[";
		for(int i = 0 ; i < this.BandWArrays.size();){
			entity = this.BandWArrays.get(i);
			encode += "{";

			encode += "\"list_type\":\"" + entity.getList_type() + "\";";
			encode += "\"Id\":\"" + entity.getId() + "\";";
			encode += "\"Title\":\"" + entity.getTitle() + "\";";
			encode += "\"Typeid\":\"" + entity.getTypeid() + "\";";
			encode += "\"Detail\":\"" + entity.getDetail() + "\";";
			encode += "\"Pic\":\"" + entity.getPicName() + "\";";
			encode += "\"Time\":\"" + entity.getTime() + "\"";
			
			encode += "}";
			if(++i < this.BandWArrays.size()){
				encode += ",";
			}
		}
		encode += "]";
		if(log)LogHelper.trace(TAG, encode);
		return encode;
	}
	public String Talk2String() {
		// TODO Auto-generated method stub
    	TalkEntity entity = new TalkEntity();
    	String encode = "[";
		for(int i = 0 ; i < this.TalkList.size();){
			entity = this.TalkList.get(i);
			encode += "{";

			encode += "\"isMsg\":\"" + entity.getIsMsg() + "\";";
			encode += "\"isSend\":\"" + entity.getIsSend() + "\";";
			encode += "\"detail\":\"" + entity.getDetail() + "\";";
			encode += "\"PostTime\":\"" + entity.getPostTime() + "\";";
//			encode += "\"ark_id\":\"" + entity.getArk_id() + "\";";
			encode += "\"send_user_id\":\"" + entity.getSend_user_id() + "\";";
			encode += "\"msg_id\":\"" + entity.getMsg_id() + "\";";
			encode += "\"state\":\"" + entity.getState() + "\"";
			
			encode += "}";
			if(++i < this.TalkList.size()){
				encode += ",";
			}
		}
		encode += "]";
		if(log)LogHelper.trace(TAG, encode);
		return encode;
	}
	
	/**********************change byte[] to bitmap************************/
    public Bitmap getBitmapFromByte(byte[] temp){   
        if(temp != null){   
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);   
            return bitmap;
        }else{   
            return null;   
        }   
    } 
    /**********************change byte[] to bitmap************************/
    
    //copy String
    public boolean copy (byte[] src, byte[] dest,int start,int read_length){
    	for(int i = 0;i<read_length;i++){
    		dest[start+i] = src[i];
    	}
		return true;
    }
    
    //change byte to int
	public int byte2int (byte[] line){
		int length;
		length = line[3] & 0xFF;
		length |= ((line[2] << 8) & 0xFF00);
		length |= ((line[1] << 16) & 0xFF0000);
		length |= ((line[0] << 24) & 0xFF000000);
		return length;
	}
	
	/*****************heart thread******************/
	public class heart implements Runnable {
		int un_work = 0;
		@Override
		public void run() {
			OutputStream os;
			try {
				os = socket.getOutputStream();
			while(true){
				if(!socket.isConnected()){
					if(log)Log.v("heart", "die0");
					break;
				}
				if(log)Log.v("heart", "beating");
				Thread.sleep(40*1000);
				
				if(heart_code){
					if(log)Log.v("heart", "heart_code true");
					heart_code = false;
					continue;
				}

				else{
		        	int Uid_length = 20;
		        	int Len_total = StringUtil.HEAD_LENGTH
    	                       +Uid_length;
//		        	if(log)Log.v("heart","Len_total:"+String.valueOf(Len_total));
		        	int Cmd_id = 0x00040003;
		        	int Seq_id = 0;
		        	String Uid = new String(ID.getBytes("GB2312"),"8859_1");
		        	
		        	int length = 0;
		        	
		        	byte[] send = new byte[Len_total];
		        	length = FuntionUtil.send_head(length, send, StringUtil.Len_total_length, Len_total
		        			, StringUtil.Cmd_id_length, Cmd_id, StringUtil.Seq_id_length, Seq_id);
		        	
		        	for(int i = 0;i<Uid_length;i++){
		            	if(i<Uid.length())
		            		send[length+i] = (byte)(Uid.charAt(i));
		            }
			        length += Uid_length;
//			    	if(log)Log.v("heart","length final:"+String.valueOf(length));
			    	
	               	os.write(send);
	               	
	               	for(long exitTime = 0;exitTime < TIMEOUT;exitTime += m_rate){
		        		try {
//		               		if(log)Log.v("heart", "sleep");
		        			Thread.sleep(m_rate);
		        			
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                       	if(heart_code){
//		               		if(log)Log.v("heart", "heart_code true");
                       		break;
                       	}	
		        	}
	               	if(heart_code){
	               		un_work = 0;
	               		continue;
	               	}
	               	else{
	               		if(log)Log.v("heart", "die");
	               		un_work++;
	               		if(un_work>3){
	               			closeSocket();
	               			return;
	               		}
	               		else continue;
	               	}
				}
			}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
           		if(log)Log.v("heart", "die1: " +e1);
				e1.printStackTrace();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
           		if(log)Log.v("heart", "die2" + e1);
				e1.printStackTrace();
			}
		}
	}
	/*****************recieve thread******************/

//	public int send_head(int length,byte[] send,int Len_total_length,int Len_total,int Cmd_id_length,int Cmd_id,int Seq_id_length,int Seq_id){
//		for(int i = 0;i<Len_total_length;i++){
//            send[length + i] = (byte)(Len_total >> 24-i*8);
//        }
//        length += Len_total_length;
////    	if(log)Log.v("heart ","length1:"+String.valueOf(length));
//        for(int i = 0;i<Cmd_id_length;i++){
//            send[length + i] = (byte)(Cmd_id >> 24-i*8);
//        }
//        length += Cmd_id_length;
////    	if(log)Log.v("heart ","length2:"+String.valueOf(length));
//        for(int i = 0;i<Seq_id_length;i++){
//            send[length+i] = (byte)(Seq_id >> 24-i*8);
//        }
//        length += Seq_id_length;
////    	if(log)Log.v("heart ","length3:"+String.valueOf(length));
//		return length;
//	}
	
	public void selectDate(Context context,final Activity currentActivity){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater factory = LayoutInflater.from(context);
        final View textEntryView = factory.inflate(R.layout.inquire, null);
//            builder.setIcon(R.drawable.ic_launcher);
            builder.setTitle("选择查询范围");
            builder.setView(textEntryView);
            
            selectAll = (Button) textEntryView.findViewById(R.id.btn_inquire_selectAll);
            
            sTime = (Button) textEntryView.findViewById(R.id.btn_inquire_startTime); 
            eTime = (Button) textEntryView.findViewById(R.id.btn_inquire_endTime);
            
            sTime.setText(getStartDate());
            eTime.setText(getEndDate());
            
            selectAll.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					sTime.setText("2013-01-01 00:00:00");
					Calendar eDate = Calendar.getInstance();
					eTime.setText(eDate.get(Calendar.YEAR)+"-"+(eDate.get(Calendar.MONTH)+1)+"-"+eDate.get(Calendar.DATE));
				}
			});
            
            sTime.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					showDialog(0);
				}
			});
            
            eTime.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					showDialog(1);
				}
			});
            
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	
                		setStartDate(sTime.getText().toString());
                		setEndDate(eTime.getText().toString());
//                		time_change();
                		dialog.dismiss();
//                		
//            			if (currentActivity instanceof bussiness_message) {
//            				((bussiness_message) currentActivity).onResume();
//            				}
//            			if (currentActivity instanceof weather) {
//            		        ((weather) currentActivity).onResume();
//            		        }
//            			if (currentActivity instanceof Text_information) {
//            		        ((Text_information) currentActivity).onResume();
//            		        }
//            			if (currentActivity instanceof image_information) {
//            		        ((image_information) currentActivity).onResume();
//            		        }
                	}
                });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	dialog.dismiss();
                }
            });
            builder.create().show();
	}

	public void showCopyed(Context context){
		if(toast != null)toast.cancel();
		toast = Toast.makeText(context, "已经复制到粘贴板", Toast.LENGTH_LONG);
		toast.show();
	}
	
	public void toast(Context context,String msg){
		cancelPD();
		if(toast != null)toast.cancel();
		toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		toast.show();
	}

	/******************creat dialog for time selecting*******************************/

    public Dialog showDialog(int id) {
    	Dialog dialog = null;
    	switch (id) {
    	case 0:
    		dialog = new DatePickerDialog(
            this,
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                	if(month<9){
                    	if(dayOfMonth<10){
                        	sTime.setText(year + "-0" + (month+1) + "-0" + dayOfMonth);
                    	}
                    	else sTime.setText(year + "-0" + (month+1) + "-" + dayOfMonth);
                	}
                	else{
                		if(dayOfMonth<10){
                        	sTime.setText(year + "-" + (month+1) + "-0" + dayOfMonth);
                    	}
                    	else sTime.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                	}
                }
            }, 
            sDate.get(Calendar.YEAR), // 传入年份
            sDate.get(Calendar.MONTH), // 传入月份
            sDate.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
    		dialog.setTitle("选择开始日期");
        break;
    case 1:
        dialog = new DatePickerDialog(
            this,
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                	if(month<9){
                    	if(dayOfMonth<10){
                        	eTime.setText(year + "-0" + (month+1) + "-0" + dayOfMonth);
                    	}
                    	else eTime.setText(year + "-0" + (month+1) + "-" + dayOfMonth);
                	}
                	else{
                    	if(dayOfMonth<10){
                        	eTime.setText(year + "-" + (month+1) + "-0" + dayOfMonth);
                    	}
                    	else eTime.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                	}
                }
            },
            eDate.get(Calendar.YEAR), // 传入年份
            eDate.get(Calendar.MONTH), // 传入月份
            eDate.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
        dialog.setTitle("选择结束日期");
        break;
        }
    	return dialog;
    }
    /******************creat dialog for time selecting*******************************/
   
    public int getCurrent_code() {
		return current_code;
	}

	public void setCurrent_code(int current_code) {
		this.current_code = current_code;
	}


	public ClipboardManager getClipboard() {
		return clipboard;
	}

	public void setClipboard(ClipboardManager clipboard) {
		this.clipboard = clipboard;
	}

	public boolean isSound() {
		return sound;
	}

	public void setSound(boolean sound) {
		this.sound = sound;
	}

	public boolean isShake() {
		return shake;
	}

	public void setShake(boolean shake) {
		this.shake = shake;
	}

	public boolean isLogin_code() {
		return login_code;
	}

	public void setLogin_code(boolean login_code) {
		this.login_code = login_code;
	}

	public boolean isBreak_code() {
		return break_code;
	}

	public void setBreak_code(boolean break_code) {
		this.break_code = break_code;
	}

	public boolean isHeart_code() {
		return heart_code;
	}

	public void setHeart_code(boolean heart_code) {
		this.heart_code = heart_code;
	}

	public boolean isSend_code() {
		return send_code;
	}

	public void setSend_code(boolean send_code) {
		this.send_code = send_code;
	}

	public int getM_rate() {
		return m_rate;
	}

	public void setM_rate(int m_rate) {
		this.m_rate = m_rate;
	}

	public Toast getToast() {
		return toast;
	}
	
	public void setToast(Toast toast) {
		this.toast = toast;
	}
	

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setEndDate() {
		this.endDate = s.format(eDate.getTime());
	}

	public void setStartDate() {
		this.startDate = "2013-01-01 00:00:00";
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getUrlServer() {
		return urlServer;
	}

	public String getUrlServer(Context context) throws Exception {
		if(urlServer.equals("")){
				String urlServer = SpUtil.read(context,"urlServer");
			if(urlServer.equals("")){
				urlServer = StringUtil.URLSERVER;
				SpUtil.write(context, urlServer, "urlServer");
				Log.v(TAG + "getUrlServer","write UrlServer");;
			}
			this.urlServer = urlServer;
		}
		return urlServer;
	}

	public void setUrlServer(String urlServer) {
		this.urlServer = urlServer;
	}

	public int getSERVERPORT() {
		return SERVERPORT;
	}

	public void setSERVERPORT(int sERVERPORT) {
		SERVERPORT = sERVERPORT;
	}

	public int getTIMEOUT() {
		return TIMEOUT;
	}

	public void setTIMEOUT(int tIMEOUT) {
		TIMEOUT = tIMEOUT;
	}
	
	public boolean isCheck_push() {
		return check_push;
	}

	public void setCheck_push(boolean check_push) {
		this.check_push = check_push;
	}

	public List<BandWEntity> getBandWArrays() {
		return BandWArrays;
	}

	public void setBandWArrays(List<BandWEntity> bandWArrays) {
		BandWArrays = bandWArrays;
	}

	public List<TalkEntity> getTalkList() {
		return TalkList;
	}

	public void setTalkList(List<TalkEntity> talkList) {
		TalkList = talkList;
	}

	public boolean isBandW_code() {
		return BandW_code;
	}

	public void setBandW_code(boolean bandW_code) {
		BandW_code = bandW_code;
	}

	public boolean isTalk_code() {
		return Talk_code;
	}

	public void setTalk_code(boolean talk_code) {
		Talk_code = talk_code;
	}

	public boolean isBandW_push() {
		return BandW_push;
	}

	public void setBandW_push(boolean bandW_push) {
		BandW_push = bandW_push;
	}

	public boolean isTalk_push() {
		return Talk_push;
	}

	public void setTalk_push(boolean talk_push) {
		Talk_push = talk_push;
	}


	public boolean isBandW_change() {
		return BandW_change;
	}

	public void setBandW_change(boolean bandW_change) {
		BandW_change = bandW_change;
	}

	public boolean isTalk_change() {
		return Talk_change;
	}

	public void setTalk_change(boolean talk_change) {
		Talk_change = talk_change;
	}

	public int getBandWUpdateLines() {
		return BandWUpdateLines;
	}

	public void setBandWUpdateLines(int bandWUpdateLines) {
		BandWUpdateLines = bandWUpdateLines;
	}

	public int getTalkUpdateLines() {
		return TalkUpdateLines;
	}

	public void setTalkUpdateLines(int talkUpdateLines) {
		TalkUpdateLines = talkUpdateLines;
	}

	public int getBandW_push_UnGet() {
		return BandW_push_UnGet;
	}

	public void setBandW_push_UnGet(int bandW_push_UnGet) {
		BandW_push_UnGet = bandW_push_UnGet;
	}

	public int getTalk_push_UnGet() {
		return Talk_push_UnGet;
	}

	public void setTalk_push_UnGet(int talk_push_UnGet) {
		Talk_push_UnGet = talk_push_UnGet;
	}

	public boolean isTalk_check_push() {
		return talk_check_push;
	}

	public void setTalk_check_push(boolean talk_check_push) {
		this.talk_check_push = talk_check_push;
	}

}
