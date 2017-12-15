package com.tmoo7.creativemindstask.Layers;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tmoo7.creativemindstask.Models.LocationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by othello on 12/2/2017.
 */

public class VolleyRequests {

    private Context mContext;
    private  OnRequestFinished mOnRequestFinished;

    public VolleyRequests(Context context, final OnRequestFinished onRequestFinished ) {
        this.mContext = context;
        this.mOnRequestFinished = onRequestFinished;
    }
    public interface OnRequestFinished
    {
        void onrequestCompeleted(int Code, List<LocationModel> productModels);

    }

    public void volleyJsonObjectRequest(int Method,String url){

        final String  REQUEST_TAG = "CreativeMinds";
         JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Method,url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                  //      Log.e(REQUEST_TAG, response.toString());
                        try {
                            JSONArray dataarray = response
                                    .getJSONObject("response")
                                    .getJSONArray("groups")
                                    .getJSONObject(0)
                                    .getJSONArray("items");


                            if (dataarray.length() != 0) {
                                List<LocationModel> productModelArrayList = new ArrayList<LocationModel>();
                                for (int i = 0; i < dataarray.length(); i++) {
                                    try {
                                        JSONObject object = dataarray.getJSONObject(i);
                                        String name = object.getJSONObject("venue").getString("name");
                                        String formattedPhone = "";
                                        if (object.getJSONObject("venue").getJSONObject("contact").isNull("formattedPhone"))
                                        {
                                            formattedPhone = "No Phone Availble";
                                        }
                                        else
                                        {
                                            formattedPhone = object.getJSONObject("venue").getJSONObject("contact").getString("formattedPhone");

                                        }
                                        String categories = object.getJSONObject("venue").getJSONArray("categories").getJSONObject(0).getString("shortName");
                                        String status = "No Statue Avaible";
                                        if (!object.getJSONObject("venue").isNull("hours"))
                                        {
                                            if (!object.getJSONObject("venue").getJSONObject("hours").isNull("status"))
                                            status =object.getJSONObject("venue").getJSONObject("hours").getString("status");
                                        }
                                        String photourl = "NULL";
                                        if (!object.isNull("tips"))
                                        {
                                            if (!object.getJSONArray("tips").getJSONObject(0).isNull("photourl"))
                                            photourl = object.getJSONArray("tips").getJSONObject(0).getString("photourl");
                                        }
                                        LocationModel locationModel = new LocationModel(name,formattedPhone,categories,status,photourl);
                                        productModelArrayList.add(locationModel);

                                    } catch (JSONException e) {
                                        Log.e(REQUEST_TAG, "JSONException 2: " + e.getMessage());

                                        e.printStackTrace();
                                    }
                                }
                                mOnRequestFinished.onrequestCompeleted(200, productModelArrayList);

                            }
                            else
                            {
                                mOnRequestFinished.onrequestCompeleted(1, null);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(REQUEST_TAG, "JSONException: " + e.getMessage());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                 NetworkResponse response = error.networkResponse;
                if (response != null)
                {
                    int code= response.statusCode;
                    Log.e(REQUEST_TAG, "Error: " + code);

                }
                else
                {
                    Log.e(REQUEST_TAG, "No Internet");

                }
                Log.e(REQUEST_TAG, "Error: " + error.getMessage());
                mOnRequestFinished.onrequestCompeleted(2, null);
             }
        });

         VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectReq,REQUEST_TAG);
    }
 }
