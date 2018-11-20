package com.tc2r.greedisland.book;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.tc2r.greedisland.R;

import java.util.List;

/**
 * Created by Tc2r on 1/27/2017.
 * <p>
 * Description: Adapter for adding Rules!
 */

public class RulesAdapter extends RecyclerView.Adapter<RulesAdapter.ViewHolder> {
    private List<RuleObject> mRuleObjects;
    private Context mContext;

    public RulesAdapter(List<RuleObject> mRuleObjects, Context mContext) {
        this.mRuleObjects = mRuleObjects;
        this.mContext = mContext;
    }

    private Context getContext() {
        return mContext;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rules_card, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        // Get the data model based on position
        RuleObject ruleObject = mRuleObjects.get(position);
        h.ruleTitleText.setText(ruleObject.getRuleTitle());
        h.ruleDescText.setText(ruleObject.getRuleDescription());

        Glide.with(mContext).load(ruleObject.getRuleImage()).transition(new DrawableTransitionOptions().crossFade()).into(h.ruleImage);
    }

    @Override
    public int getItemCount() {
        return mRuleObjects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ruleTitleText, ruleDescText;
        public ImageView ruleImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ruleTitleText = (TextView) itemView.findViewById(R.id.rule_title);
            ruleDescText = (TextView) itemView.findViewById(R.id.rule_description);
            ruleImage = (ImageView) itemView.findViewById(R.id.rule_image);


        }
    }
}
