package com.kuaishan.obtainmsg.ui.adapter;
/**
 * Created by test on 2017/11/23.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.library.banner.RecyclerViewBannerBase;
import com.kuaishan.obtainmsg.R;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView适配器
 */
public class NormalRecyclerAdapter extends RecyclerView.Adapter<NormalRecyclerAdapter.NormalHolder> {

    private RecyclerViewBannerBase.OnBannerItemClickListener onBannerItemClickListener;
    private Context context;
    private List<String> urlList;

    public NormalRecyclerAdapter(Context context, List<String> urlList, RecyclerViewBannerBase.OnBannerItemClickListener onBannerItemClickListener) {
        this.context = context;
        this.urlList = urlList;
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    @Override
    public NormalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalHolder(LayoutInflater.from(context).inflate(R.layout.item_image,
                parent,false));
    }

    @Override
    public void onBindViewHolder(NormalHolder holder, final int position) {
        if (urlList == null || urlList.isEmpty())
            return;
        String url = urlList.get(position % urlList.size());
        CardView cardView = (CardView) holder.itemView;
        ImageView img = cardView.findViewById(R.id.image);
        Glide.with(context).load(context.getResources().getIdentifier("gonglv", "mipmap",
                context.getPackageName())).into(img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBannerItemClickListener != null) {
                    onBannerItemClickListener.onItemClick(position % urlList.size());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class NormalHolder extends RecyclerView.ViewHolder {
        CardView bannerItem;

        NormalHolder(View itemView) {
            super(itemView);
//            ImageView bannerItem = itemView.findViewById(R.id.image);
//            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT);
//            bannerItem.setLayoutParams(params);
//            bannerItem.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

}
