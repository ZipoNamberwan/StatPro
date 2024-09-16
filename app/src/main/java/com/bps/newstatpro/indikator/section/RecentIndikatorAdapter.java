package com.bps.newstatpro.indikator.section;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bps.newstatpro.AppUtils;
import com.bps.newstatpro.R;
import com.bps.newstatpro.RecyclerViewClickListener;
import com.bps.newstatpro.indikator.RecentIndikatorItem;
import com.bps.newstatpro.indikator.IndikatorViewActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecentIndikatorAdapter extends RecyclerView.Adapter<RecentIndikatorAdapter.Holder> {

    private List<RecentIndikatorItem> list;
    private Context context;
    private RecyclerViewClickListener listener;

    public RecentIndikatorAdapter(List<RecentIndikatorItem> list, Context context, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recent_indikator_adapter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        final RecentIndikatorItem item = list.get(position);
        holder.bind(item, listener);
        if (item.isLoaded()) {
            holder.cardView.setVisibility(View.VISIBLE);
            if (position == 0) {
                holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.md_green_100));
                Drawable background = holder.nilai.getBackground();
                background.mutate();
                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable)background).getPaint().setColor(context.getResources().getColor(R.color.md_green_200));
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable)background).setColor(context.getResources().getColor(R.color.md_green_200));
                } else if (background instanceof ColorDrawable) {
                    ((ColorDrawable)background).setColor(context.getResources().getColor(R.color.md_green_200));
                }
            } else if (position == 1) {
                holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.md_orange_100));
                Drawable background = holder.nilai.getBackground();
                background.mutate();
                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable)background).getPaint().setColor(context.getResources().getColor(R.color.md_orange_200));
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable)background).setColor(context.getResources().getColor(R.color.md_orange_200));
                } else if (background instanceof ColorDrawable) {
                    ((ColorDrawable)background).setColor(context.getResources().getColor(R.color.md_orange_200));
                }
            } else if (position == 2) {
                holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.md_blue_100));
                Drawable background = holder.nilai.getBackground();
                background.mutate();
                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable)background).getPaint().setColor(context.getResources().getColor(R.color.md_blue_200));
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable)background).setColor(context.getResources().getColor(R.color.md_blue_200));
                } else if (background instanceof ColorDrawable) {
                    ((ColorDrawable)background).setColor(context.getResources().getColor(R.color.md_blue_200));
                }
            } else if (position == 3) {
                holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.md_yellow_100));
                Drawable background = holder.nilai.getBackground();
                background.mutate();
                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable)background).getPaint().setColor(context.getResources().getColor(R.color.md_yellow_300));
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable)background).setColor(context.getResources().getColor(R.color.md_yellow_300));
                } else if (background instanceof ColorDrawable) {
                    ((ColorDrawable)background).setColor(context.getResources().getColor(R.color.md_yellow_300));
                }
            } else if (position == 4) {
                holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.md_cyan_100));
                Drawable background = holder.nilai.getBackground();
                background.mutate();
                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable)background).getPaint().setColor(context.getResources().getColor(R.color.md_cyan_200));
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable)background).setColor(context.getResources().getColor(R.color.md_cyan_200));
                } else if (background instanceof ColorDrawable) {
                    ((ColorDrawable)background).setColor(context.getResources().getColor(R.color.md_cyan_200));
                }
            } else if (position == 5) {
                holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.md_red_100));
                Drawable background = holder.nilai.getBackground();
                background.mutate();
                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable)background).getPaint().setColor(context.getResources().getColor(R.color.md_red_200));
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable)background).setColor(context.getResources().getColor(R.color.md_red_200));
                } else if (background instanceof ColorDrawable) {
                    ((ColorDrawable)background).setColor(context.getResources().getColor(R.color.md_red_200));
                }
            }
            holder.shimmerFrameLayout.setVisibility(View.GONE);
            holder.shimmerFrameLayout.stopShimmer();
            holder.judul.setText(item.getJudul());

            holder.nilai.setText(AppUtils.formatNumberSeparator(Float.parseFloat(item.getNilai())));
            holder.deskripsi.setText(item.getDeskripsi());

            if (item.getSatuan().equals("Tidak Ada Satuan") | item.getSatuan().equals("")) {
                holder.satuan.setText("");
                //holder.satuan.setVisibility(View.GONE);
            } else {
                holder.satuan.setText(item.getSatuan());
            }
            if (!item.getSumber().equals("")) {
                holder.sumber.setText("*Sumber: " + item.getSumber());
            } else {
                holder.sumber.setText("*Sumber: BPS Kab Probolinggo");
            }

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PopupMenu popup = new PopupMenu(context, holder.button);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.pop_up_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            Intent i = new Intent(context, IndikatorViewActivity.class);
                            i.putExtra(IndikatorViewActivity.VAR_ID, item.getId());
                            i.putExtra(IndikatorViewActivity.VER_VAR_ID, item.getVerVar().getId());
                            i.putExtra(IndikatorViewActivity.TUR_VAR_ID, item.getTurVar().getId());
                            context.startActivity(i);
                            return true;
                        }
                    });

                    popup.show();
                }
            });
        } else {
            holder.shimmerFrameLayout.startShimmer();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private TextView judul;
        private TextView nilai;
        private TextView deskripsi;
        private TextView satuan;
        private TextView sumber;
        private ImageButton button;
        private CardView cardView;
        private ShimmerFrameLayout shimmerFrameLayout;

        public Holder(View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            nilai = itemView.findViewById(R.id.nilai);
            deskripsi = itemView.findViewById(R.id.description);
            satuan = itemView.findViewById(R.id.satuan);
            sumber = itemView.findViewById(R.id.sumber);
            button = itemView.findViewById(R.id.more_button);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer);
            cardView = itemView.findViewById(R.id.card_view);
        }

        public void bind(final RecentIndikatorItem bukuItem, final RecyclerViewClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(bukuItem);
                }
            });
        }
    }
}
