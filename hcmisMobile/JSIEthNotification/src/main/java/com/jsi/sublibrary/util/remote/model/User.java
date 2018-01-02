package com.jsi.sublibrary.util.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baba on 11/21/2017.
 */

public class User {

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("addresses")
    @Expose
    private List<Address> addresses = new ArrayList<>();

    @SerializedName("application")
    @Expose
    private Application application;

    private class Address {

        @SerializedName("addressText")
        @Expose
        private String addressText;

        @SerializedName("mediumCode")
        @Expose
        private final String mediumCode = "FCM";

        private Address() {
        }

        private Address(String addressText) {
            this.addressText = addressText;
        }

        public String getAddressTExt() {
            return addressText;
        }

        public void setAddressTExt(String addressText) {
            this.addressText = addressText;
        }

        public String getMediumCode() {
            return mediumCode;
        }

    }

    private class Application {

        @SerializedName("ApplicationCode")
        @Expose
        private String applicationCode;

        @SerializedName("ApplicationName")
        @Expose
        private String applicationName;

        public Application() {
        }

        public Application(String applicationCode, String applicationName) {
            this.applicationCode = applicationCode;
            this.applicationName = applicationName;
        }

        public String getApplicationCode() {
            return applicationCode;
        }

        public void setApplicationCode(String applicationCode) {
            this.applicationCode = applicationCode;
        }

        public String getApplicationName() {
            return applicationName;
        }

        public void setApplicationName(String applicationName) {
            this.applicationName = applicationName;
        }
    }

    public User() {
    }

    public User(String username,String token,String applicationCode,String applicationName) {
        this.username = username;
        this.getAddresses().add(new Address(token));
        this.application = new Application(applicationCode,applicationName);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}
