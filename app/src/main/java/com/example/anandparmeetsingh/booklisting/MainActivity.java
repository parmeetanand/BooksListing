package com.example.anandparmeetsingh.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anandparmeetsingh.books.R;
import com.example.anandparmeetsingh.books.WebView;
import com.example.anandparmeetsingh.books.Word;
import com.example.anandparmeetsingh.books.WordActivity;
import com.example.anandparmeetsingh.books.WordAdapter;
import com.example.anandparmeetsingh.books.WordLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Word>> {
    private static final String LOG_TAG = WordActivity.class.getName();
    /**
     * URL for word data from the USGS dataset
     */
    private static String USGS_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=harrypotter&key=AIzaSyDB2RQT9jF5WqppJqsEbKK-qa9K85DEwIk";
    /**
     * Adapter for the list of earthquakes
     */
    public WordAdapter mAdapter;
    ListView wordListView;
    ArrayList<Word> words;
    private TextView mEmptyStateTextView;
    private String mQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get a reference to the LoaderManager, in order to interact with loaders.
        wordListView = (ListView) findViewById(R.id.list);
        words = new ArrayList<>();
        mAdapter = new WordAdapter(MainActivity.this, words);


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
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (USGS_REQUEST_URL == null || !isConnected) {
            wordListView.setEmptyView(mEmptyStateTextView);
            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
            mEmptyStateTextView.setText("No Connection");
        }
        // If there is a network connection, fetch data

        final EditText mEditText = (EditText) findViewById(R.id.search_go_btn);

        if (USGS_REQUEST_URL == null || !isConnected) {
            wordListView.setEmptyView(mEmptyStateTextView);
            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
            mEmptyStateTextView.setText("No Connection");
        } else {
            USGS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
            //mAdapter.notifyDataSetChanged();
            // Get the text from the EditText and update the mQuery value.Intent in = getIntent();
            Intent in = getIntent();
            mQuery = in.getExtras().getString("location").replaceAll(" ", "+");
            // If it's empty don't proceed.
            if (mQuery.isEmpty()) {
                Toast.makeText(MainActivity.this, "Nothing to search", Toast.LENGTH_SHORT).show();
                wordListView.setEmptyView(mEmptyStateTextView);
                mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
                mEmptyStateTextView.setText("Nothing to Search");
            } else {
                // Update the mRequestUrl value with the new mQuery.
                USGS_REQUEST_URL = USGS_REQUEST_URL + mQuery + "&key=AIzaSyDB2RQT9jF5WqppJqsEbKK-qa9K85DEwIk";
                Log.i("onQueryTextSubmit", "mRequestUrl value is: " + USGS_REQUEST_URL);
                // Restart the loader.
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.restartLoader(1, null, MainActivity.this);
                Log.i("onClick", "loader restarted");
                View progressBar = findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.VISIBLE);
                // Try to make the progress bar appear again (not working)
                // Update mRequestUrl back to its original value.
                USGS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
                // This is what makes the ListView update with new info.
            }
        }

        wordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent web = new Intent(MainActivity.this, WebView.class);
                Word currentBook = mAdapter.getItem(position);
                Uri booksUri = Uri.parse(currentBook.getUrl());
                web.putExtra("locations", booksUri.toString());
                startActivity(web);
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
            mEmptyStateTextView.setText("Nothing Found");
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
