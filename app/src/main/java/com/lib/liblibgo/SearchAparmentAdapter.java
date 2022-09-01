package com.lib.liblibgo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.StackBookUpload;
import com.lib.liblibgo.dashboard.apartment_admin.UploadBookDetails;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.dashboard.book_details.BookDetails;
import com.lib.liblibgo.dashboard.book_details.CommunityBookDetailsActivity;
import com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment;
import com.lib.liblibgo.model.CommunityStatusModel;
import com.lib.liblibgo.model.SearchResModel;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.model.subadmin.CreateApartmentAdminModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAparmentAdapter extends RecyclerView.Adapter<SearchAparmentAdapter.SearchHolder>{

    private static final String TAG = "SearchAdapter";
    private List<SearchResModel.ResDataBooks.BookListData> bookModels;
    private Context ctx;
    private String communityId;

    public SearchAparmentAdapter(List<SearchResModel.ResDataBooks.BookListData> bookModels, Context context,String communityId) {
        this.bookModels = bookModels;
        this.ctx = context;
        this.communityId = communityId;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.book_list_search_row,parent,false);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        holder.bookBind();
    }

    @Override
    public int getItemCount() {
        return bookModels.size();
    }

    public class SearchHolder extends RecyclerView.ViewHolder {
        TextView tv_book,tv_author,tv_cell,tv_issued_user,tv_retun_date,tv_book_status,tv_cat_name,tv_price,tv_mrp;
        CardView card;
        ImageView iv_flag,vi_book,iv_msg,iv_whatsapp,iv_edit;
        Switch cbAllowActive;
        View view1;
        LinearLayout llContact;
        public SearchHolder(@NonNull View itemView) {
            super(itemView);

            tv_book = itemView.findViewById(R.id.tv_book);
            tv_author = itemView.findViewById(R.id.tv_author);
            card = itemView.findViewById(R.id.card);
            iv_flag = itemView.findViewById(R.id.iv_flag);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            vi_book = itemView.findViewById(R.id.vi_book);
            tv_cell = itemView.findViewById(R.id.tv_cell);
            tv_issued_user = itemView.findViewById(R.id.tv_issued_user);
            tv_retun_date = itemView.findViewById(R.id.tv_retun_date);
            iv_msg = itemView.findViewById(R.id.iv_msg);
            iv_whatsapp = itemView.findViewById(R.id.iv_whatsapp);
            tv_book_status = itemView.findViewById(R.id.tv_book_status);
            cbAllowActive = itemView.findViewById(R.id.cbAllowActive);
            tv_cat_name = itemView.findViewById(R.id.tv_cat_name);
            tv_mrp = itemView.findViewById(R.id.tv_mrp);
            tv_price = itemView.findViewById(R.id.tv_price);
            view1 = itemView.findViewById(R.id.view1);
            llContact = itemView.findViewById(R.id.llContact);
        }

        public void bookBind(){
            UserDatabase database = new UserDatabase(ctx);
            tv_book.setText(bookModels.get(getAdapterPosition()).getBook_name());

            if (bookModels.get(getAdapterPosition()).getIs_stack_upload().equals("0")){
                tv_author.setText("By "+bookModels.get(getAdapterPosition()).getAuthor_name());
            }else {
                tv_author.setVisibility(View.GONE);
            }

            tv_cat_name.setText("Category - "+bookModels.get(getAdapterPosition()).getCategory_name());

            if (bookModels.get(getAdapterPosition()).getLibrary_owner_id().equals(database.getUserId())){
                if (bookModels.get(getAdapterPosition()).getAdded_by().equals(database.getUserId())){
                    tv_cell.setVisibility(View.GONE);
                    llContact.setVisibility(View.GONE);
                }else {
                    tv_cell.setText("Added By : "+bookModels.get(getAdapterPosition()).getUser_name());
                    tv_cell.setVisibility(View.VISIBLE);
                    llContact.setVisibility(View.VISIBLE);
                }
            }else {
                tv_cell.setVisibility(View.GONE);
                llContact.setVisibility(View.GONE);
            }

            if (bookModels.get(getAdapterPosition()).getMrp().equals("0")){
                if (bookModels.get(getAdapterPosition()).getIs_stack_upload().equals("0")){
                    tv_mrp.setText("Mrp : Free");
                    view1.setVisibility(View.GONE);
                }else {
                    view1.setVisibility(View.GONE);
                    tv_mrp.setVisibility(View.GONE);
                }
            }else {
                tv_mrp.setText("Mrp : \u20B9"+bookModels.get(getAdapterPosition()).getMrp());
                view1.setVisibility(View.VISIBLE);
            }

            if (bookModels.get(getAdapterPosition()).getSelling_type().equals("For Rent")){
                if (bookModels.get(getAdapterPosition()).getRental_price().equals("0")){
                    tv_price.setText("Rent/day at : Free");
                }else {
                    tv_price.setText("Rent/day at : \u20B9"+bookModels.get(getAdapterPosition()).getRental_price());
                }
            }else if (bookModels.get(getAdapterPosition()).getSelling_type().equals("For Sale")){
                if (bookModels.get(getAdapterPosition()).getSale_price().equals("0")){
                    tv_price.setText("Buy at : Free");
                }else {
                    tv_price.setText("Buy at : \u20B9"+bookModels.get(getAdapterPosition()).getSale_price());
                }
            }else {
                if (bookModels.get(getAdapterPosition()).getRental_price().equals("0")){
                    tv_price.setText("Buy at : \u20B9"+bookModels.get(getAdapterPosition()).getSale_price()+"\n"+"Rent/day at : Free");
                }else if (bookModels.get(getAdapterPosition()).getSale_price().equals("0")){
                    tv_price.setText("Buy at : Free"+"\n"+"Rent/day at : \u20B9"+bookModels.get(getAdapterPosition()).getRental_price());
                }else if (bookModels.get(getAdapterPosition()).getSale_price().equals("0") && bookModels.get(getAdapterPosition()).getRental_price().equals("0")){
                    tv_price.setText("Buy at : Free"+"\n"+"Rent/day at : Free");
                }else {
                    tv_price.setText("Buy at : \u20B9 "+bookModels.get(getAdapterPosition()).getSale_price()+"\n"+"Rent/day at : \u20B9 "+bookModels.get(getAdapterPosition()).getRental_price());
                }
            }

            /*if (bookModels.get(getAdapterPosition()).getMobile_no().equals("")){
                iv_msg.setVisibility(View.GONE);
                iv_whatsapp.setVisibility(View.GONE);
            } if (database.getUserId().equals(bookModels.get(getAdapterPosition()).getAdded_by())){
                iv_msg.setVisibility(View.GONE);
                iv_whatsapp.setVisibility(View.GONE);
            }else {
                iv_msg.setVisibility(View.VISIBLE);
                iv_whatsapp.setVisibility(View.VISIBLE);
            }*/

            /*if (bookModels.get(getAdapterPosition()).getIssued_user().equals("")){
                tv_issued_user.setText("Issued By - No one");
            }else {
                tv_issued_user.setText("Issued By - "+bookModels.get(getAdapterPosition()).getIssued_user());
            }*/

            if (bookModels.get(getAdapterPosition()).getBook_status().equals("0")){
                cbAllowActive.setChecked(false);
                tv_book_status.setText("Inactive");
                tv_book_status.setTextColor(Color.parseColor("#D81406"));
            }else {
                cbAllowActive.setChecked(true);
                tv_book_status.setText("Active");
                tv_book_status.setTextColor(Color.parseColor("#07950D"));
            }


            if (database.getUserId().equals(bookModels.get(getAdapterPosition()).getAdded_by())){
                iv_flag.setVisibility(View.GONE);
                iv_edit.setVisibility(View.VISIBLE);
                tv_book_status.setVisibility(View.VISIBLE);
                cbAllowActive.setVisibility(View.VISIBLE);
                if (bookModels.get(getAdapterPosition()).getSelling_type().equals("For Rent")){
                    tv_retun_date.setText("For Rent");
                }else if (bookModels.get(getAdapterPosition()).getSelling_type().equals("For Sale")){
                    tv_retun_date.setText("For Sale");
                }else {
                    tv_retun_date.setText("For Rent & Sale");
                }
            }else {
                iv_flag.setVisibility(View.VISIBLE);
                iv_edit.setVisibility(View.GONE);
                tv_book_status.setVisibility(View.GONE);
                cbAllowActive.setVisibility(View.GONE);
                /*if (bookModels.get(getAdapterPosition()).getLibrary_owner_id().equals(database.getUserId())){
                    cbAllowActive.setVisibility(View.VISIBLE);
                }else {
                    cbAllowActive.setVisibility(View.GONE);
                }*/
                if (bookModels.get(getAdapterPosition()).getFlag_status().equals("null") || bookModels.get(getAdapterPosition()).getFlag_status().equals("1")){
                    iv_flag.setColorFilter(ContextCompat.getColor(ctx, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv_retun_date.setText("Book available for you.");
                }else if (bookModels.get(getAdapterPosition()).getFlag_status().equals("null") || bookModels.get(getAdapterPosition()).getFlag_status().equals("0")){
                    iv_flag.setColorFilter(ContextCompat.getColor(ctx, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv_retun_date.setText("Book not available yet.");
                }else if (bookModels.get(getAdapterPosition()).getFlag_status().equals("null") || bookModels.get(getAdapterPosition()).getFlag_status().equals("2")){
                    iv_flag.setColorFilter(ContextCompat.getColor(ctx, R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv_retun_date.setText("Someone has borrowed this book.");
                }
            }



            if (bookModels.get(getAdapterPosition()).getImage_url().equals("")){
                Glide.with(ctx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(vi_book);
            }else {
                Glide.with(ctx).load(bookModels.get(getAdapterPosition()).getImage_url()).apply(new RequestOptions().override(300, 500)).placeholder(R.drawable.no_img).into(vi_book);
            }

            iv_flag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!database.getUserId().equals("")){
                        if (bookModels.get(getAdapterPosition()).getFlag_status().equals("1")){
                            //Toast.makeText(ctx, "", Toast.LENGTH_LONG).show();
                            //Helper.showCustomToast(ctx,"Book is available for you.",R.color.green);
                            showGreenPopup(R.layout.book_info_green_pop,bookModels.get(getAdapterPosition()).getBook_id(),bookModels.get(getAdapterPosition()).getImage_url(),bookModels.get(getAdapterPosition()).getAdded_by(),bookModels.get(getAdapterPosition()).getIs_stack_upload());
                        }else if (bookModels.get(getAdapterPosition()).getFlag_status().equals("0")){
                            //Helper.showCustomToast(ctx,"Book is not available yet!",R.color.red);
                            showRedPopup(R.layout.book_info_red_pop,bookModels.get(getAdapterPosition()).getBook_id(),bookModels.get(getAdapterPosition()).getReturn_date(),bookModels.get(getAdapterPosition()).getIssued_user(),database.getUserId());
                        }else if (bookModels.get(getAdapterPosition()).getFlag_status().equals("2")){
                            //Helper.showCustomToast(ctx,"Book is over dated.",R.color.yellow);
                            showYellowPopup(R.layout.book_info_yellow_pop,bookModels.get(getAdapterPosition()).getBook_id(),bookModels.get(getAdapterPosition()).getIssued_user(),database.getUserId(),bookModels.get(getAdapterPosition()).getReturn_date());
                        }
                    }else {
                        Intent intent = new Intent(ctx, LoginWithPhoneNumber.class);
                        ctx.startActivity(intent);
                    }
                }
            });


            iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedpreferences = ctx.getSharedPreferences(CommunityLibraryFragment.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("community_id",communityId);
                    editor.commit();
                    Constants.isOwnLibrary = false;
                    if (bookModels.get(getAdapterPosition()).getIs_stack_upload().equals("0")){
                        Intent intent = new Intent(ctx, UploadBookDetails.class);
                        intent.putExtra("name",bookModels.get(getAdapterPosition()).getBook_name());
                        intent.putExtra("author",bookModels.get(getAdapterPosition()).getAuthor_name());
                        intent.putExtra("isbn",bookModels.get(getAdapterPosition()).getIsbn_no());
                        intent.putExtra("publish_date",bookModels.get(getAdapterPosition()).getPublish_date());
                        intent.putExtra("description",bookModels.get(getAdapterPosition()).getDescription());
                        intent.putExtra("imgUrl",bookModels.get(getAdapterPosition()).getImage_url());

                        intent.putExtra("rental_price",bookModels.get(getAdapterPosition()).getRental_price());
                        intent.putExtra("rental_duration",bookModels.get(getAdapterPosition()).getRent_duration());
                        intent.putExtra("book_price",bookModels.get(getAdapterPosition()).getSale_price());
                        intent.putExtra("category_id",bookModels.get(getAdapterPosition()).getCategory_id());
                        intent.putExtra("category_name",bookModels.get(getAdapterPosition()).getCategory_name());
                        intent.putExtra("shelf_id",bookModels.get(getAdapterPosition()).getShelve_id());
                        intent.putExtra("shelf_name",bookModels.get(getAdapterPosition()).getShelve_name());

                        intent.putExtra("book_id",bookModels.get(getAdapterPosition()).getBook_id());
                        intent.putExtra("mrp",bookModels.get(getAdapterPosition()).getMrp());
                        intent.putExtra("quantity",bookModels.get(getAdapterPosition()).getQuantity());

                        intent.putExtra("sale_type",bookModels.get(getAdapterPosition()).getSelling_type());
                        intent.putExtra("book_condition",bookModels.get(getAdapterPosition()).getBook_condition_type());
                        intent.putExtra("security_money",bookModels.get(getAdapterPosition()).getSecurity_money());
                        intent.putExtra("giveaway_status",bookModels.get(getAdapterPosition()).getGiveaway());
                        ctx.startActivity(intent);
                    }else {
                        Intent intent = new Intent(ctx, StackBookUpload.class);
                        intent.putExtra("name",bookModels.get(getAdapterPosition()).getBook_name());
                        intent.putExtra("description",bookModels.get(getAdapterPosition()).getDescription());
                        intent.putExtra("imgUrl",bookModels.get(getAdapterPosition()).getImage_url());
                        intent.putExtra("book_price",bookModels.get(getAdapterPosition()).getSale_price());
                        intent.putExtra("category_id",bookModels.get(getAdapterPosition()).getCategory_id());
                        intent.putExtra("category_name",bookModels.get(getAdapterPosition()).getCategory_name());
                        intent.putExtra("book_id",bookModels.get(getAdapterPosition()).getBook_id());
                        intent.putExtra("book_condition",bookModels.get(getAdapterPosition()).getBook_condition_type());
                        intent.putExtra("giveaway_status",bookModels.get(getAdapterPosition()).getGiveaway());
                        ctx.startActivity(intent);
                    }

                }
            });

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!database.getUserId().equals("")){
                        Constants.isStackUpload = bookModels.get(getAdapterPosition()).getIs_stack_upload();
                        if (bookModels.get(getAdapterPosition()).getLibrary_type().equals("physical")){
                            Intent intent = new Intent(ctx, CommunityBookDetailsActivity.class);
                            intent.putExtra("book_id",bookModels.get(getAdapterPosition()).getBook_id());
                            intent.putExtra("book_img",bookModels.get(getAdapterPosition()).getImage_url());
                            intent.putExtra("added_by",bookModels.get(getAdapterPosition()).getAdded_by());
                            ctx.startActivity(intent);
                        }else {
                            Intent intent = new Intent(ctx, BookDetails.class);
                            intent.putExtra("book_id",bookModels.get(getAdapterPosition()).getBook_id());
                            intent.putExtra("book_img",bookModels.get(getAdapterPosition()).getImage_url());
                            ctx.startActivity(intent);
                        }
                        //checkRequest(bookModels.get(getAdapterPosition()).getBook_id(),bookModels.get(getAdapterPosition()).getImage_url());
                    }else {
                        Intent intent = new Intent(ctx, LoginWithPhoneNumber.class);
                        ctx.startActivity(intent);
                    }
                }
            });


            iv_whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "https://api.whatsapp.com/send?phone=91" + bookModels.get(getAdapterPosition()).getMobile_no();
                    try {
                        PackageManager pm = view.getContext().getPackageManager();
                        pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        view.getContext().startActivity(i);
                    } catch (PackageManager.NameNotFoundException e) {
                        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }
                }
            });

            iv_msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dexter.withContext(ctx).withPermissions(Manifest.permission.SEND_SMS)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                    String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(ctx);
                                    Uri _uri = Uri.parse("tel:" +  bookModels.get(getAdapterPosition()).getMobile_no());
                                    Intent sendIntent = new Intent(Intent.ACTION_VIEW, _uri);
                                    sendIntent.putExtra("address", bookModels.get(getAdapterPosition()).getMobile_no());
                                    sendIntent.putExtra("sms_body", "");
                                    sendIntent.setPackage(defaultSmsPackageName);
                                    sendIntent.setType("vnd.android-dir/mms-sms");
                                    ctx.startActivity(sendIntent);
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                    permissionToken.continuePermissionRequest();
                                }
                            }).check();
                }
            });


            cbAllowActive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cbAllowActive.isChecked()){
                        //Toast.makeText(mCtx, ""+mList.get(getAdapterPosition()).getBook_id(), Toast.LENGTH_SHORT).show();
                        bookActiveInactive(bookModels.get(getAdapterPosition()).getBook_id(),"1",tv_book_status);
                        tv_book_status.setText("Active");
                        tv_book_status.setTextColor(Color.parseColor("#07950D"));
                    }else {
                        bookActiveInactive(bookModels.get(getAdapterPosition()).getBook_id(),"0",tv_book_status);
                        //Toast.makeText(mCtx, ""+mList.get(getAdapterPosition()).getBook_name(), Toast.LENGTH_SHORT).show();
                        tv_book_status.setText("Inactive");
                        tv_book_status.setTextColor(Color.parseColor("#D81406"));
                    }
                }
            });

        }
    }

    private void bookActiveInactive(String book_id, String status,TextView textView) {
        final ProgressDialog loginDialog = new ProgressDialog(ctx);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();

        Holders holders = AllApiClass.getInstance().getApi();
        Call<CreateApartmentAdminModel> modelCall = holders.chnageBookStatus(book_id,status);
        modelCall.enqueue(new Callback<CreateApartmentAdminModel>() {
            @Override
            public void onResponse(Call<CreateApartmentAdminModel> call, Response<CreateApartmentAdminModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        loginDialog.dismiss();
                        Toast.makeText(ctx, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(ctx, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loginDialog.dismiss();
                    Toast.makeText(ctx, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateApartmentAdminModel> call, Throwable t) {
                loginDialog.dismiss();
                Toast.makeText(ctx, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showYellowPopup(int layout, String book_id, String issued_user, String userId,String return_date) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(layout, null);
        final Button btn_done = layoutView.findViewById(R.id.btn_done);
        final TextView tv_txt = layoutView.findViewById(R.id.tv_txt);

        dialogBuilder.setView(layoutView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setLayout(650, ViewGroup.LayoutParams.WRAP_CONTENT);

        //tv_txt.setText("This book is already issued by "+issued_user+" & he well return this book by "+convertTime(return_date)+".");
        tv_txt.setText("Book borrowed by someone and return date is past due, Notify owner for return");
        //tv_txt.setText("This book is already issued by someone & he well return this book by "+convertTime(return_date)+".");

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //alertDialog.dismiss();
                sendBookRequestNotification(book_id,userId,alertDialog);
            }
        });
    }

    private void sendBookRequestNotification(String book_id, String userId, AlertDialog alertDialog) {
        final ProgressDialog progressBar = new ProgressDialog(ctx);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> resCall = holders.notifyBooksToMe(userId,book_id);
        resCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progressBar.dismiss();
                        alertDialog.dismiss();
                        Toast.makeText(ctx, "You will be notified when  book is available.", Toast.LENGTH_SHORT).show();
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(ctx, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(ctx, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(ctx, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showRedPopup(int layout, String book_id, String return_date, String issued_user,String user_id) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(layout, null);
        final Button btn_done = layoutView.findViewById(R.id.btn_done);
        final TextView tv_txt = layoutView.findViewById(R.id.tv_txt);

        dialogBuilder.setView(layoutView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setLayout(650, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (!return_date.equals("")){
            tv_txt.setText("Book is borrowed by someone, expected return date is "+convertTime(return_date)+".");
        }else {
            tv_txt.setText("Book already borrowed by someone.");
        }


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void showGreenPopup(int layout,String bookId, String imgUrl,String added_by,String isStackUpload) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(layout, null);
        final Button btn_done = layoutView.findViewById(R.id.btn_done);

        dialogBuilder.setView(layoutView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setLayout(650, ViewGroup.LayoutParams.WRAP_CONTENT);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Constants.isStackUpload = isStackUpload;
                Intent intent = new Intent(ctx, BookDetails.class);
                intent.putExtra("book_id",bookId);
                intent.putExtra("book_img",imgUrl);
                intent.putExtra("added_by",added_by);
                ctx.startActivity(intent);
                //checkRequest(bookId,imgUrl);
            }
        });
    }

    private String convertTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format1 = new SimpleDateFormat("dd MMM,yyyy");
        java.util.Date date = null;

        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String convertedDate = format1.format(date);

        return convertedDate;
    }

    public void setFilter(List<SearchResModel.ResDataBooks.BookListData> arrayList) {
        bookModels = new ArrayList<>();
        bookModels.addAll(arrayList);
        notifyDataSetChanged();
    }

}
