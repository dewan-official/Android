package com.dewansoft.app.chatapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class FragmentAdapter extends FragmentPagerAdapter{
    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Chat_List cl = new Chat_List();
                return cl;
            case 1:
                Request r = new Request();
                return r;
            case 2:
                Friends f = new Friends();
                return f;
            case 3:
                Find_Friends fn = new Find_Friends();
                return fn;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Chats";
            case 1:
                return "Requests";
            case 2:
                return "Friends";
            case 3:
                return "Find";
            default:
                return null;
        }
    }
}
