package com.tc2r.greedisland.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.tc2r.greedisland.R;
import com.tc2r.greedisland.spells.SpellsHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.makeText;

/**
 * Created by Tc2r on 2/19/2017.
 * <p>
 * Description: Handles events while user waits for travel ability to return.
 */

public class EventsManager {

    private static boolean[] cardCheck = new boolean[100];
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

    public static boolean[] LoadDeck(Context context, String arrayName, int preSize) {
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

    public static void SaveDeck(Context context) {

        SharedPreferences prefs_book = context.getSharedPreferences("UserBook", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs_book.edit();

        editor.putInt("bookPreferenceArray" + "_size", cardCheck.length);
        for (int i = 0; i < cardCheck.length; i++) {
            editor.putBoolean("bookPreferenceArray" + "_" + i, cardCheck[i]);
        }
        editor.apply();
    }

    public static void ManipulateDeck(Context context, int cardAmount, boolean givetake) {
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

                    Toast toast = makeText(context, context.getString(R.string.card_Gained) + String.valueOf(notFlipped.get(newNum)), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    notFlipped.remove(newNum);

                } else { // All Cards not flipped, flip some!
                    int newNum = random.nextInt(notFlipped.size() - 1);
                    cardCheck[notFlipped.get(newNum)] = true;

                    Toast toast = makeText(context, context.getString(R.string.card_Gained) + String.valueOf(notFlipped.get(newNum)), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    notFlipped.remove(newNum);

                }
            }
            SaveDeck(context);
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

                    Toast toast = makeText(context, context.getString(R.string.card_Lost) + String.valueOf(flipped.get(newNum)), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    flipped.remove(newNum);

                } else { // All Cards not flipped, flip some!
                    int newNum = random.nextInt(flipped.size() - 1);
                    cardCheck[flipped.get(newNum)] = false;
                    cardCheck[99] = true;

                    Toast toast = makeText(context, context.getString(R.string.card_Lost) + String.valueOf(flipped.get(newNum)), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    flipped.remove(newNum);

                }
            }
            SaveDeck(context);
        }
    }

    public static void UseTokens(Context context, int actionTokens) {
        Random rand = new Random();
        String[] messages = context.getResources().getStringArray(R.array.token_messages);

        // Load Saved Deck
        cardCheck = LoadDeck(context, "bookPreferenceArray", (cardCheck.length));

        for (int i = 0; i < actionTokens; i++) {
            int n = rand.nextInt(messages.length);
            String message = messages[n];
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            ManipulateDeck(context, 1, false);
        }
        actionTokens = 0;
    }

    private void EventCaller() {
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
                            makeText(context, "You Helped a damsel in distress!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You Helped a damsel in distress!");
                            makeText(context, "and she gave you her number!", Toast.LENGTH_LONG).show();
                            botText.setText("and she gave you her number!");
                            break;
                        case 2:
                            makeText(context, "You Helped a damsel in distress!", Toast.LENGTH_LONG).show();
                            makeText(context, "and she gave you a card as a reward.", Toast.LENGTH_LONG).show();
                            botTitle.setText("You Helped a damsel in distress!");
                            botText.setText("and she gave you a card as a reward.");
                            ManipulateDeck(context, 1, true);
                            break;
                        case 3:
                            makeText(context, "You Helped a damsel in distress!", Toast.LENGTH_LONG).show();
                            makeText(context, "and she stole a card from you!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You Helped a damsel in distress!");
                            botText.setText("and she stole a card from you!");
                            ManipulateDeck(context, 1, false);
                            break;
                        case 4:
                            makeText(context, "You were caught flirty with a married woman!", Toast.LENGTH_LONG).show();
                            makeText(context, "You gave away a card to settle things.", Toast.LENGTH_LONG).show();
                            botTitle.setText("You were caught flirty with a married woman!");
                            botText.setText("You gave away a card to settle things.");
                            ManipulateDeck(context, 1, false);

                            break;
                        case 5:
                            makeText(context, "You were caught flirty with a married woman!", Toast.LENGTH_LONG).show();
                            makeText(context, "You managed to escape unscathed.", Toast.LENGTH_LONG).show();
                            botTitle.setText("You were caught flirty with a married woman!");
                            botText.setText("You managed to escape unscathed.");
                            break;
                    }
                    break;
                case "Antokiba":
                    event = random.nextInt(7) + 1;
                    switch (event) {
                        case 1:
                            makeText(context, "You took part in the monthly rock-paper-scissors competition...", Toast.LENGTH_LONG).show();
                            makeText(context, "and won!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You took part in the monthly rock-paper-scissors competition...");
                            botText.setText("and won!");
                            ManipulateDeck(context, 1, true);
                            break;
                        case 2:
                            makeText(context, "You took part in the monthly rock-paper-scissors competition...", Toast.LENGTH_LONG).show();
                            makeText(context, "and lost!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You took part in the monthly rock-paper-scissors competition...");
                            botText.setText("and lost!");

                            break;
                        case 3:
                            makeText(context, "You were challenged to a street competition...", Toast.LENGTH_LONG).show();
                            makeText(context, "and Won!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You took part in the monthly rock-paper-scissors competition...");
                            botText.setText("and won!");
                            ManipulateDeck(context, 1, true);
                            break;
                        case 4:
                            makeText(context, "You were challenged to a street competition...", Toast.LENGTH_LONG).show();
                            makeText(context, "and lost badly.", Toast.LENGTH_LONG).show();
                            botTitle.setText("You were challenged to a street competition...");
                            botText.setText("and lost badly.");
                            ManipulateDeck(context, 1, false);

                            break;
                        case 5:
                            makeText(context, "You were challenged to a street competition...", Toast.LENGTH_LONG).show();
                            makeText(context, "and lost.", Toast.LENGTH_LONG).show();
                            botTitle.setText("You were challenged to a street competition...");
                            botText.setText("and lost.");

                            break;
                        case 6:
                            makeText(context, "You took the 30 minute food challenge...", Toast.LENGTH_LONG).show();
                            makeText(context, "and passed!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You took the 30 minute food challenge...");
                            botText.setText("and passed!");

                            // Add Galgaida card

                            break;
                        case 7:
                            makeText(context, "You took the 30 minute food challenge...", Toast.LENGTH_LONG).show();
                            makeText(context, "and failed!", Toast.LENGTH_LONG).show();
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
                            makeText(context, "You were gambling in the slot machines...", Toast.LENGTH_LONG).show();
                            makeText(context, "and you won the jackpot!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You were gambling in the slot machines...");
                            botText.setText("and you won the jackpot!");
                            ManipulateDeck(context, 3, true);
                            break;
                        case 2:
                            makeText(context, "You were gambling in the slot machines...", Toast.LENGTH_LONG).show();
                            makeText(context, "and you won!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You were gambling in the slot machines...");
                            botText.setText("and you won!");
                            ManipulateDeck(context, 1, true);
                            break;
                        case 3:
                            makeText(context, "You were gambling in the slot machines...", Toast.LENGTH_LONG).show();
                            makeText(context, "and you lost!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You were gambling in the slot machines...");
                            botText.setText("and you lost!");
                            ManipulateDeck(context, 1, false);
                            break;
                        case 4:
                            makeText(context, "You were gambling in the slot machines...", Toast.LENGTH_LONG).show();
                            makeText(context, "and you lost badly!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You were gambling in the slot machines...");
                            botText.setText("and you lost badly!");
                            ManipulateDeck(context, 2, true);
                            break;
                        case 5:
                            makeText(context, "You were gambling in the slot machines...", Toast.LENGTH_LONG).show();
                            makeText(context, "and you became addicted!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You were gambling in the slot machines...");
                            botText.setText("and you became addicted!");
                            // Increase wait time due to addiction
                            break;

                        case 6:
                            makeText(context, "You were gambling in the slot machines...", Toast.LENGTH_LONG).show();
                            makeText(context, "and you were caught trying to cheat!", Toast.LENGTH_LONG).show();
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
                            makeText(context, "You were asked to get some monsters from the nearby mountains!", Toast.LENGTH_LONG).show();
                            makeText(context, "You barely survived!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You were asked to get some monsters from the nearby mountains!");
                            botText.setText("You barely survived!");
                            break;
                        case 2:
                            makeText(context, "You were asked to get some monsters from the nearby mountains!", Toast.LENGTH_LONG).show();
                            makeText(context, "and came back plentiful! (exchange for *rare* spell cards)", Toast.LENGTH_LONG).show();
                            botTitle.setText("You were asked to get some monsters from the nearby mountains!");
                            botText.setText("and came back plentiful! (exchange for *rare* spell cards)");
                            SpellsHelper.CreateRandomSpell(context, 3);

                            break;
                        case 3:
                            makeText(context, "You were asked to get some monsters from the nearby mountains!", Toast.LENGTH_LONG).show();
                            makeText(context, "and came back with one! (exchange for a spell card)", Toast.LENGTH_LONG).show();
                            botTitle.setText("You were asked to get some monsters from the nearby mountains!");
                            botText.setText("and came back with one! (exchange for a spell card)");
                            SpellsHelper.CreateRandomSpell(context, 1);
                            break;
                        case 4:
                            makeText(context, "You made a deal with the spell shop owner!", Toast.LENGTH_LONG).show();
                            makeText(context, "and got some spell cards.", Toast.LENGTH_LONG).show();
                            botTitle.setText("You made a deal with the spell shop owner!");
                            botText.setText("and got some spell cards.");
                            SpellsHelper.CreateRandomSpell(context, 1);
                            break;
                        case 5:
                            makeText(context, "You made a deal with the spell shop owner!", Toast.LENGTH_LONG).show();
                            makeText(context, "and lost a spell card.", Toast.LENGTH_LONG).show();
                            botTitle.setText("You made a deal with the spell shop owner!");
                            botText.setText("and lost a spell card");
                            SpellsHelper.DeleteRandomSpell(context);
                            break;
                    }
                    break;
                case "Rubicuta":
                    event = random.nextInt(18) + 1;
                    switch (event) {
                        case 1:
                            makeText(context, "You stop a small shop robbery", Toast.LENGTH_LONG).show();
                            makeText(context, "You are rewarded!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You stop a small shop robbery");
                            botText.setText("You are rewarded!");
                            ManipulateDeck(context, 1, true);
                            break;
                        case 2:
                            makeText(context, "You follow an npc into an alley...", Toast.LENGTH_LONG).show();
                            makeText(context, "He gives you 2 spell cards!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You follow an npc into an alley...");
                            botText.setText("He gives you 2 spell cards!");
                            SpellsHelper.CreateRandomSpell(context, 2);

                            break;
                        case 3:
                            makeText(context, "you follow an npc into an alley...", Toast.LENGTH_LONG).show();
                            makeText(context, "He knocks you unconscious!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You follow an npc into an alley...");
                            botText.setText("He knocks you unconscious!");
                            // Add time
                            break;
                        case 4:
                            makeText(context, "you follow an npc into an alley...", Toast.LENGTH_LONG).show();
                            makeText(context, "He hands you pills that revitalize you!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You follow an npc into an alley...");
                            botText.setText("He hands you pills that revitalize you!");
                            // Reset Travel Timer!
                            break;
                        case 5:
                            makeText(context, "You pray at the temple", Toast.LENGTH_LONG).show();
                            makeText(context, "Holy light rains down from above!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("Holy light rains down from above!");
                            ManipulateDeck(context, 3, true);

                            break;
                        case 6:
                            makeText(context, "You pray at the temple", Toast.LENGTH_LONG).show();
                            makeText(context, "You hear a voice", Toast.LENGTH_SHORT).show();
                            makeText(context, "Change comes from within!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "Change comes from within!");
                            SpellsHelper.CreateRandomSpell(context, 1);
                            break;
                        case 7:
                            makeText(context, "You pray at the temple", Toast.LENGTH_LONG).show();
                            makeText(context, "You hear a voice", Toast.LENGTH_SHORT).show();
                            makeText(context, "To be free means to take responsibility", Toast.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "To be free means to take responsibility");
                            break;
                        case 8:
                            makeText(context, "You pray at the temple", Toast.LENGTH_LONG).show();
                            makeText(context, "You hear a voice", Toast.LENGTH_SHORT).show();
                            makeText(context, "All we can do is live until the day we die.", Toast.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "All we can do is live until the day we die.");
                            break;
                        case 9:
                            makeText(context, "You pray at the temple", Toast.LENGTH_LONG).show();
                            makeText(context, "You hear a voice", Toast.LENGTH_SHORT).show();
                            makeText(context, "You will gain an irreplaceable Fullmetal heart.", Toast.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "You will gain an irreplaceable Fullmetal heart.");
                            break;
                        case 10:
                            makeText(context, "You pray at the temple", Toast.LENGTH_LONG).show();
                            makeText(context, "You hear a voice", Toast.LENGTH_SHORT).show();
                            makeText(context, "Fear is not evil.", Toast.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "Fear is not evil.");
                            break;
                        case 11:
                            makeText(context, "You pray at the temple", Toast.LENGTH_LONG).show();
                            makeText(context, "You hear a voice", Toast.LENGTH_SHORT).show();
                            makeText(context, "The country? The skies? You can have them.", Toast.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "The country? The skies? You can have them.");
                            break;
                        case 12:
                            makeText(context, "You pray at the temple", Toast.LENGTH_LONG).show();
                            makeText(context, "You hear a voice", Toast.LENGTH_SHORT).show();
                            makeText(context, "When do you think people die?", Toast.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "When do you think people die?");
                            break;
                        case 13:
                            makeText(context, "You pray at the temple", Toast.LENGTH_LONG).show();
                            makeText(context, "You hear a voice", Toast.LENGTH_SHORT).show();
                            makeText(context, "I'm the villain putting on a show.", Toast.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "I'm the villain putting on a show.");
                            break;
                        case 14:
                            makeText(context, "You pray at the temple", Toast.LENGTH_LONG).show();
                            makeText(context, "You hear a voice", Toast.LENGTH_SHORT).show();
                            makeText(context, "Whatever you lose, you'll find it again.", Toast.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "Whatever you lose, you'll find it again.");
                            break;
                        case 15:
                            makeText(context, "You pray at the temple", Toast.LENGTH_LONG).show();
                            makeText(context, "You hear a voice", Toast.LENGTH_SHORT).show();
                            makeText(context, "The world is not beautiful, and that is why it is beautiful.", Toast.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "The world is not beautiful, and that is why it is beautiful.");
                            break;
                        case 16:
                            makeText(context, "You pray at the temple", Toast.LENGTH_LONG).show();
                            makeText(context, "You hear a voice", Toast.LENGTH_SHORT).show();
                            makeText(context, "We weep for the blood of a bird, but not for the blood of a fish.", Toast.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "We weep for the blood of a bird, but not for the blood of a fish.");
                            break;
                        case 17:
                            makeText(context, "You pray at the temple", Toast.LENGTH_LONG).show();
                            makeText(context, "You hear a voice", Toast.LENGTH_SHORT).show();
                            makeText(context, "Fear is freedom! Subjugation is liberation!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "Fear is freedom! Subjugation is liberation!");
                            break;
                        case 18:
                            makeText(context, "You pray at the temple", Toast.LENGTH_LONG).show();
                            makeText(context, "You hear a voice", Toast.LENGTH_SHORT).show();
                            makeText(context, "You're gonna carry that weight.", Toast.LENGTH_LONG).show();
                            botTitle.setText("You pray at the temple");
                            botText.setText("You hear a voice: \n" + "You're gonna carry that weight.");
                            break;
                    }
                    break;
                case "Soufrabi":
                    event = random.nextInt(4) + 1;
                    switch (event) {
                        case 1:
                            makeText(context, "You finish a request for an npc. ", Toast.LENGTH_LONG).show();
                            makeText(context, "You are rewarded!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You finish a request for an npc. ");
                            botText.setText("You are rewarded!");
                            ManipulateDeck(context, 1, true);
                            break;
                        case 2:
                            makeText(context, "You finish a request for an npc. ", Toast.LENGTH_LONG).show();
                            makeText(context, "The npc thanks you!", Toast.LENGTH_LONG).show();
                            botTitle.setText("You finish a request for an npc. ");
                            botText.setText("The npc thanks you!");
                            SpellsHelper.CreateRandomSpell(context, 1);

                            break;
                        case 3:
                            makeText(context, "A pirate bumps into you on the road.", Toast.LENGTH_LONG).show();
                            makeText(context, "The pirate steals a card from you.", Toast.LENGTH_LONG).show();
                            botTitle.setText("A pirate bumps into you on the road.");
                            botText.setText("The pirate steals a card from you.");
                            ManipulateDeck(context, 1, false);
                            break;
                        case 4:
                            makeText(context, "A pirate bumps into you on the road.", Toast.LENGTH_LONG).show();
                            makeText(context, "You steal a card from the pirate.", Toast.LENGTH_LONG).show();
                            botTitle.setText("A pirate bumps into you on the road.");
                            botText.setText("You steal a card from the pirate.");
                            ManipulateDeck(context, 1, true);
                            break;

                    }
                    break;
                case "Start":
                    event = random.nextInt(6) + 1;
                    switch (event) {
                        case 1:
                            makeText(context, "A Returning Player has appeared and attacks you", Toast.LENGTH_LONG).show();
                            makeText(context, "and you were defenseless.", Toast.LENGTH_LONG).show();
                            botTitle.setText("A Returning Player has appeared and attacks you");
                            botText.setText("and you were defenseless.");
                            ManipulateDeck(context, 1, false);
                            break;
                        case 2:
                            makeText(context, "A Returning Player has appeared and attacks you", Toast.LENGTH_LONG).show();
                            makeText(context, "you were more than prepared!", Toast.LENGTH_LONG).show();
                            botTitle.setText("A Returning Player has appeared and attacks you");
                            botText.setText("you were more than prepared!");
                            ManipulateDeck(context, 2, true);
                            break;
                        case 3:
                            makeText(context, "A Returning Player has appeared and attacks you", Toast.LENGTH_LONG).show();
                            makeText(context, "You escaped without harm.", Toast.LENGTH_LONG).show();
                            botTitle.setText("A Returning Player has appeared and attacks you");
                            botText.setText("You escaped without harm.");
                            break;
                        case 4:
                            makeText(context, "A New Player has appeared and you attack them", Toast.LENGTH_LONG).show();
                            makeText(context, "but they have no cards", Toast.LENGTH_LONG).show();
                            botTitle.setText("A New Player has appeared and you attack them");
                            botText.setText("but they have no cards");
                            SpellsHelper.DeleteSpell(context, 7);
                            break;
                        case 5:
                            makeText(context, "A New Player has appeared and you attack them", Toast.LENGTH_LONG).show();
                            makeText(context, "but they have no cards", Toast.LENGTH_LONG).show();
                            botTitle.setText("A New Player has appeared and you attack them");
                            botText.setText("but they have no cards");
                            SpellsHelper.DeleteSpell(context, 18);
                            break;
                        case 6:
                            makeText(context, "A New Player has appeared and you attack them", Toast.LENGTH_LONG).show();
                            makeText(context, "but you are unable to do anything.", Toast.LENGTH_LONG).show();
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
                    makeText(context, "You entered a Jan Ken tournament wagering some cards...", Toast.LENGTH_LONG).show();
                    makeText(context, "You Won!", Toast.LENGTH_LONG).show();
                    ManipulateDeck(context, 1, true);
                    botTitle.setText("You entered a Jan Ken tournament wagering some cards...");
                    botText.setText("You Won!");

                    break;
                case 2:
                    makeText(context, "You entered a Jan Ken tournament wagering some cards...", Toast.LENGTH_LONG).show();
                    makeText(context, "You Lost!", Toast.LENGTH_LONG).show();
                    botTitle.setText("You entered a Jan Ken tournament wagering some cards...");
                    botText.setText("You Lost!");

                    ManipulateDeck(context, 2, false);
                    break;
                case 3:
                    makeText(context, "Approached by The Bomber!", Toast.LENGTH_LONG).show();
                    makeText(context, "You Exchange 3 cards for your life!", Toast.LENGTH_LONG).show();
                    botTitle.setText("Approached by The Bomber!");
                    botText.setText("You Exchange 3 cards for your life!");
                    ManipulateDeck(context, 3, false);
                    break;
                case 4:
                    makeText(context, "Approached by The Bomber!", Toast.LENGTH_LONG).show();
                    makeText(context, "You Exchange 1 card for your life!", Toast.LENGTH_LONG).show();
                    botTitle.setText("Approached by The Bomber!");
                    botText.setText("You Exchange 1 card for your life!");
                    ManipulateDeck(context, 1, false);
                    break;
                case 5:
                    makeText(context, "Approached by The Bomber!", Toast.LENGTH_LONG).show();
                    makeText(context, "You Outwit and Escape Him!", Toast.LENGTH_LONG).show();
                    botTitle.setText("Approached by The Bomber!");
                    botText.setText("You Outwit and Escape Him!");
                    break;
                case 6:
                    makeText(context, "While exploring in town...", Toast.LENGTH_LONG).show();
                    makeText(context, "You found 2 cards!", Toast.LENGTH_LONG).show();
                    botTitle.setText("While exploring in town...");
                    botText.setText("You found 2 cards!");
                    ManipulateDeck(context, 2, true);
                    break;
                case 7:
                    makeText(context, "While exploring in town...", Toast.LENGTH_LONG).show();
                    makeText(context, "You found 1 card!", Toast.LENGTH_LONG).show();
                    botTitle.setText("While exploring in town...");
                    botText.setText("You found 1 card!");
                    ManipulateDeck(context, 1, true);
                    break;
                case 8:
                    makeText(context, "While exploring in town...", Toast.LENGTH_LONG).show();
                    makeText(context, "You dropped 2 card!", Toast.LENGTH_LONG).show();
                    botTitle.setText("While exploring in town...");
                    botText.setText("You dropped 2 card!");
                    ManipulateDeck(context, 2, false);
                    break;
                case 9:
                    makeText(context, "While exploring in town...", Toast.LENGTH_LONG).show();
                    makeText(context, "You dropped 1 cards!", Toast.LENGTH_LONG).show();
                    botTitle.setText("While exploring in town...");
                    botText.setText("You dropped 1 cards!");
                    ManipulateDeck(context, 1, false);
                    break;
                case 10:
                    makeText(context, "While exploring in town...", Toast.LENGTH_LONG).show();
                    makeText(context, "A thief stole 2 cards from you.", Toast.LENGTH_LONG).show();
                    botTitle.setText("While exploring in town...");
                    botText.setText("A thief stole 2 cards from you.");
                    ManipulateDeck(context, 2, false);
                    break;
                case 11:
                    makeText(context, "While exploring in town...", Toast.LENGTH_LONG).show();
                    makeText(context, "An owl delivers a letter to you!", Toast.LENGTH_LONG).show();
                    botTitle.setText("While exploring in town...");
                    botText.setText("An owl delivers a letter to you!");
                    ManipulateDeck(context, 1, true);
                    break;
                case 12:
                    makeText(context, "While exploring in town...", Toast.LENGTH_LONG).show();
                    makeText(context, "You tame a Bubble Horse!", Toast.LENGTH_LONG).show();
                    botTitle.setText("While exploring in town...");
                    botText.setText("You tame a Bubble Horse!");
                    break;
                case 13:
                    makeText(context, "While exploring in town...", Toast.LENGTH_LONG).show();
                    makeText(context, "A remote control rat fainted...", Toast.LENGTH_LONG).show();
                    botTitle.setText("While exploring in town...");
                    botText.setText("A remote control rat fainted...");
                    break;
                case 14:
                    makeText(context, "While exploring in town...", Toast.LENGTH_LONG).show();
                    makeText(context, "You see a King Giant White Beetle.", Toast.LENGTH_LONG).show();
                    botTitle.setText("While exploring in town...");
                    botText.setText("You see a King Giant White Beetle.");
                    break;
                case 15:
                    makeText(context, "Muggers corner you in an alley", Toast.LENGTH_LONG).show();
                    makeText(context, "Their mistake! you gain 2 cards.", Toast.LENGTH_LONG).show();
                    botTitle.setText("Muggers corner you in an alley");
                    botText.setText("Their mistake! you gain 2 cards.");
                    ManipulateDeck(context, 2, true);
                    break;
                case 16:
                    makeText(context, "Muggers corner you in an alley", Toast.LENGTH_LONG).show();
                    makeText(context, "They take 1 card and allow you to pass.", Toast.LENGTH_LONG).show();
                    botTitle.setText("Muggers corner you in an alley");
                    botText.setText("They take 1 card and allow you to pass.");
                    ManipulateDeck(context, 1, false);
                    break;
                case 17:
                    makeText(context, "You enter a jumping competition", Toast.LENGTH_LONG).show();
                    makeText(context, "You jump too high and sprain your ankle", Toast.LENGTH_LONG).show();
                    makeText(context, "it will take a day to heal.", Toast.LENGTH_LONG).show();
                    botTitle.setText("You enter a jumping competition");
                    botText.setText("You jump too high and sprain your ankle" + "it will take a day to heal.");
                    break;
                case 18:
                    makeText(context, "Binolt cuts and eats your hair...", Toast.LENGTH_LONG).show();
                    makeText(context, "Your hair looks daft now!", Toast.LENGTH_LONG).show();
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
            cardCheck = LoadDeck(context, "bookPreferenceArray", (cardCheck.length));

            // Once Deck Loaded, Call Events!
            EventCaller();
        }
    }


}
