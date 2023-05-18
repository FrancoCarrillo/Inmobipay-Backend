package com.coderly.inmobipay.api.model.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetPaymentScheduleResponse {
    private double van;
    private double tir;
    private List<CreditResponses> creditResponses;
}
