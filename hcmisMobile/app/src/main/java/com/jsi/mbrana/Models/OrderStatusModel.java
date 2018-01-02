package com.jsi.mbrana.Models;

/**
 * Created by Sololia on 6/22/2016.
 */
public class OrderStatusModel {
    private int Draft;
    private int Submitted;
    private int Picklist;
    private int Issued;
    private int Approved;

    public int getApproved() {
        return Approved;
    }

    public void setApproved(int approved) {
        Approved = approved;
    }

    public int getIssued() {
        return Issued;
    }

    public void setIssued(int Issued) {
        this.Issued = Issued;
    }

    public int getDraft() {
        return Draft;
    }

    public void setDraft(int draft) {
        Draft = draft;
    }

    public int getSubmitted() {
        return Submitted;
    }

    public void setSubmitted(int submitted) {
        Submitted = submitted;
    }

    public int getPicklist() {
        return Picklist;
    }

    public void setPicklist(int picklist) {
        Picklist = picklist;
    }
}
