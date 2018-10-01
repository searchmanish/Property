package com.m.property.settings;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.m.property.R;
import com.m.property.addProperty.AddPropertyActivity;
import com.m.property.login.LoginActivity;
import com.m.property.login.SigninActivity;
import com.m.property.utility.Constant;
import com.m.property.utility.SharePreferenceUtils;

import static com.m.property.R.drawable.ic_buyer_green;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
RelativeLayout rootlayout;
    ImageView buyerView,sellerView;
    Boolean b=true;
    Boolean c=true;
    FloatingActionButton floatingActionButton;
    String userName;
    String userMobile;
    //
    //checkbox
    private CheckBox mSell, mPurchase,mRent;
    private String categoryValue;

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

        // Bind checkbox
        setUpCheckBox();

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

                        //checkbox visibility
                        mSell.setVisibility(View.GONE);
                        mPurchase.setVisibility(View.GONE);
                        mRent.setVisibility(View.GONE);


                    }
                }
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPropertyIntent =new Intent(SettingsActivity.this, AddPropertyActivity.class);
                addPropertyIntent.putExtra("property_category",categoryValue);
                addPropertyIntent.putExtra("property_userName",userName);
                startActivity(addPropertyIntent);
            }
        });

         userName = SharePreferenceUtils.getInstance().getString(Constant.USER_name);
         userMobile = SharePreferenceUtils.getInstance().getString(Constant.USER_phone);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mSell.setVisibility(View.VISIBLE);
        mPurchase.setVisibility(View.VISIBLE);
        mRent.setVisibility(View.VISIBLE);
        String mTemp="Seller";
        category(mTemp);
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
                  // floatingActionButton.setVisibility(View.VISIBLE);
                   //checkbox visibility
                   mSell.setVisibility(View.VISIBLE);
                   mPurchase.setVisibility(View.VISIBLE);
                   mRent.setVisibility(View.VISIBLE);
                  /* if(temp=="Seller" && categoryValue!=null)
                   {
                       floatingActionButton.setVisibility(View.VISIBLE);
                   }*/
               }

           }
    }


    private void setUpCheckBox() {
        // Bind components and set listeners in onCreate()
        mSell = (CheckBox) findViewById(R.id.typeSell);
        mPurchase = (CheckBox) findViewById(R.id.typePurchase);
        mRent = (CheckBox) findViewById(R.id.typeRent);
        mSell.setOnCheckedChangeListener(this);
        mPurchase.setOnCheckedChangeListener(this);
        mRent.setOnCheckedChangeListener(this);

        mSell.setVisibility(View.GONE);
        mPurchase.setVisibility(View.GONE);
        mRent.setVisibility(View.GONE);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.typeSell:
                if (isChecked) {
                    mPurchase.setChecked(false);
                    mRent.setChecked(false);
                    //
                    floatingActionButton.setVisibility(View.VISIBLE);
                    categoryValue = mSell.getText().toString();
                    Toast.makeText(SettingsActivity.this, "" + categoryValue, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.typePurchase:
                if (isChecked) {
                    mSell.setChecked(false);
                    mRent.setChecked(false);
                   //
                    floatingActionButton.setVisibility(View.VISIBLE);
                    categoryValue = mPurchase.getText().toString();
                    Toast.makeText(SettingsActivity.this, "" + categoryValue, Toast.LENGTH_SHORT).show();
                    break;
                }

            case R.id.typeRent:
                if (isChecked) {
                    mPurchase.setChecked(false);
                    mSell.setChecked(false);
                    //
                    floatingActionButton.setVisibility(View.VISIBLE);
                    categoryValue = mRent.getText().toString();
                    Toast.makeText(SettingsActivity.this, "" + categoryValue, Toast.LENGTH_SHORT).show();
                    break;
                }

        }

    }



 /*   private void setUpResources() {
        buyerView = findViewById(R.id.buyer);
        sellerView = findViewById(R.id.seller);
    }*/
}
