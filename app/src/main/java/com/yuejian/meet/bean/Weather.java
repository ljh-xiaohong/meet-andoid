package com.yuejian.meet.bean;

public class Weather
{
  public String date;
  public String temperature;
  public String weather;
  
  public String getDate()
  {
    return this.date;
  }
  
  public String getTemperature()
  {
    return this.temperature;
  }
  
  public String getWeather()
  {
    return this.weather;
  }
  
  public void setDate(String paramString)
  {
    this.date = paramString;
  }
  
  public void setTemperature(String paramString)
  {
    this.temperature = paramString;
  }
  
  public void setWeather(String paramString)
  {
    this.weather = paramString;
  }
}
