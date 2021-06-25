package com.bps.statistikprobolinggo.indikator.section;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bps.statistikprobolinggo.DatabaseHelper;
import com.bps.statistikprobolinggo.R;
import com.bps.statistikprobolinggo.RecyclerViewClickListener;
import com.bps.statistikprobolinggo.infografis.InfografisItem;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecentInfografisAdapter extends RecyclerView.Adapter<RecentInfografisAdapter.Holder> {

    private List<InfografisItem> list;
    private Context context;
    private DatabaseHelper db;
    private RecyclerViewClickListener listener;

    public RecentInfografisAdapter(List<InfografisItem> list, Context context, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
        db = new DatabaseHelper(context);
        this.listener = listener;
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private final ImageView image;
        private final TextView label;
        private final View offset;

        private Holder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.infografis_image);
            label = itemView.findViewById(R.id.label);
            offset = itemView.findViewById(R.id.offset);
        }

        public void bind(final InfografisItem item, final RecyclerViewClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recent_infografis_adapter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        InfografisItem item = list.get(position);
        holder.bind(item, listener);
        if (position == 0 | position == 1){
            holder.offset.setVisibility(View.VISIBLE);
        } else {
            holder.offset.setVisibility(View.GONE);
        }
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
