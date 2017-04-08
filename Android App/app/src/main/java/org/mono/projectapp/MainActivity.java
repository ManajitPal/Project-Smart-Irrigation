package org.mono.projectapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TextView textViewJSON;
    private Button buttonGet;
    private int sensordata;
    SwitchCompat toggle;
    private static final String JSON_URL = "http://ROOT_URL/smartirrigation/moisturedata.php/";
    public static final String ROOT_URL = "http://ROOT_URL/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String tkn = FirebaseInstanceId.getInstance().getToken();
        Log.d("App", "Token ["+tkn+"]");
        textViewJSON = (TextView) findViewById(R.id.textViewJSON);
        buttonGet = (Button) findViewById(R.id.buttonGet);
        toggle = (SwitchCompat) findViewById(R.id.toggleswitch);
        buttonGet.setOnClickListener(this);
        toggle.setOnCheckedChangeListener(this);
        getJSON(JSON_URL);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (isChecked) {

                insertToggle(1);
            } else {

               insertToggle(0);
            }
        }

    private void insertToggle(int TS){
        //Here we will handle the http request to insert user to mysql db
        //Creating a RestAdapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        ToggleAPI api = adapter.create(ToggleAPI.class);

        //Defining the method insertuser of our interface
        api.insert(
                //Passing the values by getting it from editTexts
                TS,
                //Creating an anonymous callback
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {
                        //On success we will read the server's output using bufferedreader
                        //Creating a bufferedreader object
                        BufferedReader reader = null;

                        //An string to store output from the server
                        String output = "";

                        try {
                            //Initializing buffered reader
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                            //Reading the output in the string
                            output = reader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Displaying the output as a toast
                        Toast.makeText(MainActivity.this, output, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast
                        Toast.makeText(MainActivity.this, error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        );
    }


    @Override
    public void onClick(View v){

        if(v==buttonGet){
            getJSON(JSON_URL);
        }
    }

    private void getJSON(String url) {

        class GetJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Loading sensor data...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject reader = new JSONObject(s);
                    sensordata = Integer.parseInt(reader.optString("Moisture").toString());

                }

                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                loading.dismiss();
                AnimateCounter animateCounter = new AnimateCounter.Builder(textViewJSON)
                        .setCount(0, sensordata)
                        .setDuration(1150)
                        .setInterpolator(new LinearInterpolator())
                        .build();

                animateCounter.execute();

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }



}
