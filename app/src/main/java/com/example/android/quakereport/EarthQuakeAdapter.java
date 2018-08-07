package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;
import android.graphics.drawable.GradientDrawable;


public class EarthQuakeAdapter extends ArrayAdapter<Earthquake> {
    public EarthQuakeAdapter(@NonNull Context context, int resource, @NonNull ArrayList earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        Earthquake currentQuake = getItem(position);

        //If there is no current working view, inflate a new one and populate the convertView with
        //views from the Viewholder
        if (convertView == null) {
            //Create a LayoutInflator
            LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item, null);

            //Create a Viewholder and populate it with the required views for display within our convertView.
            holder.mag_textview = (TextView) convertView.findViewById(R.id.mag_textview);
            holder.loc_primary_textview = (TextView) convertView.findViewById(R.id.loc_primary_textview);
            holder.loc_offset_textview = (TextView) convertView.findViewById(R.id.loc_offset_textview);
            holder.date_textview = (TextView) convertView.findViewById(R.id.date_textview);
            holder.time_textview = (TextView) convertView.findViewById(R.id.time_textview);

            //Set the tag of the convertView with the holder
            convertView.setTag(holder);

            //If there an existing convertView, get the previous tag from the convertView
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        //If there is an Earthquake object in the current position of the Arraylist, set the details on the CurrentQuake
        if (currentQuake != null){

            //get the JSON double corresponding to the mag in the current position
            double magDouble = currentQuake.getMag();

            //create a decimal formatter with the pattern 0.0
            DecimalFormat decFormatter = new DecimalFormat("0.0");
            String magToDisplay = decFormatter.format(magDouble);
            //set the formatted String on the mag_TextView
            holder.mag_textview.setText(magToDisplay);

            // Set the proper background color on the magnitude circle.
            // Fetch the background from the TextView, which is a GradientDrawable.
            GradientDrawable magnitudeCircle = (GradientDrawable) holder.mag_textview.getBackground();

            // Get the appropriate background color based on the current earthquake magnitude
            int magnitudeColor = getMagnitudeColor(magDouble);

            // Set the color on the magnitude circle
            magnitudeCircle.setColor(magnitudeColor);

            //get the JSON long in the current position
            long timeInMilliseconds = currentQuake.getTimeInMilliseconds();
            //create a new Date object for formatting by SimpleDataFormat and pass in the long variable
            Date dateObject = new Date(timeInMilliseconds);

            //Create a SimpleDataFormat with the pattern LLL dd, yyyy
            SimpleDateFormat dateFormatter = new SimpleDateFormat("LLL dd, yyyy");
            //format the dateObject and save the String format in the variable DateToDisplay
            String dateToDisplay = dateFormatter.format(dateObject);
            //Set this String on the date TextView
            holder.date_textview.setText(dateToDisplay);

            //repeat formatting for time with the pattern h:mm a, using the same JSON long
            SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
            String timeToDisplay = timeFormatter.format(dateObject);
            holder.time_textview.setText(timeToDisplay);

            //get the JSON String in the current position corresponding to the location
            String currentLocation = currentQuake.getPlace();

            //if currentLocation contains a number, set the split Strings
            if(currentLocation.matches(".*\\d+.*")) {
                //split the String into an Array with 2 parts at the first instance of "of", and include "of" in the first indexed String
                String[] locationSplit = currentLocation.split("(?<=of)",2);
                //separate the indexes into two Strings
                String locationPrimary = locationSplit[1];
                String locationOffset = locationSplit[0];
                holder.loc_offset_textview.setText(locationOffset);
                holder.loc_primary_textview.setText(locationPrimary);
            //if currentLocation does not contain a number, set the full location on the primary TextView
            //and set the String "near the" as the location offset
            } else {
                holder.loc_offset_textview.setText(R.string.near_the);
                holder.loc_primary_textview.setText(currentLocation);
            }
        }

        return convertView;
    }
    //helper method for getting the correct color to set as background for magnitude circle
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    private static class ViewHolder{
        public TextView mag_textview;
        public TextView loc_primary_textview;
        public TextView loc_offset_textview;
        public TextView date_textview;
        public TextView time_textview;

    }

}
