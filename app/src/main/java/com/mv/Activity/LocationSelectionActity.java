package com.mv.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mv.Model.Task;
import com.mv.Model.User;
import com.mv.R;
import com.mv.Retrofit.ApiClient;
import com.mv.Retrofit.AppDatabase;
import com.mv.Retrofit.ServiceRequest;
import com.mv.Utils.Constants;
import com.mv.Utils.LocaleManager;
import com.mv.Utils.PreferenceHelper;
import com.mv.Utils.Utills;
import com.mv.databinding.ActivityLoactionSelectionActityBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationSelectionActity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ImageView img_back, img_list, img_logout;
    private TextView toolbar_title;
    private RelativeLayout mToolBar;
    private Button btn_submit;
    private ActivityLoactionSelectionActityBinding binding;

     private int mSelectRole = 0,mSelectState=1, mSelectDistrict = 1, mSelectTaluka = 0, mSelectCluster = 0, mSelectVillage = 0, mSelectSchoolName = 0;
    private List<String> mListDistrict, mListTaluka, mListCluster, mListVillage, mListSchoolName,mStateList;


    private ArrayAdapter<String> district_adapter, taluka_adapter, cluster_adapter, village_adapter, school_adapter, state_adapter,organization_adapter;
    private PreferenceHelper preferenceHelper;
    private User user;
    private Uri FinalUri = null;
    private Uri outputUri = null;
    private String imageFilePath;
    String msg="";
   private  int locationState;
    private Boolean isAdd;
    ArrayList<Task> taskList=new ArrayList<>();
    private boolean isDistrictSet = false, isRollSet = false;
    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_loaction_selection_actity);
        binding.setActivity(this);
        if(getIntent().getSerializableExtra(Constants.PROCESS_ID)!=null) {
            taskList = getIntent().getParcelableArrayListExtra(Constants.PROCESS_ID);
        }

        initViews();


    }



    private void showPopUp() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Setting Dialog Title
        alertDialog.setTitle(getString(R.string.app_name));

        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.error_no_internet));

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.logomulya);

        // Setting CANCEL Button
        alertDialog.setButton2(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });
        // Setting OK Button
        alertDialog.setButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void getDistrict() {

        Utills.showProgressDialog(this, getString(R.string.loding_district), getString(R.string.progress_please_wait));
        ServiceRequest apiService =
                ApiClient.getClientWitHeader(this).create(ServiceRequest.class);
        String url = preferenceHelper.getString(PreferenceHelper.InstanceUrl)
                + "/services/apexrest/getDistrict_Name__c?StateName=Maharashtra";
        apiService.getSalesForceData(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utills.hideProgressDialog();
                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    mListDistrict.clear();
                    mListDistrict.add("Select");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mListDistrict.add(jsonArray.getString(i));
                    }
                    district_adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utills.hideProgressDialog();

            }
        });
    }

    private void initViews() {
        setActionbar("Select Location");
        Utills.setupUI(findViewById(R.id.layout_main), this);
        preferenceHelper = new PreferenceHelper(this);
//        if (Utills.isConnected(this))
//            getState();
//        else
//        {
//
//        }

            binding.spinnerState.setOnItemSelectedListener(this);
            binding.spinnerDistrict.setOnItemSelectedListener(this);
            binding.spinnerTaluka.setOnItemSelectedListener(this);
            binding.spinnerCluster.setOnItemSelectedListener(this);
            binding.spinnerVillage.setOnItemSelectedListener(this);
            binding.spinnerSchoolName.setOnItemSelectedListener(this);
            binding.btnSubmit.setOnClickListener(this);


        mListDistrict = new ArrayList<String>();
        mListTaluka = new ArrayList<String>();
        mListCluster = new ArrayList<String>();
        mListVillage = new ArrayList<String>();
        mListSchoolName = new ArrayList<String>();

        mStateList = new ArrayList<String>();
        mListDistrict.add(User.getCurrentUser(context).getDistrict());
        mListTaluka.add("Select");
        mListCluster.add("Select");
        mListVillage.add("Select");
        mListSchoolName.add("Select");
        if (Utills.isConnected(this))
            getTaluka();
        else
        {

            mListTaluka= AppDatabase.getAppDatabase(context).userDao().getTaluka(User.getCurrentUser(context).getState(),User.getCurrentUser(context).getDistrict());
            mListTaluka.add(0,"Select");
        }


        mStateList = new ArrayList<String>(Arrays.asList(getColumnIdex((User.getCurrentUser(getApplicationContext()).getState()).split(","))));
        state_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mStateList);
        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerState.setAdapter(state_adapter);



        district_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mListDistrict);
        district_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDistrict.setAdapter(district_adapter);

        taluka_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mListTaluka);
        taluka_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTaluka.setAdapter(taluka_adapter);

        cluster_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mListCluster);
        cluster_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCluster.setAdapter(cluster_adapter);

        village_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mListVillage);
        village_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerVillage.setAdapter(village_adapter);

        school_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mListSchoolName);
        school_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSchoolName.setAdapter(school_adapter);

        if(preferenceHelper.getString(Constants.STATE_LOCATION_LEVEL).equals("State"))
        {
            binding.spinnerDistrict.setVisibility(View.GONE);
            binding.tvDistrict.setVisibility(View.GONE);

            binding.spinnerTaluka.setVisibility(View.GONE);
            binding.tvTaluka.setVisibility(View.GONE);

            binding.spinnerCluster.setVisibility(View.GONE);
            binding.tvCluster.setVisibility(View.GONE);

            binding.spinnerVillage.setVisibility(View.GONE);
            binding.tvVillage.setVisibility(View.GONE);

            binding.spinnerSchoolName.setVisibility(View.GONE);
            binding.tvSchool.setVisibility(View.GONE);
        }
        else  if(preferenceHelper.getString(Constants.STATE_LOCATION_LEVEL).equals("District"))
        {
            binding.spinnerTaluka.setVisibility(View.GONE);
            binding.tvTaluka.setVisibility(View.GONE);

            binding.spinnerCluster.setVisibility(View.GONE);
            binding.tvCluster.setVisibility(View.GONE);

            binding.spinnerVillage.setVisibility(View.GONE);
            binding.tvVillage.setVisibility(View.GONE);

            binding.spinnerSchoolName.setVisibility(View.GONE);
            binding.tvSchool.setVisibility(View.GONE);
        }else   if(preferenceHelper.getString(Constants.STATE_LOCATION_LEVEL).equals("Taluka"))
        {
            binding.spinnerCluster.setVisibility(View.GONE);
            binding.tvCluster.setVisibility(View.GONE);

            binding.spinnerVillage.setVisibility(View.GONE);
            binding.tvVillage.setVisibility(View.GONE);

            binding.spinnerSchoolName.setVisibility(View.GONE);
            binding.tvSchool.setVisibility(View.GONE);
        }
        else   if(preferenceHelper.getString(Constants.STATE_LOCATION_LEVEL).equals("Cluster"))
        {
            binding.spinnerVillage.setVisibility(View.GONE);
            binding.tvVillage.setVisibility(View.GONE);

            binding.spinnerSchoolName.setVisibility(View.GONE);
            binding.tvSchool.setVisibility(View.GONE);
        }
        else   if(preferenceHelper.getString(Constants.STATE_LOCATION_LEVEL).equals("Village"))
        {
            binding.spinnerSchoolName.setVisibility(View.GONE);
            binding.tvSchool.setVisibility(View.GONE);

            /*if (task.getTask_Response__c() != null)
                holder.spinnerResponse.setSelection(myList.indexOf(task.getTask_Response__c().trim()));*/
        }
        else   if(preferenceHelper.getString(Constants.STATE_LOCATION_LEVEL).equals("School"))
        {
    /*        if (taskList.get(0).getTask_Response__c() != null)
               binding.spinnerState.setSelection(mStateList.indexOf(taskList.get(0).getTask_Response__c().trim()));
            if (taskList.get(1).getTask_Response__c() != null)
                mListSchoolName.add(taskList.get(1).getTask_Response__c().trim());
                binding.spinnerSchoolName.setSelection(mListSchoolName.indexOf(taskList.get(1).getTask_Response__c().trim()));
            if (taskList.get(2).getTask_Response__c() != null)
                mListVillage.add(taskList.get(2).getTask_Response__c().trim());
                binding.spinnerVillage.setSelection(mListVillage.indexOf(taskList.get(2).getTask_Response__c().trim()));
            if (taskList.get(3).getTask_Response__c() != null)
                mListCluster.add(taskList.get(3).getTask_Response__c().trim());
                binding.spinnerCluster.setSelection(mListCluster.indexOf(taskList.get(3).getTask_Response__c().trim()));
            if (taskList.get(4).getTask_Response__c() != null)
                mListTaluka.add(taskList.get(4).getTask_Response__c().trim());
                binding.spinnerTaluka.setSelection(mListTaluka.indexOf(taskList.get(4).getTask_Response__c().trim()));
            if (taskList.get(5).getTask_Response__c() != null)
                mListDistrict.add(taskList.get(5).getTask_Response__c().trim());
                binding.spinnerDistrict.setSelection(mListDistrict.indexOf(taskList.get(5).getTask_Response__c().trim()));*/
        }





    }
    private void getState() {

        Utills.showProgressDialog(this, "Loading States", getString(R.string.progress_please_wait));
        ServiceRequest apiService =
                ApiClient.getClientWitHeader(getApplicationContext()).create(ServiceRequest.class);
        String url = preferenceHelper.getString(PreferenceHelper.InstanceUrl)
                + "/services/apexrest/getStateName";
        apiService.getSalesForceData(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utills.hideProgressDialog();
                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    mStateList.clear();
                    mStateList.add("Select");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mStateList.add(jsonArray.getString(i));
                    }
                    state_adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utills.hideProgressDialog();

            }
        });
    }
    public static String[] getColumnIdex(String[] value) {

        for (int i = 0; i < value.length; i++) {
            value[i] = value[i].trim();
        }
        return value;

    }
    private void setActionbar(String Title) {
        mToolBar = (RelativeLayout) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(Title);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
        img_logout = (ImageView) findViewById(R.id.img_logout);
        img_logout.setVisibility(View.GONE);
        img_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:

                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                break;
            case R.id.btn_submit:

                    sendLocation();

               // sendData();
                break;

        }
    }

    private void sendLocation() {
        if(preferenceHelper.getString(Constants.STATE_LOCATION_LEVEL).equals("State"))
        {     taskList.get(0).setTask_Response__c(binding.spinnerState.getSelectedItem().toString());
            locationState=1;
        }
        else  if(preferenceHelper.getString(Constants.STATE_LOCATION_LEVEL).equals("District"))
        {
            locationState=2;
            taskList.get(0).setTask_Response__c(binding.spinnerState.getSelectedItem().toString());
            taskList.get(1).setTask_Response__c(binding.spinnerDistrict.getSelectedItem().toString());
        }else   if(preferenceHelper.getString(Constants.STATE_LOCATION_LEVEL).equals("Taluka"))
        {
            locationState=3;
            taskList.get(0).setTask_Response__c(binding.spinnerState.getSelectedItem().toString());
            taskList.get(1).setTask_Response__c(binding.spinnerDistrict.getSelectedItem().toString());
            taskList.get(2).setTask_Response__c(binding.spinnerTaluka.getSelectedItem().toString());
        }
        else   if(preferenceHelper.getString(Constants.STATE_LOCATION_LEVEL).equals("Cluster"))
        {
            locationState=4;
            taskList.get(0).setTask_Response__c(binding.spinnerState.getSelectedItem().toString());
            taskList.get(1).setTask_Response__c(binding.spinnerDistrict.getSelectedItem().toString());
            taskList.get(2).setTask_Response__c(binding.spinnerTaluka.getSelectedItem().toString());
            taskList.get(3).setTask_Response__c(binding.spinnerCluster.getSelectedItem().toString());

        }
        else   if(preferenceHelper.getString(Constants.STATE_LOCATION_LEVEL).equals("Village"))
        {
            locationState=5;
            taskList.get(0).setTask_Response__c(binding.spinnerState.getSelectedItem().toString());
            taskList.get(1).setTask_Response__c(binding.spinnerDistrict.getSelectedItem().toString());
            taskList.get(2).setTask_Response__c(binding.spinnerTaluka.getSelectedItem().toString());
            taskList.get(3).setTask_Response__c(binding.spinnerCluster.getSelectedItem().toString());
            taskList.get(4).setTask_Response__c(binding.spinnerVillage.getSelectedItem().toString());

        }
        else   if(preferenceHelper.getString(Constants.STATE_LOCATION_LEVEL).equals("School"))
        {
            locationState=6;
            taskList.get(0).setTask_Response__c(binding.spinnerState.getSelectedItem().toString());
            taskList.get(1).setTask_Response__c(binding.spinnerDistrict.getSelectedItem().toString());
            taskList.get(2).setTask_Response__c(binding.spinnerTaluka.getSelectedItem().toString());
            taskList.get(3).setTask_Response__c(binding.spinnerCluster.getSelectedItem().toString());
            taskList.get(4).setTask_Response__c(binding.spinnerVillage.getSelectedItem().toString());
            taskList.get(5).setTask_Response__c(binding.spinnerSchoolName.getSelectedItem().toString());
        }
        msg="";
            for (int i=0;i<locationState;i++)
            {
                if(taskList.get(i).getTask_Response__c().equals("Select"))
                {
                  msg="Please Select "+taskList.get(i).getTask_Text__c();
                  break;
                }
            }
            if(msg.isEmpty()) {
                preferenceHelper.insertBoolean(Constants.NEW_PROCESS, true);
                Intent openClass = new Intent(context, ProcessDeatailActivity.class);
                openClass.putExtra(Constants.PROCESS_ID, taskList);
                openClass.putParcelableArrayListExtra(Constants.PROCESS_ID, taskList);
                //  openClass.putExtra("stock_list", resultList.get(getAdapterPosition()).get(0));
                startActivity(openClass);
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
            else
            {
                Utills.showToast(msg,getApplicationContext());
            }


    }


    private boolean isValidate() {
        String msg = "";
        if (mSelectState == 0) {
            msg = "Please select State";
        }
      else if (mSelectDistrict == 0) {
            msg = "Please select District";
        } else if ( (mSelectTaluka == 0)) {
            msg = "Please select Taluka";
        } else if ( (mSelectCluster == 0)) {
            msg = "Please select Cluster";
        } else if  (mSelectVillage == 0) {
            msg = "Please select Village";
        } else if (mSelectSchoolName == 0) {
            msg = "Please select School";
        }
        if (TextUtils.isEmpty(msg))
            return true;
        Utills.showToast(msg, this);
        return false;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }
    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
         /*   case R.id.spinner_state:
                mSelectState = i;

                if (mSelectState != 0) {

                    getDistrict();
                }
                mListDistrict.clear();
                mListTaluka.clear();
                mListCluster.clear();
                mListVillage.clear();
                mListSchoolName.clear();
                mListDistrict.add("Select");
                mListTaluka.add("Select");
                mListCluster.add("Select");
                mListVillage.add("Select");
                mListSchoolName.add("Select");
                district_adapter.notifyDataSetChanged();
                taluka_adapter.notifyDataSetChanged();
                cluster_adapter.notifyDataSetChanged();
                village_adapter.notifyDataSetChanged();
                school_adapter.notifyDataSetChanged();
                break;

            case R.id.spinner_district:
                mSelectDistrict = i;
                if (mSelectDistrict != 0) {
                    getTaluka();
                }
                mListTaluka.clear();
                mListCluster.clear();
                mListVillage.clear();
                mListSchoolName.clear();
                mListTaluka.add("Select");
                mListCluster.add("Select");
                mListVillage.add("Select");
                mListSchoolName.add("Select");
                taluka_adapter.notifyDataSetChanged();
                cluster_adapter.notifyDataSetChanged();
                village_adapter.notifyDataSetChanged();
                school_adapter.notifyDataSetChanged();

                break;*/
            case R.id.spinner_taluka:
                mSelectTaluka = i;
                if (mSelectTaluka != 0) {
                    if (Utills.isConnected(this))
                    getCluster();
                    else {
                        mListCluster.clear();
                        mListCluster.add("Select");
                        mListCluster = AppDatabase.getAppDatabase(context).userDao().getCluster(User.getCurrentUser(context).getState(), User.getCurrentUser(context).getDistrict(), mListTaluka.get(mSelectTaluka));
                        mListCluster.add(0,"Select");
                        cluster_adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, mListCluster);
                        cluster_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.spinnerCluster.setAdapter(cluster_adapter);

                    }
                }
              //  mListCluster.clear();
                mListVillage.clear();
                mListSchoolName.clear();
              //  mListTaluka.add("Select");
               // mListCluster.add("Select");
                mListVillage.add("Select");
                mListSchoolName.add("Select");

                village_adapter.notifyDataSetChanged();
                school_adapter.notifyDataSetChanged();

                break;
            case R.id.spinner_cluster:
                mSelectCluster = i;
                if (mSelectCluster != 0) {

                    if (Utills.isConnected(this))
                        getVillage();
                    else {
                        mListVillage.clear();
                        mListVillage = AppDatabase.getAppDatabase(context).userDao().getVillage(User.getCurrentUser(context).getState(), User.getCurrentUser(context).getDistrict(), mListTaluka.get(mSelectTaluka),mListCluster.get(mSelectCluster));
                        mListVillage.add(0,"Select");
                        village_adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, mListVillage);
                        village_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.spinnerVillage.setAdapter(village_adapter);

                    }
                 //   getVillage();
                }

              //  mListVillage.clear();
                mListSchoolName.clear();

              //  mListVillage.add("Select");
                mListSchoolName.add("Select");
              //  village_adapter.notifyDataSetChanged();
                school_adapter.notifyDataSetChanged();

                break;
            case R.id.spinner_village:
                mSelectVillage = i;
                if (mSelectVillage != 0) {
                    if (Utills.isConnected(this))
                        getSchool();
                    else {
                        mListSchoolName.clear();
                        mListSchoolName.add("Select");
                        mListSchoolName = AppDatabase.getAppDatabase(context).userDao().getSchoolName(User.getCurrentUser(context).getState(), User.getCurrentUser(context).getDistrict(), mListTaluka.get(mSelectTaluka),mListCluster.get(mSelectCluster),mListVillage.get(mSelectVillage));
                        mListSchoolName.add(0,"Select");
                        school_adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, mListSchoolName);
                        school_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.spinnerSchoolName.setAdapter(school_adapter);

                    }
                }
               /* mListSchoolName.clear();
                mListSchoolName.add("Select");*/
             //   school_adapter.notifyDataSetChanged();

                break;
            case R.id.spinner_school_name:
                mSelectSchoolName = i;

                break;
        }
    }

    private void getCluster() {
        Utills.showProgressDialog(this, getString(R.string.loding_cluster), getString(R.string.progress_please_wait));
        ServiceRequest apiService =
                ApiClient.getClientWitHeader(this).create(ServiceRequest.class);
        String url = preferenceHelper.getString(PreferenceHelper.InstanceUrl)
                + "/services/apexrest/getCluster_Name__c?StateName=Maharashtra&district=" + User.getCurrentUser(context).getDistrict() + "&taluka=" + mListTaluka.get(mSelectTaluka);
        apiService.getSalesForceData(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utills.hideProgressDialog();
                try {
                    mListCluster.clear();
                    mListCluster.add("Select");
                    JSONArray jsonArr = new JSONArray(response.body().string());
                    for (int i = 0; i < jsonArr.length(); i++) {
                        mListCluster.add(jsonArr.getString(i));
                    }
                    cluster_adapter.notifyDataSetChanged();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utills.hideProgressDialog();
            }
        });
    }

    private void getTaluka() {

        Utills.showProgressDialog(this, getString(R.string.loding_taluka), getString(R.string.progress_please_wait));
        ServiceRequest apiService =
                ApiClient.getClientWitHeader(this).create(ServiceRequest.class);
        String url = preferenceHelper.getString(PreferenceHelper.InstanceUrl)
                + "/services/apexrest/getTaluka_Name__c?DistrictName=" + User.getCurrentUser(context).getDistrict() + "&StateName=Maharashtra";


        apiService.getSalesForceData(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utills.hideProgressDialog();
                try {
                    mListTaluka.clear();
                    mListTaluka.add("Select");
                    JSONArray jsonArr = new JSONArray(response.body().string());
                    for (int i = 0; i < jsonArr.length(); i++) {
                        mListTaluka.add(jsonArr.getString(i));
                    }
                    taluka_adapter.notifyDataSetChanged();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utills.hideProgressDialog();
            }
        });
    }

    private void getVillage() {
        Utills.showProgressDialog(this, getString(R.string.loding_village), getString(R.string.progress_please_wait));
        ServiceRequest apiService =
                ApiClient.getClientWitHeader(this).create(ServiceRequest.class);
        String url = preferenceHelper.getString(PreferenceHelper.InstanceUrl)
                + "/services/apexrest/getVillage_Name__c?StateName=Maharashtra&district=" + User.getCurrentUser(context).getDistrict() + "&taluka=" + mListTaluka.get(mSelectTaluka) + "&Cluster=" + mListCluster.get(mSelectCluster);


        apiService.getSalesForceData(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utills.hideProgressDialog();
                try {
                    mListVillage.clear();
                    mListVillage.add("Select");
                    JSONArray jsonArr = new JSONArray(response.body().string());
                    for (int i = 0; i < jsonArr.length(); i++) {
                        mListVillage.add(jsonArr.getString(i));
                    }
                    village_adapter.notifyDataSetChanged();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utills.hideProgressDialog();
            }
        });
    }

    private void getSchool() {
        Utills.showProgressDialog(this, getString(R.string.loding_school), getString(R.string.progress_please_wait));
        ServiceRequest apiService =
                ApiClient.getClientWitHeader(this).create(ServiceRequest.class);
        String url = preferenceHelper.getString(PreferenceHelper.InstanceUrl)
                + "/services/apexrest/getSchoolName?StateName=Maharashtra&district=" + User.getCurrentUser(context).getDistrict() + "&taluka=" + mListTaluka.get(mSelectTaluka) + "&Cluster=" + mListCluster.get(mSelectCluster) + "&Village=" + mListVillage.get(mSelectVillage);


        apiService.getSalesForceData(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utills.hideProgressDialog();
                try {
                    mListSchoolName.clear();
                    mListSchoolName.add("Select");

                    JSONArray jsonArr = new JSONArray(response.body().string());
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObject = jsonArr.getJSONObject(i);
                        mListSchoolName.add(jsonObject.getString("school_name"));
                    }
                    school_adapter.notifyDataSetChanged();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utills.hideProgressDialog();
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }








}
