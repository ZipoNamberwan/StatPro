package com.bps.newstatpro.infografis;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bps.newstatpro.DatabaseHelper;
import com.bps.newstatpro.EndlessRecyclerViewScrollListener;
import com.bps.newstatpro.R;
import com.bps.newstatpro.RecyclerViewClickListener;
import com.bps.newstatpro.VolleySingleton;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

import static com.bps.newstatpro.MainActivity.SEARCH_KEYWORD;

public class InfografisFragment extends Fragment {

    private DatabaseHelper db;
    private RequestQueue queue;
    private boolean isLoading;
    private boolean isViewCreated;

    private ArrayList<InfografisItem> list;
    private InfografisAdapter adapter;
    private RecyclerView recyclerView;
    private ShimmerFrameLayout shimmerFrameLayout;
    private View failureView;
    private String keyword;
    private int currentPage;

    public InfografisFragment() {
        // Required empty public constructor
        isViewCreated = false;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_infografis, container, false);
        db = new DatabaseHelper(getContext());
        queue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        isLoading = false;
        currentPage = 0;

        if (getArguments() != null) {
            keyword = "&keyword=" + getArguments().getString(SEARCH_KEYWORD);
        } else {
            keyword = "";
        }

        failureView = view.findViewById(R.id.view_failure);
        Button refreshButton = view.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDataToArray(1);
            }
        });

        recyclerView = view.findViewById(R.id.listview);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setHasFixedSize(true);

        shimmerFrameLayout = view.findViewById(R.id.shimmer);

        list = new ArrayList<>();
        adapter = new InfografisAdapter(list, getActivity(), R.layout.layout_infografis_adapter, new RecyclerViewClickListener() {
            @Override
            public void onItemClick(Object object) {
                Intent i = new Intent(getContext(), ViewInfografisActivity.class);
                i.putExtra(ViewInfografisActivity.INFOGRAFIS, ((InfografisItem)object));
                i.putExtra(ViewInfografisActivity.LIST_INFOGRAFIS, list);
                i.putExtra(ViewInfografisActivity.STARTING_PAGE, currentPage);
                startActivity(i);
            }
        });
        SlideInBottomAnimationAdapter animatedAdapter = new SlideInBottomAnimationAdapter(adapter);
        animatedAdapter.setDuration(500);
        recyclerView.setAdapter(new AlphaInAnimationAdapter(animatedAdapter));
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                addDataToArray(page + 1);
                currentPage = page + 1;
            }
        });

        setViewVisibility(false, true, false);

        if (isVisible()) {
            addDataToArray(1);
        }

        isViewCreated = true;

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isViewCreated & isVisibleToUser) {
            if (list.isEmpty()) {
                addDataToArray(1);
            }
        }
    }

    private void addDataToArray(final int page) {
        isLoading = true;
        if (list.isEmpty()) {
            setViewVisibility(false, true, false);
        }
        String url = getString(R.string.web_service_path_list_infographics)
                + getString(R.string.api_key) + "&page=" + page + keyword;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (list.isEmpty()) {
                            setViewVisibility(true, false, false);
                        }
                        addJSONToAdapter(jsonObject, page);
                        isLoading = false;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                isLoading = false;
                if (list.isEmpty()) {
                    setViewVisibility(false, false, true);
                }
            }
        });
        queue.add(request);
    }

    private void addJSONToAdapter(JSONObject jsonObject, int page) {
        try {
            if (jsonObject.getString("data-availability").equals("available")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONArray(1);
                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(new InfografisItem(jsonArray.getJSONObject(i).getString("inf_id"),
                            jsonArray.getJSONObject(i).getString("title"),
                            jsonArray.getJSONObject(i).getString("img"),
                            jsonArray.getJSONObject(i).getString("desc"),
                            jsonArray.getJSONObject(i).getString("dl")));
                }
                adapter.notifyItemRangeInserted(page * 10 + 1, jsonArray.length());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setViewVisibility(boolean isMainVisible, boolean isShimmerVisible, boolean isFailureVisible) {

        if (isMainVisible) {
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
        }

        if (isShimmerVisible) {
            shimmerFrameLayout.startShimmer();
            shimmerFrameLayout.setVisibility(View.VISIBLE);
        } else {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        }

        if (isFailureVisible) {
            failureView.setVisibility(View.VISIBLE);
        } else {
            failureView.setVisibility(View.GONE);
        }

    }

}
