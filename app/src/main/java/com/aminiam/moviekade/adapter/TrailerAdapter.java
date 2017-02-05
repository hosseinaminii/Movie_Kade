package com.aminiam.moviekade.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aminiam.moviekade.R;
import com.aminiam.moviekade.utility.NetworkUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{
    private static final String LOG_TAG = TrailerAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<String> mTrailerKeys;
    private LinearLayout mEmptyView;
    private TrailerClickListener mListener;

    public TrailerAdapter(Context context,TrailerClickListener listener, LinearLayout emptyView) {
        mContext = context;
        mEmptyView = emptyView;
        mListener = listener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_item,
                parent, false);
        return new TrailerViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        if(mTrailerKeys == null) return;

        String trailerKey = mTrailerKeys.get(position);
        Picasso.with(mContext).load(NetworkUtility.buildTrailerImagePath(trailerKey))
                .into(holder.mImgTrailer);
    }

    @Override
    public int getItemCount() {
        return mTrailerKeys != null ? mTrailerKeys.size() : 0;
    }

    public void populateData(ArrayList<String> trailerKeys) {
        mTrailerKeys = trailerKeys;
        notifyDataSetChanged();
        mEmptyView.setVisibility(mTrailerKeys.size() == 0 ? View.VISIBLE : View.INVISIBLE);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView mImgTrailer;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            mImgTrailer = (ImageView) itemView.findViewById(R.id.imgTrailer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String key = mTrailerKeys.get(getAdapterPosition());
            mListener.onTrailerClick(key);
        }
    }

    public interface TrailerClickListener {
        void onTrailerClick(String key);
    }
}
