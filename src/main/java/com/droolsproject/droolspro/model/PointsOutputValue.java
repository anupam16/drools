package com.droolsproject.droolspro.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "povId")
public class PointsOutputValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer povId;
    private String tagName;
    private Integer groupId;
    private String outValue;
    private String unit;
    private String deviceStatus;
    @Transient
    private String name;
    @Transient
    private String encodedCr;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_timestamp")
    private Date updatetimestamp = new Date();
    // Many-to-one relationship with URLPoints
    @JsonIgnoreProperties("pointsOutputValues")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "urlid", referencedColumnName = "urlid")
    private URLPoints urlPoints;
    
    @Transient
    public String getName() {
        if (urlPoints != null) {
            return urlPoints.getDeviceName();
        }
        return null;
    }
}