package com.jsi.mbrana.Models;

import java.util.ArrayList;

/**
 * Created by pc-6 on 7/4/2016.
 */
public class ApiVoidIssueModel {
    IssueHeaderModel Issue;
    ArrayList<ApiIssueModel> IssueDetail;

    public ArrayList<ApiIssueModel> getIssueDetail() {
        return IssueDetail;
    }

    public void setIssue(IssueHeaderModel issue) {
        Issue = issue;
    }

    public void setIssueDetail(ArrayList<ApiIssueModel> issueDetail) {
        IssueDetail = issueDetail;
    }

    public IssueHeaderModel getIssue() {
        return Issue;
    }
}
