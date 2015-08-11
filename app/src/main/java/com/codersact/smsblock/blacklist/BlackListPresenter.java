package com.codersact.smsblock.blacklist;

import com.codersact.smsblock.model.SmsData;

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

    public ArrayList<SmsData> onSaveClick() {
        String smsName = blacklistView.getSmsName();
        String smsNumber = blacklistView.getSmsNumber();
        return blackListService.getSmsInfo();
    }
}
