package mj.wt.wtapp.http;

import android.text.TextUtils;

import com.classic.okhttp.callback.StringCallback;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import okhttp3.Call;

/**
 * 对象解析
 *
 *    使用：onBefore()：请求之前的操作
 *      onResponse()：该类对onResponse进行Gson的数据解析
 *          onSuccessed()：数据成功
 *          onErrored()：接口返回失败，或者数据解析失败
 *      onError()：请求取消、返回请求码不在[200,300}、请求失败
 *      onAfter()：成功或者失败之后都会执行该方法
 *
 */
public abstract class ObjectCallback<T> extends StringCallback {



    public static final String KEY_CODE     = "code";
    public static final String KEY_MSG    = "message";

    public static final String KEY_OBJECT   = "data";

    public static final int    SUCCESS_CODE = 200;
    public static final String KEY_SUCCESS     = "OK";

    @Override
    public void onResponse(String response) {
        if (TextUtils.isEmpty(response)) {
            return;
        }
        try {
            JSONObject object = new JSONObject(response);
            final int code = object.getInt(KEY_CODE);
            final String msg=object.getString(KEY_MSG);
            if (code == SUCCESS_CODE&&msg.equals(KEY_SUCCESS))
            {
                final String content = object.getString(KEY_OBJECT);
                T obj = new Gson().fromJson(content, getType());
                onSuccessed(obj);
            } else {
                onError(null,null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            onError(null,e);
        }
    }

    //请求失败
    @Override public void onError(Call call, Exception e) {
    }

    //请求成功
    protected abstract void onSuccessed(T response);

    //设置要转换的对象类型  <br />
    public abstract Type getType();
}
