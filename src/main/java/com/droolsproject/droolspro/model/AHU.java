package com.droolsproject.droolspro.model;

import lombok.Data;

@Data
public class AHU {
    private boolean fanStatus;
    private boolean coolingStatus;
    private Double dischargeTempSensorValue;
    private Double mixedAirSensorValue;
    private Double threshold;
    private Double ductPressure;
    private String output;
	private boolean ValveLeakageAlarm;
	private Double SineWave;
	private Double SineWave1;
	private boolean DA_Fan_Sts;
	private Double DaTemp;
	private Double MaTemp;
	private Double DATSP;
	private Double PIDKp1;
	private Double PIDKi;
	private Double PID;
	private Double CHW_Vlv_Ctrl;
	private boolean LessThan;
	private boolean LessThan1;
	private boolean And;
}
