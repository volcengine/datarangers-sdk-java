/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.datarangers.event;

import com.datarangers.config.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class HeaderV3 implements Serializable, Header, Cloneable {
    @JsonProperty("__source")
    private String source;
    private Integer aid;
    private String appLanguage;
    private String appName;
    private String appRegion;
    private String appVersion;
    private String appVersionMinor;
    private String appKey;
    private String buildSerial;
    private String carrier;
    private String channel;
    private String clientudid;
    private String cpuAbi;
    private Map<String, Object> custom;
    private Long deviceId;
    private String brand;
    private String deviceBrand;
    private String deviceManufacturer;
    private String deviceModel;
    private String deviceType;
    private String displayName;
    private String displayDensity;
    private String densityDpi;
    private String idfa;
    private String installId;
    private String language;
    private String openudid;
    private String os;
    private String osApi;
    private String osName;
    private String osVersion;
    @JsonProperty("package")
    private String packages;
    private String region;
    private String sdkVersion;
    private String sdkLib;
    private Integer timezone = Constants.TIME_ZONE_OFFSET;
    private Integer tzOffset = Constants.TIME_ZONE_OFFSET_CURRENT;
    private String tzName = Constants.TIME_ZONE_NAME;
    private String udid;
    private String userUniqueId;
    private String vendorId;
    private String clientIp;

    // 新增的预置属性
    @JsonProperty("$latest_referrer")
    private String latestReferrer;

    @JsonProperty("$latest_referrer_host")
    private String latestReferrerHost;

    @JsonProperty("$latest_search_keyword")
    private String latestSearchKeyword;

    @JsonProperty("$latest_traffic_source_type")
    private String latestTrafficSourceType;

    @JsonProperty("$user_unique_id_type")
    private String userUniqueIdType;

    private String height;
    private String width;
    private String resolution;
    private String appChannel;
    private String networkType;
    private String networkCarrier;
    private String platform;
    private String browser;
    private String browserVersion;

    public HeaderV3() {
    }

    public String getSource() {
        return source;
    }

    public HeaderV3 setSource(String source) {
        this.source = source;
        return this;
    }

    public HeaderV3 setClientIp(String clientIp) {
        this.clientIp = clientIp;
        return this;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getVendorId() {
        return vendorId;
    }

    public HeaderV3 setVendorId(String vendorId) {
        this.vendorId = vendorId;
        return this;
    }

    public Integer getAid() {
        return aid;
    }


    public HeaderV3 setAid(Integer aid) {
        this.aid = aid;
        return this;
    }

    public String getAppLanguage() {
        return appLanguage;
    }

    public HeaderV3 setAppLanguage(String appLanguage) {
        this.appLanguage = appLanguage;
        return this;
    }

    public String getAppName() {
        return appName;
    }

    public HeaderV3 setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public String getAppRegion() {
        return appRegion;
    }

    public HeaderV3 setAppRegion(String appRegion) {
        this.appRegion = appRegion;
        return this;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public HeaderV3 setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public String getAppVersionMinor() {
        return appVersionMinor;
    }

    public HeaderV3 setAppVersionMinor(String appVersionMinor) {
        this.appVersionMinor = appVersionMinor;
        return this;
    }

    public String getAppKey() {
        return appKey;
    }

    public HeaderV3 setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public String getBuildSerial() {
        return buildSerial;
    }

    public HeaderV3 setBuildSerial(String buildSerial) {
        this.buildSerial = buildSerial;
        return this;
    }

    public String getCarrier() {
        return carrier;
    }

    public HeaderV3 setCarrier(String carrier) {
        this.carrier = carrier;
        return this;
    }

    public String getChannel() {
        return channel;
    }

    public HeaderV3 setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public String getClientudid() {
        return clientudid;
    }

    public HeaderV3 setClientudid(String clientudid) {
        this.clientudid = clientudid;
        return this;
    }

    public String getCpuAbi() {
        return cpuAbi;
    }

    public HeaderV3 setCpuAbi(String cpuAbi) {
        this.cpuAbi = cpuAbi;
        return this;
    }

    public Map<String, Object> getCustom() {
        return custom;
    }

    public HeaderV3 setCustom(Map<String, Object> custom) {
        this.custom = custom;
        return this;
    }

    public HeaderV3 addCustom(Map<String, Object> custom) {
        if (this.custom == null) this.custom = new HashMap<>();
        this.custom.putAll(custom);
        return this;
    }

    public HeaderV3 addCustom(String key, Object value) {
        if (this.custom == null) this.custom = new HashMap<>();
        this.custom.put(key, value);
        return this;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public HeaderV3 setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    @Override
    public HeaderV3 putCustom(String key, Object value) {
        if (custom == null) custom = new HashMap<>();
        custom.put(key, value);
        return this;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public HeaderV3 setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
        return this;
    }

    public String getDeviceManufacturer() {
        return deviceManufacturer;
    }

    public HeaderV3 setDeviceManufacturer(String deviceManufacturer) {
        this.deviceManufacturer = deviceManufacturer;
        return this;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public HeaderV3 setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
        return this;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public HeaderV3 setDeviceType(String deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public HeaderV3 setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getDisplayDensity() {
        return displayDensity;
    }

    public HeaderV3 setDisplayDensity(String displayDensity) {
        this.displayDensity = displayDensity;
        return this;
    }

    public String getDensityDpi() {
        return densityDpi;
    }

    public HeaderV3 setDensityDpi(String densityDpi) {
        this.densityDpi = densityDpi;
        return this;
    }

    public String getIdfa() {
        return idfa;
    }

    public HeaderV3 setIdfa(String idfa) {
        this.idfa = idfa;
        return this;
    }

    public String getInstallId() {
        return installId;
    }

    public HeaderV3 setInstallId(String installId) {
        this.installId = installId;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public HeaderV3 setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getOpenudid() {
        return openudid;
    }

    public HeaderV3 setOpenudid(String openudid) {
        this.openudid = openudid;
        return this;
    }

    public String getOs() {
        return os;
    }

    public HeaderV3 setOs(String os) {
        this.os = os;
        return this;
    }

    public String getOsApi() {
        return osApi;
    }

    public HeaderV3 setOsApi(String osApi) {
        this.osApi = osApi;
        return this;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public HeaderV3 setOsVersion(String osVersion) {
        this.osVersion = osVersion;
        return this;
    }

    public String getPackages() {
        return packages;
    }

    public HeaderV3 setPackages(String packages) {
        this.packages = packages;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public HeaderV3 setRegion(String region) {
        this.region = region;
        return this;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public HeaderV3 setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
        return this;
    }

    public Integer getTimezone() {
        return timezone;
    }

    public HeaderV3 setTimezone(Integer timezone) {
        this.timezone = timezone;
        return this;
    }

    public Integer getTzOffset() {
        return tzOffset;
    }

    public HeaderV3 setTzOffset(Integer tzOffset) {
        this.tzOffset = tzOffset;
        return this;
    }

    public String getTzName() {
        return tzName;
    }

    public HeaderV3 setTzName(String tzName) {
        this.tzName = tzName;
        return this;
    }

    public String getUdid() {
        return udid;
    }

    public HeaderV3 setUdid(String udid) {
        this.udid = udid;
        return this;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public HeaderV3 setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
        return this;
    }

    @Override
    public Integer getAppId() {
        return aid;
    }

    public HeaderV3 setAppId(Integer appId) {
        aid = appId;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public HeaderV3 setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getOsName() {
        return osName;
    }

    public HeaderV3 setOsName(String osName) {
        this.osName = osName;
        return this;
    }

    public String getSdkLib() {
        return sdkLib;
    }

    public HeaderV3 setSdkLib(String sdkLib) {
        this.sdkLib = sdkLib;
        return this;
    }

    public String getLatestReferrer() {
        return latestReferrer;
    }

    public HeaderV3 setLatestReferrer(String latestReferrer) {
        this.latestReferrer = latestReferrer;
        return this;
    }

    public String getLatestReferrerHost() {
        return latestReferrerHost;
    }

    public HeaderV3 setLatestReferrerHost(String latestReferrerHost) {
        this.latestReferrerHost = latestReferrerHost;
        return this;
    }

    public String getLatestSearchKeyword() {
        return latestSearchKeyword;
    }

    public HeaderV3 setLatestSearchKeyword(String latestSearchKeyword) {
        this.latestSearchKeyword = latestSearchKeyword;
        return this;
    }

    public String getLatestTrafficSourceType() {
        return latestTrafficSourceType;
    }

    public HeaderV3 setLatestTrafficSourceType(String latestTrafficSourceType) {
        this.latestTrafficSourceType = latestTrafficSourceType;
        return this;
    }

    public String getUserUniqueIdType() {
        return userUniqueIdType;
    }

    public HeaderV3 setUserUniqueIdType(String userUniqueIdType) {
        this.userUniqueIdType = userUniqueIdType;
        return this;
    }

    public String getHeight() {
        return height;
    }

    public HeaderV3 setHeight(String height) {
        this.height = height;
        return this;
    }

    public String getWidth() {
        return width;
    }

    public HeaderV3 setWidth(String width) {
        this.width = width;
        return this;
    }

    public String getResolution() {
        return resolution;
    }

    public HeaderV3 setResolution(String resolution) {
        this.resolution = resolution;
        return this;
    }

    public String getAppChannel() {
        return appChannel;
    }

    public HeaderV3 setAppChannel(String appChannel) {
        this.appChannel = appChannel;
        return this;
    }

    public String getNetworkType() {
        return networkType;
    }

    public HeaderV3 setNetworkType(String networkType) {
        this.networkType = networkType;
        return this;
    }

    public String getNetworkCarrier() {
        return networkCarrier;
    }

    public HeaderV3 setNetworkCarrier(String networkCarrier) {
        this.networkCarrier = networkCarrier;
        return this;
    }

    public String getPlatform() {
        return platform;
    }

    public HeaderV3 setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public String getBrowser() {
        return browser;
    }

    public HeaderV3 setBrowser(String browser) {
        this.browser = browser;
        return this;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public HeaderV3 setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
        return this;
    }


    @Override
    public HeaderV3 clone(){
        try {
            return (HeaderV3) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public static final class Builder {
        private HeaderV3 headerV3;

        public Builder() {
            headerV3 = new HeaderV3();
            setDeviceId(0l);
        }

        public static Builder getInstance() {
            return new Builder();
        }

        public Builder setSource(String source) {
            headerV3.setSource(source);
            return this;
        }

        public Builder setAid(Integer aid) {
            headerV3.setAid(aid);
            return this;
        }

        public Builder setClientIp(String clientIp) {
            headerV3.setClientIp(clientIp);
            return this;
        }

        public Builder setLatestReferrer(String latestReferrer) {
            headerV3.setLatestReferrer(latestReferrer);
            return this;
        }

        public Builder setLatestReferrerHost(String latestReferrerHost) {
            headerV3.setLatestReferrerHost(latestReferrerHost);
            return this;
        }

        public Builder setLatestSearchKeyword(String latestSearchKeyword) {
            headerV3.setLatestSearchKeyword(latestSearchKeyword);
            return this;
        }

        public Builder setLatestTrafficSourceType(String latestTrafficSourceType) {
            headerV3.setLatestTrafficSourceType(latestTrafficSourceType);
            return this;
        }

        public Builder setUserUniqueIdType(String userUniqueIdType) {
            headerV3.setUserUniqueIdType(userUniqueIdType);
            return this;
        }

        public Builder setHeight(String height) {
            headerV3.setHeight(height);
            return this;
        }

        public Builder setWidth(String width) {
            headerV3.setWidth(width);
            return this;
        }

        public Builder setResolution(String resolution) {
            headerV3.setResolution(resolution);
            return this;
        }

        public Builder setAppChannel(String appChannel) {
            headerV3.setAppChannel(appChannel);
            return this;
        }

        public Builder setNetworkType(String networkType) {
            headerV3.setNetworkType(networkType);
            return this;
        }

        public Builder setNetworkCarrier(String networkCarrier) {
            headerV3.setNetworkCarrier(networkCarrier);
            return this;
        }

        public Builder setPlatform(String platform) {
            headerV3.setPlatform(platform);
            return this;
        }

        public Builder setBrowser(String browser) {
            headerV3.setBrowser(browser);
            return this;
        }

        public Builder setBrowserVersion(String browserVersion) {
            headerV3.setBrowserVersion(browserVersion);
            return this;
        }

        public Builder setVendorId(String vendorId) {
            headerV3.setVendorId(vendorId);
            return this;
        }

        public Builder setAppLanguage(String appLanguage) {
            headerV3.setAppLanguage(appLanguage);
            return this;
        }

        public Builder setAppName(String appName) {
            headerV3.setAppName(appName);
            return this;
        }

        public Builder setAppRegion(String appRegion) {
            headerV3.setAppRegion(appRegion);
            return this;
        }

        public Builder setAppVersion(String appVersion) {
            headerV3.setAppVersion(appVersion);
            return this;
        }

        public Builder setAppVersionMinor(String appVersionMinor) {
            headerV3.setAppVersionMinor(appVersionMinor);
            return this;
        }

        public Builder setAppKey(String appKey) {
            headerV3.setAppKey(appKey);
            return this;
        }

        public Builder setBuildSerial(String buildSerial) {
            headerV3.setBuildSerial(buildSerial);
            return this;
        }

        public Builder setCarrier(String carrier) {
            headerV3.setCarrier(carrier);
            return this;
        }

        public Builder setChannel(String channel) {
            headerV3.setChannel(channel);
            return this;
        }

        public Builder setClientudid(String clientudid) {
            headerV3.setClientudid(clientudid);
            return this;
        }

        public Builder setCpuAbi(String cpuAbi) {
            headerV3.setCpuAbi(cpuAbi);
            return this;
        }

        public Builder setCustom(Map<String, Object> custom) {
            headerV3.setCustom(custom);
            return this;
        }

        public Builder addCustom(String key, Object value) {
            headerV3.putCustom(key, value);
            return this;
        }

        public Builder addCustom(Map<String, Object> custom) {
            headerV3.addCustom(custom);
            return this;
        }

        public Builder setDeviceId(Long deviceId) {
            headerV3.setDeviceId(deviceId);
            return this;
        }

        public Builder setBrand(String brand) {
            headerV3.setBrand(brand);
            return this;
        }


        public Builder setDeviceBrand(String deviceBrand) {
            headerV3.setDeviceBrand(deviceBrand);
            return this;
        }

        public Builder setDeviceManufacturer(String deviceManufacturer) {
            headerV3.setDeviceManufacturer(deviceManufacturer);
            return this;
        }

        public Builder setDeviceModel(String deviceModel) {
            headerV3.setDeviceModel(deviceModel);
            return this;
        }


        public Builder setDeviceType(String deviceType) {
            headerV3.setDeviceType(deviceType);
            return this;
        }

        public Builder setDisplayName(String displayName) {
            headerV3.setDisplayName(displayName);
            return this;
        }


        public Builder setDisplayDensity(String displayDensity) {
            headerV3.setDisplayDensity(displayDensity);
            return this;
        }

        public Builder setDensityDpi(String densityDpi) {
            headerV3.setDensityDpi(densityDpi);
            return this;
        }

        public Builder setIdfa(String idfa) {
            headerV3.setIdfa(idfa);
            return this;
        }

        public Builder setInstallId(String installId) {
            headerV3.setInstallId(installId);
            return this;
        }


        public Builder setLanguage(String language) {
            headerV3.setLanguage(language);
            return this;
        }

        public Builder setOpenudid(String openudid) {
            headerV3.setOpenudid(openudid);
            return this;
        }


        public Builder setOs(String os) {
            headerV3.setOs(os);
            return this;
        }


        public Builder setOsApi(String osApi) {
            headerV3.setOsApi(osApi);
            return this;
        }

        public Builder setOsName(String osName) {
            headerV3.setOsName(osName);
            return this;
        }

        public Builder setOsVersion(String osVersion) {
            headerV3.setOsVersion(osVersion);
            return this;
        }


        public Builder setPackages(String packages) {
            headerV3.setPackages(packages);
            return this;
        }


        public Builder setRegion(String region) {
            headerV3.setRegion(region);
            return this;
        }


        public Builder setSdkVersion(String sdkVersion) {
            headerV3.setSdkVersion(sdkVersion);
            return this;
        }

        public Builder setSdkLib(String sdkLib) {
            headerV3.setSdkLib(sdkLib);
            return this;
        }

        public Builder setTimezone(Integer timezone) {
            headerV3.setTimezone(timezone);
            return this;
        }

        public Builder setTzOffset(Integer tzOffset) {
            headerV3.setTzOffset(tzOffset);
            return this;
        }

        public Builder setTzName(String tzName) {
            headerV3.setTzName(tzName);
            return this;
        }


        public Builder setUdid(String udid) {
            headerV3.setUdid(udid);
            return this;
        }


        public Builder setUserUniqueId(String userUniqueId) {
            headerV3.setUserUniqueId(userUniqueId);
            return this;
        }


        public Builder setAppId(Integer appId) {
            headerV3.setAppId(appId);
            return this;
        }

        public Header build() {
            headerV3.addCustom("__sdk_platform", Constants.SDK_VERSION);
            return this.headerV3;
        }

    }
}
