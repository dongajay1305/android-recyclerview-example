package com.leanagriassignment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leanagriassignment.common.Constants;
import com.leanagriassignment.model.Movie_Data;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Detail_Activity extends AppCompatActivity {

    Movie_Data data;
    ImageView ivImage,ivGradient;
    FrameLayout flMain;
    TextView tvTitle, tvOverview, tvDate;
    LinearLayout llBottom;

    ImageView ivLine1, ivLine2,ivLine3;
    TextView tvPop, tvRating, tvVotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        data = (Movie_Data) getIntent().getSerializableExtra("data");

        ivImage  = findViewById(R.id.iv_image);
        flMain = findViewById(R.id.fl_main);
        tvTitle = findViewById(R.id.tv_title);
        tvOverview = findViewById(R.id.tv_overview);
        tvDate = findViewById(R.id.tv_time);
        llBottom = findViewById(R.id.ll_bottom);
        ivGradient = findViewById(R.id.iv_gradient);

        ivLine1 = findViewById(R.id.iv_line1);
        ivLine2 = findViewById(R.id.iv_line2);
        ivLine3 = findViewById(R.id.iv_line3);

        tvPop = findViewById(R.id.tv_popularity);
        tvRating = findViewById(R.id.tv_rating);
        tvVotes = findViewById(R.id.tv_votes);

        DateFormat dateFormatAPI = new SimpleDateFormat("yyyy-mm-dd");
        DateFormat dateFormatPrint = new SimpleDateFormat("dd-MMM-yyyy");
        Date dateAPI = new Date();

        try {
            dateAPI = dateFormatAPI.parse(data.getReleaseDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

            tvTitle.setText(data.getTitle());
            tvOverview.setText(data.getOverview());
            tvDate.setText("Release Date : "+dateFormatPrint.format(dateAPI));

            tvPop.setText("Popularity\n"+data.getPopularity());
            tvRating.setText("Rating\n"+data.getVoteAverage());
            tvVotes.setText("Votes\n"+data.getVoteCount());

            Picasso.get().load(Constants.IMAGE_URL_PATH+data.getPosterPath()).into(ivImage, new Callback() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onSuccess() {

                    try{
                        Bitmap bitmap = ((BitmapDrawable)ivImage.getDrawable()).getBitmap();
                        Palette.from(bitmap)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {

                                        Palette.Swatch textSwatch = palette.getDominantSwatch();
                                        Palette.Swatch textSwatchVib = palette.getVibrantSwatch();
                                        if (textSwatch == null) {
                                            textSwatch = palette.getDominantSwatch();
                                        }
                                        int colorVirant;
                                        try {
                                             colorVirant = textSwatchVib.getRgb();
                                        }catch (Exception e){
                                             colorVirant = textSwatch.getTitleTextColor();
                                        }

                                        ivLine1.setBackgroundColor(colorVirant);
                                        ivLine2.setBackgroundColor(colorVirant);
                                        ivLine3.setBackgroundColor(colorVirant);
                                        tvPop.setTextColor(colorVirant);
                                        tvRating.setTextColor(colorVirant);
                                        tvVotes.setTextColor(colorVirant);


                                        int color = textSwatch.getRgb();
                                        int transparent1 = Color.argb(255, Color.red(color), Color.green(color), Color.blue(color));
                                        int transparent2 = Color.argb(200, Color.red(color), Color.green(color), Color.blue(color));
                                        int transparent3 = Color.argb(100, Color.red(color), Color.green(color), Color.blue(color));
                                        int transparent4 = Color.argb(50, Color.red(color), Color.green(color), Color.blue(color));

                                        GradientDrawable gd = new GradientDrawable(
                                                GradientDrawable.Orientation.TOP_BOTTOM,
                                                new int[] {0x00ffffff,transparent4, transparent3, transparent2, transparent1});
                                        gd.setCornerRadius(0f);
                                        ivGradient.setBackground(gd);
                                        llBottom.setBackgroundColor(color);
                                        flMain.setBackgroundColor(color);

                                        tvTitle.setTextColor(textSwatch.getTitleTextColor());
                                        tvOverview.setTextColor(textSwatch.getBodyTextColor());
                                        tvDate.setTextColor(textSwatch.getBodyTextColor());


                                        ivLine1.setBackgroundColor(colorVirant);
                                        ivLine2.setBackgroundColor(colorVirant);
                                        ivLine3.setBackgroundColor(colorVirant);
                                        tvPop.setTextColor(textSwatch.getTitleTextColor());
                                        tvRating.setTextColor(textSwatch.getTitleTextColor());
                                        tvVotes.setTextColor(textSwatch.getTitleTextColor());


                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            Window window = getWindow();
                                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                            window.setStatusBarColor(color);
                                        }

                                    }
                                });

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(Exception e) {

                }
            });


    }
}
