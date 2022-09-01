package com.lib.liblibgo.adapter.subadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.model.subadmin.CreateApartmentAdminModel;
import com.lib.liblibgo.model.subadmin.CustList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerAdapterTwo extends RecyclerView.Adapter<CustomerAdapterTwo.CustManagementHolder> {
    List<CustList> mList;
    Context mCtx;
    String userStatus ="";

    public CustomerAdapterTwo(List<CustList> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public CustManagementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.customer_row, parent, false);
        return new CustManagementHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustManagementHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CustManagementHolder extends RecyclerView.ViewHolder {
        TextView tvCustName, tvPhoneNumber, tvEmail,tvApartmentName,tvFlatNo;
        Switch switchStatus;
        public CustManagementHolder(@NonNull View itemView) {
            super(itemView);
            tvCustName = itemView.findViewById(R.id.tv_name);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvApartmentName = itemView.findViewById(R.id.tv_apart);
            tvFlatNo = itemView.findViewById(R.id.tv_flat_no);
            switchStatus = itemView.findViewById(R.id.switch_status);
        }

        public void bind() {
            tvCustName.setText("Name : "+mList.get(getAdapterPosition()).getName());
            tvPhoneNumber.setText("Phone No : "+mList.get(getAdapterPosition()).getMobile());
            tvEmail.setText("Email-Id : "+mList.get(getAdapterPosition()).getEmail());
            tvApartmentName.setText("Apartment Name : "+mList.get(getAdapterPosition()).getApartment());
            tvFlatNo.setText("Flat No : "+mList.get(getAdapterPosition()).getFlat_no());

            if (mList.get(getAdapterPosition()).getStatus().equals("1")){
                switchStatus.setChecked(true);
            }else {
                switchStatus.setChecked(false);
            }

            switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked == true){
                        userStatus = "1";
                        //changeUserStatus(userStatus,mListItems.get(position).getUser_id());
                    }else {
                        userStatus = "0";
                        //changeUserStatus(userStatus,mListItems.get(position).getUser_id());
                    }

                }
            });

            switchStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeUserStatus(userStatus,mList.get(getAdapterPosition()).getUser_id());
                }
            });
        }
    }

    private void changeUserStatus(String userStatus, String user_id) {
        final ProgressDialog loginDialog = new ProgressDialog(mCtx);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();

        Holders holders = AllApiClass.getInstance().getApi();
        Call<CreateApartmentAdminModel> modelCall = holders.changeStatusCust(user_id,userStatus);
        modelCall.enqueue(new Callback<CreateApartmentAdminModel>() {
            @Override
            public void onResponse(Call<CreateApartmentAdminModel> call, Response<CreateApartmentAdminModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        loginDialog.dismiss();
                        Toast.makeText(mCtx, "Change status successfully !", Toast.LENGTH_SHORT).show();
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(mCtx, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loginDialog.dismiss();
                    Toast.makeText(mCtx, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateApartmentAdminModel> call, Throwable t) {
                loginDialog.dismiss();
                Toast.makeText(mCtx, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setFilter(List<CustList> arrayList) {
        mList = new ArrayList<>();
        mList.addAll(arrayList);
        notifyDataSetChanged();
    }
}
