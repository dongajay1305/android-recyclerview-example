package com.recyclerview;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.recyclerview.modal.Movie_Data;
import com.recyclerview.modal.Return_Data;
import com.recyclerview.roomdb.MovieViewModel;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Adapter_Movies mAdapter;
    SwipeRefreshLayout mswipe;
    LinearLayoutManager mlinearLayoutManager;
    private StaggeredGridLayoutManager mGridLayoutManager;

    ProgressBar progressbar;

    public static int ADVANCE_SCROLLING_COUNT = 6;

    boolean isloading = false;
    boolean nomorefeed = false;


    //Room DB
    private MovieViewModel mMovieViewModel;
    boolean is_gridview = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mswipe =  findViewById(R.id.swipe);
        mRecyclerView =  findViewById(R.id.recycler_view);
        progressbar =  findViewById(R.id.progressbar);


        mswipe.setColorSchemeResources(R.color.colorAccent, R.color.colorAccent, R.color.colorAccent);
        mswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                    refreshContent(true);


            }
        });

        mGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new Adapter_Movies(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(getResources().getInteger(R.integer.spacing_main_quarter)));


        new GetFeeds(1, false).execute();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView mRecyclerView, int newState) {
                super.onScrollStateChanged(mRecyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {
                super.onScrolled(mRecyclerView, dx, dy);

                if (!nomorefeed) {
                    int visibleItemCount = mRecyclerView.getChildCount();
                    int totalItemCount = mGridLayoutManager.getItemCount();
                    int[] firstVisibleItems = new int[2];
                    int[] firstVisibleItem_ = mGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                    int firstVisibleItem = firstVisibleItem_[0];
                    int currentpage =  mAdapter.GetPageNo();

                    if (!isloading && firstVisibleItem>10) {
                        if (visibleItemCount + firstVisibleItem >= (totalItemCount - ADVANCE_SCROLLING_COUNT)) {
                            //load more
                            isloading = true;
                            mAdapter.SetLoadmoreProgress();
                            mRecyclerView.post(new Runnable() {
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


        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

           mMovieViewModel.getAllMovies().observe(this, new Observer<List<Movie_Data>>() {
            @Override
            public void onChanged(@Nullable final List<Movie_Data> movies) {
                // Update the cached copy of the words in the adapter.
                mAdapter.setMovies(movies);
            }
        });

    }

    public void refreshContent(boolean fromswipe){

        mAdapter.RefreshData();
        mAdapter.notifyDataSetChanged();

        if(!fromswipe) {
            progressbar.setVisibility(View.VISIBLE);
        }
        mRecyclerView.setVisibility(View.GONE);

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

            if(is_gridview) {
                if (position < 2) {
                    outRect.top = dpToPx(verticalSpaceHeight);
                }

                if (spanIndex == 0) {
                    outRect.left = dpToPx(verticalSpaceHeight);
                } else {
                    outRect.right = dpToPx(verticalSpaceHeight);
                }
            }
        }
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
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
                return_data = getMovies("https://api.themoviedb.org/3/discover/movie?api_key=c7245b4549f3ef8d5957f8ae184b4cfd&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page="+page_no);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return return_data;
        }

        protected void onPostExecute(Return_Data return_data) {
            super.onPostExecute(return_data);

            try {

                if(return_data.getResults()!=null&&return_data.getResults().size()>0){
                    if(page_no==1){
                        mMovieViewModel.deleteAll();
                    }

                    mAdapter.SetPageNo(page_no);
                    mMovieViewModel.insertAll(return_data.getResults());

                }else{
                    nomorefeed = true;
                }

                mswipe.setRefreshing(false);
                isloading = false;
                progressbar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

            }catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}

