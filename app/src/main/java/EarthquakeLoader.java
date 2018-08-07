import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.quakereport.Earthquake;
import com.example.android.quakereport.QueryUtils;

import java.net.URL;
import java.util.List;

//Loads a list of Earthquakes by using AsyncTask to perform the network request given to the URL

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private String mUrl;

    public EarthquakeLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<Earthquake> resultList = QueryUtils.fetchEarthquakeData(mUrl);
        return resultList;

    }


}
