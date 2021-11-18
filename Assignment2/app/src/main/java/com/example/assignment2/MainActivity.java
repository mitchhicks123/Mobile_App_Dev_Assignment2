package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.math.*;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements Dialog.DialogListener {

    // creating variables for our edittext, button and dbhandler
    EditText addressInputVal;
    TextView latitudeOutputVal, longitudeOutputValue;
    Button addLocationButton, updateLocationButton, deleteLocationButton, searchLocationButton;
    double longitude, latitude;

    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing editText and Button Variables.
        addressInputVal = findViewById(R.id.addressInput);
        addLocationButton = findViewById(R.id.saveButton);
        updateLocationButton = findViewById(R.id.updateButton);
        deleteLocationButton = findViewById(R.id.deleteButton);
        searchLocationButton = findViewById(R.id.searchButton);
        latitudeOutputVal = findViewById(R.id.latitudeOutput);
        longitudeOutputValue = findViewById(R.id.longitudeOutput);

        // creating a new dbhandler object
        // and passing our context to it.
        dbHandler = new DBHandler(MainActivity.this);

        //latitude and longitude pairs
        Double[] latitudes = {40.7127837,
                14.579,
                -26.2044,
                35.6785,
                19.4271,
                12.97,
                4.6473,
                13.0827,
                17.37,
                25.04,
                34.5155,
                9.0084,
                -25.746111,
                -0.2295,
                17.7041,
                19.2925,
                33.6751,
                53.9678,
                15.3556,
                48.2092,
                20.6666,
                45.4625,
                42.7105,
                50.8371,
                -16.517,
                53.5444,
                45.25,
                -16.5,
                31.7857,
                45.815,
                -29.116667,
                39.45,
                38.8921,
                15.3315,
                55.6763,
                -14.0089,
                41.3317,
                28.2096,
                -13.525,
                -22.5749,
                11.941667,
                -29.2976,
                29.39,
                35.1725,
                27.6866};

        Double[] longitudes = {-74.0059413,
                120.9726,
                28.0456,
                139.6823,
                -99.1276,
                77.59,
                -74.0962,
                80.2707,
                78.48,
                102.41,
                69.1952,
                38.7575,
                28.188056,
                -78.5243,
                83.2977,
                -99.6569,
                73.0946,
                27.5766,
                44.2081,
                16.3728,
                -103.35,
                9.186389,
                23.3238,
                4.3676,
                -68.167,
                -113.4909,
                -75.41,
                -68.15,
                35.2007,
                15.9785,
                26.216667,
                -104.52,
                -77.0241,
                38.9183,
                12.5681,
                -67.3223,
                19.8172,
                83.9856,
                -71.9722,
                17.0805,
                108.438333,
                27.4854,
                91.07,
                75.384,
                83.4323};


        //need to geocode the address for the first 50 locations given the latitude and longitude pairs above
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        System.out.println ("\n \n \n 50 database values \n" );
        try {
            for (int i = 0; i < latitudes.length; i++){
                List<Address> listOfAddresses = geocoder.getFromLocation( latitudes[i],longitudes[i], 1);
                if (listOfAddresses.size() > 0 ){
                    String cityName = listOfAddresses.get(0).getLocality();
                    String countryName = listOfAddresses.get(0).getCountryName();
                    String fullAddress = cityName + ", " + countryName;
                    System.out.println(fullAddress + "   " + latitudes[i] + "   " + longitudes[i]);

                    dbHandler.addLocation(fullAddress, latitudes[i], longitudes[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // search the database for the address given by the user
        searchLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressInputVal.getText().toString();
                double[] answer;
                double checker = 200;

                //check to ensure user inputted a value
                if (address.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter an address you would like to add!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //call db to query the address
                answer = dbHandler.readLocationAddress(address);
                //if doesn't exist then make a toast message other wise print lat and long
                if (checker == answer[0]){
                    Toast.makeText(MainActivity.this, "Address is not in database... please save!", Toast.LENGTH_SHORT).show();
                }
                else{
                    String longitude ="";
                    String latitude = "";
                    latitude = String.valueOf(Math.round(answer[0] * 10000.0) / 10000.0) + " deg";
                    longitude = String.valueOf(Math.round(answer[1] * 10000.0) / 10000.0) + " deg";

                    latitudeOutputVal.setText(latitude);
                    longitudeOutputValue.setText(longitude);
                }
            }
        });

        //add an address to the database
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressInputVal.getText().toString();

                //check to ensure user inputted a value
                if (address.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter an address you would like to add!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    //use geocoder to get the latitude and longitude for the users added address
                    List<Address> listOfAddresses = geocoder.getFromLocationName( address, 1);
                        if (listOfAddresses.size() > 0 ){
                            Address testAddress = listOfAddresses.get(0);
                            double lat  = testAddress.getLatitude();
                            double longt  = testAddress.getLongitude();
                            System.out.println("\n \n \n Address Added \n" + address + " " + lat + " " + longt);
                            dbHandler.addLocation(address, lat, longt);
                        }
                    } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "Address has been successfully added!", Toast.LENGTH_SHORT).show();
                String longitude ="";
                String latitude = "";
                latitude = "- 0.0000 deg";
                longitude = "+ 0.0000 deg";

                latitudeOutputVal.setText(latitude);
                longitudeOutputValue.setText(longitude);

            }
        });

        //update the address given by the user
        updateLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressInputVal.getText().toString();
                double[] answer;
                double checker = 200;

                //check to ensure user inputted a value
                if (address.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter location you would like to update!", Toast.LENGTH_SHORT).show();
                    return;
                }
                answer = dbHandler.readLocationAddress(address);
                if (checker == answer[0]){
                    Toast.makeText(MainActivity.this, "Address is not in database... please try a different address!", Toast.LENGTH_SHORT).show();
                }
                else{

                    openDialog();//calls the alert dialog to change an existing address

                    String longitude ="";
                    String latitude = "";
                    latitude = "- 0.0000 deg";
                    longitude = "+ 0.0000 deg";

                    latitudeOutputVal.setText(latitude);
                    longitudeOutputValue.setText(longitude);
                }
            }
        });

        // delete the address given by the user
        deleteLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressInputVal.getText().toString();

                //check to ensure user inputted a value
                if (address.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter the address you would like to delete!", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbHandler.deleteLocationAddress(address);

                // after adding the data we are displaying a toast message.
                Toast.makeText(MainActivity.this, "Address has been successfully deleted", Toast.LENGTH_SHORT).show();
                addressInputVal.setText("");
                String longitude ="";
                String latitude = "";
                latitude = "- 0.0000 deg";
                longitude = "+ 0.0000 deg";

                latitudeOutputVal.setText(latitude);
                longitudeOutputValue.setText(longitude);
            }
        });
    }
    public void openDialog(){
        //calls Dialog pop up
        Dialog newDialog = new Dialog();
        newDialog.show(getSupportFragmentManager(), "location dialog");
    }

    @Override
    public void text(String newAddress) {
        String address = addressInputVal.getText().toString();

        //calls db to update
        dbHandler.updateLocationAddress(newAddress, address);

        Toast.makeText(MainActivity.this, "Address has been successfully updated", Toast.LENGTH_SHORT).show();
        addressInputVal.setText("");
    }
}
