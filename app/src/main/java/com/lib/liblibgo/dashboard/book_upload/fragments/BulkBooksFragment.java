package com.lib.liblibgo.dashboard.book_upload.fragments;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.FileUtil;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.BookListActivity;
import com.lib.liblibgo.dashboard.apartment_admin.MyBooksActivity;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.listner.MoveToCommunityClickListener;
import com.lib.liblibgo.model.CheckCommunityModel;
import com.lib.liblibgo.model.CommunityListModel;
import com.lib.liblibgo.model.SubmitDataResModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class BulkBooksFragment extends Fragment {

    private Context mContext;
    private UserDatabase database;
    private CheckBox cbCommunity;
    private CheckBox cbOpen;
    private CheckBox cbSelect;
    private TextView tvLibraryName;
    private RecyclerView rvCommunity;
    private RelativeLayout rlOwnCommunity;
    private LinearLayout llCommunities;
    private LinearLayout llCommunityList;
    private RelativeLayout rlExpandView;
    private ImageView ivExpand;
    private TextView tvExpand;
    private List<CommunityListModel.ResDataBooks.CommunityList> myList = new ArrayList<>();
    private CommunityAdapterNew adapter;
    private TextView tvFileName;
    private ImageView ivFileSelect;
    private Button btnDownload;
    private Button uploadCsvFileBtn;
    DownloadManager manager;
    private int EXCEL_IMPORTED = 1;
    private String isOwnLirary = "0";
    private String path = "";
    private String isOpenLib = "1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bulk_books, container, false);

        database = new UserDatabase(mContext);
        Constants.selectedBulkCommunityIds = "";

        cbCommunity = (CheckBox)view.findViewById(R.id.cbCommunity);
        cbOpen = (CheckBox)view.findViewById(R.id.cbOpen);
        cbSelect = (CheckBox)view.findViewById(R.id.cbSelect);
        tvLibraryName = (TextView)view.findViewById(R.id.tvLibraryName);
        rvCommunity = (RecyclerView)view.findViewById(R.id.rvCommunity);
        rlOwnCommunity = (RelativeLayout)view.findViewById(R.id.rlOwnCommunity);

        llCommunities = (LinearLayout)view.findViewById(R.id.llCommunities);
        llCommunityList = (LinearLayout)view.findViewById(R.id.llCommunityList);
        rlExpandView = (RelativeLayout)view.findViewById(R.id.rlExpandView);
        ivExpand = (ImageView)view.findViewById(R.id.ivExpand);
        tvExpand = (TextView)view.findViewById(R.id.tvExpand);

        tvFileName = (TextView)view.findViewById(R.id.tvFileName);
        ivFileSelect = (ImageView)view.findViewById(R.id.ivFileSelect);
        btnDownload = (Button)view.findViewById(R.id.btnDownload);
        uploadCsvFileBtn = (Button)view.findViewById(R.id.uploadCsvFileBtn);

        /*Constants.selectedStackCommunityIds = "";
        database = new UserDatabase(mContext);*/
        checkCommunity();

        cbOpen.setChecked(true);
        cbOpen.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

        cbOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbOpen.isChecked()){
                    isOpenLib = "1";
                    cbOpen.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                }else {
                    isOpenLib = "0";
                    cbOpen.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.black_fade)));
                }
            }
        });

        ivFileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse("https://liblibgo.com/uploads/csv/SampleBooks.xlsx");
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "SampleBooks.xlsx");
                request.allowScanningByMediaScanner();
                long reference = manager.enqueue(request);
                Toast.makeText(mContext, "Downloading...", Toast.LENGTH_SHORT).show();
            }
        });

        ivFileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, EXCEL_IMPORTED);
            }
        });

        uploadCsvFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (path.equals("")){
                    Toast.makeText(mContext, "Select file", Toast.LENGTH_SHORT).show();
                }else {
                    readExcelData(path);
                }

            }
        });

        return view;
    }

    private void readExcelData(String path) {
        Log.d("myExelFile",path);
        final ProgressDialog progressBar = new ProgressDialog(mContext);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();

        MultipartBody.Part part = null;
        try {
            File file = new File(path);
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("application/xlsx"), file);
            part = MultipartBody.Part.createFormData("uploadFile", file.getName(), fileReqBody);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("myFilePath",""+part);
        RequestBody userIdReq = RequestBody.create(MediaType.parse("multipart/form-data"), database.getUserId());
        RequestBody isOwnLiraryReq = RequestBody.create(MediaType.parse("multipart/form-data"), isOwnLirary);
        RequestBody communityIdReq = RequestBody.create(MediaType.parse("multipart/form-data"), Constants.selectedBulkCommunityIds);
        RequestBody isOpenLibraryReq = RequestBody.create(MediaType.parse("multipart/form-data"), isOpenLib);
        // database.getUserId(),et_library_name.getText().toString().trim(),mEtAddress.getText().toString().trim(), Constants.latitude,Constants.longitude,isShelfPickup

        Call<SubmitDataResModel> resCall = holders.uploadBookByCsvFile(userIdReq,part,isOwnLiraryReq,communityIdReq,isOpenLibraryReq);
        resCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progressBar.dismiss();
                        CollectApartmentBooksActivity.flag = 1;
                        getActivity().finish();
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(mContext, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(mContext, "All the fields are required.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == EXCEL_IMPORTED) {
                Uri uri = data.getData();
                Log.d("MyPath", "uri path: "+uri.getPath());
                path = FileUtil.getFileAbsolutePath(mContext, uri);
                tvFileName.setText(""+path);
                Log.d("MyPath", "file path: "+path);
            }else {
                path = "";
            }
        } else {
            path = "";
            Log.i("debinf cliinfo", "resultCol NOT OK");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        /*if (isVisibleToUser){

        }*/
    }

    private void checkCommunity() {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CheckCommunityModel> call = holders.checkUserCommunityStatus(database.getUserId());
        call.enqueue(new Callback<CheckCommunityModel>() {
            @Override
            public void onResponse(Call<CheckCommunityModel> call, Response<CheckCommunityModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        if (response.body().getResponse().getCommunity_status().equals("true")){
                            cbCommunity.setEnabled(true);
                            cbCommunity.setTextColor(Color.parseColor("#2B2A2A"));
                            cbCommunity.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                            showCommunityList();
                        }else {
                            cbCommunity.setChecked(false);
                            cbCommunity.setEnabled(false);
                            cbCommunity.setTextColor(Color.parseColor("#C4C4C4"));
                            cbCommunity.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.rect_color)));
                        }

                        cbCommunity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (cbCommunity.isChecked()){
                                    llCommunities.setVisibility(View.VISIBLE);
                                    Constants.selectedBulkCommunityIds =  android.text.TextUtils.join(",",Constants.selectedBulkCommunityList);
                                }else {
                                    llCommunities.setVisibility(View.GONE);
                                    Constants.selectedBulkCommunityIds = "";
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckCommunityModel> call, Throwable t) {

            }
        });
    }

    private void showCommunityList() {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CommunityListModel> call = holders.getMyAllCommunities(database.getUserId());
        call.enqueue(new Callback<CommunityListModel>() {
            @Override
            public void onResponse(Call<CommunityListModel> call, Response<CommunityListModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        Constants.selectedBulkCommunityList = new ArrayList<>();
                        myList = response.body().getResponse().getCommunity_list();
                        if (response.body().getResponse().getCommunity_library_status().equals("1")){
                            rlOwnCommunity.setVisibility(View.VISIBLE);
                            tvLibraryName.setText(response.body().getResponse().getOwn_library_name());
                            Constants.selectedBulkCommunityList.add("0");
                            cbSelect.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (cbSelect.isChecked()){
                                        Constants.selectedBulkCommunityList.add("0");
                                    }else {
                                        Constants.selectedBulkCommunityList.remove("0");
                                    }

                                    Constants.selectedBulkCommunityIds =  android.text.TextUtils.join(",",Constants.selectedBulkCommunityList);
                                }
                            });

                        }else {
                            rlOwnCommunity.setVisibility(View.GONE);
                        }

                        if (myList.size() > 0){
                            adapter = new CommunityAdapterNew(myList, mContext);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                            rvCommunity.setLayoutManager(verticalManager);
                            rvCommunity.setAdapter(adapter);
                        }
                    }
                }else {
                    Toast.makeText(mContext, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommunityListModel> call, Throwable t) {
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class CommunityAdapterNew extends RecyclerView.Adapter<CommunityAdapterNew.MyHolder> {
        List<CommunityListModel.ResDataBooks.CommunityList> mList;
        Context mCtx;
        MoveToCommunityClickListener listener;
        private boolean isSelected = true;

        public CommunityAdapterNew(List<CommunityListModel.ResDataBooks.CommunityList> mList, Context mCtx) {
            this.mList = mList;
            this.mCtx = mCtx;
        }

        @NonNull
        @Override
        public CommunityAdapterNew.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(mCtx).inflate(R.layout.community_select_row, parent, false);
            return new CommunityAdapterNew.MyHolder(mView);
        }

        @Override
        public void onBindViewHolder(@NonNull CommunityAdapterNew.MyHolder holder, int position) {
            holder.bind();
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder {
            TextView tvLibraryName;
            CheckBox cbSelectHolder;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                tvLibraryName = itemView.findViewById(R.id.tvLibraryName);
                cbSelectHolder = itemView.findViewById(R.id.cbSelectHolder);

            }

            public void bind() {
                tvLibraryName.setText(mList.get(getAdapterPosition()).getCommunity_library_name());

                Constants.selectedBulkCommunityList.add(mList.get(getAdapterPosition()).getCommunity_id());
                Constants.selectedBulkCommunityIds =  android.text.TextUtils.join(",",Constants.selectedBulkCommunityList);

                cbSelectHolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //isSelected = mList.get(getAdapterPosition()).getIsSelected() ? true : false;
                        if (cbSelectHolder.isChecked()){
                            Constants.selectedBulkCommunityList.add(mList.get(getAdapterPosition()).getCommunity_id());
                        }else {
                            Constants.selectedBulkCommunityList.remove(mList.get(getAdapterPosition()).getCommunity_id());
                        }

                        Constants.selectedBulkCommunityIds =  android.text.TextUtils.join(",",Constants.selectedBulkCommunityList);
                    }
                });

            }
        }

        public void setOnItemClickListener(MoveToCommunityClickListener listener){
            this.listener = listener;
        }

    }
}