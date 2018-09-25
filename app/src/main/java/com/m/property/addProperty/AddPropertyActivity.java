package com.m.property.addProperty;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.m.property.R;
import com.m.property.beanResponse.AddProperty;
import com.m.property.settings.SettingsActivity;
import com.m.property.utility.AppUtilits;
import com.m.property.utility.NetworkUtility;
import com.m.property.utility.SharePreferenceUtils;
import com.m.property.webServices.ServiceWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPropertyActivity extends AppCompatActivity {
    private String TAG ="AddPropertyActivity";

    EditText mUserId,mMrp,mAddress,mPhone,mPropertyTtype,mDetails;
    TextView mAddProperty;

    private TextInputLayout inputLayoutUserId, inputLayoutPhone, inputLayoutMrp,
            inputLayoutAddress,inputLayoutPropertyType,inputLayoutDetails;

    RelativeLayout rootlayout;

    String user_id,user_phone,address,mrp,details,propertyType;
    boolean isValid=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New Property");
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_previous);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpWidgets();
        getDataFromSharedPrefrence();

      mAddProperty.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
             // String address,mrp,details,propertyType;
              address = mAddress.getText().toString().trim();
               mrp= mMrp.getText().toString().trim();
               details = mDetails.getText().toString().trim();
               propertyType = mPropertyTtype.getText().toString().trim();
              Snackbar.make(rootlayout, " " + address +"mrp "+ mrp +"details "+ details + "propertyType "+propertyType, Snackbar.LENGTH_LONG).show();
              dataValidation();
              if(isValid)
              {
                  addNewPropertyReq();
              }

          }
      });


    }

    private void getDataFromSharedPrefrence() {
        user_id= SharePreferenceUtils.getInstance().getString("USER_id");
        user_phone = SharePreferenceUtils.getInstance().getString("USER_phone");
        mUserId.setText(user_id);
        mPhone.setText(user_phone);
        Toast.makeText(AddPropertyActivity.this, ""+user_id, Toast.LENGTH_SHORT).show();
    }

    private void setUpWidgets() {

        inputLayoutUserId = findViewById(R.id.inputLayoutUserId);
        inputLayoutPhone = findViewById(R.id.inputLayoutPhone);
        inputLayoutAddress = findViewById(R.id.inputLayoutAddress);
        inputLayoutDetails = findViewById(R.id.inputLayoutDetails);
        inputLayoutMrp = findViewById(R.id.inputLayoutMrp);
        inputLayoutPropertyType = findViewById(R.id.inputLayoutPropertyType);

        rootlayout = findViewById(R.id.rootlayout);
        mUserId = findViewById(R.id.user_id);
        mMrp = findViewById(R.id.mrp);
        mAddress = findViewById(R.id.address);
        mPhone = findViewById(R.id.phone);
        mPropertyTtype = findViewById(R.id.property_type);
        mDetails = findViewById(R.id.details);
        mAddProperty = findViewById(R.id.add_property);
    }

    public void addNewPropertyReq(){

        if (!NetworkUtility.isNetworkConnected(AddPropertyActivity.this)){
            AppUtilits.displayMessage(AddPropertyActivity.this,  getString(R.string.network_not_connected));

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<AddProperty> callNewREgistration=   serviceWrapper.addNewProperty(user_id, mrp,address, user_phone,propertyType,
                   details );
            callNewREgistration.enqueue(new Callback<AddProperty>() {
                @Override
                public void onResponse(Call<AddProperty> call, Response<AddProperty> response) {
                    Log.d(TAG, "reponse : "+ response.toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1){
                            String property_id=""+response.body().getInformation().getPropertyId();
                            Toast.makeText(AddPropertyActivity.this, "Property id= "+response.body().getInformation().getPropertyId(), Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar.make(rootlayout,"Property Added Sucessfully",Snackbar.LENGTH_LONG);
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(getResources().getColor(R.color.green));
                            snackbar.show();

                            // start home activity
                            Intent intent = new Intent(AddPropertyActivity.this, ImageActivity.class);
                            intent.putExtra("property_id",property_id);
                            //intent.putExtra("userid", "sdfsd");
                            startActivity(intent);
                            finish();

                        }else {
                            AppUtilits.displayMessage(AddPropertyActivity.this,  response.body().getMsg());
                        }
                    }else {
                        AppUtilits.displayMessage(AddPropertyActivity.this,  getString(R.string.failed_request));

                    }
                }

                @Override
                public void onFailure(Call<AddProperty> call, Throwable t) {
                    Log.e(TAG, " failure "+ t.toString());
                    AppUtilits.displayMessage(AddPropertyActivity.this,  getString(R.string.failed_request));

                }
            });
        }


    }


    private void dataValidation() {
        isValid = true;

        if (mUserId.getText().toString().isEmpty()) {
            inputLayoutUserId.setError("User Id is mandatory");
            isValid = false;
        } else {
            inputLayoutUserId.setErrorEnabled(false);
        }

        if (mMrp.getText().toString().isEmpty()) {
            inputLayoutMrp.setError("MRP Missing");
            isValid = false;
        } else {
            inputLayoutMrp.setErrorEnabled(false);
        }

        if (mPhone.getText().toString().trim().isEmpty() ) {
            inputLayoutPhone.setError("Enter Valid Phone Number");
            isValid = false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
        }
        if (mAddress.getText().toString().trim().isEmpty() ) {
            inputLayoutAddress.setError("Enter Address");
            isValid = false;
        } else {
            inputLayoutAddress.setErrorEnabled(false);
        }
        if (mPropertyTtype.getText().toString().trim().isEmpty() ) {
            inputLayoutPropertyType.setError("Enter property type");
            isValid = false;
        } else {
            inputLayoutPropertyType.setErrorEnabled(false);
        }
        if (mDetails.getText().toString().trim().isEmpty() ) {
            inputLayoutDetails.setError("Enter Property Details");
            isValid = false;
        } else {
            inputLayoutDetails.setErrorEnabled(false);
        }


        if (isValid) {
            Toast.makeText(AddPropertyActivity.this, "Data Verified", Toast.LENGTH_SHORT).show();
        }
    }
}
