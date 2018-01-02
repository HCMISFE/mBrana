package com.jsi.mbrana.Models;

/**
 * Created by Sololia on 6/29/2016.
 */
public class ReceiptStatusModel {
    private static int Draft;
    private static int Submitted;

    public static int getDraft() {
        return Draft;
    }

    public static void setDraft(int draft) {
        Draft = draft;
    }

    public static int getSubmitted() {
        return Submitted;
    }

    public static void setSubmitted(int submitted) {
        Submitted = submitted;
    }
}
