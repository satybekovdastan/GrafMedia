package info.androidhive.glide.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import info.androidhive.glide.Folder;
import info.androidhive.glide.R;
import info.androidhive.glide.activity.MainActivity;

/**
 * Created by Erlan on 23.06.2017.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {
    Context context;
    Folder vse;

    public class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView rubrica;
        ImageView image_sonku_kabar;


        PersonViewHolder(final View itemView) {
            super(itemView);

            rubrica = (TextView) itemView.findViewById(R.id.name_text);


            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    vse = new Folder();
                    int id = getAdapterPosition();
                    vse = listVse.get(id);
                    int idd = vse.getID();
                    String name = vse.getName();
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("id", idd);
                    intent.putExtra("name", name);
                    Activity activity = (Activity) context;
                    activity.startActivity(intent);
                }

            });


        }
    }

    ArrayList<Folder> listVse;

    public RVAdapter(Context context, ArrayList<Folder> listVse) {
        this.listVse = listVse;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = null;
        try {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rubrica, viewGroup, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        vse = new Folder();
        vse = listVse.get(i);
        personViewHolder.rubrica.setText(vse.getName());

    }


    @Override
    public int getItemCount() {
        return listVse.size();
    }
}