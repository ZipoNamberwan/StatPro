package com.bps.newstatpro.infografis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bps.newstatpro.AppUtils;
import com.bps.newstatpro.EndlessRecyclerViewScrollListener;
import com.bps.newstatpro.R;
import com.bps.newstatpro.RecyclerViewClickListener;
import com.bps.newstatpro.VolleySingleton;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

public class ViewInfografisActivity extends AppCompatActivity {


    public static final String INFOGRAFIS = "infografis";
    public static final String LIST_INFOGRAFIS = "list infografis";
    public static final String STARTING_PAGE = "starting page";

    private InfografisItem initialInfografis;
    private ArrayList<InfografisItem> initialList;
    private int initialPage;

    private ArrayList<InfografisItem> list;
    private InfografisAdapter adapter;
    private RecyclerView recyclerView;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_infografis);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        ImageView mainImage = findViewById(R.id.main_image);

        recyclerView = findViewById(R.id.listview);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager);

        initialInfografis = (InfografisItem) getIntent().getSerializableExtra(INFOGRAFIS);
        initialPage = getIntent().getIntExtra(STARTING_PAGE, 2);

        setTitle(initialInfografis.getJudul());
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Picasso.get()
                .load(initialInfografis.getGambar())
                .error(new IconicsDrawable(getApplicationContext()).color(ContextCompat.getColor(getApplicationContext(), R.color.md_grey_300)).icon(GoogleMaterial.Icon.gmd_broken_image))
                .placeholder(new IconicsDrawable(getApplicationContext()).color(ContextCompat.getColor(getApplicationContext(), R.color.md_grey_300)).icon(GoogleMaterial.Icon.gmd_image))
                .into(mainImage);
        initialList = (ArrayList<InfografisItem>) getIntent().getSerializableExtra(LIST_INFOGRAFIS);

        list = new ArrayList<>();
        list.addAll(initialList);

        adapter = new InfografisAdapter(list, this, R.layout.layout_infografis_adapter_small, new RecyclerViewClickListener() {
            @Override
            public void onItemClick(Object object) {
                initialInfografis = (InfografisItem)object;
                Picasso.get()
                        .load(((InfografisItem)object).getGambar())
                        .error(new IconicsDrawable(getApplicationContext()).color(ContextCompat.getColor(getApplicationContext(), R.color.md_grey_300)).icon(GoogleMaterial.Icon.gmd_broken_image))
                        .placeholder(new IconicsDrawable(getApplicationContext()).color(ContextCompat.getColor(getApplicationContext(), R.color.md_grey_300)).icon(GoogleMaterial.Icon.gmd_image))
                        .into(mainImage);
                setTitle(((InfografisItem)object).getJudul());
            }
        });
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
        animationAdapter.setDuration(500);
        recyclerView.setAdapter(animationAdapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager, initialPage) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                addDataToArray(page + 1);
            }
        });
        addDataToArray(initialPage);
    }

    private void addDataToArray(final int page) {
        String url = getString(R.string.web_service_path_list_infographics)
                + getString(R.string.api_key) + "&page="+page;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        addJSONToAdapter(jsonObject, page);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
    }

    private void addJSONToAdapter(JSONObject jsonObject, int page) {
        try {
            if (jsonObject.getString("data-availability").equals("available")){
                JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONArray(1);
                for (int i = 0; i < jsonArray.length(); i++){
                    list.add(new InfografisItem(jsonArray.getJSONObject(i).getString("inf_id"),
                            jsonArray.getJSONObject(i).getString("title"),
                            jsonArray.getJSONObject(i).getString("img"),
                            jsonArray.getJSONObject(i).getString("desc"),
                            jsonArray.getJSONObject(i).getString("dl")));
                }
                adapter.notifyItemRangeInserted(page * 10 + 1,jsonArray.length());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_infografis, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            AppUtils.downloadFile(this,initialInfografis.getUrlDownload(),initialInfografis.getJudul(),".jpg");
        }

        return super.onOptionsItemSelected(item);
    }
}