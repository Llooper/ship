package com.example.shop_util;

import android.util.Log;

public final class LogHelper { 
    private static boolean mIsDebugMode = false;//��ȡ��ջ��Ϣ��Ӱ�����ܣ�����Ӧ��ʱ�ǵùر�DebugMode 
    private static String TAG = "main"; 
 
    private static final String CLASS_METHOD_LINE_FORMAT = "%s.%s()  Line:%d  (%s)"; 
 
    public static void trace(String str) { 
        if (mIsDebugMode) { 
            StackTraceElement traceElement = Thread.currentThread() 
                    .getStackTrace()[3];//�Ӷ�ջ��Ϣ�л�ȡ��ǰ�����õķ�����Ϣ 
            String logText = String.format(CLASS_METHOD_LINE_FORMAT, 
                    traceElement.getClassName(), traceElement.getMethodName(), 
                    traceElement.getLineNumber(), traceElement.getFileName()); 
            Log.d(TAG, logText + "->" + str);//��ӡLog 
        } 
    }
    
    public static void trace(String TAG,String str) { 
        if (mIsDebugMode) { 
            StackTraceElement traceElement = Thread.currentThread() 
                    .getStackTrace()[3];//�Ӷ�ջ��Ϣ�л�ȡ��ǰ�����õķ�����Ϣ 
            String logText = String.format(CLASS_METHOD_LINE_FORMAT, 
                    traceElement.getClassName(), traceElement.getMethodName(), 
                    traceElement.getLineNumber(), traceElement.getFileName()); 
            Log.d(TAG, logText + "->" + str);//��ӡLog 
        } 
    } 
}
