package com.lib.liblibgo.dialogs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.adapter.CommunityAdapter;
import com.lib.liblibgo.adapter.MyOwnBookAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.book_details.BookCatActivity;
import com.lib.liblibgo.dashboard.trackingbooks.fragments.MyIndBooksFragment;
import com.lib.liblibgo.listner.CustomInterfaceClass;
import com.lib.liblibgo.listner.MoveToCommunityClickListener;
import com.lib.liblibgo.model.CommunityListModel;
import com.lib.liblibgo.model.MyOwnBooksModel;
import com.lib.liblibgo.model.SubmitDataResModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCommunitiesDialog extends BottomSheetDialogFragment {

    private TextView tvTitle;
    private TextView tvLibraryName;
    private TextView tvLibraryAdr;
    private TextView tvTitle2;
    private TextView tvOk;
    private RecyclerView rvCommunity;
    private CardView card;
    private ImageView iv_img;
    private UserDatabase database;
    private List<CommunityListModel.ResDataBooks.CommunityList> myList = new ArrayList<>();
    private CommunityAdapter adapter;
    private String communityId = "0";
    private String libraryName = "";
    private CustomInterfaceClass callback;
    Context context;
    CheckBox cbSelect;

    public MyCommunitiesDialog newInstance(CustomInterfaceClass callback) {
        this.callback=callback;
        return new MyCommunitiesDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_communities_dialog, container, false);

        database = new UserDatabase(context);

        tvTitle = (TextView)view.findViewById(R.id.tvTitle1);
        tvLibraryName = (TextView)view.findViewById(R.id.tvLibraryName);
        tvLibraryAdr = (TextView)view.findViewById(R.id.tvLibraryAdr);
        tvTitle2 = (TextView)view.findViewById(R.id.tvTitle2);
        tvOk = (TextView)view.findViewById(R.id.tvOk);
        rvCommunity = (RecyclerView)view.findViewById(R.id.rvCommunity);
        card = (CardView)view.findViewById(R.id.card);
        iv_img = (ImageView)view.findViewById(R.id.iv_img);
        cbSelect = (CheckBox) view.findViewById(R.id.cbSelect);

        getCommunityList();


        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookAddInCommunity(Constants.selectedCommunityIds);
            }
        });

        return view;
    }


    private void getCommunityList() {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CommunityListModel> call = holders.getMyAllCommunities(database.getUserId());
        call.enqueue(new Callback<CommunityListModel>() {
            @Override
            public void onResponse(Call<CommunityListModel> call, Response<CommunityListModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        myList = response.body().getResponse().getCommunity_list();
                        if (response.body().getResponse().getCommunity_library_status().equals("1")){
                            tvTitle.setVisibility(View.VISIBLE);
                            card.setVisibility(View.VISIBLE);
                            tvLibraryName.setText(response.body().getResponse().getOwn_library_name());
                            tvLibraryAdr.setText(response.body().getResponse().getOwn_library_address());

                            if (response.body().getResponse().getOwn_library_image().equals("")){
                                Glide.with(context).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(iv_img);
                            }else {
                                Glide.with(context).load(response.body().getResponse().getOwn_library_image()).into(iv_img);
                            }

                            card.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    communityId = "0";
                                    //Constants.selectedCommunityIds = "0";
                                    libraryName = response.body().getResponse().getOwn_library_name();
                                    //showConfirmationPopup(libraryName,Constants.myBookId,response.body().getResponse().getLibrary_id(),communityId);
                                    //Toast.makeText(getContext(), ""+ Constants.myBookId+"\n"+response.body().getResponse().getLibrary_id()+"\n"+communityId, Toast.LENGTH_SHORT).show();
                                }
                            });

                            cbSelect.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (cbSelect.isChecked()){
                                        Constants.selectedCommunityList.add("0");
                                    }else {
                                        Constants.selectedCommunityList.remove("0");
                                    }

                                    Constants.selectedCommunityIds =  android.text.TextUtils.join(",",Constants.selectedCommunityList);
                                }
                            });

                        }else {
                            tvTitle.setVisibility(View.GONE);
                            card.setVisibility(View.GONE);
                        }

                        if (myList.size() > 0){
                            tvTitle2.setVisibility(View.VISIBLE);
                            adapter = new CommunityAdapter(myList, context);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvCommunity.setLayoutManager(verticalManager);
                            rvCommunity.setAdapter(adapter);

                            adapter.setOnItemClickListener(new MoveToCommunityClickListener() {
                                @Override
                                public void onItemClick(int position,boolean isSelect) {
                                    //showMyCommunitiesList();
                                    communityId = myList.get(position).getCommunity_id();
                                    libraryName = myList.get(position).getCommunity_library_name();
                                    //Toast.makeText(getContext(), ""+Constants.myBookId+"\n"+myList.get(position).getCommunity_library_id()+"\n"+communityId, Toast.LENGTH_SHORT).show();
                                    //showConfirmationPopup(libraryName,Constants.myBookId,myList.get(position).getCommunity_library_id(),communityId);
                                }
                            });
                        }else {
                            tvTitle2.setVisibility(View.GONE);
                            //Toast.makeText(getContext(), "You are not a part of any community.", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(context, "You are not a part of any community.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommunityListModel> call, Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showConfirmationPopup(String libraryName,String bookId, String libraryId, String communityId) {
        Log.d("myIds",bookId+", "+libraryId+", "+communityId+", "+libraryName);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.confirmation_move_dialog, null);
        final TextView tv_yes = layoutView.findViewById(R.id.tv_yes);
        final TextView tv_no = layoutView.findViewById(R.id.tv_no);
        final TextView tv_txt = layoutView.findViewById(R.id.tv_txt);

        tv_txt.setText("Do you want to move this book to\n"+libraryName+" community library?");

        dialogBuilder.setView(layoutView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT);

        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookMoved(bookId);
                final ProgressDialog progressBar = new ProgressDialog(context);
                progressBar.setMessage("Please wait...");
                progressBar.setCancelable(false);
                progressBar.show();
                Holders holders = AllApiClass.getInstance().getApi();
                Call<SubmitDataResModel> resCall = holders.moveToCommunity(bookId,libraryId,communityId);
                resCall.enqueue(new Callback<SubmitDataResModel>() {
                    @Override
                    public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                        if (response.isSuccessful()){
                            if (response.body().getResponse().getCode() == 1){
                                bookMoved(bookId);
                                progressBar.dismiss();
                                alertDialog.dismiss();
                                Toast.makeText(context, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                            }else {
                                alertDialog.dismiss();
                                progressBar.dismiss();
                                Toast.makeText(context, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            alertDialog.dismiss();
                            progressBar.dismiss();
                            Toast.makeText(context, "Something went wrong !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                        alertDialog.dismiss();
                        progressBar.dismiss();
                        Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void bookMoved(String bookId) {
        dismiss();
        callback.callbackMethod(bookId);
    }

    public void bookAddInCommunity(String communityIds) {
        dismiss();
        callback.callbackMethod(communityIds);
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
