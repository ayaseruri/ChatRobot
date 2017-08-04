package uq.edu.au.chatroom.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import uq.edu.au.chatroom.bean.ChatInfo;
import uq.edu.au.chatroom.other.Json;

/**
 * Created by wufeiyang on 2017/8/3.
 */

public class NetSocket implements Runnable {

    private static final short SEND_SUC = 0;
    private static final short RECIVE_SUC = 1;
    private static final short ERROR = 2;

    private Socket mNetSocket;
    private BufferedReader mBufferedReader;
    private OutputStream mOutputStream;
    private String mTempContent;
    private Handler mHandler;

    private String mUrl;
    private int mPort;
    private OnMessage mOnMessage;

    public NetSocket(@NonNull String url, int port, @NonNull final OnMessage onMessage) {
        mUrl = url;
        mPort = port;
        mOnMessage = onMessage;

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SEND_SUC:
                        mOnMessage.onSendSuc((ChatInfo) msg.obj);
                        break;
                    case RECIVE_SUC:
                        mOnMessage.onReciveSuc((ChatInfo) msg.obj);
                        break;
                    case ERROR:
                        mOnMessage.onError((Exception) msg.obj);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void send(final ChatInfo chatInfo) {
        final StringBuilder content = new StringBuilder(Json.getGson().toJson(chatInfo));
        if (null == mOutputStream) {
            sendMeg(ERROR, new Exception("null == outputstream!"));
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mOutputStream.write(content.append("\r\n").toString().getBytes());
                        sendMeg(SEND_SUC, chatInfo);
                    } catch (IOException e) {
                        e.printStackTrace();
                        sendMeg(ERROR, e);
                    }
                }
            }).start();
        }
    }

    @Override
    public void run() {
        try {
            mNetSocket = new Socket(mUrl, mPort);
            mBufferedReader = new BufferedReader(new InputStreamReader(mNetSocket.getInputStream()));
            mOutputStream = mNetSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            sendMeg(ERROR, e);
        }

        try {
            while ((mTempContent = mBufferedReader.readLine()) != null) {
                ChatInfo chatInfo = Json.getGson().fromJson(mTempContent, ChatInfo.class);
                sendMeg(RECIVE_SUC, chatInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
           sendMeg(ERROR, e);
        }
    }

    private <T> void sendMeg(short type, T t) {
        Message message = mHandler.obtainMessage();
        message.what = type;
        message.obj = t;
        mHandler.sendMessage(message);
    }

    public interface OnMessage {
        void onSendSuc(ChatInfo chatInfo);
        void onReciveSuc(ChatInfo chatInfo);
        void onError(Exception e);
    }
}
