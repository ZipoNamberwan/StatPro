package com.bps.newstatpro.infografis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bps.newstatpro.DatabaseHelper;
import com.bps.newstatpro.R;
import com.bps.newstatpro.RecyclerViewClickListener;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InfografisAdapter extends RecyclerView.Adapter<InfografisAdapter.Holder> {

    private List<InfografisItem> list;
    private Context context;
    private DatabaseHelper db;
    private RecyclerViewClickListener listener;
    private int layout;

    public InfografisAdapter(List<InfografisItem> list, Context context, int layout, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
        db = new DatabaseHelper(context);
        this.layout = layout;
        this.listener = listener;
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private final ImageView image;
        private final TextView label;

        private Holder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.infografis_image);
            label = itemView.findViewById(R.id.label);
        }

        public void bind(final InfografisItem item, final RecyclerViewClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        InfografisItem item = list.get(position);
        holder.bind(item, listener);
        holder.label.setText(item.getJudul());
        Picasso.get()
                .load(item.getGambar())
                .error(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.md_grey_300)).icon(GoogleMaterial.Icon.gmd_broken_image))
                .placeholder(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.md_grey_300)).icon(GoogleMaterial.Icon.gmd_image))
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

