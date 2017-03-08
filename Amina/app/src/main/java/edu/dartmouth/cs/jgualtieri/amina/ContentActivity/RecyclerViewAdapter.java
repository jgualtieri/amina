package edu.dartmouth.cs.jgualtieri.amina.ContentActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import edu.dartmouth.cs.jgualtieri.amina.MainActivity;
import edu.dartmouth.cs.jgualtieri.amina.R;

/**
 * Created by azharhussain on 3/7/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private CardObject[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleTextView;
        public TextView descriptionTextView;
        public ImageView imageView;
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
            titleTextView = (TextView) v.findViewById(R.id.title);
            descriptionTextView = (TextView) v.findViewById(R.id.description);
            imageView = (ImageView) v.findViewById(R.id.thumbnail);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapter(CardObject[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {


        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT));


        ViewHolder vh = new ViewHolder(v);  //You need a cast here
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final ViewHolder vh = holder;
        vh.titleTextView.setText(mDataset[position].getTitle());
        vh.descriptionTextView.setText(mDataset[position].getDescription());
        Context context = vh.imageView.getContext();
        int id = context.getResources().getIdentifier(mDataset[position].getImagePath(), "drawable", context.getPackageName());
        vh.imageView.setImageResource(id);

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // item clicked
                MediaPlayer mp=new MediaPlayer();
                try{
                    mp.setDataSource(mDataset[position].getVideoPath());
                    mp.setScreenOnWhilePlaying(true);
                    mp.prepare();
                    mp.start();
                }
                catch (IOException e){

                }

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

