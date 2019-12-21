package com.smalljop.mytools.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.smalljop.mytools.utils.EnvironmentUtils;
import com.smalljop.mytools.utils.OkHttpUtil;

import java.io.IOException;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsReceiver";
    
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //判断广播消息
        if (action.equals(SMS_RECEIVED_ACTION)) {
            Bundle bundle = intent.getExtras();
            //如果不为空
            if (bundle != null) {
                //将pdus里面的内容转化成Object[]数组
                Object pdusData[] = (Object[]) bundle.get("pdus");// pdus ：protocol data unit  ：
                //解析短信
                SmsMessage[] msg = new SmsMessage[pdusData.length];
                for (int i = 0; i < msg.length; i++) {
                    byte pdus[] = (byte[]) pdusData[i];
                    msg[i] = SmsMessage.createFromPdu(pdus);
                }
                StringBuffer content = new StringBuffer();//获取短信内容
                StringBuffer phoneNumber = new StringBuffer();//获取地址
                //分析短信具体参数
                for (SmsMessage temp : msg) {
                    content.append(temp.getMessageBody());
                    phoneNumber.append(temp.getOriginatingAddress());
                }
                Map<String, String> params = Maps.newConcurrentMap();
                params.put("content", content.toString());
                OkHttpUtil.postDataAsync(EnvironmentUtils.getBaseUrl() + "/msg/add", new OkHttpUtil.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                    }
                }, params);
//                System.out.println("发送者号码：" + phoneNumber.toString() + "  短信内容：" + content.toString());
                //可用于发命令执行相应的操作
               /* if ("#*location*#".equals(content.toString().trim())){
                    abortBroadcast();//截断短信广播
                }else if ("#*alarm*#".equals(content.toString().trim())){
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.guoge);
                    //播放音乐
                    mediaPlayer.start();
                    abortBroadcast();//截断短信广播
                }else if ("#*wipe*#".equals(content.toString().trim())){
                    abortBroadcast();//截断短信广播
                }else if ("#*lockscreen*#".equals(content.toString().trim())){
                    abortBroadcast();//截断短信广播
                }*/
            }
        }
    }
}
