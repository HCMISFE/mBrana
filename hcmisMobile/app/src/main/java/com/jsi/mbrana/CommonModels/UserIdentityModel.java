package com.jsi.mbrana.CommonModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserIdentityModel {
    @SerializedName("Key")
    @Expose
    private String key;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("Username")
    @Expose
    private String username;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("EnvironmentName")
    @Expose
    private String environmentName;
    @SerializedName("EnvironmentId")
    @Expose
    private Integer environmentId;
    @SerializedName("EnvironmentCode")
    @Expose
    private String environmentCode;
    @SerializedName("EnvironmentTypeId")
    @Expose
    private Integer environmentTypeId;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("RoutineActivityId")
    @Expose
    private Integer routineActivityId;
    @SerializedName("CampaignActivityId")
    @Expose
    private Integer campaignActivityId;
    @SerializedName("DefaultActivityId")
    @Expose
    private Integer defaultActivityId;
    @SerializedName("MultipleActivityAllowed")
    @Expose
    private Boolean multipleActivityAllowed;
    @SerializedName("CreateTime")
    @Expose
    private String createTime;
    @SerializedName("ExpiryTime")
    @Expose
    private String expiryTime;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    public Integer getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(Integer environmentId) {
        this.environmentId = environmentId;
    }

    public String getEnvironmentCode() {
        return environmentCode;
    }

    public void setEnvironmentCode(String environmentCode) {
        this.environmentCode = environmentCode;
    }

    public Integer getEnvironmentTypeId() {
        return environmentTypeId;
    }

    public void setEnvironmentTypeId(Integer environmentTypeId) {
        this.environmentTypeId = environmentTypeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getRoutineActivityId() {
        return routineActivityId;
    }

    public void setRoutineActivityId(Integer routineActivityId) {
        this.routineActivityId = routineActivityId;
    }

    public Integer getCampaignActivityId() {
        return campaignActivityId;
    }

    public void setCampaignActivityId(Integer campaignActivityId) {
        this.campaignActivityId = campaignActivityId;
    }

    public Integer getDefaultActivityId() {
        return defaultActivityId;
    }

    public void setDefaultActivityId(Integer defaultActivityId) {
        this.defaultActivityId = defaultActivityId;
    }

    public Boolean getMultipleActivityAllowed() {
        return multipleActivityAllowed;
    }

    public void setMultipleActivityAllowed(Boolean multipleActivityAllowed) {
        this.multipleActivityAllowed = multipleActivityAllowed;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
    }
}
