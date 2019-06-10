package net.sharksystem.key_administration;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.sharksystem.R;
import net.sharksystem.key_administration.fragments.CertificationFragment;
import net.sharksystem.key_administration.fragments.PublicKeyTabFragment;


public class KeyAdministrationPagerAdapter extends FragmentPagerAdapter {

    public static final int FRAGMENT_ITEM_COUNT = 2;
    public Context context;

    public KeyAdministrationPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PublicKeyTabFragment();
            case 1:
                return new CertificationFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                 return context.getResources().getString(R.string.fragment_public_key_title);
            case 1:
                return context.getResources().getString(R.string.fragment_certification_title);
            default:
                return null;
        }    }

    @Override
    public int getCount() {
        return FRAGMENT_ITEM_COUNT;
    }

}