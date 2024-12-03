package com.droolsproject.droolspro.model;

import jakarta.persistence.Transient;

import lombok.Data;

@Data
public class Devices {
	private Long deviceId;
	private String name;
	private String status;
	private String enabled;
	private String faultCause;
	private String health;
	private String network;
	private String macAddress;
    @Transient
    private String encodedCr;
    @Transient
    private Integer stationId;
}
