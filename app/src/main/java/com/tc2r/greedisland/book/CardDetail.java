package com.tc2r.greedisland.book;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tc2r.greedisland.R;
import com.tc2r.greedisland.utils.Globals;

import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;

public class CardDetail extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

	LinearLayout activityCardDetail;
	LinearLayout carddesc_image;
	private TextView desc, rank, id, limit, title;
	private ImageView image;
	private String sDesc, sRank, sTitle, sImage;
	private int sId, sLimit, imageWidth, imageHeight;
	private SharedPreferences setting;


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

		if (key.equals(getString(R.string.pref_theme_selection_key))) {
			Globals.ChangeTheme(this);

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setting = PreferenceManager.getDefaultSharedPreferences(this);
		setting.registerOnSharedPreferenceChangeListener(this);
		onSharedPreferenceChanged(setting, getString(R.string.pref_theme_selection_key));
		setContentView(R.layout.activity_card_detail);


		desc = (TextView) findViewById(R.id.desc_desc);
		rank = (TextView) findViewById(R.id.desc_rank);
		id = (TextView) findViewById(R.id.desc_id);
		limit = (TextView) findViewById(R.id.desc_limit);
		title = (TextView) findViewById(R.id.desc_title);
		image = (ImageView) findViewById(R.id.desc_image);
		activityCardDetail = (LinearLayout) findViewById(R.id.activity_card_detail);
		carddesc_image = (LinearLayout) findViewById(R.id.carddesc_image);
		ViewTreeObserver vto = activityCardDetail.getViewTreeObserver();

		Intent intent = getIntent();

		sDesc = intent.getExtras().getString("desc");
		sRank = intent.getExtras().getString("rank");
		sId = intent.getExtras().getInt("id");
		sLimit = intent.getExtras().getInt("limit");
		sTitle = intent.getExtras().getString("title");
		sImage = intent.getExtras().getString("image");
		intent = null;

		desc.setText(sDesc);
		rank.setText("RANK: "+ sRank);
		id.setText("ID: No."+ String.valueOf(sId));
		limit.setText("LIMIT: "+ String.valueOf(sLimit));
		title.setText(sTitle);


		image.post(new Runnable() {
			@Override
			public void run() {
				imageWidth = carddesc_image.getWidth();
				imageHeight = carddesc_image.getHeight();

				Glide
								.with(getApplicationContext())
								.load(sImage)
								.centerCrop()
								.placeholder(R.drawable.placeholder)
								.bitmapTransform(new SketchFilterTransformation(getApplicationContext()))
								.into(image);
			}
		});
		vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Glide
								.with(getApplicationContext())
								.load(sImage)
								.asBitmap()
								.transform(new RotateTransformation(getApplicationContext(), 90f))
								.into(new SimpleTarget<Bitmap>(activityCardDetail.getWidth(), activityCardDetail.getHeight()) {
									@Override
									public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
										Drawable drawable = new BitmapDrawable(getApplicationContext().getResources(), resource);
										if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
											activityCardDetail.setBackground(drawable);
										}
									}
								});
				activityCardDetail.getViewTreeObserver().removeOnGlobalLayoutListener(this);

			}
		});



	}
	@Override
	protected void onResume() {
		setting = PreferenceManager.getDefaultSharedPreferences(this);
		setting.registerOnSharedPreferenceChangeListener(this);
		onSharedPreferenceChanged(setting, getString(R.string.pref_theme_selection_key));
		super.onResume();
	}

	private class RotateTransformation extends BitmapTransformation {

		private float rotateAngle = 0f;

		public RotateTransformation(Context context, float rotateAngle) {
			super(getApplicationContext());

			this.rotateAngle = rotateAngle;
		}


		@Override
		protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
			Matrix matrix = new Matrix();

			matrix.postRotate(rotateAngle);

			return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
		}

		@Override
		public String getId() {
			return "rotate" + rotateAngle;
		}
	}
}
