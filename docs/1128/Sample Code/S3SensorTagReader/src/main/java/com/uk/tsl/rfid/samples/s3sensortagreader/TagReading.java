package com.uk.tsl.rfid.samples.s3sensortagreader;

/**
 * Stores the reading from a temperature sensor tag
 * Supports averaging multiple readings
 * A converter can be specified to provide temperature in Celsius from the code value.
 */
public class TagReading
{
    //public static float MINIMUM_TEMPERATURE = -273.15f;
    public static float INVALID_TEMPERATURE = -999.0f;

    public String getEPC()
    {
        return mEPC;
    }

    // The current number of samples for this reading
    public int getSampleCount() { return mSampleCount; }

    // The number of samples averaged for a temperature reading
    public int getSampleSize() { return mSampleSize; }

    // The last complete averaged temperature code, rounded to the nearest integer
    public int getTemperatureCode()
    {
        return mTemperatureCode;
    }

    // The last recorded Sensor Code value
    public int getSensorCode()
    {
        return mSensorCode;
    }

    // The RSSI for the last reading added
    public int getRSSI()
    {
        return mRSSI;
    }

    // The channel frequency for the last reading added
    public int getChannelFrequency()
    {
        return mChannelFrequency;
    }

    public double getTemperature()
    {
        return mTemperature;
    }

    // An instance of a class for converting temperature codes into Celsius values
    public ITemperatureConverter getConverter()
    {
        return mConverter;
    }

    public void setConverter(ITemperatureConverter converter)
    {
        mConverter = converter;
    }

    public int getScansSinceLastResponse()
    {
        return mScansSinceLastResponse;
    }

    /**
     * Construct an instance identified by the given EPC
     * @param EPC the EPC identifier for this reading
     * @param samplesToAverage the number of samples to average the Code over
     */
    public TagReading(String EPC, int samplesToAverage)
    {
        mEPC = EPC;
        mSampleSize = samplesToAverage;

        mTemperatureCode = 0;
        mRSSI = 0;
        mChannelFrequency = 0;

        mTemperature = INVALID_TEMPERATURE;
        mAccumulatedCodeValue = 0;
        mSampleCount = 0;
        mConverter = null;
        mScansSinceLastResponse = 0;
    }


    /**
     *
     * @param rssi the tag's returned RSSI value
     */
    public void update(int rssi)
    {
        mRSSI = rssi;
    }

    /**
     *
     * @param rssi the tag's returned RSSI value
     * @param frequency the channel frequency used when scannint the tag
     */
    public void update(int rssi, int frequency)
    {
        mRSSI = rssi;
        mChannelFrequency = frequency;
    }


    /**
     * Updates the reading with a new valid temperature sample
     * @param temperatureCode the temperature code
     * @param sensorCode the sensor code
     * @param rssi the RSSI
     * @param channel the channel frequency
     * @return true if this updated the Temperature property
     */
    public boolean update(int temperatureCode, int sensorCode, int rssi, int channel)
    {
        boolean isNewValueReady = false;

        mScansSinceLastResponse = 0;

        mSensorCode = sensorCode;
        mRSSI = rssi;
        mChannelFrequency = channel;

        // Adjust the averaged temperature code value
        mAccumulatedCodeValue += temperatureCode;
        mSampleCount += 1;
        if( mSampleCount >= mSampleSize)
        {
            isNewValueReady = true;
            // Calculate the averaged code value
            float average = mAccumulatedCodeValue / (float) mSampleCount;
            mTemperatureCode = (int)(average + 0.5f);
            
            // Convert, if possible
            if( mConverter != null )
            {
                mTemperature = mConverter.temperatureFromCode(mTemperatureCode);
            }

            mSampleCount = 0;
            mAccumulatedCodeValue = 0;
        }
        return isNewValueReady;
    }

    /**
     *  Call this to indicate that the tag was not seen this scan
     */
    public void notSeen()
    {
        mScansSinceLastResponse += 1;
    }

    //
    // Private variables
    //
    private String mEPC;

    /**
     * @return the TagReading's current message
     */
    public String getMessage()
    {
        return mMessage;
    }

    /**
     * Sets the TagReading's message
     * @param message an arbitrary message
     */
    public void setMessage(String message)
    {
        mMessage = message;
    }

    private String mMessage;
    private int mTemperatureCode;
    private int mSensorCode;
    private int mRSSI;
    private int mChannelFrequency;
    private float mTemperature = INVALID_TEMPERATURE;
    private float mAccumulatedCodeValue;

    private int mSampleSize = 1;
    private int mSampleCount = 1;
    private ITemperatureConverter mConverter = null;

    private int mScansSinceLastResponse;
}
