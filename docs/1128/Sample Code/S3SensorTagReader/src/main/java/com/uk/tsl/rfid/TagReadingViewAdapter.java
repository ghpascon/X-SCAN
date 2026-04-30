package com.uk.tsl.rfid;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uk.tsl.rfid.samples.s3sensortagreader.BuildConfig;
import com.uk.tsl.rfid.samples.s3sensortagreader.TagReading;
import com.uk.tsl.rfid.samples.s3sensortagreader.databinding.TagReadingBinding;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;

public class TagReadingViewAdapter extends ArrayAdapter<TagReading>
{
    // Debug control
    private static final boolean D = BuildConfig.DEBUG;

    private final Context mContext;

    private TagReadingBinding binding;

    public TagReadingViewAdapter(Context context, int resource,
                                      List<TagReading> objects) {
        super(context, resource, objects);
        mContext = context;
    }

    private class ViewHolder
    {
        TextView epc;
        TextView message;
        TextView sampleCount;
        TextView temperatureCode;
        TextView sensorCode;
        TextView frequency;
        TextView rssi;
        TextView temperature;
    }

//    private int mEnabledTextColor;
//    private int mDisabledTextColor;

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        ViewHolder holder = null;
        TagReading rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            binding = TagReadingBinding.inflate(mInflater, parent, false);
            convertView = binding.getRoot();
            holder = new ViewHolder();
            holder.epc = binding.epc;
            holder.message = binding.message;
            holder.sampleCount = binding.sampleCount;
            holder.temperatureCode = binding.temperatureCode;
            holder.sensorCode = binding.sensorCode;
            holder.frequency = binding.frequency;
            holder.rssi = binding.rssi;
            holder.temperature = binding.temperature;

            binding.getRoot().setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        // Fade the view if not seen recently
        convertView.setAlpha((rowItem.getScansSinceLastResponse() > 7) ? 0.4f : 1.0f);
		holder.epc.setText(rowItem.getEPC());
        holder.message.setText(rowItem.getMessage());
        holder.sampleCount.setText(String.format(Locale.US, "S:%d/%d", rowItem.getSampleCount(), rowItem.getSampleSize()));
        holder.temperatureCode.setText(String.format(Locale.US, "TC:%04X", rowItem.getTemperatureCode()));
        holder.sensorCode.setText(String.format(Locale.US, "SC:%04X", rowItem.getSensorCode()));
        holder.frequency.setText(String.format(Locale.US, "F:%d", rowItem.getChannelFrequency()));
        holder.rssi.setText(String.format(Locale.US, "RS:%d", rowItem.getRSSI()));
        if( rowItem.getTemperature() <= TagReading.INVALID_TEMPERATURE )
        {
            holder.temperature.setText(" --.- C");
        }
        else
        {
            holder.temperature.setText(String.format(Locale.US, "%.1f C", rowItem.getTemperature()));
        }

        return convertView;
    }

}
