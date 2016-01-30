package com.codersact.blocker.blacklist;

import com.codersact.blocker.model.MobileData;

import java.util.ArrayList;

/**
 * Created by masum on 11/08/2015.
 */
public class BlackListPresenter {
    BlacklistView blacklistView;
    BlackListService blackListService;

    public BlackListPresenter(BlacklistView blacklistView, BlackListService blackListService) {
        this.blacklistView = blacklistView;
        this.blackListService = blackListService;
    }

    public ArrayList<MobileData> onSaveClick() {
        String smsName = blacklistView.getSmsName();
        String smsNumber = blacklistView.getSmsNumber();
        return blackListService.getSmsInfo();
    }
}
