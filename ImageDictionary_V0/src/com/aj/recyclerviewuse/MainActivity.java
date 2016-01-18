package com.aj.recyclerviewuse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.aj.imagedictionary.R;

public class MainActivity extends Activity {

	List<WordObject> words;
	Context context;
	private SharedPreferences sp;
	private DatabaseHandler db;
	private static String url = "http://appsculture.com/vocab/words.json";
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sp = getSharedPreferences("Info", MODE_PRIVATE);
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);
		db = new DatabaseHandler(this);

		if (sp.getBoolean("first_time", true)) {
			if (isNetworkAvailable()) {
				new GetWords().execute();
				sp.edit().putBoolean("first_time", false).commit();
			}
		}

		RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
		rv.setHasFixedSize(true);

		LinearLayoutManager llm = new LinearLayoutManager(context);
		rv.setLayoutManager(llm);

		words = new ArrayList<>();

		words = db.getAllData();

		if (words != null) {
			RVAdapter adapter = new RVAdapter(words);
			rv.setAdapter(adapter);
		} else if (isNetworkAvailable()) {
			new GetWords().execute();
			RVAdapter adapter = new RVAdapter(words);
			rv.setAdapter(adapter);
			words = db.getAllData();
		} else {
			Toast.makeText(MainActivity.this,
					"Internet Connection is not Working", Toast.LENGTH_LONG)
					.show();
		}

	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetWords extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog.setMessage("Fetching Words ...");
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					JSONArray words = jsonObj.getJSONArray("words");

					for (int i = 0; i < words.length(); i++) {
						JSONObject c = words.getJSONObject(i);
						int id = Integer.parseInt(c.getString("id"));
						String word = c.getString("word");
						String meaning = c.getString("meaning");
						String ratio = c.getString("ratio");
						if (!ratio.contains("-")) {

							db.insertdata(id, word, meaning, ratio);
						}

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			Toast.makeText(MainActivity.this, "database updated",
					Toast.LENGTH_LONG).show();
		}

	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
