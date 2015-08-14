package com.codersact.smsblock.utility;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import com.codersact.smsblock.blacklist.BlackListFragment;

import activity.masum.com.smsblock.R;

/**
 * Created by masum on 11/08/2015.
 */
public class UtilityMethod {

    public static  void blackListFragment(Activity activity) {
        android.app.Fragment fragment = null;
        fragment = new BlackListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        FragmentManager frgManager = activity.getFragmentManager();
        android.app.FragmentTransaction ft = frgManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment, "SEARCH_FRAGMENT");
        ft.commit();
    }

}
