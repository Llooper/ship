package com.example.shop_util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

public class FuntionUtil {
	
	private static final boolean log = false;
	
	public static String toHTMLString(String in) {
        StringBuffer out = new StringBuffer();
        for (int i = 0; in != null && i < in.length(); i++) {
            char c = in.charAt(i);
            if (c == '\'')
                out.append("&#039;");
            else if (c == '\"')
                out.append("&#034;");
            else if (c == '<')
                out.append("&lt;");
            else if (c == '>')
                out.append("&gt;");
            else if (c == '&')
                out.append("&amp;");
            else if (c == ' ')
                out.append("&nbsp;");
            else if (c == '\n')
                out.append("<br/>");
            else
                out.append(c);
        }
        return out.toString();
    }
	
	public static void doSth(){
		
		// ÅÐ¶ÏAPI>=11
		if(Build.VERSION.SDK_INT >= 11){
			/********************very important for android 4.0(and above) to connect internet************************/
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			.detectDiskReads().detectDiskWrites().detectNetwork()
			.penaltyLog().build());
			
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
			.build());
			/********************very important for android 4.0(and above) to connect internet************************/
	        }
	}
	
	/***********down picture from urlPic******************/
    public static Bitmap downloadPic(String urlPic){
		try {
			doSth();
			URL url = new URL(urlPic);
			InputStream is = url.openStream();
			Bitmap bit = BitmapFactory.decodeStream(is);
			return bit ;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    /***********down picture from URL******************/
	
	public static int send_head(int length,byte[] send,int Len_total_length,int Len_total,int Cmd_id_length,int Cmd_id,int Seq_id_length,int Seq_id){
		for(int i = 0;i<Len_total_length;i++){
            send[length + i] = (byte)(Len_total >> 24-i*8);
        }
        length += Len_total_length;
//    	Log.v("heart ","length1:"+String.valueOf(length));
        for(int i = 0;i<Cmd_id_length;i++){
            send[length + i] = (byte)(Cmd_id >> 24-i*8);
        }
        length += Cmd_id_length;
//    	Log.v("heart ","length2:"+String.valueOf(length));
        for(int i = 0;i<Seq_id_length;i++){
            send[length+i] = (byte)(Seq_id >> 24-i*8);
        }
        length += Seq_id_length;
//    	Log.v("heart ","length3:"+String.valueOf(length));
		return length;
	}
	
	public static int byte2int (int answer,byte[] line){
		answer = line[15] & 0xFF;
        answer |= ((line[14] << 8) & 0xFF00);
        answer |= ((line[13] << 16) & 0xFF0000);
        answer |= ((line[12] << 24) & 0xFF000000);
		return answer;
	}
	
	public static byte[] HexStrToBin(byte[] hexStr)
	{
		int len = hexStr.length;
		byte[] bin = new byte[len/2];
		if(null == bin || null == hexStr) {
			return bin;
		}
		int lenBin = 0;
		int index = 0;
		while(index<len) {
			bin[lenBin] = (byte) (hexStr[index]-(hexStr[index]<'A'?'0':hexStr[index]<'a'?'A'-10:'a'-10));
			bin[lenBin] = (byte) ((bin[lenBin] << 4) & 0xF0);
			++index;
			
			bin[lenBin] += hexStr[index]-(hexStr[index]<'A'?'0':hexStr[index]<'a'?'A'-10:'a'-10);
			++index;
			
			++lenBin;
			if(log)Log.v("send", "HexStrToBin for index: "+String.valueOf(index));
		}
		if(log)Log.v("send", "HexStrToBin done.");

		return bin;
	}
	
	public static int send_body_first(int length,byte[] send,int time_send_length,byte[] time_send2
			,int senderId_length,int senderMobile_length
			,int senderMobileLen_length,String senderMobile,int grade
			,int grade_length,short option,int option_length,int cntRecver
			,int cntRecver_length,int recverLen,int recverLen_length
			,int recver_length,String send_id,int cntGrp_length
			,int data_type,int data_type_length,int len_data_length
			,int data_length){
		for(int i = 0;i<time_send_length;i++){
			send[length + i] = time_send2[i];
			//if(log)Log.v("length",String.valueOf(i)+" time_send16:"+Integer.toHexString(send[length + i]));
			//if(log)Log.v("length",String.valueOf(i)+" time_send2:"+Integer.toBinaryString(send[length + i]));
			//if(log)Log.v("length",String.valueOf(i)+" time_send10:"+Integer.toOctalString(send[length + i]));
			//if(log)Log.v("length",String.valueOf(i)+" time_send:"+String.valueOf(send[length + i]));
		}
		length += time_send_length;
		if(log)Log.v("length","time_send_length:"+String.valueOf(length));
		
		for(int i = 0;i<senderId_length;i++){
			send[length+i] = (byte)(0 >> 24-i*8);
		}
		length += senderId_length;
		//if(log)Log.v("length","5:"+String.valueOf(length));

		send[length] = (byte)senderMobile_length;
		length += senderMobileLen_length;
		if(log)Log.v("send", "mobile_length: " + String.valueOf(senderMobile_length));
		//if(log)Log.v("length","6:"+String.valueOf(length));

		for(int i = 0;i<senderMobile_length;i++){
			send[length+i] = (byte) senderMobile.charAt(i);
		}
		length += senderMobile_length;
		if(log)Log.v("send", "mobile: " + senderMobile);
		//if(log)Log.v("length","7:"+String.valueOf(length));

		send[length] = (byte)grade;
		length += grade_length;
		if(log)Log.v("send", "grade_length: " + String.valueOf(grade));
		//if(log)Log.v("length","8:"+String.valueOf(length));

		send[length] = (byte) ((option >> 8) & 0xff);
		send[length + 1] = (byte) (option & 0xff);
		if(log)Log.v("send", "option: " + String.valueOf(send[length]));
		//if(log)Log.v("send", "option1: " + String.valueOf(send[length+1]));
		length += option_length;
		//if(log)Log.v("length","9:"+String.valueOf(length));

		send[length] = (byte) cntRecver;
		length += cntRecver_length;
		//if(log)Log.v("length","10:"+String.valueOf(length));

		send[length] = (byte) recverLen;
		length += recverLen_length;
		//if(log)Log.v("length","11:"+String.valueOf(length));

		for(int i = 0;i<recver_length;i++){
			send[length+i] = (byte) send_id.charAt(i);
		}
		length += recver_length;
		if(log)Log.v("length","recver_length:"+String.valueOf(recver_length));

		send[length] = 0;
		length += cntGrp_length;
		//if(log)Log.v("length","12:"+String.valueOf(length));

		send[length] = (byte) data_type;
		length += data_type_length;
		//if(log)Log.v("length","13:"+String.valueOf(length));

		for(int i = 0;i<len_data_length;i++){
			send[length+i] = (byte)(data_length >> 24-i*8);
		}
		length += len_data_length;
		if(log)Log.v("send", "data_length: " + String.valueOf(data_length));
		//if(log)Log.v("length","14:"+String.valueOf(length));
		return length;
	}
}
