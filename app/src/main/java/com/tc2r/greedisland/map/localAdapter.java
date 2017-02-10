package com.tc2r.greedisland.map;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tc2r.greedisland.R;

import java.util.List;

/**
 * Created by Tc2r on 2/9/2017.
 * <p>
 * Description:
 */

public class localAdapter extends RecyclerView.Adapter<localAdapter.ViewHolder> {

	private Context context;
	private List<localHunter> hunters;

	localAdapter(Context context, List<localHunter> hunters) {
		super();
		this.context = context;
		this.hunters = hunters;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.locals_list, parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {

		localHunter localHunter = hunters.get(position);
		holder.textViewHunterName.setText(localHunter.getHunterName());

	}

	@Override
	public int getItemCount() {
		return hunters.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		private TextView textViewHunterName;

		private ViewHolder(View itemView) {
			super(itemView);
			textViewHunterName = (TextView) itemView.findViewById(R.id.textViewName);
		}
	}
}


