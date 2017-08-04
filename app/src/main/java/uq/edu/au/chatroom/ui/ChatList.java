package uq.edu.au.chatroom.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import uq.edu.au.chatroom.R;
import uq.edu.au.chatroom.bean.ChatInfo;
import uq.edu.au.chatroom.other.Utils;

/**
 * Created by wufeiyang on 2017/7/5.
 */

public class ChatList extends RecyclerView {

    public static final short MODE_ROBOT = 0;
    public static final short MODE_CHAT_ROOM = 1;

    private short mMode = MODE_ROBOT;

    private ChatListAdapter mRobotAdapter;
    private ChatListAdapter mChatRoomAdapter;

    public ChatList(Context context) {
        super(context);
        init();
    }

    public ChatList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayoutManager(new LinearLayoutManager(getContext(), VERTICAL, false));

        mRobotAdapter = new ChatListAdapter();
        mChatRoomAdapter = new ChatListAdapter();

        mRobotAdapter.add(getRobotWelcomeChatInfo());

        changeMode(mMode);
    }

    private int getItemCount() {
        switch (mMode) {
            case MODE_ROBOT:
                return mRobotAdapter.getItemCount();
            case MODE_CHAT_ROOM:
                return mChatRoomAdapter.getItemCount();
            default:
                return 0;
        }
    }

    public void scrollToNow() {
        smoothScrollToPosition(getItemCount());
    }

    public void changeMode(Short mode) {
        mMode = mode;
        switch (mMode) {
            case MODE_ROBOT:
                setAdapter(mRobotAdapter);
                break;
            case MODE_CHAT_ROOM:
                setAdapter(mChatRoomAdapter);
                break;
            default:
                break;
        }
        scrollToNow();
    }

    public void addChatInfo(ChatInfo chatInfo) {
        switch (mMode) {
            case MODE_ROBOT:
                mRobotAdapter.add(chatInfo);
                break;
            case MODE_CHAT_ROOM:
                mChatRoomAdapter.add(chatInfo);
                break;
            default:
                break;
        }
    }

    private ChatInfo getRobotWelcomeChatInfo() {
        ChatInfo chartInfo = new ChatInfo();
        chartInfo.setNick("Robot");
        chartInfo.setContent(getResources().getString(R.string.robot_welcome));
        chartInfo.setTime(System.currentTimeMillis());
        return chartInfo;
    }

    public short getMode() {
        return mMode;
    }

    private static class ChatListAdapter extends RecyclerView.Adapter<ChatItemHolder> {

        private static final short TYPE_MINE = 0;
        private static final short TYPE_OTHER = 1;

        private List<ChatInfo> mChartInfos;

        public ChatListAdapter() {
            mChartInfos = new ArrayList<>();
        }

        @Override
        public ChatItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layoutId = R.layout.item_chat_list_outgoing;
            if (TYPE_OTHER == viewType) {
                layoutId = R.layout.item_chat_list_incoming;
            }
            return new ChatItemHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        }

        @Override
        public void onBindViewHolder(ChatItemHolder holder, int position) {
            ChatInfo chartInfo = mChartInfos.get(holder.getAdapterPosition());
            holder.nick.setText(chartInfo.getNick());
            holder.content.setText(chartInfo.getContent());
            holder.time.setText(Utils.parseTime(chartInfo.getTime()));
        }

        @Override
        public int getItemCount() {
            return null == mChartInfos ? 0 : mChartInfos.size();
        }

        @Override
        public int getItemViewType(int position) {
            ChatInfo chartInfo = mChartInfos.get(position);
            return chartInfo.isMine() ? TYPE_MINE : TYPE_OTHER;
        }

        public void refresh(List<ChatInfo> chartInfos) {
            mChartInfos.clear();
            mChartInfos.addAll(chartInfos);
            notifyDataSetChanged();
        }

        public void add(ChatInfo chartInfo) {
            mChartInfos.add(chartInfo);
            notifyItemInserted(mChartInfos.size());
        }
    }

    private static class ChatItemHolder extends RecyclerView.ViewHolder {
        public View root;
        public TextView nick;
        public TextView content;
        public TextView time;

        public ChatItemHolder(View itemView) {
            super(itemView);
            nick = (TextView) itemView.findViewById(R.id.nick);
            content = (TextView) itemView.findViewById(R.id.content);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
