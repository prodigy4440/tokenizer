package com.fahdisa.data.common.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SecretData {
    private String pan;
    private String expiration;
    private String cvv;
    private String transactionId;

    public SecretData() {
    }

    public SecretData(String pan, String expiration, String cvv, String transactionId) {
        this.pan = pan;
        this.expiration = expiration;
        this.cvv = cvv;
        this.transactionId = transactionId;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecretData that = (SecretData) o;
        return Objects.equals(pan, that.pan) && Objects.equals(expiration, that.expiration) && Objects.equals(cvv, that.cvv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pan, expiration, cvv);
    }

    @Override
    public String toString() {
        return "SecretData{" +
                "pan='" + pan + '\'' +
                ", expiration='" + expiration + '\'' +
                ", cvv='" + cvv + '\'' +
                '}';
    }
}
