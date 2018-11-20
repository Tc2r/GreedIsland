package com.tc2r.greedisland.restrict;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.tc2r.greedisland.R;
import com.tc2r.greedisland.book.CardDetailActivity;

import java.util.List;

/**
 * Created by Tc2r on 1/22/2017.
 * <p>
 * Description:
 */
public class RestrictCardAdapter extends RecyclerView.Adapter<RestrictCardAdapter.BaseHolder> {


    private Context context;
    private List<RestrictCardObject> cardList;
    private boolean[] revealed;
    private boolean local;
    private int count = 0;
    private String hidCardNum;

    RestrictCardAdapter(Context context, List<RestrictCardObject> cardList, boolean local, boolean[] cardCheck) {
        this.context = context;
        this.cardList = cardList;
        this.local = local;
        this.revealed = cardCheck;

    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 1)
        {
            return new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_front_layout, parent, false));

        } else
        {
            return new ViewHolder0(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_back_layout, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 1:

                final ViewHolder1 viewHolder1 = (ViewHolder1) holder;
                final RestrictCardObject restrictCardObject = cardList.get(position);
                if (local) {

                    viewHolder1.card_description.setText(restrictCardObject.description);
                    viewHolder1.card_title.setText(restrictCardObject.getTitle());

                    count++;
                    viewHolder1.card_designation.setText(String.valueOf(count));
                    String ranklimit = ("SS-17");
                    viewHolder1.card_rankLimit.setText(ranklimit);
                    // Loading images with glide library
                    Glide.with(context).load(cardList.get(position).getImageItem()).apply(new RequestOptions().placeholder(R.drawable.placeholder).centerCrop()).into(viewHolder1.card_image);
                } else {
                    viewHolder1.card_description.setText(restrictCardObject.description);
                    viewHolder1.card_title.setText(restrictCardObject.getTitle());
                    viewHolder1.card_designation.setText(String.valueOf(restrictCardObject.getId()));
                    final String ranklimit = restrictCardObject.getRank() + "-" + restrictCardObject.getLimit();
                    viewHolder1.card_rankLimit.setText(ranklimit);


                    // Setting the Card Colors
                    //switch (restrictCardObject.getType()) {

                    int pick = 1;
                    switch (pick) {
                        case 1:
                            // Red
                            viewHolder1.cardBorder.setBackgroundResource(R.drawable.text_border_restrict);
                            break;
                        case 2:
                            // Yellow
                            viewHolder1.cardBorder.setBackgroundResource(R.drawable.text_border_free);

                            break;
                        case 3:
                            // Blue
                            viewHolder1.cardBorder.setBackgroundResource(R.drawable.text_border_spell);
                            break;
                    }

                    // Loading images with glide library

                    final String newUrl = "http://res.cloudinary.com/munaibh/image/upload/v1485443356/HxH/" + (cardList.get(position).getId()) + ".png";
                    Glide.with(context).load(newUrl).apply(new RequestOptions().placeholder(R.drawable.placeholder).centerCrop()).transition(new DrawableTransitionOptions().crossFade()).into(viewHolder1.card_image);

                    viewHolder1.card_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent i = new Intent(v.getContext(), CardDetailActivity.class);
                            i.putExtra("desc", restrictCardObject.getDescription());
                            i.putExtra("rank", restrictCardObject.getRank());
                            i.putExtra("title", restrictCardObject.getTitle());
                            i.putExtra("image", newUrl);
                            i.putExtra("limit", restrictCardObject.getLimit());
                            i.putExtra("id", restrictCardObject.getId());
                            v.getContext().startActivity(i);
                        }
                    });
                }


                break;
            case 0:
                ViewHolder0 viewHolder0 = (ViewHolder0) holder;
                int hidcardID = cardList.get(position).getId();
                if (hidcardID < 10) {
                    hidCardNum = ("00" + String.valueOf(hidcardID));
                } else if (hidcardID < 100) {
                    hidCardNum = ("0" + String.valueOf(hidcardID));
                }

                viewHolder0.hiddenCardNum.setText(hidCardNum);

                break;
        }


    }

    @Override
    public int getItemViewType(int position) {

        if (revealed.length > cardList.get(position).getId() && revealed[cardList.get(position).getId()]) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class ViewHolder0 extends BaseHolder {

        TextView hiddenCardNum;

        ViewHolder0(View itemView) {
            super(itemView);
            hiddenCardNum = (TextView) itemView.findViewById(R.id.hidden_card_number);
        }
    }

    private class ViewHolder1 extends BaseHolder {

        LinearLayout cardBorder;

        ViewHolder1(View itemView) {
            super(itemView);
            card_title = (TextView) itemView.findViewById(R.id.card_title);
            card_description = (TextView) itemView.findViewById(R.id.card_description);
            card_image = (ImageView) itemView.findViewById(R.id.card_image);
            card_designation = (TextView) itemView.findViewById(R.id.card_designation);
            card_rankLimit = (TextView) itemView.findViewById(R.id.card_ranklimit);
            cardBorder = (LinearLayout) itemView.findViewById(R.id.text_border_layout);


        }
    }

    class BaseHolder extends RecyclerView.ViewHolder {
        ImageView card_image;

        TextView card_title;
        TextView card_description;
        TextView card_thumbnail;
        TextView card_designation;
        TextView card_rankLimit;
        int card_limit;
        int card_number;

        BaseHolder(View itemView) {
            super(itemView);


        }
    }


}
