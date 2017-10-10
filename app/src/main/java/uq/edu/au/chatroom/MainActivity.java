package uq.edu.au.chatroom;

import java.util.Random;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import uq.edu.au.chatroom.bean.ChatInfo;
import uq.edu.au.chatroom.net.NetSocket;
import uq.edu.au.chatroom.ui.ChatList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NetSocket.OnMessage {

    private String mIp = "";
    private EditText mEditText;
    private NetSocket mNetSocket;
    private ChatList mChatList;
    private Thread mNetThread;
    private String mNicks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] nicks = getResources().getStringArray(R.array.nicks);
        mNicks = nicks[new Random().nextInt(nicks.length)];
        if (BuildConfig.DEBUG) {
            mIp = "172.18.37.64";
//            setIp();
        }

        setSocket();

        mChatList = (ChatList) findViewById(R.id.chat_list);
        mChatList.setOnVote(new ChatList.OnVote() {
            ChatInfo chatInfo = new ChatInfo();
            @Override
            public void onAddVote(String answerId) {
                chatInfo.setAnswerId(answerId);
                chatInfo.setAdd(true);
                mNetSocket.send(chatInfo);
            }

            @Override
            public void onDelVote(String answerId) {
                chatInfo.setAnswerId(answerId);
                chatInfo.setDel(true);
                mNetSocket.send(chatInfo);
            }
        });
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.chat_with_robort:
                        mChatList.changeMode(ChatList.MODE_ROBOT);
                        return true;
                    case R.id.chat_with_other_people:
                        mChatList.changeMode(ChatList.MODE_CHAT_ROOM);
                        return true;
                }
                return false;
            }
        });

        mEditText = (EditText) findViewById(R.id.edit);
        Button sendButton = (Button) findViewById(R.id.send);

        sendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String sendText = mEditText.getText().toString();
        if (!TextUtils.isEmpty(sendText) && null != mNetSocket) {
            ChatInfo chatInfo = new ChatInfo();
            chatInfo.setNick(mChatList.getMode() == ChatList.MODE_CHAT_ROOM ? mNicks : "Me");
            chatInfo.setContent(sendText);
            chatInfo.setMine(true);
            chatInfo.setTime(System.currentTimeMillis());
            chatInfo.setChatRoom(mChatList.getMode() == ChatList.MODE_CHAT_ROOM);
            mNetSocket.send(chatInfo);
        }
    }

    @Override
    public void onSendSuc(ChatInfo chatInfo) {
        if (null != mEditText) {
            mEditText.setText("");
        }

        if (!TextUtils.isEmpty(chatInfo.getContent())) {
            mChatList.addChatInfo(chatInfo);
        }
        mChatList.scrollToNow();
    }

    @Override
    public void onReciveSuc(ChatInfo chatInfo) {
        if (!TextUtils.isEmpty(chatInfo.getContent())) {
            mChatList.addChatInfo(chatInfo);
        }
        mChatList.scrollToNow();
    }

    @Override
    public void onError(Exception e) {
        Snackbar.make(findViewById(R.id.bottom_nav), e.getMessage(), Snackbar.LENGTH_INDEFINITE)
                .setAction("reconnect", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setIp();
                    }
                }).show();
    }

    private void setIp() {
        final View contentView = LayoutInflater.from(this).inflate(R.layout.view_ip_set_dialog, null);
        final EditText ipEditText = ((EditText) contentView.findViewById(R.id.edit));
        ipEditText.setText(mIp);
        new AlertDialog.Builder(this)
                .setTitle("Please input ip:")
                .setCancelable(false)
                .setView(contentView)
                .setPositiveButton("go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mIp = ipEditText.getText().toString();
                        setSocket();
                    }
                }).show();
    }

    private void setSocket() {
        mNetSocket = new NetSocket(mIp, 10012, MainActivity.this);
        mNetThread = new Thread(mNetSocket);
        mNetThread.start();
    }
}
