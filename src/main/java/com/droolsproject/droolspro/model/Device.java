package com.droolsproject.droolspro.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Device {
  private String output;
  
  Boolean valveLeakageAlarm;
  
  Boolean da_Fan_Sts;
  
  Boolean lessThan;
  
  Boolean lessThan1;
  
  Boolean and;
  
  Double sineWave;
  
  Double sineWave1;
  
  Double daTemp;
  
  Double maTemp;
  
  Double datsp;
  
  Double pidkp1;
  
  Double pidki;
  
  Double pid;
  
  Double chw_Vlv_Ctrl;
  
  Boolean fanStatus;
  
  Boolean coolingStatus;
  
  Double dischargeTempSensorValue;
  
  Double threshold;
  
  public String getOutput() {
    return this.output;
  }
  
  public void setOutput(String output) {
    this.output = output;
  }
  
  public Boolean getValveLeakageAlarm() {
    return this.valveLeakageAlarm;
  }
  
  public void setValveLeakageAlarm(Boolean paramBoolean) {
    this.valveLeakageAlarm = paramBoolean;
  }
  
  public Boolean getDa_Fan_Sts() {
    return this.da_Fan_Sts;
  }
  
  public void setDa_Fan_Sts(Boolean paramBoolean) {
    this.da_Fan_Sts = paramBoolean;
  }
  
  public Boolean getLessThan() {
    return this.lessThan;
  }
  
  public void setLessThan(Boolean paramBoolean) {
    this.lessThan = paramBoolean;
  }
  
  public Boolean getLessThan1() {
    return this.lessThan1;
  }
  
  public void setLessThan1(Boolean paramBoolean) {
    this.lessThan1 = paramBoolean;
  }
  
  public Boolean getAnd() {
    return this.and;
  }
  
  public void setAnd(Boolean paramBoolean) {
    this.and = paramBoolean;
  }
  
  public Double getSineWave() {
    return this.sineWave;
  }
  
  public void setSineWave(Double paramDouble) {
    this.sineWave = paramDouble;
  }
  
  public Double getSineWave1() {
    return this.sineWave1;
  }
  
  public void setSineWave1(Double paramDouble) {
    this.sineWave1 = paramDouble;
  }
  
  public Double getDaTemp() {
    return this.daTemp;
  }
  
  public void setDaTemp(Double paramDouble) {
    this.daTemp = paramDouble;
  }
  
  public Double getMaTemp() {
    return this.maTemp;
  }
  
  public void setMaTemp(Double paramDouble) {
    this.maTemp = paramDouble;
  }
  
  public Double getDatsp() {
    return this.datsp;
  }
  
  public void setDatsp(Double paramDouble) {
    this.datsp = paramDouble;
  }
  
  public Double getPidkp1() {
    return this.pidkp1;
  }
  
  public void setPidkp1(Double paramDouble) {
    this.pidkp1 = paramDouble;
  }
  
  public Double getPidki() {
    return this.pidki;
  }
  
  public void setPidki(Double paramDouble) {
    this.pidki = paramDouble;
  }
  
  public Double getPid() {
    return this.pid;
  }
  
  public void setPid(Double paramDouble) {
    this.pid = paramDouble;
  }
  
  public Double getChw_Vlv_Ctrl() {
    return this.chw_Vlv_Ctrl;
  }
  
  public void setChw_Vlv_Ctrl(Double paramDouble) {
    this.chw_Vlv_Ctrl = paramDouble;
  }
  
  public Boolean getFanStatus() {
    return this.fanStatus;
  }
  
  public void setFanStatus(Boolean paramBoolean) {
    this.fanStatus = paramBoolean;
  }
  
  public Boolean getCoolingStatus() {
    return this.coolingStatus;
  }
  
  public void setCoolingStatus(Boolean paramBoolean) {
    this.coolingStatus = paramBoolean;
  }
  
  public Double getDischargeTempSensorValue() {
    return this.dischargeTempSensorValue;
  }
  
  public void setDischargeTempSensorValue(Double paramDouble) {
    this.dischargeTempSensorValue = paramDouble;
  }
  
  public Double getThreshold() {
    return this.threshold;
  }
  
  public void setThreshold(Double paramDouble) {
    this.threshold = paramDouble;
  }
  
  public String toString() {
    return "Device(valveLeakageAlarm=" + this.valveLeakageAlarm + ", " + "da_Fan_Sts=" + this.da_Fan_Sts + ", " + "lessThan=" + this.lessThan + ", " + "lessThan1=" + this.lessThan1 + ", " + "and=" + this.and + ", " + "sineWave=" + this.sineWave + ", " + "sineWave1=" + this.sineWave1 + ", " + "output=" + this.output + ", " + "daTemp=" + this.daTemp + ", " + "maTemp=" + this.maTemp + ", " + "datsp=" + this.datsp + ", " + "pidkp1=" + this.pidkp1 + ", " + "pidki=" + this.pidki + ", " + "pid=" + this.pid + ", " + "chw_Vlv_Ctrl=" + this.chw_Vlv_Ctrl + ", " + "fanStatus=" + this.fanStatus + ", " + "coolingStatus=" + this.coolingStatus + ", " + "dischargeTempSensorValue=" + this.dischargeTempSensorValue + ", " + "threshold=" + this.threshold + ")";
  }
}
