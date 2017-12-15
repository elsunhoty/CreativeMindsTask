package com.tmoo7.creativemindstask.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tmoo7.creativemindstask.Helpers.RoundedImageView;
import com.tmoo7.creativemindstask.Models.LocationModel;
import com.tmoo7.creativemindstask.R;

import java.util.List;

/**
 * Created by othello on 12/15/2017.
 */
public class Location_recycler_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<LocationModel> mLocationModels;

    public Location_recycler_adapter(Context context, List<LocationModel> locationModels) {
        mContext = context;
        mLocationModels = locationModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

       View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_recycler_item, parent, false);
        vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {

            MyViewHolder holders = (MyViewHolder) holder;

            holders.name.setText(mLocationModels.get(position).getName());
            holders.formattedPhone.setText(mLocationModels.get(position).getFormattedPhone());
            holders.categories.setText(mLocationModels.get(position).getCategories());
            holders.status.setText(mLocationModels.get(position).getStatus());
            if (mLocationModels.get(position).getPhotourl().equals("NULL"))
            {

            }
            else
            {
                Picasso.with(mContext)
                        .load(mLocationModels.get(position).getPhotourl())
                        .placeholder(R.drawable.loading)
                        .into(holders.photourl);
            }

        }
    }

    @Override
    public int getItemCount() {
        return mLocationModels.size();
    }
    private class  MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name= null;
        TextView formattedPhone= null;
        TextView categories= null;
        TextView status= null;
        RoundedImageView photourl = null;
         MyViewHolder(View itemView) {
            super(itemView);
             this.name = (TextView) itemView.findViewById(R.id.name);
             this.formattedPhone = (TextView) itemView.findViewById(R.id.phone);
             this.categories = (TextView) itemView.findViewById(R.id.categories);
            this.status = (TextView) itemView.findViewById(R.id.status);
            this.photourl = (RoundedImageView) itemView.findViewById(R.id.photourl);
        }
    }
}
