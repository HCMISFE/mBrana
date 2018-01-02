package com.jsi.mbrana.Models;

/**
 * Created by user on 21/3/2017.
 */

public class PeriodModel {
    int PeriodID;
    String PeriodStart;
    String NextPeriodStart;

    public int getPeriodID() {
        return PeriodID;
    }

    public void setPeriodID(int periodID) {
        PeriodID = periodID;
    }

    public String getPeriodStart() {
        return PeriodStart;
    }

    public void setPeriodStart(String periodStart) {
        PeriodStart = periodStart;
    }

    public String getNextPeriodStart() {
        return NextPeriodStart;
    }

    public void setNextPeriodStart(String nextPeriodStart) {
        NextPeriodStart = nextPeriodStart;
    }
}
