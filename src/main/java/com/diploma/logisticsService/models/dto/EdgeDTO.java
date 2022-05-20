package com.diploma.logisticsService.models.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "EDGE")
@Getter
@Setter
public class EdgeDTO {

    @Id
    private long id;

    @ManyToOne
    @JoinColumn(name = "FROM_NODE", referencedColumnName = "id")
    private NodeDTO from;

    @ManyToOne
    @JoinColumn
    private NodeDTO to;

    @Column(name = "DISTANCE", columnDefinition = "float")
    private double distance;

    @Column(name = "STREET_TYPE")
    private int streetType; // 1 - общ 2 - ограничение для грузовиков

    @Column(name = "TRAFFIC_JAM_POINT")
    private int trafficJamPoint;

    @Column(name = "IS_AVAILABLE")
    private boolean isAvailable = true;
}
