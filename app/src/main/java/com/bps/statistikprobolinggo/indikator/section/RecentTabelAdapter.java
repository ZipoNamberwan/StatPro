package com.bps.statistikprobolinggo.indikator.section;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bps.statistikprobolinggo.AppUtils;
import com.bps.statistikprobolinggo.DatabaseHelper;
import com.bps.statistikprobolinggo.R;
import com.bps.statistikprobolinggo.RecyclerViewClickListener;
import com.bps.statistikprobolinggo.tabelstatis.TabelItem;
import com.google.android.material.snackbar.Snackbar;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecentTabelAdapter extends RecyclerView.Adapter<RecentTabelAdapter.Holder> {

    private List<TabelItem> list;
    private Context context;
    private DatabaseHelper db;
    private RecyclerViewClickListener listener;

    public RecentTabelAdapter(List<TabelItem> list, Context context, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
        db = new DatabaseHelper(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recent_tabel_adapter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        TabelItem item = list.get(position);
        if (position == 0){
            holder.offset.setVisibility(View.VISIBLE);
        } else {
            holder.offset.setVisibility(View.GONE);
        }
        holder.bind(item, listener);
        holder.judul.setText(Html.fromHtml(item.getJudul()));
        holder.subjek.setText(item.getSubjek());
        holder.tanggal.setText(AppUtils.getDate(item.getTanggal(), false));
        holder.theme.setBackgroundColor(ContextCompat.getColor(context, AppUtils.getColorTheme(item.getKategori())));

        setupButton(holder.download, holder.share, holder.bookmark, item);

        setupCardView(holder.cardView);

        holder.theme.setVisibility(View.GONE);
    }

    private void setupButton(ImageButton download, ImageButton share, ImageButton bookmark, final TabelItem item) {
        if (item.isBookmarked()){
            bookmark.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_check));
        }else {
            bookmark.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_plus_outline));
        }

        download.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.blue)).icon(GoogleMaterial.Icon.gmd_file_download));

        share.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.green)).icon(GoogleMaterial.Icon.gmd_share));

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do Your Code Here
                if (item.isBookmarked()){
                    ((ImageButton)v).setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_plus_outline));
                    item.setIsBookmarked(false);
                    db.unbookmarkTabel(item.getId());
                    Snackbar.make(v, "Bookmark dihapus", Snackbar.LENGTH_LONG)
                            .setAction("Undo", null).show();
                }else {
                    ((ImageButton)v).setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_check));
                    item.setIsBookmarked(true);
                    db.bookmarkTabel(item.getId(), item.getSubjek(), item.getTanggal(), item.getJudul(),
                            item.getExcel(), item.getUrlShare(), item.getHtml(), item.getKategori(),
                            System.currentTimeMillis());
                    Snackbar.make(v, "Tabel di-bookmark", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do Your Code Here
                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                /*Intent i = new Intent(context, AuthActivity.class);
                                String token = AppUtils.getToken((Activity) context);
                                if (token==null){
                                    context.startActivity(i);
                                }else {
                                    String s = item.getExcel() + token;
                                    String namaFile = item.getJudul().replaceAll("\\W+", "");
                                    AppUtils.downloadFile((Activity) context, s, item.getJudul(), namaFile + ".xls");
                                }*/
                                String s = item.getExcel().replace("&tokenuser=", "");
                                String namaFile = item.getJudul().replaceAll("\\W+", "");
                                AppUtils.downloadFile((Activity) context, s, item.getJudul(), namaFile + ".xls");
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setTitle("Download")
                        .setMessage("Download "+item.getJudul()+"?")
                        .setPositiveButton("Ya", onClickListener)
                        .setNegativeButton("Tidak", onClickListener)
                        .show();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do Your Code Here
                AppUtils.share((Activity) context, item.getJudul(), item.getUrlShare());
            }
        });
    }

    private void setupCardView(CardView cardView) {
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
        TextView subjek;
        TextView tanggal;
        View theme;
        ImageButton share;
        ImageButton bookmark;
        CardView cardView;
        ImageButton download;
        View offset;

        public Holder(View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            subjek = itemView.findViewById(R.id.subjek_deskripsi);
            tanggal = itemView.findViewById(R.id.tanggal);
            theme = itemView.findViewById(R.id.color_theme);
            bookmark = itemView.findViewById(R.id.bookmark_button);
            share = itemView.findViewById(R.id.share_button);
            download = itemView.findViewById(R.id.download_button);
            cardView = itemView.findViewById(R.id.card_view_chat);
            offset = itemView.findViewById(R.id.offset);
        }

        public void bind(final TabelItem tabelItem, final RecyclerViewClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(tabelItem);
                }
            });
        }
    }
}
