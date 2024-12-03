package com.droolsproject.droolspro.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "urlid")
public class URLPoints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer urlid;
    private String tagName;
    private String name;
    private String apiurl;
    private String status;
    private String deviceId;
    private String deviceName;
    private String display;
    private String user;
    @Transient
    private Integer stationId;
    private Integer timeinterval;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_timestamp")
    private Date updatetimestamp = new Date();
    @JsonIgnoreProperties("urlPoints")
    @OneToMany(mappedBy = "urlPoints", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<PointsOutputValue> pointsOutputValues = new ArrayList<>();
}