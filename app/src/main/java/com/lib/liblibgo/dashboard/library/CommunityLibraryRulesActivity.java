package com.lib.liblibgo.dashboard.library;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lib.liblibgo.R;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserCurrentLocation;

import java.util.List;

public class CommunityLibraryRulesActivity extends AppCompatActivity {

    private String mCount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_library_rules);

        mCount = getIntent().getStringExtra("count");

    }

    public void onBackButton(View view) {
        onBackPressed();
    }

    public void onVertualLibrary(View view) {
        UserCurrentLocation location = new UserCurrentLocation(this);
        Log.d("locationnn",""+location.latitude);
        Log.d("locationnn",""+location.longitude);
        Constants.latitude = String.valueOf(location.latitude);
        Constants.longitude = String.valueOf(location.longitude);
        Constants.SelectedLibraryProfileImage = "";
        Intent intent = new Intent(CommunityLibraryRulesActivity.this, CreateLibraryActivity.class);
        Constants.isOwnLibrary = false;
        Constants.libraryType = "virtual";
        startActivity(intent);
    }

   /* public void onPhysicalLibrary(View view) {
        UserCurrentLocation location = new UserCurrentLocation(this);
        Log.d("locationnn",""+location.latitude);
        Log.d("locationnn",""+location.longitude);
        Constants.latitude = String.valueOf(location.latitude);
        Constants.longitude = String.valueOf(location.longitude);
        Constants.SelectedLibraryProfileImage = "";
        Intent intent = new Intent(CommunityLibraryRulesActivity.this, CreateLibraryActivity.class);
        Constants.isOwnLibrary = false;
        Constants.libraryType = "physical";
        startActivity(intent);
    }*/

    public void gotoLibList(View view) {
        Dexter.withContext(CommunityLibraryRulesActivity.this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (mCount.equals("0")){
                            Intent intentHome = new Intent(CommunityLibraryRulesActivity.this, LibraryActivity.class);
                            startActivity(intentHome);
                        }else {
                            Intent intentHome = new Intent(CommunityLibraryRulesActivity.this, RequestedLibraryActivity.class);
                            overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_top);
                            startActivity(intentHome);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
}