package com.jsi.mbrana.Models;

/**
 * Created by Sololia on 6/18/2016.
 */
public class EnvironmentModel {
    private int EnvironmentID;
    private String Environment;
    private String EnvironmentCode;

    public int getEnvironmentID() {
        return EnvironmentID;
    }

    public void setEnvironmentID(int environmentID) {
        EnvironmentID = environmentID;
    }

    public String getEnvironment() {
        return Environment;
    }

    public void setEnvironment(String environment) {
        Environment = environment;
    }

    public String getEnvironmentCode() {
        return EnvironmentCode;
    }

    public void setEnvironmentCode(String environmentCode) {
        EnvironmentCode = environmentCode;
    }
}
