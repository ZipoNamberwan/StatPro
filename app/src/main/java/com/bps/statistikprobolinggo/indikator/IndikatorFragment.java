package com.bps.statistikprobolinggo.indikator;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bps.statistikprobolinggo.berita.BeritaItem;
import com.bps.statistikprobolinggo.brs.BrsItem;
import com.bps.statistikprobolinggo.infografis.InfografisItem;
import com.bps.statistikprobolinggo.publikasi.PublikasiItem;

import java.util.ArrayList;

import com.bps.statistikprobolinggo.R;
import com.bps.statistikprobolinggo.tabelstatis.TabelItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndikatorFragment extends Fragment {

    private ArrayList<Object> objects = new ArrayList<>();

    public IndikatorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_indikator, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_View);
        MainAdapter adapter = new MainAdapter(getActivity(), getObject());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private ArrayList<Object> getObject() {
        objects.add("Indikator Strategis");
        objects.add(new IndikatorItem("","","","","","", 0));
        objects.add("Semua Data Statistik Kabupaten Probolinggo Ada di Sini");
        objects.add(Integer.valueOf(1));
        objects.add("Publikasi Terbaru");
        objects.add(new PublikasiItem("","","","","","","","",false, false));
        objects.add("Infografis Terbaru");
        objects.add(new InfografisItem("","","","",""));
        objects.add("Tabel Statistik Terbaru");
        objects.add(new TabelItem("","","","","","","",0,false,false));
        objects.add("Berita Resmi Statistik (BRS) Terbaru");
        objects.add(new BrsItem("","","","","","","",4,false,false));
        objects.add("Berita");
        objects.add(new BeritaItem("","","","","","","",false, false));
        return objects;
    }
}
