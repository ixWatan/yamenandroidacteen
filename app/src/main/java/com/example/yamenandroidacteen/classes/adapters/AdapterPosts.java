package com.example.yamenandroidacteen.classes.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.classes.interfaces.SelectListener;
import com.example.yamenandroidacteen.classes.models.ModelPost;
import com.squareup.picasso.Picasso;





import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.PostViewHolder> {

    private List<ModelPost> posts;
    private OnPostClickListener onPostClickListener;

    public AdapterPosts(List<ModelPost> posts, OnPostClickListener onPostClickListener) {
        this.posts = posts;
        this.onPostClickListener = onPostClickListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_posts, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        ModelPost post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView pImageIv;

        private TextView pTitleTv, pDateTv, pLocationTv;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            pImageIv = itemView.findViewById(R.id.pImageIv);
            pTitleTv = itemView.findViewById(R.id.pTitleTv);
            pDateTv = itemView.findViewById(R.id.pDateTv);
            pLocationTv = itemView.findViewById(R.id.pLocationTv);


            itemView.setOnClickListener(this);
        }

        public void bind(ModelPost post) {
            pTitleTv.setText(post.getpTitle());
            pDateTv.setText(deleteFirstFiveLetters(post.getpDate()));


            pLocationTv.setText(post.getpLocationLinkReal());


            // set post image

            // if there is no image dont show it
            if(post.getpImage().equals("noImage")) {

                // hide imageView
                pImageIv.setVisibility(View.GONE);

            }
            else {
                try {
                    Picasso.get().load(post.getpImage()).into(pImageIv);

                } catch (Exception e ) {

                }
            }
        }

        @Override
        public void onClick(View v) {
            if (onPostClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onPostClickListener.onPostClick(position);
                }
            }
        }
    }

    public interface OnPostClickListener {
        void onPostClick(int position);
    }

    public static String deleteFirstFiveLetters(String inputString) {
        if (inputString == null || inputString.length() <= 5) {
            return "";
        } else {
            return inputString.substring(5);
        }
    }
}















