package com.droolsproject.droolspro.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Station {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String stationName;
	private String stationType;
	private String stationurl;
	private String status;
	private String username;
	private String pssword;
	private Date updateTimestamp = new Date();
}
