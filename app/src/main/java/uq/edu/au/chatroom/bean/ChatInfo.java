package uq.edu.au.chatroom.bean;

/**
 * Created by wufeiyang on 2017/7/5.
 */

public class ChatInfo {

    private String nick;
    private String content;
    private long time;

    private String mAnswerId;

    private boolean isChatRoom;
    private boolean isMine;

    private boolean isDel;
    private boolean isAdd;

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

    public String getmAnswerId() {
        return mAnswerId;
    }

    public void setmAnswerId(String mAnswerId) {
        this.mAnswerId = mAnswerId;
    }

    public String getAnswerId() {
        return mAnswerId;
    }

    public void setAnswerId(String answerId) {
        mAnswerId = answerId;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
