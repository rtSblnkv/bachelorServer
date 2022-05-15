package com.diploma.logisticsService.models.csv;


import lombok.*;

/**
 * orders.csv Data Object
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Order {
    private String orderType;
    private String branchCode;
    private double latitude;
    private double longitude;
    private Boolean hasLoyality;
    private double deliveryFee;
    public String getBranchCode() {
        return branchCode.toUpperCase();
    }
}
