package com.tc2r.greedisland.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tc2r.greedisland.R;
import com.tc2r.greedisland.spells.SpellsHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Tc2r on 2/19/2017.
 * <p>
 * Description: Handles events while user waits for travel ability to return.
 */

public class EventsManager {

    private static boolean[] cardCheck = new boolean[100];
    private View view;
    private Context context;
    private TextView botTitle, botText;

    public EventsManager(Context context) {

        //EventsManager.context = context;
        this.botTitle = (TextView) ((Activity) context).findViewById(R.id.tv_details_title);
        this.botText = (TextView) ((Activity) context).findViewById(R.id.tv_details_desc);
        // Step 1, Load Deck Into boolean Array!
        Handler handler = new Handler();
        Runnable r = new LoadDeckRunnable();
        handler.post(r);

    }

    public static boolean[] loadDeck(Context context, String arrayName, int preSize) {
        SharedPreferences prefs_book = context.getSharedPreferences("UserBook", MODE_PRIVATE);
        int size = prefs_book.getInt(("bookPreferenceArray" + "_size"), MODE_PRIVATE);
        boolean array[];
        if (size == 0) {
            array = new boolean[preSize];
        } else {
            array = new boolean[size];
        }
        for (int i = 0; i < size; i++) {
            array[i] = prefs_book.getBoolean("bookPreferenceArray" + "_" + i, false);
        }
        PerformanceTracking.TrackEvent("Loading Deck Complete");
        return array;
    }

