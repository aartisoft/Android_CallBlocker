package com.codersact.smsblock.inbox;

import com.codersact.smsblock.model.SmsData;

import java.util.ArrayList;

/**
 * Created by masum on 11/08/2015.
 */
public class InboxPresenter {
    InboxView inboxView;
    InboxService inboxService;

    public InboxPresenter (InboxView inboxView, InboxService inboxService) {
        this.inboxView = inboxView;
        this.inboxService = inboxService;

    }

    public ArrayList<SmsData> onFetchList() {
        ArrayList<SmsData> smsDatas = inboxService.fetchInboxSms(inboxView.getContext());
        return smsDatas;
    }

}
