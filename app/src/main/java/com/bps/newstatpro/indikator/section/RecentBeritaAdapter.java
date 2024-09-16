package com.bps.newstatpro.indikator.section;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bps.newstatpro.AppUtils;
import com.bps.newstatpro.DatabaseHelper;
import com.bps.newstatpro.R;
import com.bps.newstatpro.RecyclerViewClickListener;
import com.bps.newstatpro.berita.BeritaItem;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecentBeritaAdapter extends RecyclerView.Adapter<RecentBeritaAdapter.Holder> {

    private List<BeritaItem> list;
    private Context context;
    private DatabaseHelper db;
    private RecyclerViewClickListener listener;

    public RecentBeritaAdapter(List<BeritaItem> list, Context context, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
        db = new DatabaseHelper(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recent_berita_adapter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        BeritaItem item = list.get(position);
        if (position == 0){
            holder.offset.setVisibility(View.VISIBLE);
        } else {
            holder.offset.setVisibility(View.GONE);
        }
        holder.bind(item, listener);
        holder.judul.setText(Html.fromHtml(item.getJudul()));
        holder.tgl.setText(AppUtils.getDate(item.getTanggal(), false));
        holder.jenis.setText(item.getJenis());

        Picasso.get()
                .load(item.getUrlFoto())
                .error(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.md_grey_300)).icon(GoogleMaterial.Icon.gmd_broken_image))
                .placeholder(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.md_grey_300)).icon(GoogleMaterial.Icon.gmd_image))
                .fit()
                .into(holder.photo);

        holder.photo.setVisibility(View.GONE);

        setupCardView(holder.cardView);
    }

    private void setupCardView(CardView cardView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cardView.getLayoutParams();
            layoutParams.setMargins(5,4,5,4);
            cardView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        TextView judul;
        TextView tgl;
        TextView jenis;
        ImageView photo;
        CardView cardView;
        View offset;

        public Holder(View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul_berita);
            jenis = itemView.findViewById(R.id.jenis_berita);
            tgl = itemView.findViewById(R.id.tanggal_berita);
            photo = itemView.findViewById(R.id.foto_berita);
            cardView = itemView.findViewById(R.id.card_view_chat);
            offset = itemView.findViewById(R.id.offset);
        }

        public void bind(final BeritaItem item, final RecyclerViewClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}

