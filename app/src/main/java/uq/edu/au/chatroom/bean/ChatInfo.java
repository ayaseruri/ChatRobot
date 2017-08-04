package uq.edu.au.chatroom.bean;

/**
 * Created by wufeiyang on 2017/7/5.
 */

public class ChatInfo {
    private String nick;
    private String content;
    private long time;

    private long mAnswerId;

    private boolean isChatRoom;
    private boolean isMine;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean isChatRoom() {
        return isChatRoom;
    }

    public void setChatRoom(boolean chatRoom) {
        isChatRoom = chatRoom;
    }

    public long getmAnswerId() {
        return mAnswerId;
    }

    public void setmAnswerId(long mAnswerId) {
        this.mAnswerId = mAnswerId;
    }
}
