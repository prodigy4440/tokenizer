package com.fahdisa.data.common.api;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;

public class CardInfo {

    @NotEmpty(message = "Card number is required")
    private String cardNumber;

    @NotEmpty(message = "Card expiration is required")
    @Size(min = 7, max = 7, message = "Enter expiration in format yyyy/MM")
    private String expirationDate;

    @Size(min = 3, max = 3, message = "Invalid CVV length")
    private String cvv;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardInfo cardInfo = (CardInfo) o;
        return Objects.equals(cardNumber, cardInfo.cardNumber) && Objects.equals(expirationDate, cardInfo.expirationDate) && Objects.equals(cvv, cardInfo.cvv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, expirationDate, cvv);
    }

    @Override
    public String toString() {
        return "CardInfo{" +
                "cardNumber='" + cardNumber + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", cvv='" + cvv + '\'' +
                '}';
    }
}
