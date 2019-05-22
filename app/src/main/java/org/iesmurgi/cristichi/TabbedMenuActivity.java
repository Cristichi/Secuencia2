package org.iesmurgi.cristichi;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.iesmurgi.cristichi.data.CharacterStylePack;
import org.iesmurgi.cristichi.data.ImageStylePack;
import org.iesmurgi.cristichi.data.WordStylePack;

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

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(CharacterStylePack.values()[0].getIcon());
        tabLayout.getTabAt(1).setIcon(ImageStylePack.values()[0].getIcon());
        tabLayout.getTabAt(2).setIcon(WordStylePack.values()[0].getIcon());
        //tabLayout.getTabAt(3).setIcon(SpecialStylePack.values()[0].getIcon());
    }

    private void setupViewPager(ViewPager viewPager) {
        Resources res = getResources();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        FragmentStylePacks fspChar = new FragmentStylePacks();
        fspChar.setPacks(CharacterStylePack.values());

        FragmentStylePacks fspImg = new FragmentStylePacks();
        fspImg.setPacks(ImageStylePack.values());

        FragmentStylePacks fspWords = new FragmentStylePacks();
        fspWords.setPacks(WordStylePack.values());

        adapter.addFragment(fspChar, res.getString(R.string.choose_csp));
        adapter.addFragment(fspImg, res.getString(R.string.choose_isp));
        adapter.addFragment(fspWords, res.getString(R.string.choose_wsp));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<FragmentStylePacks> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public FragmentStylePacks getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(FragmentStylePacks fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
