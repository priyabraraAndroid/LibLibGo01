package com.lib.liblibgo.dashboard.book_details;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserCurrentLocation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.lib.liblibgo.common.UserCurrentLocation.LOCATION_SETTINGS_REQUEST_CODE;

public class AddressActivity extends AppCompatActivity {
    private TextView titleTool;
    private ImageView backTool;
    private EditText etName;
    private EditText etPhone;
    private EditText etPincode;
    private EditText etFlat;
    private EditText etArea;
    private EditText etCity;
    private EditText etState;
    private EditText etLandmark;
    List<Address> addresses;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setTopView(getString(R.string.my_address));

        etName = (EditText)findViewById(R.id.etName);
        etPhone = (EditText)findViewById(R.id.etPhone);
        etPincode = (EditText)findViewById(R.id.etPincode);
        etFlat = (EditText)findViewById(R.id.etFlat);
        etArea = (EditText)findViewById(R.id.etArea);
        etCity = (EditText)findViewById(R.id.etCity);
        etState = (EditText)findViewById(R.id.etState);
        etLandmark = (EditText)findViewById(R.id.etLandmark);

        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        showAddressDetails();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String pinCode = etPincode.getText().toString().trim();
                String flat = etFlat.getText().toString().trim();
                String area = etArea.getText().toString().trim();
                String city = etCity.getText().toString().trim();
                String state = etState.getText().toString().trim();
                String landmark = etLandmark.getText().toString().trim();

                if (name.equals("")){
                    Toast.makeText(AddressActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                }else if (phone.equals("")){
                    Toast.makeText(AddressActivity.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                }else if (pinCode.equals("")){
                    Toast.makeText(AddressActivity.this, "Enter pincode", Toast.LENGTH_SHORT).show();
                }else if (flat.equals("")){
                    Toast.makeText(AddressActivity.this, "Enter flat", Toast.LENGTH_SHORT).show();
                }else if (area.equals("")){
                    Toast.makeText(AddressActivity.this, "Enter city", Toast.LENGTH_SHORT).show();
                }else if (city.equals("")){
                    Toast.makeText(AddressActivity.this, "Enter city", Toast.LENGTH_SHORT).show();
                }else if (state.equals("")){
                    Toast.makeText(AddressActivity.this, "Enter state", Toast.LENGTH_SHORT).show();
                }else {
                    String fullAddress = area +","+flat+","+landmark+","+city+","+state+","+pinCode;
                    CheckoutActivity.tvAddress.setText(fullAddress);
                    CheckoutActivity.tvUserName.setText(name);
                    CheckoutActivity.tv_phone.setText(phone);
                    CheckoutActivity.tv_phone.setTextColor(Color.parseColor("#737272"));
                    CheckoutActivity.tv_phone.setEnabled(false);
                    Constants.fullAddress = fullAddress;
                    finish();
                }
            }
        });

    }

    private void showAddressDetails() {
        UserCurrentLocation location = new UserCurrentLocation(AddressActivity.this);
        Log.d("locationnn",""+location.latitude);
        Log.d("locationnn",""+location.longitude);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!addresses.isEmpty()){
            if (addresses.size() == 1){
                String city = addresses.get(0).getSubAdminArea();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getLocality();// Only if available else return NULL
                //adr = knownName+","+city+","+state+","+country+","+postalCode;
                etPincode.setText(postalCode);
                etCity.setText(city);
                etState.setText(state);
                etArea.setText(knownName);
            }else {
                String city = addresses.get(1).getSubAdminArea();
                String state = addresses.get(1).getAdminArea();
                String country = addresses.get(1).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(1).getLocality();// Only if available else return NULL
                //adr = knownName+","+city+","+state+","+country+","+postalCode;
                etPincode.setText(postalCode);
                etCity.setText(city);
                etState.setText(state);
                etArea.setText(knownName);
            }

        }else {
            etArea.setText("");
            etCity.setText("");
            etState.setText("");
            etPincode.setText("");
        }
    }

    private void setTopView(String string) {
        titleTool.setText(string);
        backTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    public void onGoCheckout(View view) {

    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case LOCATION_SETTINGS_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        System.out.println("test user has turned the gps back on");
                        showAddressDetails();
                        break;
                    case Activity.RESULT_CANCELED:
                        System.out.println("test user has denied the gps to be turned on");
                        Toast.makeText(AddressActivity.this, "Location is required to order stations", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }*/
}

