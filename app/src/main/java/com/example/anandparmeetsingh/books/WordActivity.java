package com.example.anandparmeetsingh.books;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WordActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Word>> {
    private static final String LOG_TAG = WordActivity.class.getName();
    /**
     * URL for word data from the USGS dataset
     */
    private static String USGS_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=harrypotter";
    /**
     * Adapter for the list of earthquakes
     */
    public WordAdapter mAdapter;
    ListView wordListView;
    ArrayList<Word> words;
    private TextView mEmptyStateTextView;
    private String mQuery = "";


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_word);
        // Get a reference to the LoaderManager, in order to interact with loaders.
        wordListView = (ListView) findViewById(R.id.list);
        words = new ArrayList<>();
        mAdapter = new WordAdapter(WordActivity.this, words);


        // Create a new adapter that takes an empty list of earthquakes as input


        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface

        wordListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected word.


        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface

        // Start the AsyncTask to fetch the word data
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        wordListView.setEmptyView(mEmptyStateTextView);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        //imageView = (ImageView) findViewById(R.id.imageView);


        // Get details on the currently active default data network
        final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (USGS_REQUEST_URL == null || networkInfo == null) {
            wordListView.setEmptyView(mEmptyStateTextView);
            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        }
        // If there is a network connection, fetch data

        final EditText mEditText = (EditText) findViewById(R.id.search_go_btn);
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (USGS_REQUEST_URL == null || networkInfo == null) {
                    wordListView.setEmptyView(mEmptyStateTextView);
                    mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
                }
                USGS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
                //mAdapter.notifyDataSetChanged();
                // Get the text from the EditText and update the mQuery value.
                mQuery = mEditText.getText().toString().replaceAll(" ", "+");
                // If it's empty don't proceed.
                if (mQuery.isEmpty()) {
                    Toast.makeText(WordActivity.this, "Nothing to search", Toast.LENGTH_SHORT).show();
                }
                // Update the mRequestUrl value with the new mQuery.
                USGS_REQUEST_URL = USGS_REQUEST_URL + mQuery + "&maxResults=15";
                Log.i("onQueryTextSubmit", "mRequestUrl value is: " + USGS_REQUEST_URL);
                // Restart the loader.
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.restartLoader(1, null, WordActivity.this);
                Log.i("onClick", "loader restarted");
                View progressBar = findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.VISIBLE);
                // Try to make the progress bar appear again (not working)
                //View progressBar = findViewById(R.id.progress_bar);
                //progressBar.setVisibility(View.VISIBLE);
                // Update mRequestUrl back to its original value.
                USGS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
                // This is what makes the ListView update with new info.

            }
        });
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(1, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(View.GONE);

            //Update empty state with no connection error message
            wordListView.setEmptyView(mEmptyStateTextView);
            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
            mEmptyStateTextView.setText("No Connection");
        }


    }

    public Loader<List<Word>> onCreateLoader(int i, Bundle bundle) {
        return new WordLoader(this, USGS_REQUEST_URL);
    }

    /**
     * {@link } to perform the network request on a background thread, and then
     * update the UI with the list of earthquakes in the response.
     * <p>
     * AsyncTask has three generic parameters: the input type, a type used for progress updates, and
     * an output type. Our task will take a String URL, and return an EarthquakeAdapter. We won't do
     * progress updates, so the second generic is just Void.
     * <p>
     * We'll only override two of the methods of AsyncTask: doInBackground() and onPostExecute().
     * The doInBackground() method runs on a background thread, so it can run long-running code
     * (like network activity), without interfering with the responsiveness of the app.
     * Then onPostExecute() is passed the result of doInBackground() method, but runs on the
     * UI thread, so it can use the produced data to update the UI.
     */

    @Override
    public void onLoadFinished(Loader<List<Word>> loader, List<Word> earthquakes) {
        // Clear the adapter of previous word data
        View loadingIndicator = findViewById(R.id.progress_bar);
        loadingIndicator.setVisibility(View.GONE);
        words.clear();
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.

        if (earthquakes != null && !earthquakes.isEmpty()) {
            words.addAll(earthquakes);
        } else {
            wordListView.setEmptyView(mEmptyStateTextView);
            mEmptyStateTextView.setText("No Connection");
        }
        mAdapter.notifyDataSetChanged();

      /*  words.addAll(earthquakes);
        mAdapter.notifyDataSetChanged();*/
    }

    @Override
    public void onLoaderReset(Loader<List<Word>> loader) {
        // Loader reset, so we can clear out our existing data.
        words.clear();
    }


}



