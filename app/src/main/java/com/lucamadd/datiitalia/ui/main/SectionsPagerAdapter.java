package com.lucamadd.datiitalia.ui.main;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.lucamadd.datiitalia.Helper.DataHelper;
import com.lucamadd.datiitalia.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3,R.string.tab_text_4};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return PlaceholderFragment.newInstance(position + 1);

            case 1:
                return RegioniFragment.newInstance(position + 1);
                //return BlankFragment.newInstance("","");
            case 2:
                //return RegioniFragment.newInstance(position + 1);
                return ProvinceFragment.newInstance(position + 1);
            case 3:
                //return RegioniFragment.newInstance(position + 1);
                return GraphFragment.newInstance(position + 1);
        }
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position +1);
        //return BlankFragment.newInstance("","");
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 4;
    }
}