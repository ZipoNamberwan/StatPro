package com.bps.statistikprobolinggo.indikator;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bps.statistikprobolinggo.AppUtils;
import com.bps.statistikprobolinggo.R;
import com.bps.statistikprobolinggo.RecyclerViewClickListener;
import com.bps.statistikprobolinggo.VolleySingleton;
import com.bps.statistikprobolinggo.berita.BeritaItem;
import com.bps.statistikprobolinggo.berita.ViewBeritaActivity;
import com.bps.statistikprobolinggo.brs.BrsItem;
import com.bps.statistikprobolinggo.brs.ViewBrsActivity;
import com.bps.statistikprobolinggo.indikator.section.RecentBeritaAdapter;
import com.bps.statistikprobolinggo.indikator.section.RecentBrsAdapter;
import com.bps.statistikprobolinggo.indikator.section.RecentIndikatorAdapter;
import com.bps.statistikprobolinggo.indikator.section.RecentInfografisAdapter;
import com.bps.statistikprobolinggo.indikator.section.RecentPublikasiAdapter;
import com.bps.statistikprobolinggo.indikator.section.RecentTabelAdapter;
import com.bps.statistikprobolinggo.infografis.InfografisItem;
import com.bps.statistikprobolinggo.infografis.ViewInfografisActivity;
import com.bps.statistikprobolinggo.publikasi.PublikasiItem;
import com.bps.statistikprobolinggo.publikasi.ViewPublikasiActivity;
import com.bps.statistikprobolinggo.tabelstatis.TabelItem;
import com.bps.statistikprobolinggo.tabelstatis.ViewTabelActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;


