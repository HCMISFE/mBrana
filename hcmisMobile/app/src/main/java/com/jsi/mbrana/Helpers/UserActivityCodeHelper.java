package com.jsi.mbrana.Helpers;

import java.util.Objects;

/**
 * Created by Surafel Nigussie on 10/4/2017.
 */

public class UserActivityCodeHelper {
    static String CampaignActivityCode;
    static String RoutineActivityCode;
    static String DefaultActivityCode;
    static String AssignedTo;
    static Boolean MultipleActivityCodeAllowed;
    static Boolean isCheckableByDefault = false; //Used for handling the "Is Campaign" checkbox in Issue and Receive, uncheckable by default

    public static String getActivityCode(Boolean IsCampaign) {
        if (!getMultipleActivityCodeAllowed())
            return getDefaultActivityCode();
        else if (IsCampaign)
            return getCampaignActivityCode();
        else
            return getRoutineActivityCode();
    }

    public UserActivityCodeHelper(String CampaignActivityCode, String RoutineActivityCode, String DefaultActivityCode, String AssignedTo, Boolean MultipleActivityCodeAllowed) {
        if (Objects.equals(DefaultActivityCode, CampaignActivityCode))
            this.isCheckableByDefault = true;

        this.CampaignActivityCode = CampaignActivityCode;
        this.RoutineActivityCode = RoutineActivityCode;
        this.DefaultActivityCode = DefaultActivityCode;
        this.AssignedTo = AssignedTo;
        this.MultipleActivityCodeAllowed = MultipleActivityCodeAllowed;
    }

    public static String getAssignedTo() {
        return AssignedTo;
    }

    public static void setAssignedTo(String assignedTo) {
        AssignedTo = assignedTo;
    }

    public static Boolean getMultipleActivityCodeAllowed() {
        return MultipleActivityCodeAllowed;
    }

    public static void setMultipleActivityCodeAllowed(Boolean multipleActivityCodeAllowed) {
        MultipleActivityCodeAllowed = multipleActivityCodeAllowed;
    }

    public static String getCampaignActivityCode() {
        return CampaignActivityCode;
    }

    public static void setCampaignActivityCode(String campaignActivityCode) {
        CampaignActivityCode = campaignActivityCode;
    }

    public static String getRoutineActivityCode() {
        return RoutineActivityCode;
    }

    public static void setRoutineActivityCode(String routineActivityCode) {
        RoutineActivityCode = routineActivityCode;
    }

    public static String getDefaultActivityCode() {
        return DefaultActivityCode;
    }

    public static void setDefaultActivityCode(String defaultActivityCode) {
        DefaultActivityCode = defaultActivityCode;
    }
}
