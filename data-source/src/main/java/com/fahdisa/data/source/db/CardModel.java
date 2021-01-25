package com.fahdisa.data.source.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardModel {
    private String transactionId;
    private String pan;
    private String expiration;
    private String cvv;

    public CardModel() {
    }

    public CardModel(String transactionId, String pan, String expiration, String cvv) {
        this.transactionId = transactionId;
        this.pan = pan;
        this.expiration = expiration;
        this.cvv = cvv;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
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

    @Override
    public String toString() {
        return "CardInfo{" +
                "transactionId='" + transactionId + '\'' +
                ", pan='" + pan + '\'' +
                ", expiration='" + expiration + '\'' +
                ", cvv='" + cvv + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardModel that = (CardModel) o;
        return Objects.equals(transactionId, that.transactionId) && Objects.equals(pan, that.pan) && Objects.equals(expiration, that.expiration) && Objects.equals(cvv, that.cvv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, pan, expiration, cvv);
    }
}
