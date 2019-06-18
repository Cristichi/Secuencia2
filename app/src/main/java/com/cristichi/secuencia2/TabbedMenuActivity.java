package com.cristichi.secuencia2;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.cristichi.secuencia2.data.CharacterGamemode;
import com.cristichi.secuencia2.data.ImageGamemode;
import com.cristichi.secuencia2.data.WordGamemode;

import java.util.ArrayList;
import java.util.List;

public class TabbedMenuActivity extends ActivityWithMusic {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_menu);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        try{
            tabLayout.getTabAt(0).setIcon(CharacterGamemode.values()[0].getIcon());
        }catch (NullPointerException e){}
        try{
            tabLayout.getTabAt(1).setIcon(ImageGamemode.values()[0].getIcon());
        }catch (NullPointerException e){}
        try{
            tabLayout.getTabAt(2).setIcon(WordGamemode.values()[0].getIcon());
        }catch (NullPointerException e){}
        try{
            tabLayout.getTabAt(3).setIcon(R.drawable.cloud_download);
        }catch (NullPointerException e){}
    }

    private void setupViewPager(ViewPager viewPager) {
        Resources res = getResources();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        FragmentGamemodes fspChar = new FragmentGamemodes();
        fspChar.setPacks(CharacterGamemode.values());

        FragmentGamemodes fspImg = new FragmentGamemodes();
        fspImg.setPacks(ImageGamemode.values());

        FragmentGamemodes fspWords = new FragmentGamemodes();
        fspWords.setPacks(WordGamemode.values());

        FragmentCustomGamemode fspCustom = new FragmentCustomGamemode();

        adapter.addFragment(fspChar, res.getString(R.string.choose_csp));
        adapter.addFragment(fspImg, res.getString(R.string.choose_isp));
        adapter.addFragment(fspWords, res.getString(R.string.choose_wsp));
        adapter.addFragment(fspCustom, res.getString(R.string.choose_customsp));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<FragmentGamemodes> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public FragmentGamemodes getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(FragmentGamemodes fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
