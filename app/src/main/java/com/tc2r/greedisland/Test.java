package com.tc2r.greedisland;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tc2r.greedisland.spells.SpellCardObject;
import com.tc2r.greedisland.spells.SpellsHelper;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Test extends Fragment implements View.OnClickListener {
    TextView creditTitle, creditText;
    SpellsHelper db;
    EditText spell;
    List<SpellCardObject> userSpells;

    public Test() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        creditTitle = (TextView) view.findViewById(R.id.credits_title);
        Button addSpell = (Button) view.findViewById(R.id.createButton);
        Button removeSpell = (Button) view.findViewById(R.id.removeButton);
        Button removeRndSpell = (Button) view.findViewById(R.id.removerndButton);
        spell = (EditText) view.findViewById(R.id.spellId);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.secret);

        userSpells = SpellsHelper.loadUserSpells(view.getContext());

        db = new SpellsHelper(view.getContext());
        addSpell.setOnClickListener(this);
        removeRndSpell.setOnClickListener(this);
        removeSpell.setOnClickListener(this);

        return view;

    }

    @Override
    public void onClick(View v) {
        int id;
        SpellCardObject reward;
        switch (v.getId()) {
            case R.id.createButton:
                id = Integer.parseInt(String.valueOf(spell.getText()));
                reward = db.CreateSpell(id);
                userSpells.add(reward);
                SpellsHelper.saveUserSpells(v.getContext(), userSpells);
                break;
            case R.id.removeButton:
                id = Integer.parseInt(String.valueOf(spell.getText()));
                SpellsHelper.deleteSpell(v.getContext(), id);
                break;
            case R.id.removerndButton:
                SpellsHelper.createRandomSpell(getView(), 3);
                break;
        }

    }


}
