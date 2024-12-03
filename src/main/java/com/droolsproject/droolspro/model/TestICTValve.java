package com.droolsproject.droolspro.model;

import lombok.Data;

@Data
public class TestICTValve {
	private String ValveLeakageAlarm;
	private Double sineWave;
	private Double sineWave1;
	private boolean da_fan_status;
	private Double daTemp;
	private Double maTemp;
	private Double DATSP;
	private Double PDKp1;
	private Double PDKpi;
	private Double PID;
	private Double CH_Vlv_ctrl;
	private boolean lessthan;
	private boolean lessthan1;
	private boolean And;
}
