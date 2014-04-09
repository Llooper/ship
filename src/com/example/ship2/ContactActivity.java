package com.example.ship2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author test
 * ����ͨѶ¼���ý���
 */
public class ContactActivity extends Activity{

	private static final boolean log = true;
	private static final String TAG = "ContactActivity";
	protected boolean get_result = true;
	
	private String usernumber;
	private List<String> list;
	
	protected EditText edtSelectedContact;
	protected ImageView imvSelectedContact;

	private static final String IMAGE_FILE_NAME = "/faceImage.jpg";
	
	private static final int IMAGE_REQUEST_CODE = 0 ;
	private static final int CAMERA_REQUEST_CODE = 1 ;
	private static final int RESULT_REQUEST_CODE = 2 ;
	private static final int CONTACT_REQUEST_CODE = 3 ;

	protected boolean no_picture = true;
	/**
	 * ����ͨѶ¼����
	 */
	public void startContact(){
		Intent intent = new Intent(); 
		intent.setAction(Intent.ACTION_PICK); 
		intent.setData(ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(intent, CONTACT_REQUEST_CODE);
		list = new ArrayList<String>();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, 
			Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if(log)Log.v("ContactActivity","RESULT_OK");
//			try{
//				ContentResolver reContentResolverol = getContentResolver();
//				Uri contactData = data.getData();
//			
//				@SuppressWarnings("deprecation")
//				Cursor cursor = managedQuery(contactData, null, null, null, null);
//				cursor.moveToFirst();
//				String contactId = cursor.getString(cursor.getColumnIndex(
//						ContactsContract.Contacts._ID));
//				Cursor phone = reContentResolverol.query(ContactsContract.
//						CommonDataKinds.Phone.CONTENT_URI, null, 
//						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, 
//						null, null);
//				while (phone.moveToNext()) {
//					usernumber = phone.getString(phone.getColumnIndex(
//							ContactsContract.CommonDataKinds.Phone.NUMBER));
//					list.add(splitePhoneNumber(usernumber));
//				}
//				getNumber(list);
//				list = null;
//			}catch(NullPointerException e){
				if(log)Log.v("ContactActivity","RESULT_OK err: ");
				switch (requestCode){
				case CONTACT_REQUEST_CODE:
					try{

						ContentResolver reContentResolverol = getContentResolver();
						Uri contactData = data.getData();
					
						@SuppressWarnings("deprecation")
						Cursor cursor = managedQuery(contactData, null, null, null, null);
						cursor.moveToFirst();
						String contactId = cursor.getString(cursor.getColumnIndex(
								ContactsContract.Contacts._ID));
						Cursor phone = reContentResolverol.query(ContactsContract.
								CommonDataKinds.Phone.CONTENT_URI, null, 
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, 
								null, null);
						while (phone.moveToNext()) {
							usernumber = phone.getString(phone.getColumnIndex(
									ContactsContract.CommonDataKinds.Phone.NUMBER));
							list.add(splitePhoneNumber(usernumber));
						}
						getNumber(list);
						list = null;
					}catch(Exception e){
						if(log)Log.e(TAG,"CONTACT_REQUEST_CODE Exception: "+e);
						GlobalID globalID = (GlobalID)getApplication();
						if(globalID.toast != null)globalID.toast.cancel();
						globalID.toast = Toast.makeText(this, "��ϵ����Ϣ��ȡ����", Toast.LENGTH_LONG);
						globalID.toast.show();
					}
					break;
				case IMAGE_REQUEST_CODE:
					if(log)Log.v("ContactActivity","IMAGE_REQUEST_CODE");
					startPhotoZoom(data.getData());
					break;
				case CAMERA_REQUEST_CODE:
					if(log)Log.v("ContactActivity","CAMERA_REQUEST_CODE");
					if(Tools.hasSdcard()){
						File tempFile = new File(
								Environment.getExternalStorageDirectory() + IMAGE_FILE_NAME);
						startPhotoZoom(Uri.fromFile(tempFile));
//						startPhotoZoom(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME)));
					}else{
						Toast.makeText(ContactActivity.this, "δ�ҵ��洢�����޷��洢��Ƭ", Toast.LENGTH_LONG).show();
					}
					break;
				case RESULT_REQUEST_CODE:
					if(log)Log.v("ContactActivity","RESULT_REQUEST_CODE");
					if(data != null){
						getImageToView(data);
					}
					break;
					}
//			}
			}
//		if(resultCode != RESULT_CANCELED){
//			
//			}
	}
	
	public void startPhotoZoom(Uri uri){
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		
		intent.putExtra("crop", true);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		
		intent.putExtra("outputX", 90);
		intent.putExtra("outputY", 90);	
		intent.putExtra("return-data", true);
		startActivityForResult(intent , RESULT_REQUEST_CODE);
	}

	private void getImageToView(Intent data){
		Bundle extras = data.getExtras();
		if(extras != null ){
			Bitmap photo = extras.getParcelable("data");
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(photo);
			imvSelectedContact.setImageDrawable(drawable);
			if(log)Log.v("send", "has picture");
			no_picture = false;
		}
	}
	
	/**
	 * ��ȡ����ʾ�ֻ�����
	 * @param list
	 */
	public void getNumber(List<String> list){
		GlobalID globalID = ((GlobalID)getApplication());
		if(list != null && list.size() > 0){
			//ѭ�������ж��Ƿ����ֻ�����
			for(String str : list){
				if(!isMobileNO(str)){
					list.remove(str);
				}
			}
			int size = list.size();
			if(size == 1){//����ϵ����ֻ��һ���绰����
				//edt_input.setText(list.toString());
				for (String str : list) {
					edtSelectedContact.setText(str);
					//���ù��λ��
					edtSelectedContact.setSelection(str.length());
				}
			}else if(size > 1){//����ϵ�����ж���绰����
				String[] arr = (String[])list.toArray(new String[size]);
				showSingleDialog(arr, edtSelectedContact);
			}else{
				if(globalID.toast != null)globalID.toast.cancel();
				globalID.toast = Toast.makeText(this, "��ѡ����ȷ�ĵ绰����", Toast.LENGTH_LONG);
				globalID.toast.show();
			}
		}else{
			if(globalID.toast != null)globalID.toast.cancel();
			globalID.toast = Toast.makeText(this, "����ϵ����û���ֻ�����", Toast.LENGTH_LONG);
			globalID.toast.show();
		}
	}
	
	/**
	 * ��ȡ�ֻ�����
	 * �û��ֻ�����ǰ����17951����86
	 * @param number
	 * @return
	 */
	private String splitePhoneNumber(String number) {
		String mobile = "";
		if (number != null && number.length() >= 11) {
			mobile = number.substring(number.length() - 11);
		}
		return mobile;
	}
	
	 /**
     * @param mobile
     * @param edt
     * ��ʾ��ϵ�˵�ѡ�Ի���
     */
    private void showSingleDialog(final String[] mobile,final EditText edt){
    	AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("��ѡ����ϵ��");
		builder.setSingleChoiceItems(mobile, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				edt.setText(mobile[which]);
				//���ù��λ��
				edt.setSelection(mobile[which].length());
				dialog.cancel();
			}
		});
		builder.create().show();
    }
	
	/**
	 * ��֤�ֻ������ʽ�Ƿ���ȷ
	 * @param mobiles
	 * @return
	 */
	private boolean isMobileNO(String mobiles){ 
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"); 
		Matcher m = p.matcher(mobiles);  
		return m.matches();
	}
	
}
