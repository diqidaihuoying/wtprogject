package com.classic.okhttp.builder;

import com.classic.okhttp.OkHttpUtils;
import com.classic.okhttp.request.OtherRequest;
import com.classic.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers).build();
    }
}
