package com.uk.tsl.rfid.samples.s3sensortagreader;

public interface ITemperatureConverter
{
    /**
     *  Converts a code value into a temperature value in Celsius
     * @param code the temperature code value
     * @return the corresponding temperature in Celsius
     */
    float temperatureFromCode(int code);
}
