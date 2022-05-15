package com.diploma.logisticsService.models.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NODE")
@Getter
@Setter
public class NodeDTO {

    @Id
    private long id;

    @Column(name = "LAT", columnDefinition = "float")
    private double latitude;

    @Column(name = "LON", columnDefinition = "float")
    private double longitude;
}
