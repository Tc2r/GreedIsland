package com.tc2r.greedisland.spells;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.tc2r.greedisland.R;
import com.tc2r.greedisland.utils.AnimationCardReceived;
import com.tc2r.greedisland.utils.GreedSnackbar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Tc2r on 2/25/2017.
 * <p>
 * Description:
 */

public class SpellsHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "spells.db";
    private static final String TABLE_NAME = "game_spells";
    private static final String Prime_ID = "Id";
    private static final int DATABASE_VERSION = 1;


    public SpellsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    public static void createRandomSpell(View view, int count) {

        Context context = view.getContext();
        ArrayList<SpellCardObject> userSpells = loadUserSpells(context);
        SpellsHelper db = new SpellsHelper(context);
        Random rand = new Random();

        for (int i = 0; i < count; i++) {
            int randNum = rand.nextInt(40) + 1;
            SpellCardObject newCard = db.CreateSpell(randNum);
            GreedSnackbar.createSnackBar(view, newCard.getName() + " Found!", Snackbar.LENGTH_LONG).show();
            showCard(context, null, newCard);
            userSpells.add(newCard);
        }
        // Save User Spells
        saveUserSpells(context, userSpells);

    }

    public static void deleteSpell(Context context, int id) {
        // Get User List From Saved Preferences!
        ArrayList<SpellCardObject> userSpells = loadUserSpells(context);

        for (int i = 0; i < userSpells.size(); i++) {
            if (userSpells.get(i).getId() == id) {
                userSpells.remove(i);
                saveUserSpells(context, userSpells);
                break;
            }
        }
    }

    public static void deleteRandomSpell(View view) {
        // Get User List From Saved Preferences!

        Context context = view.getContext();
        ArrayList<SpellCardObject> userSpells = loadUserSpells(context);

        // Delete Random Card
        Random rand = new Random();
        int randNum = rand.nextInt(userSpells.size());
        GreedSnackbar.createSnackBar(view, userSpells.get(randNum).getName() + " Deleted", Snackbar.LENGTH_LONG).show();
        userSpells.remove(randNum);

        // Save User Spells
        saveUserSpells(context, userSpells);
    }

    public static ArrayList<SpellCardObject> loadUserSpells(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("UserSpellList", null);
        //Log.wtf("json = ", json);
        ArrayList<SpellCardObject> userSpells;
        if (json == null) {
            // Create New List, Add Analysis to list
            userSpells = new ArrayList<SpellCardObject>();
            SpellsHelper db = new SpellsHelper(context);
            SpellCardObject first = db.CreateSpell(31);
            db.close();
            userSpells.add(first);
            // Save User Spells
            saveUserSpells(context, userSpells);
        } else {
            //Log.wtf("Fetch = ", "Fetch Spells");
            Type type = new TypeToken<ArrayList<SpellCardObject>>() {
            }.getType();
            userSpells = gson.fromJson(json, type);
        }
        return userSpells;
    }

    public static void saveUserSpells(Context context, List<SpellCardObject> userSpells) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userSpells);
        editor.putString("UserSpellList", json);
        editor.apply();
    }

    public static void showCard(final Context context, final View card, SpellCardObject spellCard) {
        Activity activity = (Activity) context;
        View child = activity.getLayoutInflater().inflate(R.layout.new_card_layout, null);
        final RelativeLayout reward = (RelativeLayout) activity.findViewById(R.id.rewardLayout);
        reward.addView(child);
        TextView title = (TextView) child.findViewById(R.id.new_card_title);
        TextView designation = (TextView) child.findViewById(R.id.new_card_designation);
        TextView ranklimit = (TextView) child.findViewById(R.id.new_card_ranklimit);
        //LinearLayout border = (LinearLayout) child.findViewById(R.id.new_text_border_layout);
        TextView description = (TextView) child.findViewById(R.id.new_card_description);
        ImageView image = (ImageView) child.findViewById(R.id.new_card_image);

        title.setText(spellCard.getName());
        designation.setText(String.valueOf(spellCard.getCardNumber()));
        ranklimit.setText(spellCard.getRank() + "-" + String.valueOf(spellCard.getLimit()));
        description.setText(spellCard.getDescription());
        TypedArray images = context.getResources().obtainTypedArray(R.array.spell_cards);
        Drawable drawable = images.getDrawable(spellCard.getId() - 1);
        image.setImageDrawable(drawable);
        reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationCardReceived.ReceiveCard(reward, card);
            }
        });
        images.recycle();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);

    }

    public Cursor getAlldata() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;


    }

    public List<SpellCardObject> getSpellCardList() {

        List<SpellCardObject> scList = new ArrayList<SpellCardObject>();
        String query = "SELECT * FROM " + TABLE_NAME;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                SpellCardObject spellCard = new SpellCardObject();
                spellCard.setId(cursor.getInt(0));
                spellCard.setCardNumber(cursor.getInt(1));
                spellCard.setName(cursor.getString(2));
                spellCard.setDescription(cursor.getString(3));
                spellCard.setLimit(cursor.getInt(4));
                spellCard.setImage(cursor.getString(5));
                scList.add(spellCard);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return scList;
    }

    public SpellCardObject CreateSpell(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{"Id", "Number", "Name", "Rank", "cLimit", "Image", "Description"}, "Id" + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        SpellCardObject newCard = null;
        if (cursor != null) {
            cursor.moveToFirst();
            newCard = new SpellCardObject(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6));
            cursor.close();
        }

        //return newCard!
        return newCard;
    }

    public boolean updateData(int id, int number, String name, String description, String rank, int limit, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//		values.put(Number, number);
//		values.put(Card_Name, name);
//		values.put(Card_Description, description);
//		values.put(Card_Rank, limit);
//		values.put(Card_Limit, limit);
//		values.put(Card_Image, image);
        db.update(TABLE_NAME, values, Prime_ID + "== ?", new String[]{String.valueOf(id)});
        db.close();
        return true;
    }
}