public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RequestQueue queue;
    private Context context;
    private ArrayList<Object> items;
    private final int SECTION_TITLE = 1;
    private final int INDIKATOR_SECTION = 2;
    private final int PUBLIKASI_SECTION = 3;
    private final int TABEL_SECTION = 4;
    private final int BRS_SECTION = 5;
    private final int BERITA_SECTION = 6;
    private final int INFOGRAFIS_SECTION = 7;
    private final int IN_FIGURES_PUBLICATION = 8;
    private final int MARGIN = -1;
    private ArrayList<PublikasiItem> pubList = new ArrayList<>();
    private RecentPublikasiAdapter publikasiAdapter;
    private ArrayList<TabelItem> tabelList = new ArrayList<>();
    private RecentTabelAdapter tabelAdapter;
    private ArrayList<BrsItem> brsList = new ArrayList<>();
    private RecentBeritaAdapter beritaAdapter;
    private ArrayList<BeritaItem> beritaList = new ArrayList<>();
    private RecentBrsAdapter brsAdapter;
    private ArrayList<InfografisItem> infografisList = new ArrayList<>();
    private RecentInfografisAdapter infografisAdapter;

    private ArrayList<RecentIndikatorItem> indikatorList = new ArrayList<>();
    private RecentIndikatorAdapter indikatorAdapter;
    private boolean isIndikatorApiCalled = false;
    private boolean isInFiguresPublicationApiCalled = false;

    public MainAdapter(Context context, ArrayList<Object> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case INDIKATOR_SECTION:
                view = inflater.inflate(R.layout.layout_recycler_view, parent, false);
                holder = new IndikatorViewHolder(view);
                break;
            case PUBLIKASI_SECTION:
                view = inflater.inflate(R.layout.layout_recent_publikasi_recycler_view, parent, false);
                holder = new PublikasiViewHolder(view);
                break;
            case TABEL_SECTION:
                view = inflater.inflate(R.layout.layout_recent_tabel_recycler_view, parent, false);
                holder = new TabelViewHolder(view);
                break;
            case BRS_SECTION:
                view = inflater.inflate(R.layout.layout_recent_brs_recycler_view, parent, false);
                holder = new BrsViewHolder(view);
                break;
            case BERITA_SECTION:
                view = inflater.inflate(R.layout.layout_recent_berita_recycler_view, parent, false);
                holder = new BeritaViewHolder(view);
                break;
            case INFOGRAFIS_SECTION:
                view = inflater.inflate(R.layout.layout_recent_infografis_recycler_view, parent, false);
                holder = new InfografisViewHolder(view);
                break;
            case IN_FIGURES_PUBLICATION:
                view = inflater.inflate(R.layout.layout_in_figures_publication, parent, false);
                holder = new InFiguresPublikasiViewHolder(view);
                break;
            case MARGIN:
                view = inflater.inflate(R.layout.layout_empty_view, parent, false);
                holder = new MarginViewHolder(view);
                break;
            default:
                view = inflater.inflate(R.layout.layout_home_section_title, parent, false);
                holder = new SectionTitleViewHolder(view);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == INDIKATOR_SECTION)
            indikatorView((IndikatorViewHolder) holder);
        else if (holder.getItemViewType() == PUBLIKASI_SECTION)
            publikasiView((PublikasiViewHolder) holder);
        else if (holder.getItemViewType() == TABEL_SECTION)
            tabelView((TabelViewHolder) holder);
        else if (holder.getItemViewType() == BRS_SECTION)
            brsView((BrsViewHolder) holder);
        else if (holder.getItemViewType() == BERITA_SECTION)
            beritaView((BeritaViewHolder) holder);
        else if (holder.getItemViewType() == INFOGRAFIS_SECTION)
            infografisView((InfografisViewHolder) holder);
        else if (holder.getItemViewType() == IN_FIGURES_PUBLICATION)
            inFiguresView((InFiguresPublikasiViewHolder) holder);
        else if (holder.getItemViewType() == SECTION_TITLE)
            sectionTitleView((SectionTitleViewHolder) holder, position);
    }

    private void inFiguresView(InFiguresPublikasiViewHolder holder) {
        if (!isInFiguresPublicationApiCalled) {
            queue = VolleySingleton.getInstance(context).getRequestQueue();
            inFiguresPublicationDataToArray(holder);
            isInFiguresPublicationApiCalled = true;
        }
    }

    private void infografisView(InfografisViewHolder holder) {
        if (infografisList.isEmpty()) {
            queue = VolleySingleton.getInstance(context).getRequestQueue();
            infografisAdapter = new RecentInfografisAdapter(infografisList, context, new RecyclerViewClickListener() {
                @Override
                public void onItemClick(Object object) {
                    Intent i = new Intent(context, ViewInfografisActivity.class);
                    i.putExtra(ViewInfografisActivity.INFOGRAFIS, ((InfografisItem) object));
                    i.putExtra(ViewInfografisActivity.LIST_INFOGRAFIS, infografisList);
                    context.startActivity(i);
                }
            });
            AlphaInAnimationAdapter adapter = new AlphaInAnimationAdapter(infografisAdapter);
            adapter.setDuration(500);

            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
            holder.recyclerView.setLayoutManager(staggeredGridLayoutManager);
            //holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerView.setAdapter(adapter);
            holder.shimmerFrameLayout.startShimmer();
            infografisDataToArray(holder);
        }
    }

    private void beritaView(BeritaViewHolder holder) {
        if (beritaList.isEmpty()) {
            queue = VolleySingleton.getInstance(context).getRequestQueue();
            beritaAdapter = new RecentBeritaAdapter(beritaList, context, new RecyclerViewClickListener() {
                @Override
                public void onItemClick(Object object) {
                    Intent i = new Intent(context, ViewBeritaActivity.class);
                    i.putExtra(ViewBeritaActivity.ID_BERITA, ((BeritaItem) object).getId());
                    context.startActivity(i);
                }
            });
            AlphaInAnimationAdapter adapter = new AlphaInAnimationAdapter(beritaAdapter);
            adapter.setDuration(500);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerView.setAdapter(adapter);
            holder.shimmerFrameLayout.startShimmer();
            beritaDataToArray(holder);
        }
    }

    private void brsView(BrsViewHolder holder) {
        if (brsList.isEmpty()) {
            queue = VolleySingleton.getInstance(context).getRequestQueue();
            brsAdapter = new RecentBrsAdapter(brsList, context, new RecyclerViewClickListener() {
                @Override
                public void onItemClick(Object object) {
                    Intent i = new Intent(context, ViewBrsActivity.class);
                    i.putExtra(ViewBrsActivity.ID_BRS, ((BrsItem) object).getId());
                    context.startActivity(i);
                }
            });

            AlphaInAnimationAdapter adapter = new AlphaInAnimationAdapter(brsAdapter);
            adapter.setDuration(500);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerView.setAdapter(adapter);
            holder.shimmerFrameLayout.startShimmer();
            brsDataToArray(holder);
        }
    }

    private void tabelView(TabelViewHolder holder) {
        if (tabelList.isEmpty()) {
            queue = VolleySingleton.getInstance(context).getRequestQueue();
            tabelAdapter = new RecentTabelAdapter(tabelList, context, new RecyclerViewClickListener() {
                @Override
                public void onItemClick(Object object) {
                    String s = ((TabelItem) object).getId();
                    Intent i = new Intent(context, ViewTabelActivity.class);
                    i.putExtra(ViewTabelActivity.ID_TABEL, s);
                    context.startActivity(i);
                }
            });

            AlphaInAnimationAdapter adapter = new AlphaInAnimationAdapter(tabelAdapter);
            adapter.setDuration(500);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerView.setAdapter(adapter);
            holder.shimmerFrameLayout.startShimmer();
            tabelDataToArray(holder);
        }
    }

    private void sectionTitleView(SectionTitleViewHolder holder, int position) {
        holder.textView.setText((String) items.get(position));
    }

    private void indikatorView(IndikatorViewHolder holder) {
        if (!isIndikatorApiCalled) {
            indikatorList.add(new RecentIndikatorItem("55", "Indeks Pembangunan Manusia", "", "", "0", "", new VertikalVariabel("5", ""), new TurunanVertikalVariabel("0", ""), false));
            indikatorList.add(new RecentIndikatorItem("51", "Persentase Penduduk Miskin", "", "", "0", "", new VertikalVariabel("3513", ""), new TurunanVertikalVariabel("0", ""), false));
            indikatorList.add(new RecentIndikatorItem("124", "Jumlah Penduduk", "", "", "0", "", new VertikalVariabel("3513", ""), new TurunanVertikalVariabel("43", ""), false));
            indikatorList.add(new RecentIndikatorItem("35", "Pertumbuhan Ekonomi", "", "", "0", "", new VertikalVariabel("18", ""), new TurunanVertikalVariabel("0", ""), false));
            indikatorList.add(new RecentIndikatorItem("59", "Tingkat Pengangguran", "", "", "0", "Persen", new VertikalVariabel("1", ""), new TurunanVertikalVariabel("0", ""), false));
            indikatorList.add(new RecentIndikatorItem("60", "Gini Rasio", "", "", "0", "", new VertikalVariabel("1", ""), new TurunanVertikalVariabel("0", ""), false));

            queue = VolleySingleton.getInstance(context).getRequestQueue();
            indikatorAdapter = new RecentIndikatorAdapter(indikatorList, context, new RecyclerViewClickListener() {
                @Override
                public void onItemClick(Object object) {
                    Intent i = new Intent(context, IndikatorViewActivity.class);
                    i.putExtra(IndikatorViewActivity.VAR_ID, ((RecentIndikatorItem) object).getId());
                    i.putExtra(IndikatorViewActivity.VER_VAR_ID, ((RecentIndikatorItem) object).getVerVar().getId());
                    i.putExtra(IndikatorViewActivity.TUR_VAR_ID, ((RecentIndikatorItem) object).getTurVar().getId());
                    context.startActivity(i);
                }
            });
            indikatorAdapter.notifyDataSetChanged();

            SlideInBottomAnimationAdapter animatedAdapter = new SlideInBottomAnimationAdapter(indikatorAdapter);
            animatedAdapter.setDuration(500);
            holder.recyclerView.setAdapter(new AlphaInAnimationAdapter(animatedAdapter));
            LinearLayoutManager mLayoutManager = new GridLayoutManager(context, 2);
            holder.recyclerView.setLayoutManager(mLayoutManager);
            //holder.recyclerView.setHasFixedSize(true);
            indikatorDataToArray();
            isIndikatorApiCalled = true;
        }
    }


    private void publikasiView(PublikasiViewHolder holder) {
        if (pubList.isEmpty()) {
            queue = VolleySingleton.getInstance(context).getRequestQueue();
            publikasiAdapter = new RecentPublikasiAdapter(pubList, context, new RecyclerViewClickListener() {
                @Override
                public void onItemClick(Object object) {
                    Intent i = new Intent(context, ViewPublikasiActivity.class);
                    i.putExtra(ViewPublikasiActivity.ID_PUBLIKASI, ((PublikasiItem) object).getId());
                    context.startActivity(i);
                }
            });

            AlphaInAnimationAdapter pubAdapter = new AlphaInAnimationAdapter(publikasiAdapter);
            pubAdapter.setDuration(500);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerView.setAdapter(pubAdapter);
            holder.shimmerFrameLayout.startShimmer();
            pubAddDataToArray(holder);
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < items.size()) {
            if (items.get(position) instanceof String)
                return SECTION_TITLE;
            if (items.get(position) instanceof IndikatorItem)
                return INDIKATOR_SECTION;
            if (items.get(position) instanceof PublikasiItem)
                return PUBLIKASI_SECTION;
            if (items.get(position) instanceof TabelItem)
                return TABEL_SECTION;
            if (items.get(position) instanceof BrsItem)
                return BRS_SECTION;
            if (items.get(position) instanceof BeritaItem)
                return BERITA_SECTION;
            if (items.get(position) instanceof InfografisItem)
                return INFOGRAFIS_SECTION;
            if (items.get(position) instanceof Integer)
                return IN_FIGURES_PUBLICATION;
        }
        return MARGIN;

    }

    public class MarginViewHolder extends RecyclerView.ViewHolder {

        MarginViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class PublikasiViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        ShimmerFrameLayout shimmerFrameLayout;

        PublikasiViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.inner_recyclerView);
            shimmerFrameLayout = (ShimmerFrameLayout) itemView.findViewById(R.id.shimmer);
        }
    }

    public class InFiguresPublikasiViewHolder extends RecyclerView.ViewHolder {

        TextView judul;
        TextView tgl;
        TextView nomerKatalog;
        TextView abstrak;
        ImageView cover;
        ImageButton download;
        ImageButton bookmark;
        ImageButton share;
        CardView cardView;
        ShimmerFrameLayout shimmerFrameLayout;

        InFiguresPublikasiViewHolder(View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            nomerKatalog = itemView.findViewById(R.id.nomer_katalog);
            tgl = itemView.findViewById(R.id.tanggal);
            abstrak = itemView.findViewById(R.id.abstrak);
            cover = itemView.findViewById(R.id.cover_publikasi);
            bookmark = itemView.findViewById(R.id.bookmark_button);
            download = itemView.findViewById(R.id.download_button);
            share = itemView.findViewById(R.id.share_button);
            cardView = itemView.findViewById(R.id.card_view_chat);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer);
        }
    }

    public class InfografisViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        ShimmerFrameLayout shimmerFrameLayout;

        InfografisViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.inner_recyclerView);
            shimmerFrameLayout = (ShimmerFrameLayout) itemView.findViewById(R.id.shimmer);
        }
    }

    public class BeritaViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        ShimmerFrameLayout shimmerFrameLayout;

        BeritaViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.inner_recyclerView);
            shimmerFrameLayout = (ShimmerFrameLayout) itemView.findViewById(R.id.shimmer);
        }
    }

    public class BrsViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        ShimmerFrameLayout shimmerFrameLayout;

        BrsViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.inner_recyclerView);
            shimmerFrameLayout = (ShimmerFrameLayout) itemView.findViewById(R.id.shimmer);
        }
    }

    public class TabelViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        ShimmerFrameLayout shimmerFrameLayout;

        TabelViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.inner_recyclerView);
            shimmerFrameLayout = (ShimmerFrameLayout) itemView.findViewById(R.id.shimmer);
        }
    }

    public class IndikatorViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        IndikatorViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.inner_recyclerView);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
            layoutParams.setMargins(16, 0, 16, 0);
            recyclerView.setLayoutParams(layoutParams);
        }
    }

    public class SectionTitleViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public SectionTitleViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.section_title);
        }
    }

    private void inFiguresPublicationDataToArray(InFiguresPublikasiViewHolder holder) {
        String url = context.getString(R.string.web_service_path_list_publication)
                + context.getString(R.string.api_key) + "&page=1&keyword=Probolinggo%20Dalam%20Angka";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (jsonObject.getString("data-availability").equals("available")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONArray(1);

                                PublikasiItem item = new PublikasiItem(jsonArray.getJSONObject(0).getString("pub_id"),
                                        jsonArray.getJSONObject(0).getString("title"), jsonArray.getJSONObject(0).getString("rl_date"),
                                        jsonArray.getJSONObject(0).getString("issn"), jsonArray.getJSONObject(0).getString("title"),
                                        jsonArray.getJSONObject(0).getString("issn"), jsonArray.getJSONObject(0).getString("cover"),
                                        jsonArray.getJSONObject(0).getString("pdf"), false, false);

                                holder.judul.setText(Html.fromHtml(item.getJudul()));
                                holder.tgl.setText(AppUtils.getDate(item.getTanggal(), false));
                                holder.nomerKatalog.setText(String.format("%s/%s", item.getIsbn(), item.getNomerKatalog()));
                                holder.abstrak.setText(Html.fromHtml(item.getAbstrak()));

                                Picasso.get()
                                        .load(item.getUrlCover())
                                        .error(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.md_grey_300)).icon(GoogleMaterial.Icon.gmd_broken_image))
                                        .placeholder(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.md_grey_300)).icon(GoogleMaterial.Icon.gmd_image))
                                        .fit()
                                        .into(holder.cover);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
                                    layoutParams.setMargins(5, 4, 5, 4);
                                    holder.cardView.setLayoutParams(layoutParams);
                                }

                                holder.download.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.blue)).icon(GoogleMaterial.Icon.gmd_file_download));

                                holder.share.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.green)).icon(GoogleMaterial.Icon.gmd_share));

                                holder.download.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //Do Your Code Here
                                        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        String s = item.getUrlPdf().replace("&tokenuser=", "");
                                                        String namaFile = item.getJudul().replaceAll("\\W+", "");
                                                        AppUtils.downloadFile((Activity) context, s, item.getJudul(), namaFile + ".pdf");
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

                                holder.share.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String urlShare =  AppUtils.getUrlShare(context.getString(R.string.web_share_publication), item.getTanggal(), item.getId(), item.getJudul());
                                        AppUtils.share((Activity) context, item.getJudul(), urlShare);
                                    }
                                });

                                holder.cardView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(context, ViewPublikasiActivity.class);
                                        i.putExtra(ViewPublikasiActivity.ID_PUBLIKASI, item.getId());
                                        context.startActivity(i);
                                    }
                                });

                                holder.shimmerFrameLayout.setVisibility(View.GONE);
                                holder.cardView.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        queue.add(request);
    }

    private void pubAddDataToArray(PublikasiViewHolder holder) {
        String url = context.getString(R.string.web_service_path_list_publication)
                + context.getString(R.string.api_key) + "&page=1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (jsonObject.getString("data-availability").equals("available")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONArray(1);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    pubList.add(new PublikasiItem(jsonArray.getJSONObject(i).getString("pub_id"),
                                            jsonArray.getJSONObject(i).getString("title"), jsonArray.getJSONObject(i).getString("rl_date"),
                                            jsonArray.getJSONObject(i).getString("issn"), jsonArray.getJSONObject(i).getString("title"),
                                            jsonArray.getJSONObject(i).getString("issn"), jsonArray.getJSONObject(i).getString("cover"),
                                            jsonArray.getJSONObject(i).getString("pdf"), false, false));
                                }
                                publikasiAdapter.notifyItemRangeInserted(10 + 1, jsonArray.length());
                                holder.recyclerView.setVisibility(View.VISIBLE);
                                holder.shimmerFrameLayout.stopShimmer();
                                holder.shimmerFrameLayout.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        queue.add(request);
    }

    private void indikatorDataToArray() {
        for (int index = 0; index < indikatorList.size(); index++) {
            String varId = indikatorList.get(index).getId();
            String url = context.getString(R.string.web_service_path_list_data)
                    + context.getString(R.string.api_key) + "&var=" + varId;
            int finalIndex = index;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                if (jsonObject.getString("data-availability").equals("available")) {
                                    //Setup Tahun
                                    List<TahunVariabel> tahunVariabels = new ArrayList<>();
                                    JSONArray tahunArrayJson = jsonObject.getJSONArray("tahun");
                                    for (int i = 0; i < tahunArrayJson.length(); i++) {
                                        tahunVariabels.add(new TahunVariabel(tahunArrayJson.getJSONObject(i).getString("val"),
                                                tahunArrayJson.getJSONObject(i).getString("label")));
                                    }
                                    Collections.sort(tahunVariabels, new Comparator<TahunVariabel>() {
                                        @Override
                                        public int compare(TahunVariabel o1, TahunVariabel o2) {
                                            Integer a = Integer.parseInt(o1.getLabel());
                                            Integer b = Integer.parseInt(o2.getLabel());
                                            return a.compareTo(b);
                                        }
                                    });

                                    //Setup Turunan Tahun
                                    List<TurunanTahunVariabel> turunanTahunVariabels = new ArrayList<>();
                                    JSONArray turTahunArrayJson = jsonObject.getJSONArray("turtahun");
                                    for (int i = 0; i < turTahunArrayJson.length(); i++) {
                                        turunanTahunVariabels.add(new TurunanTahunVariabel(turTahunArrayJson.getJSONObject(i).getString("val"),
                                                turTahunArrayJson.getJSONObject(i).getString("label")));
                                    }
                                    Collections.sort(turunanTahunVariabels, new Comparator<TurunanTahunVariabel>() {
                                        @Override
                                        public int compare(TurunanTahunVariabel o1, TurunanTahunVariabel o2) {
                                            Integer a = Integer.parseInt(o1.getId());
                                            Integer b = Integer.parseInt(o2.getId());
                                            return a.compareTo(b);
                                        }
                                    });

                                    TahunVariabel latestTahun = tahunVariabels.get(tahunVariabels.size() - 1);
                                    TurunanTahunVariabel latestTurTahun = turunanTahunVariabels.get(turunanTahunVariabels.size() - 1);
                                    //String judul = jsonObject.getJSONArray("var").getJSONObject(0).getString("label") + ", " + latestTurTahun.getLabel() + " " + latestTahun.getLabel();
                                    String source = jsonObject.getJSONArray("var").getJSONObject(0).getString("note");
                                    String desc = latestTurTahun.getLabel() + " " + latestTahun.getLabel();
                                    String unit = "";
                                    if (!indikatorList.get(finalIndex).getSatuan().equals("")){
                                        unit = indikatorList.get(finalIndex).getSatuan();
                                    } else {
                                        unit = jsonObject.getJSONArray("var").getJSONObject(0).getString("unit");
                                    }
                                    String idDataContent = indikatorList.get(finalIndex).getVerVar().getId() + varId + indikatorList.get(finalIndex).getTurVar().getId() + latestTahun.getId() + latestTurTahun.getId();
                                    String data = "";
                                    JSONObject dataContentJson = jsonObject.getJSONObject("datacontent");

                                    if (jsonObject.getJSONObject("datacontent").has(idDataContent)) {
                                        data = dataContentJson.getString(idDataContent).toString();
                                    }
                                    indikatorList.set(finalIndex, new RecentIndikatorItem(varId, indikatorList.get(finalIndex).getJudul(), desc, source, data, unit, indikatorList.get(finalIndex).getVerVar(), indikatorList.get(finalIndex).getTurVar(), true));
                                    indikatorAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                }
            });
            queue.add(request);
        }
    }

    private void tabelDataToArray(TabelViewHolder holder) {
        String url = context.getString(R.string.web_service_path_list_table)
                + context.getString(R.string.api_key) + "&page=1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (jsonObject.getString("data-availability").equals("available")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONArray(1);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    tabelList.add(new TabelItem(jsonArray.getJSONObject(i).getString("table_id"),
                                            jsonArray.getJSONObject(i).getString("subj"), jsonArray.getJSONObject(i).getString("updt_date"),
                                            jsonArray.getJSONObject(i).getString("title"), jsonArray.getJSONObject(i).getString("excel"),
                                            jsonArray.getJSONObject(i).getString("updt_date"), jsonArray.getJSONObject(i).getString("updt_date"),
                                            4, false, false));
                                }
                                tabelAdapter.notifyItemRangeInserted(10 + 1, jsonArray.length());
                                holder.recyclerView.setVisibility(View.VISIBLE);
                                holder.shimmerFrameLayout.stopShimmer();
                                holder.shimmerFrameLayout.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
    }

    private void brsDataToArray(BrsViewHolder holder) {
        String url = context.getString(R.string.web_service_path_list_press_release)
                + context.getString(R.string.api_key) + "&page=1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (jsonObject.getString("data-availability").equals("available")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONArray(1);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    brsList.add(new BrsItem(jsonArray.getJSONObject(i).getString("brs_id"),
                                            jsonArray.getJSONObject(i).getString("title"), jsonArray.getJSONObject(i).getString("brs_id"),
                                            jsonArray.getJSONObject(i).getString("rl_date"), jsonArray.getJSONObject(i).getString("pdf"),
                                            jsonArray.getJSONObject(i).getString("subj"), jsonArray.getJSONObject(i).getString("title"),
                                            4, false, false));
                                }
                                brsAdapter.notifyItemRangeInserted(10 + 1, jsonArray.length());
                                holder.recyclerView.setVisibility(View.VISIBLE);
                                holder.shimmerFrameLayout.stopShimmer();
                                holder.shimmerFrameLayout.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        queue.add(request);
    }

    private void beritaDataToArray(BeritaViewHolder holder) {
        String url = context.getString(R.string.web_service_path_list_news)
                + context.getString(R.string.api_key) + "&page=1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (jsonObject.getString("data-availability").equals("available")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONArray(1);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    beritaList.add(new BeritaItem(jsonArray.getJSONObject(i).getString("news_id"),
                                            jsonArray.getJSONObject(i).getString("newscat_name"), jsonArray.getJSONObject(i).getString("rl_date"),
                                            jsonArray.getJSONObject(i).getString("news_id"), jsonArray.getJSONObject(i).getString("title"),
                                            jsonArray.getJSONObject(i).getString("news"), jsonArray.getJSONObject(i).getString("news_id"),
                                            false, false));
                                }
                                beritaAdapter.notifyItemRangeInserted(10 + 1, jsonArray.length());
                                holder.recyclerView.setVisibility(View.VISIBLE);
                                holder.shimmerFrameLayout.stopShimmer();
                                holder.shimmerFrameLayout.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
    }

    private void infografisDataToArray(InfografisViewHolder holder) {
        String url = context.getString(R.string.web_service_path_list_infographics)
                + context.getString(R.string.api_key) + "&page=1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (jsonObject.getString("data-availability").equals("available")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONArray(1);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    infografisList.add(new InfografisItem(jsonArray.getJSONObject(i).getString("inf_id"),
                                            jsonArray.getJSONObject(i).getString("title"),
                                            jsonArray.getJSONObject(i).getString("img"),
                                            jsonArray.getJSONObject(i).getString("desc"),
                                            jsonArray.getJSONObject(i).getString("dl")));
                                }
                                infografisAdapter.notifyItemRangeInserted(10 + 1, jsonArray.length());
                                holder.shimmerFrameLayout.stopShimmer();
                                holder.shimmerFrameLayout.setVisibility(View.GONE);
                                holder.recyclerView.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
    }
}