    public static void saveDeck(Context context) {

        SharedPreferences prefs_book = context.getSharedPreferences("UserBook", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs_book.edit();

        editor.putInt("bookPreferenceArray" + "_size", cardCheck.length);
        for (int i = 0; i < cardCheck.length; i++) {
            editor.putBoolean("bookPreferenceArray" + "_" + i, cardCheck[i]);
        }
        editor.apply();
    }

    public static void manipulateDeck(View view, int cardAmount, boolean givetake) {
        view = view;
        Context context = view.getContext();
        List<Integer> notFlipped = new ArrayList<>();
        Random random = new Random();

        if (givetake) { // If give user cards
            notFlipped.clear();
            // Cycle through entire array for all variables false
            // save false variables to notFlipped List
            for (int i = 1; i < cardCheck.length; i++) {

                // if boolean at i in cardCheck is false
                if (!cardCheck[i]) {

                    //Add position i to notFlipped List
                    notFlipped.add(i);
                }
            }
            for (int j = 0; j < cardAmount; j++) {

                if (notFlipped.size() == 0) {
                    // Zero Cards Left


                    /// WHEN ALL CARDS ARE FLIPPED!

                } else if (notFlipped.size() == 1) { // ONLY 1 CARD LEFT!
                    int newNum = random.nextInt(notFlipped.size());
                    cardCheck[notFlipped.get(newNum)] = true;
                    GreedSnackbars.createSnackBar(view, context.getString(R.string.card_Gained) + String.valueOf(notFlipped.get(newNum)), Snackbar.LENGTH_LONG).show();
                    notFlipped.remove(newNum);

                } else { // All Cards not flipped, flip some!
                    int newNum = random.nextInt(notFlipped.size() - 1);
                    cardCheck[notFlipped.get(newNum)] = true;
                    GreedSnackbars.createSnackBar(view, context.getString(R.string.card_Gained) + String.valueOf(notFlipped.get(newNum)), Snackbar.LENGTH_LONG).show();
                    notFlipped.remove(newNum);

                }
            }
            saveDeck(context);
        } else { // Take cards from user
            List<Integer> flipped = new ArrayList<>();
            flipped.clear();
            // Cycle through entire array for all variables TRUE
            // save false variables to notFlipped List

            for (int i = 1; i < cardCheck.length; i++) {

                // if boolean at i in cardCheck is TRUE
                if (cardCheck[i]) {

                    //Add position i to flipped List
                    flipped.add(i);
                }
            }
            // Take
            for (int j = 0; j < cardAmount; j++) {

                if (flipped.size() == 0) { // User Has No Cards

                } else if (flipped.size() == 1) { // User Only Has 1 Card, take it
                    int newNum = random.nextInt(flipped.size());
                    cardCheck[flipped.get(newNum)] = false;
                    GreedSnackbars.createSnackBar(view, context.getString(R.string.card_Lost) + String.valueOf(flipped.get(newNum)), Snackbar.LENGTH_LONG).show();
                    flipped.remove(newNum);

                } else { // All Cards not flipped, flip some!
                    int newNum = random.nextInt(flipped.size() - 1);
                    cardCheck[flipped.get(newNum)] = false;
                    cardCheck[99] = true;
                    GreedSnackbars.createSnackBar(view, context.getString(R.string.card_Lost) + String.valueOf(flipped.get(newNum)), Snackbar.LENGTH_SHORT).show();
                    flipped.remove(newNum);

                }
            }
            saveDeck(context);
        }
    }

    public static void useTokens(View view, int actionTokens) {

        Context context = view.getContext();
        Random rand = new Random();
        String[] messages = context.getResources().getStringArray(R.array.token_messages);

        // Load Saved Deck
        cardCheck = loadDeck(context, "bookPreferenceArray", (cardCheck.length));

        for (int i = 0; i < actionTokens; i++) {
            int n = rand.nextInt(messages.length);
            String message = messages[n];

            GreedSnackbars.createSnackBar(view, message, Snackbar.LENGTH_LONG).show();
            manipulateDeck(view, 1, false);
        }
        actionTokens = 0;
    }

    private void eventCaller() {
        Toast toast;
        Random random = new Random();

        int event = random.nextInt(6) + 1;
        // 20% chance of specific town event
        // 30% chance of random event
        // 10% chance of no event

        if (event < 3) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String currentLocation = sharedPreferences.getString(context.getString(R.string.pref_current_location_key), context.getString(R.string.pref_town_first));
            // 20% chance of town specific event
            switch (currentLocation) {
                case "Aiai":
                    event = random.nextInt(5) + 1;
                    switch (event) {
                        case 1:
                            GreedSnackbars.createSnackBar(view, "You Helped a damsel in distress!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You Helped a damsel in distress!");
                            GreedSnackbars.createSnackBar(view, "and she gave you her number!", Snackbar.LENGTH_LONG).show();
                            botText.setText("and she gave you her number!");
                            break;
                        case 2:
                            GreedSnackbars.createSnackBar(view, "You Helped a damsel in distress!", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and she gave you a card as a reward.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You Helped a damsel in distress!");
                            botText.setText("and she gave you a card as a reward.");
                            manipulateDeck(view, 1, true);
                            break;
                        case 3:
                            GreedSnackbars.createSnackBar(view, "You Helped a damsel in distress!", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and she stole a card from you!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You Helped a damsel in distress!");
                            botText.setText("and she stole a card from you!");
                            manipulateDeck(view, 1, false);
                            break;
                        case 4:
                            GreedSnackbars.createSnackBar(view, "You were caught flirty with a married woman!", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You gave away a card to settle things.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You were caught flirty with a married woman!");
                            botText.setText("You gave away a card to settle things.");
                            manipulateDeck(view, 1, false);

                            break;
                        case 5:
                            GreedSnackbars.createSnackBar(view, "You were caught flirty with a married woman!", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You managed to escape unscathed.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You were caught flirty with a married woman!");
                            botText.setText("You managed to escape unscathed.");
                            break;
                    }
                    break;
                case "Antokiba":
                    event = random.nextInt(7) + 1;
                    switch (event) {
                        case 1:
                            GreedSnackbars.createSnackBar(view, "You took part in the monthly rock-paper-scissors competition...", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and won!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You took part in the monthly rock-paper-scissors competition...");
                            botText.setText("and won!");
                            manipulateDeck(view, 1, true);
                            break;
                        case 2:
                            GreedSnackbars.createSnackBar(view, "You took part in the monthly rock-paper-scissors competition...", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and lost!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You took part in the monthly rock-paper-scissors competition...");
                            botText.setText("and lost!");

                            break;
                        case 3:
                            GreedSnackbars.createSnackBar(view, "You were challenged to a street competition...", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and Won!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You took part in the monthly rock-paper-scissors competition...");
                            botText.setText("and won!");
                            manipulateDeck(view, 1, true);
                            break;
                        case 4:
                            GreedSnackbars.createSnackBar(view, "You were challenged to a street competition...", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and lost badly.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You were challenged to a street competition...");
                            botText.setText("and lost badly.");
                            manipulateDeck(view, 1, false);

                            break;
                        case 5:
                            GreedSnackbars.createSnackBar(view, "You were challenged to a street competition...", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and lost.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You were challenged to a street competition...");
                            botText.setText("and lost.");

                            break;
                        case 6:
                            GreedSnackbars.createSnackBar(view, "You took the 30 minute food challenge...", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and passed!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You took the 30 minute food challenge...");
                            botText.setText("and passed!");

                            // Add Galgaida card

                            break;
                        case 7:
                            GreedSnackbars.createSnackBar(view, "You took the 30 minute food challenge...", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and failed!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You took the 30 minute food challenge...");
                            botText.setText("and failed!");

                            // Increase wait time due to feeling bloated.

                            break;
                    }
                    break;
                case "Dorias":
                    event = random.nextInt(6) + 1;
                    switch (event) {
                        case 1:
                            GreedSnackbars.createSnackBar(view, "You were gambling in the slot machines...", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and you won the jackpot!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You were gambling in the slot machines...");
                            botText.setText("and you won the jackpot!");
                            manipulateDeck(view, 3, true);
                            break;
                        case 2:
                            GreedSnackbars.createSnackBar(view, "You were gambling in the slot machines...", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and you won!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You were gambling in the slot machines...");
                            botText.setText("and you won!");
                            manipulateDeck(view, 1, true);
                            break;
                        case 3:
                            GreedSnackbars.createSnackBar(view, "You were gambling in the slot machines...", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and you lost!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You were gambling in the slot machines...");
                            botText.setText("and you lost!");
                            manipulateDeck(view, 1, false);
                            break;
                        case 4:
                            GreedSnackbars.createSnackBar(view, "You were gambling in the slot machines...", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and you lost badly!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You were gambling in the slot machines...");
                            botText.setText("and you lost badly!");
                            manipulateDeck(view, 2, true);
                            break;
                        case 5:
                            GreedSnackbars.createSnackBar(view, "You were gambling in the slot machines...", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and you became addicted!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You were gambling in the slot machines...");
                            botText.setText("and you became addicted!");
                            // Increase wait time due to addiction
                            break;

                        case 6:
                            GreedSnackbars.createSnackBar(view, "You were gambling in the slot machines...", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and you were caught trying to cheat!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You were gambling in the slot machines...");
                            botText.setText("and you were caught trying to cheat!");
                            // Increase wait time due to caught
                            break;

                    }
                    break;
                case "Limeiro":

                    break;
                case "Masadora":
                    event = random.nextInt(5) + 1;
                    switch (event) {
                        case 1:
                            GreedSnackbars.createSnackBar(view, "You were asked to get some monsters from the nearby mountains!", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You barely survived!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You were asked to get some monsters from the nearby mountains!");
                            botText.setText("You barely survived!");
                            break;
                        case 2:
                            GreedSnackbars.createSnackBar(view, "You were asked to get some monsters from the nearby mountains!", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and came back plentiful! (exchange for *rare* spell cards)", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You were asked to get some monsters from the nearby mountains!");
                            botText.setText("and came back plentiful! (exchange for *rare* spell cards)");
                            SpellsHelper.createRandomSpell(view, 3);

                            break;
                        case 3:
                            GreedSnackbars.createSnackBar(view, "You were asked to get some monsters from the nearby mountains!", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and came back with one! (exchange for a spell card)", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You were asked to get some monsters from the nearby mountains!");
                            botText.setText("and came back with one! (exchange for a spell card)");
                            SpellsHelper.createRandomSpell(view, 1);
                            break;
                        case 4:
                            GreedSnackbars.createSnackBar(view, "You made a deal with the spell shop owner!", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and got some spell cards.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You made a deal with the spell shop owner!");
                            botText.setText("and got some spell cards.");
                            SpellsHelper.createRandomSpell(view, 1);
                            break;
                        case 5:
                            GreedSnackbars.createSnackBar(view, "You made a deal with the spell shop owner!", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and lost a spell card.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You made a deal with the spell shop owner!");
                            botText.setText("and lost a spell card");
                            SpellsHelper.deleteRandomSpell(view);
                            break;
                    }
                    break;
                case "Rubicuta":
                    event = random.nextInt(18) + 1;
                    switch (event) {
                        case 1:
                            GreedSnackbars.createSnackBar(view, "You stop a small shop robbery", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You are rewarded!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You stop a small shop robbery");
                            botText.setText("You are rewarded!");
                            manipulateDeck(view, 1, true);
                            break;
                        case 2:
                            GreedSnackbars.createSnackBar(view, "You follow an npc into an alley...", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "He gives you 2 spell cards!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You follow an npc into an alley...");
                            botText.setText("He gives you 2 spell cards!");
                            SpellsHelper.createRandomSpell(view, 2);

                            break;
                        case 3:
                            GreedSnackbars.createSnackBar(view, "you follow an npc into an alley...", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "He knocks you unconscious!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You follow an npc into an alley...");
                            botText.setText("He knocks you unconscious!");
                            // Add time
                            break;
                        case 4:
                            GreedSnackbars.createSnackBar(view, "you follow an npc into an alley...", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "He hands you pills that revitalize you!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You follow an npc into an alley...");
                            botText.setText("He hands you pills that revitalize you!");
                            // Reset Travel Timer!
                            break;
                        case 5:
                            GreedSnackbars.createSnackBar(view, "You pray at the temple", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "Holy light rains down from above!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("Holy light rains down from above!");
                            manipulateDeck(view, 3, true);

                            break;
                        case 6:
                            GreedSnackbars.createSnackBar(view, "You pray at the temple", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You hear a voice", Snackbar.LENGTH_SHORT).show();
                            GreedSnackbars.createSnackBar(view, "Change comes from within!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "Change comes from within!");
                            SpellsHelper.createRandomSpell(view, 1);
                            break;
                        case 7:
                            GreedSnackbars.createSnackBar(view, "You pray at the temple", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You hear a voice", Snackbar.LENGTH_SHORT).show();
                            GreedSnackbars.createSnackBar(view, "To be free means to take responsibility", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "To be free means to take responsibility");
                            break;
                        case 8:
                            GreedSnackbars.createSnackBar(view, "You pray at the temple", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You hear a voice", Snackbar.LENGTH_SHORT).show();
                            GreedSnackbars.createSnackBar(view, "All we can do is live until the day we die.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "All we can do is live until the day we die.");
                            break;
                        case 9:
                            GreedSnackbars.createSnackBar(view, "You pray at the temple", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You hear a voice", Snackbar.LENGTH_SHORT).show();
                            GreedSnackbars.createSnackBar(view, "You will gain an irreplaceable Fullmetal heart.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "You will gain an irreplaceable Fullmetal heart.");
                            break;
                        case 10:
                            GreedSnackbars.createSnackBar(view, "You pray at the temple", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You hear a voice", Snackbar.LENGTH_SHORT).show();
                            GreedSnackbars.createSnackBar(view, "Fear is not evil.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "Fear is not evil.");
                            break;
                        case 11:
                            GreedSnackbars.createSnackBar(view, "You pray at the temple", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You hear a voice", Snackbar.LENGTH_SHORT).show();
                            GreedSnackbars.createSnackBar(view, "The country? The skies? You can have them.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "The country? The skies? You can have them.");
                            break;
                        case 12:
                            GreedSnackbars.createSnackBar(view, "You pray at the temple", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You hear a voice", Snackbar.LENGTH_SHORT).show();
                            GreedSnackbars.createSnackBar(view, "When do you think people die?", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "When do you think people die?");
                            break;
                        case 13:
                            GreedSnackbars.createSnackBar(view, "You pray at the temple", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You hear a voice", Snackbar.LENGTH_SHORT).show();
                            GreedSnackbars.createSnackBar(view, "I'm the villain putting on a show.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "I'm the villain putting on a show.");
                            break;
                        case 14:
                            GreedSnackbars.createSnackBar(view, "You pray at the temple", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You hear a voice", Snackbar.LENGTH_SHORT).show();
                            GreedSnackbars.createSnackBar(view, "Whatever you lose, you'll find it again.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "Whatever you lose, you'll find it again.");
                            break;
                        case 15:
                            GreedSnackbars.createSnackBar(view, "You pray at the temple", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You hear a voice", Snackbar.LENGTH_SHORT).show();
                            GreedSnackbars.createSnackBar(view, "The world is not beautiful, and that is why it is beautiful.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "The world is not beautiful, and that is why it is beautiful.");
                            break;
                        case 16:
                            GreedSnackbars.createSnackBar(view, "You pray at the temple", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You hear a voice", Snackbar.LENGTH_SHORT).show();
                            GreedSnackbars.createSnackBar(view, "We weep for the blood of a bird, but not for the blood of a fish.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "We weep for the blood of a bird, but not for the blood of a fish.");
                            break;
                        case 17:
                            GreedSnackbars.createSnackBar(view, "You pray at the temple", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You hear a voice", Snackbar.LENGTH_SHORT).show();
                            GreedSnackbars.createSnackBar(view, "Fear is freedom! Subjugation is liberation!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "Fear is freedom! Subjugation is liberation!");
                            break;
                        case 18:
                            GreedSnackbars.createSnackBar(view, "You pray at the temple", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You hear a voice", Snackbar.LENGTH_SHORT).show();
                            GreedSnackbars.createSnackBar(view, "You're gonna carry that weight.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "You're gonna carry that weight.");
                            break;
                    }
                    break;
                case "Soufrabi":
                    event = random.nextInt(4) + 1;
                    switch (event) {
                        case 1:
                            GreedSnackbars.createSnackBar(view, "You finish a request for an npc. ", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You are rewarded!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You finish a request for an npc. ");
                            botText.setText("You are rewarded!");
                            manipulateDeck(view, 1, true);
                            break;
                        case 2:
                            GreedSnackbars.createSnackBar(view, "You finish a request for an npc. ", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "The npc thanks you!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("You finish a request for an npc. ");
                            botText.setText("The npc thanks you!");
                            SpellsHelper.createRandomSpell(view, 1);

                            break;
                        case 3:
                            GreedSnackbars.createSnackBar(view, "A pirate bumps into you on the road.", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "The pirate steals a card from you.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("A pirate bumps into you on the road.");
                            botText.setText("The pirate steals a card from you.");
                            manipulateDeck(view, 1, false);
                            break;
                        case 4:
                            GreedSnackbars.createSnackBar(view, "A pirate bumps into you on the road.", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You steal a card from the pirate.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("A pirate bumps into you on the road.");
                            botText.setText("You steal a card from the pirate.");
                            manipulateDeck(view, 1, true);
                            break;

                    }
                    break;
                case "Start":
                    event = random.nextInt(6) + 1;
                    switch (event) {
                        case 1:
                            GreedSnackbars.createSnackBar(view, "A Returning Player has appeared and attacks you", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "and you were defenseless.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("A Returning Player has appeared and attacks you");
                            botText.setText("and you were defenseless.");
                            manipulateDeck(view, 1, false);
                            break;
                        case 2:
                            GreedSnackbars.createSnackBar(view, "A Returning Player has appeared and attacks you", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "you were more than prepared!", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("A Returning Player has appeared and attacks you");
                            botText.setText("you were more than prepared!");
                            manipulateDeck(view, 2, true);
                            break;
                        case 3:
                            GreedSnackbars.createSnackBar(view, "A Returning Player has appeared and attacks you", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "You escaped without harm.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("A Returning Player has appeared and attacks you");
                            botText.setText("You escaped without harm.");
                            break;
                        case 4:
                            GreedSnackbars.createSnackBar(view, "A New Player has appeared and you attack them", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "but they have no cards", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("A New Player has appeared and you attack them");
                            botText.setText("but they have no cards");
                            SpellsHelper.deleteSpell(context, 7);
                            break;
                        case 5:
                            GreedSnackbars.createSnackBar(view, "A New Player has appeared and you attack them", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "but they have no cards", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("A New Player has appeared and you attack them");
                            botText.setText("but they have no cards");
                            SpellsHelper.deleteSpell(context, 18);
                            break;
                        case 6:
                            GreedSnackbars.createSnackBar(view, "A New Player has appeared and you attack them", Snackbar.LENGTH_LONG).show();
                            GreedSnackbars.createSnackBar(view, "but you are unable to do anything.", Snackbar.LENGTH_LONG).show();
                            botTitle.setText("A New Player has appeared and you attack them");
                            botText.setText("but you are unable to do anything.");
                            break;
                    }

                    break;

            }


        } else if (event > 2 && event < 6) {
            // 30% chance of global event
            event = random.nextInt(18) + 1;

            switch (event) {
                case 1:
                    GreedSnackbars.createSnackBar(view, "You entered a Jan Ken tournament wagering some cards...", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "You Won!", Snackbar.LENGTH_LONG).show();
                    manipulateDeck(view, 1, true);
                    botTitle.setText("You entered a Jan Ken tournament wagering some cards...");
                    botText.setText("You Won!");

                    break;
                case 2:
                    GreedSnackbars.createSnackBar(view, "You entered a Jan Ken tournament wagering some cards...", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "You Lost!", Snackbar.LENGTH_LONG).show();
                    botTitle.setText("You entered a Jan Ken tournament wagering some cards...");
                    botText.setText("You Lost!");

                    manipulateDeck(view, 2, false);
                    break;
                case 3:
                    GreedSnackbars.createSnackBar(view, "Approached by The Bomber!", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "You Exchange 3 cards for your life!", Snackbar.LENGTH_LONG).show();
                    botTitle.setText("Approached by The Bomber!");
                    botText.setText("You Exchange 3 cards for your life!");
                    manipulateDeck(view, 3, false);
                    break;
                case 4:
                    GreedSnackbars.createSnackBar(view, "Approached by The Bomber!", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "You Exchange 1 card for your life!", Snackbar.LENGTH_LONG).show();
                    botTitle.setText("Approached by The Bomber!");
                    botText.setText("You Exchange 1 card for your life!");
                    manipulateDeck(view, 1, false);
                    break;
                case 5:
                    GreedSnackbars.createSnackBar(view, "Approached by The Bomber!", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "You Outwit and Escape Him!", Snackbar.LENGTH_LONG).show();
                    botTitle.setText("Approached by The Bomber!");
                    botText.setText("You Outwit and Escape Him!");
                    break;
                case 6:
                    GreedSnackbars.createSnackBar(view, "While exploring in town...", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "You found 2 cards!", Snackbar.LENGTH_LONG).show();
                    botTitle.setText("While exploring in town...");
                    botText.setText("You found 2 cards!");
                    manipulateDeck(view, 2, true);
                    break;
                case 7:
                    GreedSnackbars.createSnackBar(view, "While exploring in town...", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "You found 1 card!", Snackbar.LENGTH_LONG).show();
                    botTitle.setText("While exploring in town...");
                    botText.setText("You found 1 card!");
                    manipulateDeck(view, 1, true);
                    break;
                case 8:
                    GreedSnackbars.createSnackBar(view, "While exploring in town...", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "You dropped 2 card!", Snackbar.LENGTH_LONG).show();
                    botTitle.setText("While exploring in town...");
                    botText.setText("You dropped 2 card!");
                    manipulateDeck(view, 2, false);
                    break;
                case 9:
                    GreedSnackbars.createSnackBar(view, "While exploring in town...", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "You dropped 1 cards!", Snackbar.LENGTH_LONG).show();
                    botTitle.setText("While exploring in town...");
                    botText.setText("You dropped 1 cards!");
                    manipulateDeck(view, 1, false);
                    break;
                case 10:
                    GreedSnackbars.createSnackBar(view, "While exploring in town...", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "A thief stole 2 cards from you.", Snackbar.LENGTH_LONG).show();
                    botTitle.setText("While exploring in town...");
                    botText.setText("A thief stole 2 cards from you.");
                    manipulateDeck(view, 2, false);
                    break;
                case 11:
                    GreedSnackbars.createSnackBar(view, "While exploring in town...", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "An owl delivers a letter to you!", Snackbar.LENGTH_LONG).show();
                    botTitle.setText("While exploring in town...");
                    botText.setText("An owl delivers a letter to you!");
                    manipulateDeck(view, 1, true);
                    break;
                case 12:
                    GreedSnackbars.createSnackBar(view, "While exploring in town...", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "You tame a Bubble Horse!", Snackbar.LENGTH_LONG).show();
                    botTitle.setText("While exploring in town...");
                    botText.setText("You tame a Bubble Horse!");
                    break;
                case 13:
                    GreedSnackbars.createSnackBar(view, "While exploring in town...", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "A remote control rat fainted...", Snackbar.LENGTH_LONG).show();
                    botTitle.setText("While exploring in town...");
                    botText.setText("A remote control rat fainted...");
                    break;
                case 14:
                    GreedSnackbars.createSnackBar(view, "While exploring in town...", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "You see a King Giant White Beetle.", Snackbar.LENGTH_LONG).show();
                    botTitle.setText("While exploring in town...");
                    botText.setText("You see a King Giant White Beetle.");
                    break;
                case 15:
                    GreedSnackbars.createSnackBar(view, "Muggers corner you in an alley", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "Their mistake! you gain 2 cards.", Snackbar.LENGTH_LONG).show();
                    botTitle.setText("Muggers corner you in an alley");
                    botText.setText("Their mistake! you gain 2 cards.");
                    manipulateDeck(view, 2, true);
                    break;
                case 16:
                    GreedSnackbars.createSnackBar(view, "Muggers corner you in an alley", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "They take 1 card and allow you to pass.", Snackbar.LENGTH_LONG).show();
                    botTitle.setText("Muggers corner you in an alley");
                    botText.setText("They take 1 card and allow you to pass.");
                    manipulateDeck(view, 1, false);
                    break;
                case 17:
                    GreedSnackbars.createSnackBar(view, "You enter a jumping competition", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "You jump too high and sprain your ankle", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "it will take a day to heal.", Snackbar.LENGTH_LONG).show();
                    botTitle.setText("You enter a jumping competition");
                    botText.setText("You jump too high and sprain your ankle" + "it will take a day to heal.");
                    break;
                case 18:
                    GreedSnackbars.createSnackBar(view, "Binolt cuts and eats your hair...", Snackbar.LENGTH_LONG).show();
                    GreedSnackbars.createSnackBar(view, "Your hair looks daft now!", Snackbar.LENGTH_LONG).show();
                    botTitle.setText("Binolt cuts and eats your hair...");
                    botText.setText("Your hair looks daft now!");
                    break;
                default:
                    break;
            }
        }
    }

    public class LoadDeckRunnable implements Runnable {

        @Override
        public void run() {
            // Load Saved Deck
            cardCheck = loadDeck(context, "bookPreferenceArray", (cardCheck.length));

            // Once Deck Loaded, Call Events!
            eventCaller();
        }
    }


}
