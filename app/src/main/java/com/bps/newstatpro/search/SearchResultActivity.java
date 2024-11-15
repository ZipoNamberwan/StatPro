package com.bps.newstatpro.search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bps.newstatpro.MainActivity;
import com.bps.newstatpro.R;
import com.bps.newstatpro.ViewPagerAdapter;
import com.bps.newstatpro.berita.BeritaFragment;
import com.bps.newstatpro.brs.BrsFragment;
import com.bps.newstatpro.publikasi.PublikasiFragment;
import com.bps.newstatpro.tabelstatis.TabelFragment;
import com.google.android.material.tabs.TabLayout;

public class SearchResultActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra("query");
            doMySearch(query);
//        }
    }

    private void doMySearch(String query) {
        setTitle("'" + query + "'");
        Bundle args = new Bundle();
        args.putString(MainActivity.SEARCH_KEYWORD, query);

        PublikasiFragment publikasiFragment = new PublikasiFragment();
        publikasiFragment.setArguments(args);
        TabelFragment tabelFragment = new TabelFragment();
        tabelFragment.setArguments(args);
        BeritaFragment beritaFragment = new BeritaFragment();
        beritaFragment.setArguments(args);

        BrsFragment brsFragment = new BrsFragment();
        args.putBoolean(BrsFragment.IS_FIRST_FRAGMENT, true);
        brsFragment.setArguments(args);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(publikasiFragment, "Publikasi");
        viewPagerAdapter.addFragment(tabelFragment, "Tabel Statis");
        viewPagerAdapter.addFragment(brsFragment, "BRS");
        viewPagerAdapter.addFragment(beritaFragment, "Berita");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(viewPagerAdapter.getCount());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
