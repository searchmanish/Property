package com.m.property.home;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.m.property.MainActivity;
import com.m.property.R;
import com.m.property.beanResponse.PropertyDetails;
import com.m.property.beanResponse.PropertyDetailsHot;
import com.m.property.beanResponse.PropertyDetailsOwner;
import com.m.property.beanResponse.PropertySell;
import com.m.property.login.SigninActivity;
import com.m.property.search.SearchActivity;
import com.m.property.settings.SettingsActivity;
import com.m.property.utility.AppUtilits;
import com.m.property.utility.NetworkUtility;
import com.m.property.utility.SharePreferenceUtils;
import com.m.property.webServices.ServiceWrapper;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recycler_Propertydetailsfresh;
    private ArrayList<PropertyDetailsModelFresh> mPDetailsModelFreshList = new ArrayList<PropertyDetailsModelFresh>();
    private PropertyAdapterFresh propertyAdapterFresh;

    //
    private RecyclerView recycler_Propertydetailsowner;
    private ArrayList<PropertyDetailsModelFresh> mPDetailsModelOwnerList = new ArrayList<PropertyDetailsModelFresh>();
    private PropertyAdapterFresh propertyAdapterOwner;

    //
    private RecyclerView recycler_Propertydetailshot;
    private ArrayList<PropertyDetailsModelFresh> mPDetailsModelHotList = new ArrayList<PropertyDetailsModelFresh>();
    private PropertyAdapterFresh propertyAdapterHot;
