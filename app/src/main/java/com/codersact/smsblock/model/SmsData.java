package com.codersact.smsblock.model;

/**
 * Created by masum on 30/07/2015.
 */
public class SmsData {
    private String smsNo;
    private String smsString;
    private String smsAddress;
    private String smsId;

    public String getSmsThreadNo() {
        return smsNo;
    }

    public void setSmsThreadId(String smsNo) {
        this.smsNo = smsNo;
    }

    public String getSmsString() {
        return smsString;
    }

    public void setSmsString(String smsString) {
        this.smsString = smsString;
    }

    public String getSmsAddress() {
        return smsAddress;
    }

    public void setSmsAddress(String smsAddress) {
        this.smsAddress = smsAddress;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }
}
