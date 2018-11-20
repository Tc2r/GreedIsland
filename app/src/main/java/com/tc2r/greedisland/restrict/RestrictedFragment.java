package com.tc2r.greedisland.restrict;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tc2r.greedisland.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestrictedFragment extends Fragment {

    private RestrictCardAdapter adapter;
    private List<RestrictCardObject> deck;
    private RecyclerView recyclerView;
    private HorizontalScrollView horizontalScrollView;
    private GridLayoutManager layoutManager;
    private GridLayoutManager layoutManager2;
    private RecyclerView.LayoutManager mCurrentLayoutManager;
    private boolean[] cardCheck;
    private ProgressBar progressBar;
    private TextView progressText;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    public RestrictedFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_restricted, container, false);

        deck = new ArrayList<>();

        initView(view);
        horizontalScrollView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        // Setup RecyclerView to display cards in user book.
        recyclerView.setLayoutManager(layoutManager);
        mCurrentLayoutManager = layoutManager;
        prepareCards(1);
        progressText.setVisibility(View.VISIBLE);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);


                // Due to an error in the library, Recycler's state changes aren't being
                // called at the proper time, there is lag. To prevent this and add
                // smoother scrolling of the book, the code block below is needed.
                // https://stackoverflow.com/questions/48204549/recyclerview-scroll-state-idle-is-being-called-late
                if (newState == RecyclerView.SCROLL_STATE_SETTLING)
                {
                    recyclerView.stopScroll();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


                // We handle loading from the api in batches of 30 cards, once the recyclerview has
                // the last complete position, we load more into the recyclerview.

                if (layoutManager.findLastCompletelyVisibleItemPosition() == (deck.size() - 1)) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressText.setVisibility(View.VISIBLE);
                    prepareCards((deck.get(deck.size() - 1).getId()) + 1);
                }
            }
        });


        /// MANAGING THE VISIBLE DECK!
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            cardCheck = bundle.getBooleanArray("data");
        }

        adapter = new RestrictCardAdapter(view.getContext(), deck, false, cardCheck);
        recyclerView.setAdapter(adapter);
//        //set scale gesture detector
//        ScaleGestureDetector mScaleGestureDetector = new ScaleGestureDetector(view.getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener()
//        {
//            @Override
//            public boolean onScale(ScaleGestureDetector detector)
//            {
//                if (detector.getCurrentSpan() > 200 && detector.getTimeDelta() > 200)
//                {
//                    if (detector.getCurrentSpan() - detector.getPreviousSpan() < -1)
//                    {
//                        Log.wtf("Pinch", "Working");
//                        if (mCurrentLayoutManager == layoutManager)
//                        {
//                            mCurrentLayoutManager = layoutManager2;
//                            recyclerView.setLayoutManager(layoutManager2);
//                            return true;
//                        }
//                    } else if (detector.getCurrentSpan() - detector.getPreviousSpan() > 1)
//                    {
//                        Log.wtf("Pinch", "LIES");
//                        if (mCurrentLayoutManager == layoutManager2)
//                        {
//                            mCurrentLayoutManager = layoutManager;
//                            recyclerView.setLayoutManager(layoutManager);
//                            return true;
//                        }
//                    }
//                }
//                return false;
//            }
//        });


        return view;
    }

    private void initView(View view)
    {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.hor_scrollview);
        layoutManager = new GridLayoutManager(view.getContext(), 3);
        layoutManager2 = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10), true));
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressText = (TextView) view.findViewById(R.id.progresstext);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void prepareCards(final int id) {
        // For Server Data
        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {


                OkHttpClient client = new OkHttpClient.Builder().protocols(Arrays.asList(Protocol.HTTP_1_1)).build();
                Request request = new Request.Builder().url("http://tchost.000webhostapp.com/greedget.php?id=" + (id)).build();

                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());


                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        RestrictCardObject temp = new RestrictCardObject((object.getInt("id") - 1), object.getString("name"), object.getString("rank"), object.getInt("limit"), object.getString("description"), object.getString("image"), object.getInt("type"));
                        deck.add(temp);
                        progressStatus = i + 1;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(progressStatus);

                                progressText.setText("Progress: " + progressStatus + "/" + progressBar.getMax());
                            }
                        });

                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                progressBar.setProgress(0);
                progressStatus = 0;
                progressText.setVisibility(View.GONE);
                handler.removeCallbacks(null);


            }
        };
        task.execute(id);
    }


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
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
