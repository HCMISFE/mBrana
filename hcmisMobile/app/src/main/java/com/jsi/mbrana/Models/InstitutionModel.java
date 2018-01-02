package com.jsi.mbrana.Models;

import com.jsi.mbrana.Helpers.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pc-6 on 6/15/2016.
 */
public class InstitutionModel {
    private int institutionID;
    private String institutionName;
    private String IssueDate;
    private String type;
    private int QuantityIssued;

    public void setQuantityIssued(int quantityIssued) {
        QuantityIssued = quantityIssued;
    }

    public int getQuantityIssued() {
        return QuantityIssued;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setIssueDate(String issueDate) {
        IssueDate = issueDate;
    }

    public String getIssueDate() {
        return IssueDate;
    }

    public void setInstitutionID(int institutionID) {
        this.institutionID = institutionID;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public int getInstitutionID() {
        return institutionID;
    }

    @Override
    public String toString() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        Date issuedate = null;
        try {
            issuedate = simpleDateFormat.parse(getIssueDate().replace("T", " "));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String institutionNames="";
        if (getIssueDate().equalsIgnoreCase("0001-01-01T00:00:00"))
            institutionNames= "<b>"+getInstitutionName() + "</b> - NA";
        else
            institutionNames= "<b>"+getInstitutionName() + "</b> - " + Helper.getMomentFromNow(issuedate);

        return institutionNames;
    }
}
