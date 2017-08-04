package uq.edu.au.chatroom.other;

import com.google.gson.Gson;

/**
 * Created by wufeiyang on 2017/8/3.
 */

public class Json {

    private static volatile Gson sGson;

    public static Gson getGson () {
        if (null == sGson) {
            synchronized(Json.class) {
                if (null == sGson) {
                    sGson = new Gson();
                }
            }
        }

        return sGson;
    }
}
