package com.diploma.logisticsService.models.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Table(name = "EDGE")
@Getter
@Setter
public class EdgeDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @OneToOne(mappedBy = "edge")
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private NodeDTO from;

    @OneToOne(mappedBy = "edge")
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private NodeDTO to;

    @Column(name = "DISTANCE", columnDefinition = "float")
    private double distance;

    @Column(name = "STREET_TYPE")
    private int streetType; // 1 - общ 2 - ограничение для грузовиков

    @Column(name = "TRAFFIC_JAM_POINT")
    private int trafficJamPoint;

    @Column(name = "IS_AVAILABLE")
    private boolean isAvailable;
}
