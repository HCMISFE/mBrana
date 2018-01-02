package com.jsi.mbrana.Models;

/**
 * Created by pc-6 on 7/20/2016.
 */
public class TransactionSummary {
    private int IssuedFacilities;
    private int NoOfItems;
    private int NoConfirmedReceipts;
    private int OutStandingReceipts;
    private String LastReceivedDate;
    private String LastIssuedDate;
    private String LastOrderedDate;
    private int NoIssuesConfirmed;

    public int getNoIssuesConfirmed() {
        return NoIssuesConfirmed;
    }

    public void setNoIssuesConfirmed(int noIssuesConfirmed) {
        NoIssuesConfirmed = noIssuesConfirmed;
    }

    public int getNoConfirmedReceipts() {
        return NoConfirmedReceipts;
    }

    public void setNoConfirmedReceipts(int noConfirmedReceipts) {
        NoConfirmedReceipts = noConfirmedReceipts;
    }

    public int getOutStandingReceipts() {
        return OutStandingReceipts;
    }

    public void setOutStandingReceipts(int noIssuesConfirmed) {
        OutStandingReceipts = noIssuesConfirmed;
    }

    public void setIssuedFacilities(int issuedFacilities) {
        IssuedFacilities = issuedFacilities;
    }

    public void setNoOfItems(int noOfItems) {
        NoOfItems = noOfItems;
    }

    public void setLastReceivedDate(String lastReceivedDate) {
        LastReceivedDate = lastReceivedDate;
    }

    public void setLastIssuedDate(String lastIssuedDate) {
        LastIssuedDate = lastIssuedDate;
    }

    public void setLastOrderedDate(String lastOrderedDate) {
        LastOrderedDate = lastOrderedDate;
    }

    public int getIssuedFacilities() {
        return IssuedFacilities;
    }

    public int getNoOfItems() {
        return NoOfItems;
    }

    public String getLastReceivedDate() {
        return LastReceivedDate;
    }

    public String getLastIssuedDate() {
        return LastIssuedDate;
    }

    public String getLastOrderedDate() {
        return LastOrderedDate;
    }
}
