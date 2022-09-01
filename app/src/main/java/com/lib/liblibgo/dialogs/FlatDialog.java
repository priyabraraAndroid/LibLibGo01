package com.lib.liblibgo.dialogs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.NewRegistration;
import com.lib.liblibgo.activity.UserDetails;
import com.lib.liblibgo.adapter.FlatAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.listner.OnItemClickListener;
import com.lib.liblibgo.model.FlatListData;
import com.lib.liblibgo.model.FlatModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlatDialog extends BottomSheetDialogFragment{

    private RecyclerView mCountryRv;
    private FlatAdapter adapter;
    private EditText mSearchEt;
    private List<FlatListData> mList = new ArrayList();
    private List<FlatListData> filteredModelList = new ArrayList<>();
    //private List<FlatListData> flats = new ArrayList<>();
    private ProgressBar progBar;

    public static FlatDialog newInstance() {
        return new FlatDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.flat_dialog, container, false);

        mCountryRv = (RecyclerView)view.findViewById(R.id.rv_country);
        progBar = view.findViewById(R.id.progBar);
        mList = Constants.flatList;

        mSearchEt = (EditText)view.findViewById(R.id.et_search_country);
        mSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newText = charSequence.toString();
                if (!newText.isEmpty()){
                    //searchCountry(newText);
                    loadMoreData(newText);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //adapter.setOnItemClickListener(this);

        return view;
    }

    private void loadMoreData(String txt) {
       // Toast.makeText(getContext(), ""+limit, Toast.LENGTH_SHORT).show();
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<FlatModel> resCall = holders.getFlatByApartment(Constants.AprtmentId,txt);
        resCall.enqueue(new Callback<FlatModel>() {
            @Override
            public void onResponse(Call<FlatModel> call, Response<FlatModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResponse().getCode() == 10) {
                        if (response.body().getResponse().getFlat_list() != null) {
                            progBar.setVisibility(View.GONE);
                            mList = response.body().getResponse().getFlat_list();
                            adapter = new FlatAdapter(mList,getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            mCountryRv.setLayoutManager(verticalManager);
                            mCountryRv.setAdapter(adapter);

                            adapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    addItemClick(position);
                                }
                            });
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onFailure(Call<FlatModel> call, Throwable t) {
                Toast.makeText(getContext(), "Try again", Toast.LENGTH_SHORT).show();
                progBar.setVisibility(View.GONE);
            }
        });
    }



    private boolean searchCountry(String newText) {
        if (mList.size() > 0) {
            final List<FlatListData> filteredModelList = filter(mList, newText);
            adapter.setFilter(filteredModelList);
        }
        return true;
    }

    private List<FlatListData> filter(List<FlatListData> models, String search_txt) {
        search_txt = search_txt.toLowerCase();
        filteredModelList = new ArrayList<>();
        for (FlatListData model : models) {
            final String textName = model.getFlat_no().toLowerCase();
            if (textName.contains(search_txt)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    private void addItemClick(int position) {
        if (Constants.USER_DETAIL_PAGE_TYPE.equals("1")){
            ((NewRegistration)getActivity()).setData(mList.get(position).getFlat_no(),mList.get(position).getFlat_id());
        }/*else if (Constants.USER_DETAIL_PAGE_TYPE.equals("3")){
            ((UpdateUserProfile)getActivity()).setUserData(mList.get(position).getFlat_no(),mList.get(position).getFlat_id());
        }*/else {
            ((UserDetails)getActivity()).setUserData(mList.get(position).getFlat_no(),mList.get(position).getFlat_id());
        }
        dismiss();
    }

}
