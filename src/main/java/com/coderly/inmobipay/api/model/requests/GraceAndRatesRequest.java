package com.coderly.inmobipay.api.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GraceAndRatesRequest {

    @NotNull
    private double rateByMonth;

    @NotNull
    private String graceByMonth;
}
