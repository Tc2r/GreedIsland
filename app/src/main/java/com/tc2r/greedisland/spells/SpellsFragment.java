package com.tc2r.greedisland.spells;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tc2r.greedisland.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpellsFragment extends Fragment implements View.OnClickListener {

    private SpellCardAdapter spellCardAdapter;
    private static List<SpellCardObject> userSpells;
    SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager, layoutManager2;
    private RecyclerView.LayoutManager mCurrentLayoutManager;
    private ScaleGestureDetector mScaleGestureDetector;
    private SpellsHelper db;
    private Handler handler = new Handler();
    private Context context;
    private Button num, limit, name, rank;


    public SpellsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_spells, container, false);
        context = view.getContext();

        db = new SpellsHelper(context);
        // Initiate Database, Call User Spells Array;
        userSpells = SpellsHelper.LoadUserSpells(context);
        Collections.sort(userSpells, new Comparator<SpellCardObject>() {
            @Override
            public int compare(SpellCardObject o1, SpellCardObject o2) {
                return o1.getCardNumber() + o2.getCardNumber();
            }
        });


        // Initiate Layout
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        layoutManager = new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false);
        layoutManager2 = new GridLayoutManager(context, 1);
        num = (Button) view.findViewById(R.id.btn_sort_num);
        name = (Button) view.findViewById(R.id.btn_sort_name);
        rank = (Button) view.findViewById(R.id.btn_sort_rank);
        limit = (Button) view.findViewById(R.id.btn_sort_limit);
        num.setOnClickListener(this);
        name.setOnClickListener(this);
        limit.setOnClickListener(this);
        rank.setOnClickListener(this);
        mCurrentLayoutManager = layoutManager;
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10), true));
        recyclerView.setLayoutManager(layoutManager);


        // Assign Adapter
        spellCardAdapter = new SpellCardAdapter(userSpells, context);
        recyclerView.setAdapter(spellCardAdapter);


        return view;
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()) {
            case R.id.btn_sort_num:
            case R.id.btn_sort_name:
            case R.id.btn_sort_rank:
            case R.id.btn_sort_limit:

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                final String ORDER = "SSABCDEFGH";

                if (sharedPreferences.getBoolean("Sort", true)) {
                    Log.wtf("Testing", "equals true");
                    Collections.sort(userSpells, new Comparator<SpellCardObject>() {
                        @Override
                        public int compare(SpellCardObject c1, SpellCardObject c2) {
                            if (v.getId() == R.id.btn_sort_num)
                                return c1.getCardNumber() - c2.getCardNumber();
                            if (v.getId() == R.id.btn_sort_name)
                                return c1.getName().compareTo(c2.getName());
                            if (v.getId() == R.id.btn_sort_rank)
                                return ORDER.indexOf(c1.getRank()) - ORDER.indexOf(c2.getRank());
                            if (v.getId() == R.id.btn_sort_limit)
                                return c1.getLimit() - c2.getLimit();

                            return c1.getCardNumber() - c2.getCardNumber();
                        }

                    });
                    editor.putBoolean("Sort", false);
                    editor.apply();
                } else {
                    Log.wtf("Testing", "equals false");
                    Collections.sort(userSpells, new Comparator<SpellCardObject>() {
                        @Override
                        public int compare(SpellCardObject c1, SpellCardObject c2) {
                            if (v.getId() == R.id.btn_sort_num)
                                return c2.getCardNumber() - c1.getCardNumber();
                            if (v.getId() == R.id.btn_sort_name)
                                return c2.getName().compareTo(c1.getName());
                            if (v.getId() == R.id.btn_sort_rank)
                                return ORDER.indexOf(c2.getRank()) - ORDER.indexOf(c1.getRank());
                            if (v.getId() == R.id.btn_sort_limit)
                                return c2.getLimit() - c1.getLimit();

                            return c2.getCardNumber() - c1.getCardNumber();
                        }
                    });
                    editor.putBoolean("Sort", true);
                    editor.apply();
                }
                spellCardAdapter = new SpellCardAdapter(userSpells, context);

                recyclerView.setAdapter(spellCardAdapter);
                recyclerView.scrollToPosition(0);
                recyclerView.invalidate();

                break;

        }
    }

    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        private GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
