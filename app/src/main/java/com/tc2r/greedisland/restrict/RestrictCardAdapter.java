package com.tc2r.greedisland.restrict;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tc2r.greedisland.R;
import com.tc2r.greedisland.book.CardDetail;

import java.util.List;

/**
 * Created by Tc2r on 1/22/2017.
 * <p>
 * Description:
 */
public class RestrictCardAdapter extends RecyclerView.Adapter<RestrictCardAdapter.BaseHolder> {


	boolean init = false;
	private Context context;
	private List<RestrictCard> cardList;
	private boolean[] revealed;
	private boolean local;
	private int count = 0;
	private String hidCardNum;

	public RestrictCardAdapter(Context context, List<RestrictCard> cardList, boolean local, boolean[] cardCheck) {
		this.context = context;
		this.cardList = cardList;
		this.local = local;
		this.revealed = cardCheck;

	}

	@Override
	public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		switch (viewType) {
			case 1:
				return new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_front_layout, parent, false));
			default:
				return new ViewHolder0(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_back_layout, parent, false));
		}

	}

	@Override
	public void onBindViewHolder(BaseHolder holder, int position) {
		switch (holder.getItemViewType()) {
			case 1:

				final ViewHolder1 viewHolder1 = (ViewHolder1) holder;
				final RestrictCard restrictCard = cardList.get(position);
				if (local) {

					viewHolder1.card_description.setText(restrictCard.description);
					viewHolder1.card_title.setText(restrictCard.getTitle());

					count++;
					viewHolder1.card_designation.setText(String.valueOf(count));
					String ranklimit = ("SS-17");
					viewHolder1.card_ranklimit.setText(ranklimit);
					// Loading images with glide library
					Glide.with(context).load(cardList.get(position).getImageItem())
									.placeholder(R.drawable.placeholder)
									.centerCrop()
									.into(viewHolder1.card_image);
				} else {
					viewHolder1.card_description.setText(restrictCard.description);
					viewHolder1.card_title.setText(restrictCard.getTitle());
					viewHolder1.card_designation.setText(String.valueOf(restrictCard.getId()));
					final String ranklimit = (restrictCard.getRank() + "-" + String.valueOf(restrictCard.getLimit()));
					viewHolder1.card_ranklimit.setText(ranklimit);


					// Setting the Card Colors
					//switch (restrictCard.getType()) {

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
					Glide.with(context).load(newUrl)
									.placeholder(R.drawable.placeholder)
									.centerCrop()
									.crossFade()
									.into(viewHolder1.card_image);

					viewHolder1.card_image.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {

							Intent i = new Intent(v.getContext(), CardDetail.class);
							i.putExtra("desc", restrictCard.getDescription());
							i.putExtra("rank", restrictCard.getRank());
							i.putExtra("title", restrictCard.getTitle());
							i.putExtra("image", newUrl);
							i.putExtra("limit", restrictCard.getLimit());
							i.putExtra("id", restrictCard.getId());
							v.getContext().startActivity(i);
						}
					});
				}


				break;
			case 0:
				ViewHolder0 viewHolder0 = (ViewHolder0) holder;
				RestrictCard hiddenRestrictCard = cardList.get(position);
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
		ImageView card_image;
		TextView hiddenCardNum;

		public ViewHolder0(View itemView) {
			super(itemView);
			hiddenCardNum = (TextView) itemView.findViewById(R.id.hidden_card_number);
		}
	}

	private class ViewHolder1 extends BaseHolder {
		ImageView card_image;
		TextView card_title, card_description, card_thumbnail, card_designation, card_ranklimit;
		int card_limit, card_number;
		LinearLayout cardBorder;

		public ViewHolder1(View itemView) {
			super(itemView);
			card_title = (TextView) itemView.findViewById(R.id.card_title);
			card_description = (TextView) itemView.findViewById(R.id.card_description);
			card_image = (ImageView) itemView.findViewById(R.id.card_image);
			card_designation = (TextView) itemView.findViewById(R.id.card_designation);
			card_ranklimit = (TextView) itemView.findViewById(R.id.card_ranklimit);
			cardBorder = (LinearLayout) itemView.findViewById(R.id.text_border_layout);


		}
	}

	public class BaseHolder extends RecyclerView.ViewHolder {
		ImageView card_image;

		TextView card_title, card_description, card_thumbnail, card_designation, card_ranklimit;
		int card_limit, card_number;

		public BaseHolder(View itemView) {
			super(itemView);


		}
	}


}
