package com.leanagriassignment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.leanagriassignment.common.Constants;
import com.leanagriassignment.model.Movie_Data;
import com.leanagriassignment.model.Return_Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MoviesList_Activity extends AppCompatActivity {

    private List<Movie_Data> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Adapter_Movies mAdapter;
    SwipeRefreshLayout mswipe1;
    LinearLayoutManager mlinearLayoutManager;
    private StaggeredGridLayoutManager mGridLayoutManager;
    LinearLayout llnodata;
    TextView txtrefresh;
    ProgressBar progressbar;

    public static int ADVANCE_SCROLLING_COUNT = 6;

    boolean isloading = false;
    boolean nomorefeed = false;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        pref = getSharedPreferences(Constants.PREF_NAME,0);
        editor = pref.edit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createAlertDialogForFilter();

            }
        });

        recyclerView =  findViewById(R.id.recycler_view);
        progressbar =  findViewById(R.id.progressbar);
        mswipe1 =  findViewById(R.id.swipe1);
        llnodata =  findViewById(R.id.llnodata);
        txtrefresh =  findViewById(R.id.txtrefresh);

        //mlinearLayoutManager = new LinearLayoutManager(this);
        //mlinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mGridLayoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(getResources().getInteger(R.integer.spacing_main_triple_quarter)));

        new GetFeeds(1, false).execute();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!nomorefeed&&movieList.size()>0) {
                    int visibleItemCount = recyclerView.getChildCount();
                    int totalItemCount = mGridLayoutManager.getItemCount();
                    int[] firstVisibleItems = new int[2];
                    int[] firstVisibleItem_ = mGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                    int firstVisibleItem = firstVisibleItem_[0];
                    int currentpage = (Integer) mAdapter.GetPageNo();

                    //for enable disable swiperefreshlayout
                    int topRowVerticalPosition = 0;
                    topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                    mswipe1.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);

                    if (!isloading) {
                        if (visibleItemCount + firstVisibleItem >= (totalItemCount - ADVANCE_SCROLLING_COUNT)) {
                            //load more
                            isloading = true;
                            mAdapter.SetLoadmoreProgress();
                            recyclerView.post(new Runnable() {
                                public void run() {
                                    mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
                                }
                            });
                            new GetFeeds(currentpage + 1,false).execute();
                        }
                    }

                } else {

                }

            }
        });

    }

    public void refreshContent(){

        movieList.clear();
        mAdapter.RefreshData();
        mAdapter.notifyDataSetChanged();

        progressbar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        new GetFeeds(1, false).execute();

    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {

            int position = parent.getChildAdapterPosition(view);
            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams)view .getLayoutParams();
            int spanIndex = lp.getSpanIndex();

            if(position>1){

                if(spanIndex==0){
                    outRect.left = dpToPx(verticalSpaceHeight);
                }else{
                    outRect.right = dpToPx(verticalSpaceHeight);
                }

            }


        }
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }


    public class GetFeeds extends AsyncTask<String, Void, Return_Data> {

        private Integer page_no = 1;
        private boolean isRefresh = false;

        GetFeeds(int page_number, boolean isrefresh) {
            this.page_no = page_number;
            this.isRefresh = isrefresh;
        }

        OkHttpClient client = new OkHttpClient();

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        Return_Data getMovies(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            System.out.println("Webservice Request : "+url);
            try (Response response = client.newCall(request).execute()) {
                System.out.println("Webservice Response : "+response.body().toString());
                Gson gson=new Gson();
                return  gson.fromJson(response.body().string(), Return_Data.class);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Return_Data doInBackground(String... params) {

            Return_Data return_data = new Return_Data();
            try {
                return_data = getMovies("https://api.themoviedb.org/3/discover/movie?api_key=c7245b4549f3ef8d5957f8ae184b4cfd&language=en-US&sort_by="+pref.getString(Constants.PREF_SORT,"popularity.desc")+"&include_adult=false&include_video=false&page="+page_no);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return return_data;
        }

        protected void onPostExecute(Return_Data return_data) {
            super.onPostExecute(return_data);

            try {

                movieList = return_data.getResults();

                if(movieList!=null&&movieList.size()>0){
                    if(mAdapter!=null){
                        mAdapter.SetPageNo(page_no);
                        mAdapter.AddNewData(return_data.getResults());
                        mAdapter.notifyDataSetChanged();
                    }else{
                        mAdapter = new Adapter_Movies(movieList,MoviesList_Activity.this);
                        mAdapter.SetPageNo(page_no);
                        mAdapter.RemoveLoadmoreProgress();
                        recyclerView.setAdapter(mAdapter);
                    }
                }else{
                    nomorefeed = true;
                }

                isloading = false;
                progressbar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                llnodata.setVisibility(View.GONE);

            }catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    AlertDialog alertDialog;

    CharSequence[] filter_keys = {"Popularity Ascending",
            "Popularity Descending",
            "Release Date Ascending",
            "Release Date Descending",
            "Revenue Ascending",
            "Revenue Descending",
            "Title Ascending",
            "Title Descending",
            "Vote Average Ascending",
            "Vote Average Descending"};

    CharSequence[] filter_values = {"popularity.asc",
            "popularity.desc",
            "release_date.asc",
            "release_date.desc",
            "revenue.asc",
            "revenue.desc",
            "original_title.asc",
            "original_title.desc",
            "vote_average.asc",
            "vote_average.desc"};

    public void createAlertDialogForFilter(){
        int current_selection = -1;
        for(int i=0;i<filter_values.length;i++){
            if(pref.getString(Constants.PREF_SORT,"popularity.desc").equalsIgnoreCase(filter_values[i].toString())){
                current_selection = i;

            }
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(MoviesList_Activity.this);

        builder.setTitle("Sort");

        builder.setSingleChoiceItems(filter_keys, current_selection, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                editor.putString(Constants.PREF_SORT,filter_values[item].toString());
                editor.commit();

                alertDialog.dismiss();
                refreshContent();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();

    }

}

