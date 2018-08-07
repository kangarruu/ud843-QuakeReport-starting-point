package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    //Method to take in a String and convert it into a URL object
    private static URL createUrl (String queryString) {
        URL url = null;
        try {
            url = new URL(queryString);
        } catch (MalformedURLException e) {
            Log.e("LOG_TAG", "Error creating URL object from queryString", e);
        }
        return url;
    }

    //Method to take in a URL and connect to the server. Returns a String containing the contents
    // of the inputStream received from the server
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //if url is null return empty jsonResponse String
        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            //if the connection was successful, proceed with getting the InputStream
            //read the stream and parse the response
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("LOG_Tag", "Error creating URLConnection: " + urlConnection.getResponseCode());
            }

        } catch(IOException e){
            Log.e("LOG_TAG", "Error making HTTP request",e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //Method to convert an InputStream into a String containing the contents of the InputStream
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder outputString = new StringBuilder();
        if(inputStream != null){
            InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String nextLine = bufferedReader.readLine();
            while (nextLine != null){
                outputString.append(nextLine);
                nextLine = bufferedReader.readLine();
            }
        } return outputString.toString();
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Earthquake> extractEarthquakes(String jsonResponse){
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();

        try {
            //Parse the response given by the jsonResponse string and
            // build up a list of Earthquake objects with the corresponding data.
            //turn the query response into a JSONObject
            JSONObject parsedResponse = new JSONObject(jsonResponse);
            //Create a new JSONArray called features by extracting all Feature objects
            JSONArray features = parsedResponse.getJSONArray("features");

            //iterate through a loop and collect the mag, place and time keys for all listed Feature objects in the Array
            for (int i = 0; i < features.length(); i++){
                JSONObject earthquake = features.getJSONObject(i);
                JSONObject properties = earthquake.getJSONObject("properties");
                double mag = properties.getDouble("mag");
                String place = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");

                Earthquake quake = new Earthquake(place, time, mag, url);
                earthquakes.add(quake);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    public static List<Earthquake> fetchEarthquakeData(String requestUrl){
        //Create a URL object from input param
        URL url = createUrl(requestUrl);
        // perform HTTP request and receive a JSON response String. Initiate a String before try block
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){

            Log.e("LOG_TAG", "Error making HTTP request",e);
        }
        //Extract relevant fields from the JSON response and create a list of Earthquakes
        List<Earthquake> earthquakes = extractEarthquakes(jsonResponse);
        return earthquakes;
    }

}