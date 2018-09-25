package com.m.property.settings;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.m.property.R;
import com.m.property.addProperty.AddPropertyActivity;
import com.m.property.login.LoginActivity;
import com.m.property.login.SigninActivity;
import com.m.property.utility.Constant;
import com.m.property.utility.SharePreferenceUtils;

import static com.m.property.R.drawable.ic_buyer_green;

public class SettingsActivity extends AppCompatActivity {
RelativeLayout rootlayout;
    ImageView buyerView,sellerView;
    Boolean b=true;
    Boolean c=true;
    FloatingActionButton floatingActionButton;
    String userName;
    String userMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_previous);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rootlayout= findViewById(R.id.rootlayout);
        buyerView = findViewById(R.id.buyer);
        sellerView = findViewById(R.id.seller);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        /*setUpResources();*/

        buyerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c) {
                    if (b) {
                        buyerView.setImageResource(R.drawable.ic_buyer_green);
                        b = false;
                        String buyer="Buyer";
                        category(buyer);
                    } else {
                        buyerView.setImageResource(R.drawable.ic_buyer);
                        b = true;

                    }
                }
            }
        });


        sellerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b) {
                    if (c) {
                        sellerView.setImageResource(R.drawable.ic_agent_green);
                        c = false;
                       // floatingActionButton.setVisibility(View.VISIBLE);
                        String seller="Seller";
                         category(seller);
                    } else {
                        sellerView.setImageResource(R.drawable.ic_agent);
                        c = true;
                        floatingActionButton.setVisibility(View.GONE);


                    }
                }
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPropertyIntent =new Intent(SettingsActivity.this, AddPropertyActivity.class);
                startActivity(addPropertyIntent);
            }
        });

         userName = SharePreferenceUtils.getInstance().getString(Constant.USER_name);
         userMobile = SharePreferenceUtils.getInstance().getString(Constant.USER_phone);

    }

    private void category(String temp) {

           if(userMobile.isEmpty()) {

               Snackbar snackbar = Snackbar.make(rootlayout, "Selected Category  " + temp, Snackbar.LENGTH_LONG);
               View snackBarView = snackbar.getView();
               snackBarView.setBackgroundColor(getResources().getColor(R.color.green));
               snackbar.show();
               snackbar.setAction("Login", new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent settingsIntent = new Intent(SettingsActivity.this, SigninActivity.class);
                       startActivity(settingsIntent);
                       finish();
                   }
               });
           }
           else {

                 Snackbar snackbar = Snackbar.make(rootlayout, "Selected Category  " + temp+" Welcome "+ userName, Snackbar.LENGTH_LONG);
               View snackBarView = snackbar.getView();
               snackBarView.setBackgroundColor(getResources().getColor(R.color.green));
                 snackbar.show();
               if(temp=="Seller") {
                   floatingActionButton.setVisibility(View.VISIBLE);
               }

           }
    }






 /*   private void setUpResources() {
        buyerView = findViewById(R.id.buyer);
        sellerView = findViewById(R.id.seller);
    }*/
}
