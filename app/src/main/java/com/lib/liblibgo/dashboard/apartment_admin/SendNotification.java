package com.lib.liblibgo.dashboard.apartment_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.subadmin.CreateApartmentAdminModel;
import com.lib.liblibgo.views.MyButton;
import com.lib.liblibgo.views.MyEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendNotification extends AppCompatActivity {
    private ImageView ivToolbarBack;
    private TextView tvToolbarTitle;
    private MyEditText etTitle,etmsg;
    //private Spinner spCust;
    //private CustAdapter custAdapter;
    /*List<CustList> custList = new ArrayList<>();
    List<CustList> temp_list = new ArrayList<>();*/
    private String userId = "";
    private MyButton sendBtn;
    UserDatabase preferences;
    private TextView customerNameTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        ivToolbarBack = (ImageView) findViewById(R.id.backTool);
        tvToolbarTitle = (TextView) findViewById(R.id.titleTool);
        setUpToolbar(getString(R.string.info_notification_send));

        preferences = new UserDatabase(SendNotification.this);

        userId = getIntent().getStringExtra("customer_id");

        etTitle = (MyEditText)findViewById(R.id.etTitle);
        etmsg = (MyEditText)findViewById(R.id.etmsg);
        customerNameTv = (TextView)findViewById(R.id.customerNameTv);
        //spCust = (Spinner)findViewById(R.id.spCust);
        sendBtn = (MyButton)findViewById(R.id.sendBtn);

        //showCustomer();
        customerNameTv.setText(getIntent().getStringExtra("customer_name"));

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etTitle.getText().toString().trim();
                String msg = etmsg.getText().toString().trim();
                if (title.equals("")){
                    Toast.makeText(SendNotification.this, "Enter title.", Toast.LENGTH_SHORT).show();
                }else if (msg.equals("")){
                    Toast.makeText(SendNotification.this, "Enter message.", Toast.LENGTH_SHORT).show();
                }else {
                    sendNotification(userId,title,msg);
                }
            }
        });
    }

    /*private void showCustomer() {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CustomerModel> modelCall = holders.getCustomer(preferences.getUserId());
        modelCall.enqueue(new Callback<CustomerModel>() {
            @Override
            public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        custList = response.body().getResponse().getCustomer_list();
                        CustList data = new CustList();
                        data.setUser_id("");
                        data.setName("All Customers");
                        temp_list.add(data);
                        for (int i=0;i<custList.size();i++){
                            temp_list.add(custList.get(i));
                        }
                        spCust.setAdapter(new CustAdapter());

                        spCust.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (i != 0 || !temp_list.get(i).getName().equals("All Customers")){
                                    //Toast.makeText(AddApartmentAdminActivity.this, "id : "+temp_list.get(i).getApartmentId(), Toast.LENGTH_SHORT).show();
                                    userId = temp_list.get(i).getUser_id();
                                }else {
                                    userId = "";
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        sendBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String title = etTitle.getText().toString().trim();
                                String msg = etmsg.getText().toString().trim();
                                if (title.equals("")){
                                    Toast.makeText(SendNotification.this, "Enter title.", Toast.LENGTH_SHORT).show();
                                }else if (msg.equals("")){
                                    Toast.makeText(SendNotification.this, "Enter message.", Toast.LENGTH_SHORT).show();
                                }else {
                                    sendNotification(userId,title,msg);
                                }
                            }
                        });

                    }else {
                        Toast.makeText(SendNotification.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(SendNotification.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CustomerModel> call, Throwable t) {
                Toast.makeText(SendNotification.this, "Server not responding !", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

   /* public class CustAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return temp_list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            convertView = inflater.inflate(R.layout.row_spinner_item, null);
            TextView tv_name = (TextView) convertView.findViewById(R.id.tvApartmentName);
            tv_name.setText(temp_list.get(position).getName());
            return convertView;
        }
    }*/

    private void sendNotification(String userId, String title, String msg) {
        final ProgressDialog loginDialog = new ProgressDialog(SendNotification.this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CreateApartmentAdminModel> modelCall = holders.sendNotificationUser(userId,title,msg);
        modelCall.enqueue(new Callback<CreateApartmentAdminModel>() {
            @Override
            public void onResponse(Call<CreateApartmentAdminModel> call, Response<CreateApartmentAdminModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        loginDialog.dismiss();
                        Toast.makeText(SendNotification.this, "Notification Sent !", Toast.LENGTH_SHORT).show();
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(SendNotification.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loginDialog.dismiss();
                    Toast.makeText(SendNotification.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateApartmentAdminModel> call, Throwable t) {
                loginDialog.dismiss();
                Toast.makeText(SendNotification.this, " "+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpToolbar(String param) {
        tvToolbarTitle.setText(param);
        ivToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }
}