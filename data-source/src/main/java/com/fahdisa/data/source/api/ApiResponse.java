package com.fahdisa.data.source.api;

import java.util.Objects;

public class ApiResponse {

    private Boolean success;
    private String transactionId;

    public ApiResponse(Boolean success, String transactionId) {
        this.success = success;
        this.transactionId = transactionId;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
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
        ApiResponse that = (ApiResponse) o;
        return Objects.equals(success, that.success) && Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, transactionId);
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
