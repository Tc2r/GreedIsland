package com.tc2r.greedisland.spells;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tc2r.greedisland.R;
import com.tc2r.greedisland.book.DeckActivity;
import com.tc2r.greedisland.utils.AnimationCardReceived;

import java.util.List;
import java.util.Random;

import static com.tc2r.greedisland.utils.EventsManager.manipulateDeck;

/**
 * Created by Tc2r on 2/26/2017.
 * <p>
 * Description:
 */
public class SpellCardAdapter extends RecyclerView.Adapter<SpellCardAdapter.SpellViewHolder> {
    private List<SpellCardObject> spellCardList;
    private Context mContext;
    private RelativeLayout reward;
    private DeckActivity deckActivity;

    SpellCardAdapter(List<SpellCardObject> spellCardList, Context mContext) {
        this.spellCardList = spellCardList;
        this.mContext = mContext;
        deckActivity = (DeckActivity) mContext;
    }

    @NonNull
    @Override
    public SpellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new SpellViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.spell_card_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final SpellViewHolder holder, final int position) {
        final SpellCardObject spellCard = spellCardList.get(holder.getAdapterPosition());
        holder.cardNumber.setText(String.valueOf(spellCard.getCardNumber()));
        holder.cardTitle.setText(spellCard.getName());
        holder.cardDescription.setText(spellCard.getDescription());
        holder.cardRankLimit.setText(spellCard.getRank() + "-" + String.valueOf(spellCard.getLimit()));
        TypedArray images = mContext.getResources().obtainTypedArray(R.array.spell_cards);
        Drawable drawable = images.getDrawable(spellCard.getId() - 1);
        holder.cardImage.setImageDrawable(drawable);

        //holder.cardImage.setImageResource(R.drawable.placeholder);
        holder.cardBorder.setBackgroundResource(R.drawable.text_border_spell);
        holder.root.setOnClickListener(new View.OnClickListener() {
            int cardPosition = holder.getAdapterPosition();
            @Override
            public void onClick(View v) {

                v.setVisibility(View.INVISIBLE);
                showCard(deckActivity, v, spellCard, cardPosition);

            }
        });
        images.recycle();

    }

    @Override
    public int getItemCount() {
        return spellCardList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView mRecyclerView = recyclerView;
    }

    private void useCard(final View v, final int position, final SpellCardObject spellCard) {

        AlertDialog.Builder builder;
        switch (spellCard.getId()) {
            case 7:
            case 21:
            case 6:
                // PICK POCKET
                // Rob
                // THIEF
                builder = new AlertDialog.Builder(v.getContext()).setTitle("Activate Spell Card").setMessage("Use " + spellCard.getName() + " Now?").setPositiveButton("Gain!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // Add A card to player hand
                        manipulateDeck(v, 1, true);

                        // Send out action token for steal
                        addToken(v);

                        // Remove Spell Card
                        SpellsHelper.deleteSpell(mContext, spellCard.getId());

                        //TODO: Play sound like "Gain!"

                        MediaPlayer mp = MediaPlayer.create(mContext, R.raw.greed);
                        mp.start();

                        // Refresh Spell Card Gallery
                        spellCardList.remove(position);

                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    }
                }).setNegativeButton("Keep!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                break;
            case 18:
                // Levy
                builder = new AlertDialog.Builder(v.getContext()).setTitle("Activate Spell Card").setMessage("Use " + spellCard.getName() + " Now?").setPositiveButton("Gain!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // Add 1-3 cars to player hand
                        Random rng = new Random();

                        manipulateDeck(v, rng.nextInt(3) + 1, true);

                        // Send out action token for steal
                        SpellsHelper.deleteSpell(mContext, spellCard.getId());

                        //TODO: Play sound like "Gain!"
                        MediaPlayer mp = MediaPlayer.create(mContext, R.raw.greed);
                        mp.start();

                        // Refresh Spell Card Gallery
                        spellCardList.remove(position);

                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    }
                }).setNegativeButton("Keep!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                break;
        }
    }

    private void addToken(View v) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(v.getContext());
        final String location = settings.getString(v.getContext().getString(R.string.pref_current_location_key), v.getContext().getString(R.string.pref_town_default));
        String url = "http://tchost.000webhostapp.com/StealCard.php?currentLocation=" + location;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.wtf("Token", location + " Added");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(stringRequest);
    }

    private void showCard(Context cxt, View v, SpellCardObject sCard, int pos) {

        final View card = v;
        final SpellCardObject spellCardObject = sCard;
        final int position = pos;
        Activity activity = (Activity) cxt;
        View child = activity.getLayoutInflater().inflate(R.layout.new_card_layout, null);
        child.setVisibility(View.VISIBLE);
        reward = (RelativeLayout) activity.findViewById(R.id.rewardLayout);
        reward.addView(child);
        TextView title = (TextView) child.findViewById(R.id.new_card_title);
        TextView designation = (TextView) child.findViewById(R.id.new_card_designation);
        TextView ranklimit = (TextView) child.findViewById(R.id.new_card_ranklimit);
        //LinearLayout border = (LinearLayout) child.findViewById(R.id.new_text_border_layout);
        TextView description = (TextView) child.findViewById(R.id.new_card_description);
        ImageView image = (ImageView) child.findViewById(R.id.new_card_image);


        title.setText(spellCardObject.getName());
        designation.setText(String.valueOf(spellCardObject.getCardNumber()));
        ranklimit.setText(spellCardObject.getRank() + "-" + String.valueOf(spellCardObject.getLimit()));
        description.setText(spellCardObject.getDescription());
        TypedArray images = mContext.getResources().obtainTypedArray(R.array.spell_cards);
        Drawable drawable = images.getDrawable(spellCardObject.getId() - 1);
        image.setImageDrawable(drawable);
        reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (spellCardObject.getCardNumber()) {
                    case 1007:// Thief
                    case 1006:// Pick Pocket
                    case 1018:// Levy
                    case 1021:// Rob

                        useCard(v, position, spellCardObject);
                        AnimationCardReceived.ReceiveCard(reward, card);
                        break;

                    default:

                        AnimationCardReceived.ReceiveCard(reward, card);
                        break;
                }
            }
        });
        images.recycle();
    }

    class SpellViewHolder extends RecyclerView.ViewHolder {

        LinearLayout cardBorder;
        CardView root;
        private TextView cardNumber;
        private TextView cardTitle;
        private TextView cardRankLimit;
        private TextView cardDescription;
        private ImageView cardImage;

        SpellViewHolder(View itemView) {
            super(itemView);
            root = (CardView) itemView.findViewById(R.id.layout_main);
            cardNumber = (TextView) itemView.findViewById(R.id.card_designation);
            cardTitle = (TextView) itemView.findViewById(R.id.card_title);
            cardDescription = (TextView) itemView.findViewById(R.id.card_description);
            cardRankLimit = (TextView) itemView.findViewById(R.id.card_ranklimit);
            cardImage = (ImageView) itemView.findViewById(R.id.card_image);
            cardBorder = (LinearLayout) itemView.findViewById(R.id.text_border_layout);

        }
    }

}
