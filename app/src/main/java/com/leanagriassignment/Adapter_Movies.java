package com.leanagriassignment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.leanagriassignment.common.Constants;
import com.leanagriassignment.modal.Movie_Data;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class Adapter_Movies extends RecyclerView.Adapter<Adapter_Movies.ViewHolder> {

    //private List<Movie_Data> moviesList;
    private List<Movie_Data> moviesList = Collections.emptyList();
    private int mpage_no;
    private static final int TYPE_ARTICLE = 1;
    private static final int TYPE_LOADER = 0;
    Context context;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivImage;
        public TextView tvTitle, tvOverview, tvDate;
        public ProgressBar progressBar;
        public CardView cardview;

        public ViewHolder(View view,int type) {
            super(view);
            if(type== TYPE_ARTICLE){
                ivImage = view.findViewById(R.id.iv_image);
                tvTitle = view.findViewById(R.id.tv_title);
                tvOverview =  view.findViewById(R.id.tv_overview);
                tvDate =  view.findViewById(R.id.tv_date);
                cardview = view.findViewById(R.id.card_view);

                cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    Intent i = new Intent(context,Detail_Activity.class);
                    i.putExtra("data", (Serializable) moviesList.get(getAdapterPosition()));
                    context.startActivity(i);

                    }
                });

            }else if(type == TYPE_LOADER){
                progressBar = view.findViewById(R.id.progressBar);
            }
        }
    }

    private final LayoutInflater mInflater;
    public Adapter_Movies(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        moviesList = new ArrayList<>();
    }

    public Adapter_Movies(List<Movie_Data> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if(viewType==TYPE_ARTICLE){
//           if (((MoviesList_Activity)context).is_gridview){
                itemView = mInflater.inflate(R.layout.list_item_movie, parent, false);
//            }else{
//               itemView = mInflater.inflate(R.layout.list_item_movie_list, parent, false);
//            }
        }else if(viewType==TYPE_LOADER){
            itemView = mInflater.inflate(R.layout.list_item_progressbar, parent, false);
        }else{
            itemView = mInflater.inflate(R.layout.list_item_none, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView,viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(moviesList.get(position) == null){

            holder.progressBar.setIndeterminate(true);

            return;

        }else {
            Movie_Data movie = moviesList.get(position);
            if(movie!=null) {
                holder.tvTitle .setText(movie.getTitle());
                holder.tvOverview.setText(movie.getOverview());

                DateFormat dateFormatAPI = new SimpleDateFormat("yyyy-mm-dd");
                DateFormat dateFormatPrint = new SimpleDateFormat("dd-MMM-yyyy");
                Date dateAPI = new Date();

                String printdate = "";
                try {
                    dateAPI = dateFormatAPI.parse(movie.getReleaseDate());
                    printdate = dateFormatPrint.format(dateAPI);
                } catch (ParseException e) {
                    e.printStackTrace();
                    printdate = "N/A";
                }

                holder.tvDate.setText(printdate);
                String url=Constants.IMAGE_URL_PATH+moviesList.get(position).getPosterPath();
                Log.e("my log","picture path : "+url);
                Picasso.get().load(url).placeholder(R.color.placeholder).fit().into(((ViewHolder) holder).ivImage);

            }
        }

    }

    @Override
    public int getItemCount() {
            return moviesList.size();
    }

    public void SetPageNo(int page_no){
        mpage_no=page_no;
    }

    public int GetPageNo(){
        return mpage_no;
    }

    @Override
    public int getItemViewType(int position) {
        if(moviesList.get(position)!=null){
        return TYPE_ARTICLE;
        } else{
            return TYPE_LOADER;
        }
    }

    public void RefreshData(){
        this.moviesList.clear();
    }

    public void addNewData(List<Movie_Data> mMoviesList){
        RemoveLoadmoreProgress();
        for(Movie_Data data:mMoviesList){
            if(!moviesList.contains(data)){
                moviesList.add(data);
            }
        }
        notifyDataSetChanged();
    }

    public void SetLoadmoreProgress(){
        if(moviesList.get(moviesList.size() - 1) != null) {
            this.moviesList.add(null);
        }
    }

    void setMovies(List<Movie_Data> movies) {
        moviesList = movies;
        notifyDataSetChanged();
    }

    public void RemoveLoadmoreProgress(){
                try {
                    if (moviesList != null && moviesList.size() > 0) {
                        if (moviesList.get(moviesList.size() - 1) == null) {
                            this.moviesList.remove(moviesList.size() - 1);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
    }
}