package com.bps.newstatpro.indikator.section;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
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

import com.bps.newstatpro.AppUtils;
import com.bps.newstatpro.DatabaseHelper;
import com.bps.newstatpro.R;
import com.bps.newstatpro.RecyclerViewClickListener;
import com.bps.newstatpro.brs.BrsItem;
import com.google.android.material.snackbar.Snackbar;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecentBrsAdapter extends RecyclerView.Adapter<RecentBrsAdapter.Holder>{

    private List<BrsItem> list;
    private Context context;
    private DatabaseHelper db;
    private RecyclerViewClickListener listener;

    public RecentBrsAdapter(List<BrsItem> list, Context context, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
        db = new DatabaseHelper(context);
        this.listener = listener;
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private TextView judul;
        private TextView tgl;
        private TextView subjek;
        private ImageButton download;
        private ImageButton bookmark;
        private ImageButton share;
        private CardView cardView;
        private View offset;


        public Holder(View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            subjek = itemView.findViewById(R.id.subjek_deskripsi);
            tgl = itemView.findViewById(R.id.tanggal);
            bookmark = itemView.findViewById(R.id.bookmark_button);
            download = itemView.findViewById(R.id.download_button);
            share = itemView.findViewById(R.id.share_button);
            cardView = itemView.findViewById(R.id.card_view_chat);
            offset = itemView.findViewById(R.id.offset);
        }

        public void bind(final BrsItem brsItem, final RecyclerViewClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(brsItem);
                }
            });
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recent_brs_adapter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        final BrsItem brsItem = list.get(position);
        if (position == 0){
            holder.offset.setVisibility(View.VISIBLE);
        } else {
            holder.offset.setVisibility(View.GONE);
        }
        holder.bind(brsItem, listener);
        holder.judul.setText(brsItem.getJudul());
        holder.subjek.setText(brsItem.getSubjek());
        holder.tgl.setText(AppUtils.getDate(brsItem.getTanggalRilis(), false));
        holder.tgl.setVisibility(View.GONE);

        if (brsItem.isBookmarked()){
            holder.bookmark.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_check));
        }else {
            holder.bookmark.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_plus_outline));
        }

        holder.download.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.blue)).icon(GoogleMaterial.Icon.gmd_file_download));

        holder.share.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.green)).icon(GoogleMaterial.Icon.gmd_share));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.cardView.getLayoutParams();
            layoutParams.setMargins(5, 4, 5, 4);
            holder.cardView.setLayoutParams(layoutParams);
        }

        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do Your Code Here
                if (brsItem.isBookmarked()) {
                    ((ImageButton) v).setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_plus_outline));
                    brsItem.setIsBookmarked(false);
                    db.unbookmarkBrs(brsItem.getId());
                    Snackbar.make(v, "Bookmark dihapus", Snackbar.LENGTH_LONG)
                            .setAction("Undo", null).show();
                } else {
                    ((ImageButton) v).setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_check));
                    brsItem.setIsBookmarked(true);
                    db.bookmarkBrs(brsItem.getId(), brsItem.getJudul(), brsItem.getAbstrak(), brsItem.getTanggalRilis(),
                            brsItem.getUrlPdf(), brsItem.getSubjek(), brsItem.getUrlShare(), brsItem.getKategori(),
                            System.currentTimeMillis());
                    Snackbar.make(v, "BRS di-bookmark", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        holder.download.setOnClickListener(new View.OnClickListener() {
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
                                    String s = brsItem.getUrlPdf() + token;
                                    String namaFile = brsItem.getJudul().replaceAll("\\W+", "");
                                    AppUtils.downloadFile((Activity) context, s, brsItem.getJudul(), namaFile + ".pdf");
                                }*/
                                String s = brsItem.getUrlPdf().replace("&tokenuser=","");
                                String namaFile = brsItem.getJudul().replaceAll("\\W+", "");
                                AppUtils.downloadFile((Activity) context, s, brsItem.getJudul(), namaFile + ".pdf");
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setTitle("Download")
                        .setMessage("Download " + brsItem.getJudul() + "?")
                        .setPositiveButton("Ya", onClickListener)
                        .setNegativeButton("Tidak", onClickListener)
                        .show();
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlShare = AppUtils.getUrlShare(context.getString(R.string.web_share_brs), brsItem.getTanggalRilis(), brsItem.getId(), brsItem.getJudul());
                AppUtils.share((Activity) context, brsItem.getJudul(), urlShare);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
