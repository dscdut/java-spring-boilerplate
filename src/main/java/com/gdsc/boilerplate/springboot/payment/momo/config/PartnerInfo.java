package com.gdsc.boilerplate.springboot.payment.momo.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerInfo {

    private String accessKey;
    private String partnerCode;
    private String secretKey;
    private String publicKey;
    private String returnUrl;
    private String notifyUrl;
    private String orderInfo;

    public PartnerInfo(String partnerCode, String accessKey, String secretKey, String returnUrl, String notifyUrl, String orderInfo) {
        this.accessKey = accessKey;
        this.partnerCode = partnerCode;
        this.secretKey = secretKey;
        this.returnUrl = returnUrl;
        this.notifyUrl = notifyUrl;
        this.orderInfo = orderInfo;
    }

    public PartnerInfo(String partnerCode, String accessKey, String secretKey, String publicKey) {
        this.accessKey = accessKey;
        this.partnerCode = partnerCode;
        this.secretKey = secretKey;
        this.publicKey = publicKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
