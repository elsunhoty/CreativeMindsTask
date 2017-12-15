package com.tmoo7.creativemindstask.Layers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by othello on 12/2/2017.
 */

public class VolleySingleton {
    private static VolleySingleton volleySingleton;
    private RequestQueue mRequestQueue;
    private Context mContext;

    private VolleySingleton(Context context)
    {
        this.mContext = context;
        mRequestQueue = getRequestQueue();
    }
    public static synchronized VolleySingleton getInstance(Context context) {
        if (volleySingleton == null) {
            volleySingleton = new VolleySingleton(context);
        }
        return volleySingleton;
    }
    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {

            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
