package com.m.property;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.m.property.beanResponse.PropertySell;
import com.m.property.home.PropertySellAdapter;
import com.m.property.home.PropertySell_Model;
import com.m.property.login.SigninActivity;
import com.m.property.search.SearchActivity;
import com.m.property.utility.AppUtilits;
import com.m.property.utility.NetworkUtility;
import com.m.property.utility.SharePreferenceUtils;
import com.m.property.webServices.ServiceWrapper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
private Toolbar mToolbar;
    private String TAG ="MainActivity";


// Property_Sell Recyler
 private RecyclerView recycler_PropertySell;
    private ArrayList<PropertySell_Model> mPropertySellModelList = new ArrayList<PropertySell_Model>();
   // private NewProd_Adapter newProdAdapter;
   private PropertySellAdapter propertySellAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Property Home");
        mToolbar.setSubtitle("    by Abc ");
            //generate hash key for facebook
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("hash_key", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("hash_key", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("hash_key", "printHashKey()", e);
        }
       /* Button logout =findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
            }
        });*/


        mToolbar.inflateMenu(R.menu.menu_main);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                String title = (String) item.getTitle();
                Toast.makeText(MainActivity.this, title + " Selected !", Toast.LENGTH_SHORT).show();

                switch (item.getItemId()) {

                    case R.id.search:
                        Intent intent =new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent);
                        finish();
                        // Perform the individual Menu Actions.
                        break;

                    case R.id.mail:
                        // Perform some Actions.
                        break;
                    case R.id.logout:
                        SharePreferenceUtils.getInstance().deletePref();
                        Intent signIntent = new Intent(MainActivity.this, SigninActivity.class);
                        startActivity(signIntent);

                        break;

                }

                // Similarly you can write CASES for other menu items as well.


                return true;
            }
        });



        // for new product
        recycler_PropertySell = (RecyclerView) findViewById(R.id.recycler_PropertySell);
        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false);
        recycler_PropertySell.setLayoutManager(mLayoutManger3);
        recycler_PropertySell.setItemAnimator(new DefaultItemAnimator());

        propertySellAdapter = new PropertySellAdapter(this, mPropertySellModelList, GetScreenWidth());
        recycler_PropertySell.setAdapter(propertySellAdapter);


        propertySellRes();
    }




    //
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

        if (!NetworkUtility.isNetworkConnected(MainActivity.this)){
            AppUtilits.displayMessage(MainActivity.this,  getString(R.string.network_not_connected));

        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<PropertySell> call = service.getNewProductRes("1234");
            call.enqueue(new Callback<PropertySell>() {
                @Override
                public void onResponse(Call<PropertySell> call, Response<PropertySell> response) {
                    Log.e(TAG, " response is "+ response.body().getInformation().toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1){
                            if (response.body().getInformation().size()>0){

                                mPropertySellModelList.clear();
                                for (int i=0; i< response.body().getInformation().size(); i++){

                                    mPropertySellModelList.add(new PropertySell_Model(response.body().getInformation().get(i).getPrice(), response.body().getInformation().get(i).getAddress(),
                                            response.body().getInformation().get(i).getSpecification(),response.body().getInformation().get(i).getPhone(),
                                            response.body().getInformation().get(i).getImgUrl()));

                                }

                                propertySellAdapter.notifyDataSetChanged();
                            }

                        }else {
                            Log.e(TAG, "failed to get rnew prod "+ response.body().getMsg());
                            // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                        }
                    }else {
                        // AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.failed_request));

                        //  getNewProdRes();
                    }
                }

                @Override
                public void onFailure(Call<PropertySell> call, Throwable t) {
                    Log.e(TAG, "fail new prod "+ t.toString());

                }
            });

        }

    }
}
