package org.iesmurgi.cristichi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.iesmurgi.cristichi.stylePacks.StylePack;

import java.util.ArrayList;
import java.util.List;

public class FragmentStylePacks extends Fragment {

    private List<StylePack> packs;

    public FragmentStylePacks(){
        packs = new ArrayList<>();
    }

    public void setPacks(List<StylePack> packs) {
        this.packs = packs;
    }

    public void setPacks(StylePack... packs) {
        this.packs = new ArrayList<>(packs.length);
        for (int i=0; i<packs.length; i++)
            this.packs.add(packs[i]);
    }

    public void addPack(StylePack pack){
        packs.add(pack);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View sol = inflater.inflate(R.layout.fragment_sp, container, false);
        RecyclerView rv = sol.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        rv.setAdapter(new RecyclerViewAdapter(inflater.getContext(), packs));
        return sol;
    }
}
