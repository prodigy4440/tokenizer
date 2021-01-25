package com.fahdisa.data.flow.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EncryptedInfo {
    private String token;
    private String transactionId;

    public EncryptedInfo() {
    }

    public EncryptedInfo(String token, String transactionId) {
        this.token = token;
        this.transactionId = transactionId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
        EncryptedInfo that = (EncryptedInfo) o;
        return Objects.equals(token, that.token) && Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, transactionId);
    }

    @Override
    public String toString() {
        return "EncryptedInfo{" +
                "token='" + token + '\'' +
                ", key='" + transactionId + '\'' +
                '}';
    }
}
