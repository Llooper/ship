package com.example.shop_util;

import java.text.SimpleDateFormat;

public class StringUtil {

	public static final String FILENAME = "data_info";
	
//	public static final String URLSERVER = "59.39.61.50";
	public static final String URLSERVER = "192.168.1.10";
//	public static final String DBURL = "www.wingsofark.com:8101";
//	public static final String DBURL = urlServer;

	public static final int SERVERPORT = 8102;
	public static final int TIMEOUT = 4000;
	public static final int M_RATE = 100;
	
	public static final int LINE = 5;

	public static final SimpleDateFormat SIMPLE_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat YYMMDD = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static final SimpleDateFormat MMDD = new SimpleDateFormat("MM-dd HH:mm");
	public static final SimpleDateFormat HHMM = new SimpleDateFormat("HH:mm");	
	public static final SimpleDateFormat SEND_DATEFORMAT = new SimpleDateFormat("yyMMddHHmmss");

	public static final int CLICK = 0;
	public static final int LONG = 1;
	public static final int FAIL = 2;

	public static final int Len_total_length = 4;
	public static final int Cmd_id_length = 4;
	public static final int Seq_id_length = 4;
	public static final int HEAD_LENGTH = Len_total_length+Cmd_id_length+Seq_id_length;
	
	public static final int AU_SQL = 0x00040006;
	public static final int AU_MO_CUST_DATA = 0x00040004;
	
	public static final byte BandW = 0x05;
	public static final byte Talk = 0x06;
	public static final int BandW_PUSH = 4;
	public static final int Talk_PUSH = 5;
}
