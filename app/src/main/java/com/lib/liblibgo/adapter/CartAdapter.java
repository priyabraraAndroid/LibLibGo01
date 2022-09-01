package com.lib.liblibgo.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.model.CartModel;
import com.lib.liblibgo.model.SubmitDataResModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartListHolder> {
    List<CartModel.ResDataBooks.CartListData> mList;
    Context mCtx;
    List<CartModel.ResDataBooks.CartListData.CartBookList> mSubList = new ArrayList<>();
    CartSubAdapter subAdapter;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private int cartCount;

    public CartAdapter(List<CartModel.ResDataBooks.CartListData> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public CartListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.cartlist_row, parent, false);
        return new CartListHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CartListHolder extends RecyclerView.ViewHolder {
        TextView cart_no_tv,tvLibraryName;
        RecyclerView rvBooks;
        ImageView ivDeleteCart;

        public CartListHolder(@NonNull View itemView) {
            super(itemView);
            cart_no_tv = itemView.findViewById(R.id.cart_no_tv);
            tvLibraryName = itemView.findViewById(R.id.tvLibraryName);
            rvBooks = itemView.findViewById(R.id.rvBooks);
            ivDeleteCart = itemView.findViewById(R.id.ivDeleteCart);
        }

        public void bind(int pos) {
            sharedpreferences = mCtx.getSharedPreferences(HomeActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            editor = sharedpreferences.edit();

            cart_no_tv.setText("LibCart " + (pos + 1));
            tvLibraryName.setText(mList.get(getAdapterPosition()).getLibrary_name());
            //tv_price.setText("\u20B9 "+mList.get(getAdapterPosition()).getPrice());

            mSubList = mList.get(getAdapterPosition()).getCart_list();

            if (mSubList.size() > 0){
                subAdapter = new CartSubAdapter(mSubList,mCtx);
                LinearLayoutManager verticalManager = new LinearLayoutManager(mCtx, LinearLayoutManager.VERTICAL, false);
                rvBooks.setLayoutManager(verticalManager);
                rvBooks.setAdapter(subAdapter);
            }

            ivDeleteCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(mCtx, ""+mList.get(getAdapterPosition()).getLibrary_id(), Toast.LENGTH_SHORT).show();
                    int subListSize = mList.get(getAdapterPosition()).getCart_list().size();
                    //Toast.makeText(mCtx, ""+subListSize, Toast.LENGTH_SHORT).show();
                    deleteCart(R.layout.delete_popup,mList.get(getAdapterPosition()).getLibrary_id(),getAdapterPosition(),subListSize);
                }
            });

        }
    }

    private void deleteCart(int layout, String library_id, int adapterPosition,int subListSize) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mCtx);
        LayoutInflater inflater = (LayoutInflater)mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(layout, null);
        final TextView tv_yes = layoutView.findViewById(R.id.tv_yes);
        final TextView tv_no = layoutView.findViewById(R.id.tv_no);
        final TextView tv_txt = layoutView.findViewById(R.id.tv_txt);

        tv_txt.setText("Do you want to delete this lib cart ?");

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
                 final ProgressDialog loginDialog = new ProgressDialog(mCtx);
                    loginDialog.setMessage("Please wait..");
                    loginDialog.setCancelable(false);
                    loginDialog.show();

                    Holders holders = AllApiClass.getInstance().getApi();
                    Call<SubmitDataResModel> call = holders.removeCart("library",library_id);
                    call.enqueue(new Callback<SubmitDataResModel>() {
                        @Override
                        public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                            if (response.isSuccessful()){
                                if (response.body().getResponse().getCode() == 1){
                                    loginDialog.dismiss();
                                    alertDialog.dismiss();
                                    cartCount = sharedpreferences.getInt("cart_count",0);
                                    cartCount = cartCount - subListSize;
                                    mList.remove(adapterPosition);
                                    notifyItemRemoved(adapterPosition);
                                    notifyItemRangeChanged(adapterPosition, mList.size());
                                    /*editor.putInt("cart_count",mList.size());
                                    editor.commit();*/
                                    editor.putInt("cart_count",cartCount);
                                    editor.commit();
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
                        public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                            loginDialog.dismiss();
                            Toast.makeText(mCtx, ""+t, Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });
    }
}