/*//
private AHBottomNavigation bottomNavigation;
    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();*/

   // Drawer Layout
    private DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    AVLoadingIndicatorView avi;
    private Toolbar mToolbar;
    private String TAG ="HomeActivity";

    //TextView
    TextView textView_fresh,textView_hotdeals,textView_owner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        avi= findViewById(R.id.avi);

        // casting text view
        textView_fresh =findViewById(R.id.textView_fresh);
        textView_owner = findViewById(R.id.textView_owner);
        textView_hotdeals = findViewById(R.id.textView_hotdeals);

        textView_fresh.setVisibility(View.INVISIBLE);
        textView_owner.setVisibility(View.INVISIBLE);
        textView_hotdeals.setVisibility(View.INVISIBLE);

        // Toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(" Home");
        mToolbar.setSubtitle("  by Abc ");
         
        // //setUpNavigationDrawer menu
        NavigationView navigationView = findViewById(R.id.navigationView);
        mDrawerLayout=findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this,mDrawerLayout,mToolbar,
                R.string.drawer_open,R.string.drawer_close);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //setUpNavigationDrawer();

    // Bottom navigation
      /*  bottomNavigation = findViewById(R.id.bottom_navigation);
*/
        mToolbar.inflateMenu(R.menu.menu_main);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                String title = (String) item.getTitle();
                Toast.makeText(HomeActivity.this, title + " Selected !", Toast.LENGTH_SHORT).show();

                switch (item.getItemId()) {

                    case R.id.search:
                        Intent intent =new Intent(HomeActivity.this, SearchActivity.class);
                        startActivity(intent);
                        finish();
                        // Perform the individual Menu Actions.
                        break;

                    case R.id.mail:
                        // Perform some Actions.
                        break;
                    case R.id.logout:
                        SharePreferenceUtils.getInstance().deletePref();
                        Intent signIntent = new Intent(HomeActivity.this, SigninActivity.class);
                        startActivity(signIntent);

                        break;

                    case R.id.settings:
                        Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                        startActivity(settingsIntent);

                }

                // Similarly you can write CASES for other menu items as well.


                return true;
            }
        });


        // for propertyDetailsFresh
        recycler_Propertydetailsfresh = (RecyclerView) findViewById(R.id.recycler_Propertydetailsfresh);
        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false);
        recycler_Propertydetailsfresh.setLayoutManager(mLayoutManger3);
        recycler_Propertydetailsfresh.setItemAnimator(new DefaultItemAnimator());

        propertyAdapterFresh = new PropertyAdapterFresh(this, mPDetailsModelFreshList, GetScreenWidth());
        recycler_Propertydetailsfresh.setAdapter(propertyAdapterFresh);


        // for propertyDetailsOwner
        recycler_Propertydetailsowner = (RecyclerView) findViewById(R.id.recycler_Propertydetailsowner);
        LinearLayoutManager mLayoutManger4 = new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false);
        recycler_Propertydetailsowner.setLayoutManager(mLayoutManger4);
        recycler_Propertydetailsowner.setItemAnimator(new DefaultItemAnimator());

        propertyAdapterOwner = new PropertyAdapterFresh(this, mPDetailsModelOwnerList, GetScreenWidth());
        recycler_Propertydetailsowner.setAdapter(propertyAdapterOwner);

     // for propertyDetailsHot
        recycler_Propertydetailshot = (RecyclerView) findViewById(R.id.recycler_Propertydetailshotdeal);
        LinearLayoutManager mLayoutManger5 = new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false);
        recycler_Propertydetailshot.setLayoutManager(mLayoutManger5);
        recycler_Propertydetailshot.setItemAnimator(new DefaultItemAnimator());

        propertyAdapterHot = new PropertyAdapterFresh(this, mPDetailsModelHotList, GetScreenWidth());
        recycler_Propertydetailshot.setAdapter(propertyAdapterHot);


        propertySellRes();
        propertyOwnerRes();
        propertyHotRes();


        //

       /* AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.save, R.color.color_tab_1);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.search, R.color.color_tab_2);
       // AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.ic_maps_local_restaurant, R.color.color_tab_3);

        bottomNavigationItems.add(item1);
        bottomNavigationItems.add(item2);
       // bottomNavigationItems.add(item3);

        bottomNavigation.addItems(bottomNavigationItems);*/
    }

   /* private void setUpNavigationDrawer() {
        NavigationView navigationView = findViewById(R.id.navigationView);
        mDrawerLayout=findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this,mDrawerLayout,mToolbar,
                R.string.drawer_open,R.string.drawer_close);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
       actionBarDrawerToggle.syncState();


    }*/


    //To get ScreenWidth
    public int GetScreenWidth(){
        int  width=100;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager =  (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        return width;

    }


    private void propertySellRes() {
        //avi.show();
        avi.smoothToShow();
        if (!NetworkUtility.isNetworkConnected(HomeActivity.this)){
            AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.network_not_connected));

        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<PropertyDetails> call = service.PorductDetailsFreshRes("1234");
            call.enqueue(new Callback<PropertyDetails>() {
                @Override
                public void onResponse(Call<PropertyDetails> call, Response<PropertyDetails> response) {
                    Log.e(TAG, " response is "+ response.body().getInformation().toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1){
                            // avi.hide();
                            avi.smoothToHide();
                            enabledTitleTextViews();
                            if (response.body().getInformation().size()>0){

                                mPDetailsModelFreshList.clear();
                                for (int i=0; i< response.body().getInformation().size(); i++){

                                    mPDetailsModelFreshList.add(new PropertyDetailsModelFresh(response.body().getInformation().get(i).getPropertyDetails(), response.body().getInformation().get(i).getPropertyAddress(),
                                            response.body().getInformation().get(i).getPropertyPhone(),response.body().getInformation().get(i).getPropertyImage(),
                                            response.body().getInformation().get(i).getPropertyMrp()));
                                   /* mPDetailsModelFreshList.add(new PropertyDetailsModelFresh(response.body().getInformation().get(i).getPropertyDetails(), response.body().getInformation().get(i).getPropertyAddress(),
                                            response.body().getInformation().get(i).getPropertyMrp(), response.body().getInformation().get(i).getPropertyPhone(),response.body().getInformation().get(i).getPropertyImage()
                                           ));
*/
                                }

                                propertyAdapterFresh.notifyDataSetChanged();
                            }

                        }else {
                            // avi.hide();
                            avi.smoothToHide();
                            Log.e(TAG, "failed to get rnew prod "+ response.body().getMsg());
                            // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                        }
                    }else {
                        // AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.failed_request));
                        // avi.hide();
                        avi.smoothToHide();
                        //  getNewProdRes();
                    }
                }

                @Override
                public void onFailure(Call<PropertyDetails> call, Throwable t) {
                    Log.e(TAG, "fail new prod "+ t.toString());
                    // avi.hide();
                    avi.smoothToHide();
                }
            });

        }
    }


    private void propertyOwnerRes() {
        avi.smoothToShow();
        if (!NetworkUtility.isNetworkConnected(HomeActivity.this)){
            AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.network_not_connected));

        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<PropertyDetailsOwner> call = service.PorductDetailsOwner("1234");
            call.enqueue(new Callback<PropertyDetailsOwner>() {
                @Override
                public void onResponse(Call<PropertyDetailsOwner> call, Response<PropertyDetailsOwner> response) {
                    Log.e(TAG, " response is "+ response.body().getInformation().toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1){
                            // avi.hide();
                            avi.smoothToHide();
                            enabledTitleTextViews();
                            if (response.body().getInformation().size()>0){

                                mPDetailsModelOwnerList.clear();
                                for (int i=0; i< response.body().getInformation().size(); i++){

                                    mPDetailsModelOwnerList.add(new PropertyDetailsModelFresh(response.body().getInformation().get(i).getPropertyDetails(), response.body().getInformation().get(i).getPropertyAddress(),
                                            response.body().getInformation().get(i).getPropertyPhone(),response.body().getInformation().get(i).getPropertyImage(),
                                            response.body().getInformation().get(i).getPropertyMrp()));
                                   /* mPDetailsModelFreshList.add(new PropertyDetailsModelFresh(response.body().getInformation().get(i).getPropertyDetails(), response.body().getInformation().get(i).getPropertyAddress(),
                                            response.body().getInformation().get(i).getPropertyMrp(), response.body().getInformation().get(i).getPropertyPhone(),response.body().getInformation().get(i).getPropertyImage()
                                           ));
*/
                                }

                                propertyAdapterOwner.notifyDataSetChanged();
                            }

                        }else {
                            // avi.hide();
                            avi.smoothToHide();
                            Log.e(TAG, "failed to get rnew prod "+ response.body().getMsg());
                            // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                        }
                    }else {
                        // avi.hide();
                        avi.smoothToHide();
                        // AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.failed_request));

                        //  getNewProdRes();
                    }
                }

                @Override
                public void onFailure(Call<PropertyDetailsOwner> call, Throwable t) {
                    Log.e(TAG, "fail new prod "+ t.toString());
                    // avi.hide();
                    avi.smoothToHide();
                }
            });

        }

    }


    private void propertyHotRes() {
        avi.smoothToShow();
        if (!NetworkUtility.isNetworkConnected(HomeActivity.this)){
            AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.network_not_connected));

        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<PropertyDetailsHot> call = service.PorductDetailsHot("1234");
            call.enqueue(new Callback<PropertyDetailsHot>() {
                @Override
                public void onResponse(Call<PropertyDetailsHot> call, Response<PropertyDetailsHot> response) {
                    Log.e(TAG, " response is "+ response.body().getInformation().toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1){
                            // avi.hide();
                            avi.smoothToHide();
                            enabledTitleTextViews();
                            if (response.body().getInformation().size()>0){

                                mPDetailsModelHotList.clear();
                                for (int i=0; i< response.body().getInformation().size(); i++){

                                    mPDetailsModelHotList.add(new PropertyDetailsModelFresh(response.body().getInformation().get(i).getPropertyDetails(), response.body().getInformation().get(i).getPropertyAddress(),
                                            response.body().getInformation().get(i).getPropertyPhone(),response.body().getInformation().get(i).getPropertyImage(),
                                            response.body().getInformation().get(i).getPropertyMrp()));
                                   /* mPDetailsModelFreshList.add(new PropertyDetailsModelFresh(response.body().getInformation().get(i).getPropertyDetails(), response.body().getInformation().get(i).getPropertyAddress(),
                                            response.body().getInformation().get(i).getPropertyMrp(), response.body().getInformation().get(i).getPropertyPhone(),response.body().getInformation().get(i).getPropertyImage()
                                           ));
*/
                                }

                                propertyAdapterHot.notifyDataSetChanged();
                            }

                        }else {
                            // avi.hide();
                            avi.smoothToHide();
                            Log.e(TAG, "failed to get rnew prod "+ response.body().getMsg());
                            // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                        }
                    }else {
                        // avi.hide();
                        avi.smoothToHide();
                        // AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.failed_request));

                        //  getNewProdRes();
                    }
                }

                @Override
                public void onFailure(Call<PropertyDetailsHot> call, Throwable t) {
                    Log.e(TAG, "fail new prod "+ t.toString());
                    // avi.hide();
                    avi.smoothToHide();
                }
            });

        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String itemName = (String) item.getTitle();
        Toast.makeText(HomeActivity.this, ""+itemName, Toast.LENGTH_SHORT).show();
        closeDrawer();
        switch ( item.getItemId())
        {
            case R.id.save:
                break;
            case R.id.profile:
                break;
            case R.id.setting:
                break;
        }
        return false;
    }

    private void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }
    // Open the Drawer
    private void showDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            closeDrawer();
        else
            super.onBackPressed();
    }

    public void enabledTitleTextViews(){
        textView_hotdeals.setVisibility(View.VISIBLE);
        textView_owner.setVisibility(View.VISIBLE);
        textView_fresh.setVisibility(View.VISIBLE);
    }
}
