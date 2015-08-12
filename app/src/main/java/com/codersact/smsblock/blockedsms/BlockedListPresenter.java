package com.codersact.smsblock.blockedsms;

import com.codersact.smsblock.model.SmsData;

import java.util.ArrayList;

/**
 * Created by masum on 11/08/2015.
 */
public class BlockedListPresenter {
    BlockedListView blacklistView;
    BlockedListService blackListService;

    public BlockedListPresenter(BlockedListView blacklistView, BlockedListService blackListService) {
        this.blacklistView = blacklistView;
        this.blackListService = blackListService;
    }

    public ArrayList<SmsData> onFetchClick() {
        String smsName = blacklistView.getSmsName();
        String smsNumber = blacklistView.getSmsNumber();
        return blackListService.getSmsInfo();
    }

}
