package com.example.ship_Entity;

import android.graphics.Bitmap;

public class TalkEntity {
	
	//�Ƿ��ı�
	private Boolean isMsg = true;
	//�Ƿ��ɰ��Ϸ���
	private Boolean isSend = true;
	
	private String detail;
	private Bitmap image_picture;
	
	private String PostTime;
//	private String ark_id;
	private String send_user_id;
	
	private String msg_id;
	
	private int state = 1; // ����״̬��0Ϊ���ڷ��ͣ�1Ϊ���ͳɹ���2Ϊ����ʧ��
	public static final int MSG_SENDING = 0;
	public static final int MSG_SENT = 1;
	public static final int MSG_FAILED = 2;
	
//	public int index = -1;
	
	public TalkEntity(){
		super();
	}
	
	public TalkEntity(Boolean isMsg ,Boolean isSend
			,String detail
			,String PostTime 
//			,String ark_id 
			,String send_user_id
			,String msg_id){
		super();
		setAll(isMsg, isSend, detail, PostTime, send_user_id, msg_id);
	}
	
	public void setAll(Boolean isMsg ,Boolean isSend
			,String detail
			,String PostTime 
//			,String ark_id 
			,String send_user_id
			,String msg_id){
		this.isMsg = isMsg;
		this.isSend = isSend;
		this.detail = detail;
		this.PostTime = PostTime;
//		this.ark_id = ark_id;
		this.send_user_id = send_user_id;
		this.msg_id = msg_id;
		
	}
	
	public Boolean getIsMsg() {
		return isMsg;
	}
	public void setIsMsg(Boolean isMsg) {
		this.isMsg = isMsg;
	}
	public Boolean getIsSend() {
		return isSend;
	}
	public void setIsSend(Boolean isSend) {
		this.isSend = isSend;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Bitmap getImage_picture() {
		return image_picture;
	}

	public void setImage_picture(Bitmap image_picture) {
		this.image_picture = image_picture;
	}

	public String getPostTime() {
		return PostTime;
	}
	public void setPostTime(String postTime) {
		PostTime = postTime;
	}
	
	public void setSend_user_id(String send_user_id) {
		this.send_user_id = send_user_id;
	}
	public String getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public static int getMsgSending() {
		return MSG_SENDING;
	}
	public static int getMsgSent() {
		return MSG_SENT;
	}
	public static int getMsgFailed() {
		return MSG_FAILED;
	}

	public String getSend_user_id() {
		return send_user_id;
	}
}
