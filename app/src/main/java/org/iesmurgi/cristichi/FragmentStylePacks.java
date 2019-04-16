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

public class FragmentStylePacks extends Fragment {

    private ArrayList<StylePack> packs;

    RecyclerView rv;

    public FragmentStylePacks(){
        packs = new ArrayList<>();
    }

    public void setPacks(StylePack... packs) {
        this.packs = new ArrayList<>(packs.length);
        for (int i=0; i<packs.length; i++)
            this.packs.add(packs[i]);
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
        rv = sol.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        rv.setAdapter(new RecyclerViewAdapter(inflater.getContext(), packs));
        return sol;
    }

    /*
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (packs.get(0) instanceof CharacterStylePack){
            outState.putInt("type", 0);
        }else if (packs.get(0) instanceof ImageStylePack){
            outState.putInt("type", 0);
        }else if (packs.get(0) instanceof WordStylePack){
            outState.putInt("type", 0);
        }else if (packs.get(0) instanceof SpecialGameActivity){
            outState.putInt("type", 0);
        }
        //Save the fragment's state here
    }
    */
}
