package com.lib.liblibgo.dashboard.book_details;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.CheckoutModel;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity implements PaymentResultListener {
    private TextView titleTool;
    private ImageView backTool;
    private RecyclerView rvCart;
    public static TextView tvUserName;
    public static TextView tvAddress;
    public static TextView tv_phone;
    private TextView tv_shipping_price;
    private TextView tv_coins;
    private TextView tv_order_total_price;
    private Button btnConfirOrder;
    private RelativeLayout rlLoader;
    private List<CheckoutModel.ResDataBooks.CheckoutList> cList = new ArrayList<>();
    private List<CheckoutModel.ResDataBooks.CheckoutList.ItemList> cSubList = new ArrayList<>();
    private UserDatabase database;
    private TextView tv_bagTotal;
    private int ShipingCharge = 50;
    private String libCoin = "0";
    private int FinalTotalAmount;
    private CheckoutAdapter adapter;
    private String transactionId = "";
    private JSONArray jsArray = new JSONArray();
    private JSONObject jsonObject;
    private int TotalShippingCharge = 50;
    private String deductedLibCoins = "0";
    private String userEmail = "";
    //private C
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private TextView tv_security_price_main;
    private TextView tv_book_price;
    private TextView tvBookPriceNewRent;
    private TextView tv_applyed_coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setTopView(getString(R.string.checkout));

        database = new UserDatabase(this);

        sharedpreferences = getSharedPreferences(HomeActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        rvCart = (RecyclerView)findViewById(R.id.rvCart);

        tvUserName = (TextView)findViewById(R.id.tvUserName);
        tv_security_price_main = (TextView)findViewById(R.id.tv_security_price_main);
        tvBookPriceNewRent = (TextView)findViewById(R.id.tvBookPriceNewRent);
        tv_book_price = (TextView)findViewById(R.id.tv_book_price);
        tvAddress = (TextView)findViewById(R.id.tvAddress);
        tv_phone = (TextView)findViewById(R.id.tv_phone);
        tv_shipping_price = (TextView)findViewById(R.id.tv_dis_price);
        tv_applyed_coins = (TextView)findViewById(R.id.tv_applyed_coins);
        tv_coins = (TextView)findViewById(R.id.tv_coins);
        tv_bagTotal = (TextView)findViewById(R.id.tv_bagTotal);
        tv_order_total_price = (TextView)findViewById(R.id.tv_order_total_price);
        btnConfirOrder = (Button)findViewById(R.id.btnConfirOrder);
        rlLoader = (RelativeLayout)findViewById(R.id.rlLoader);

        if (database.getEMAIL().equals("") || database.getEMAIL().equals("null")){
            userEmail = "libpayment@gmail.com";
        }else {
            userEmail = database.getEMAIL();
        }

        getCheckoutDetail();

        btnConfirOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = tv_phone.getText().toString().trim();
                if (phoneNumber.equals("Enter your phone number") || phoneNumber.equals("null")){
                    Toast.makeText(CheckoutActivity.this, "Enter your phone number first.", Toast.LENGTH_SHORT).show();
                }else {
                    if ((FinalTotalAmount + TotalShippingCharge) != 0){
                        makeOnlinePayment();
                    }else {
                        createOrder("TRANS-ID:FREE");
                    }
                    //createOrder("TransID-123456");
                }
            }
        });
    }

    private void getCheckoutDetail() {
        rlLoader.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CheckoutModel> call = holders.getCheckoutDetails(database.getUserId());

        call.enqueue(new Callback<CheckoutModel>() {
            @Override
            public void onResponse(Call<CheckoutModel> call, Response<CheckoutModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        rlLoader.setVisibility(View.GONE);

                        if (database.getPHONE().equals("") || database.getPHONE().equals("null")){
                            tv_phone.setText("Enter your phone number");
                            tv_phone.setTextColor(Color.parseColor("#D81406"));
                            tv_phone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(CheckoutActivity.this,AddressActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }else {
                            tv_phone.setText(database.getPHONE());
                        }

                        tvAddress.setText(database.getAddress()+","+database.getPin());
                        tvUserName.setText(database.getNAME());

                        cList = response.body().getResponse().getCart_list();
                        for (int i = 0; i < cList.size(); i++) {
                            jsonObject = new JSONObject();
                            jsArray.put(cList.get(i).getLibrary_id());
                        }
                        Log.d("aaaaaaaa",jsArray.toString());
                        if (cList.size() > 0){
                            adapter = new CheckoutAdapter(cList, CheckoutActivity.this);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            rvCart.setLayoutManager(verticalManager);
                            rvCart.setAdapter(adapter);
                        }

                        tv_bagTotal.setText("\u20B9 "+response.body().getResponse().getTotalCartAmount());
                        FinalTotalAmount = Integer.parseInt(response.body().getResponse().getTotalCartAmount());
                        tv_coins.setText(response.body().getResponse().getLibcoins());
                        libCoin = response.body().getResponse().getLibcoins();

                    }else {
                        rlLoader.setVisibility(View.VISIBLE);
                        Toast.makeText(CheckoutActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    rlLoader.setVisibility(View.VISIBLE);
                    Toast.makeText(CheckoutActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CheckoutModel> call, Throwable t) {
                rlLoader.setVisibility(View.VISIBLE);
                Toast.makeText(CheckoutActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTopView(String string) {
        titleTool.setText(string);
        backTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    public void onChangeAddress(View view) {
        Intent intent = new Intent(CheckoutActivity.this,AddressActivity.class);
        startActivity(intent);
    }

    public void openGreenCoinPopup(View view) {
        if (libCoin.equals("0")){
            //view.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "You have 0 LibCoin.", Toast.LENGTH_SHORT).show();
        }else {
            openGreenCoinsPopup(R.layout.green_coin_popup,view);
        }
    }

    private void openGreenCoinsPopup(int layout, View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(layout, null);
        final Button btn_done = layoutView.findViewById(R.id.btn_done);
        final TextView tv_txt = layoutView.findViewById(R.id.tv_txt);
        final EditText et_lib_coin = layoutView.findViewById(R.id.et_lib_coin);

        tv_txt.setText("You have "+libCoin+" Lib coins."+"\n"+"Exchange your coins by book price.");
        //et_lib_coin.setText(libCoin);

        if (Integer.parseInt(libCoin) >= FinalTotalAmount){
            et_lib_coin.setText(String.valueOf(FinalTotalAmount));
        }else {
            et_lib_coin.setText(libCoin);
        }

        dialogBuilder.setView(layoutView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setLayout(800, ViewGroup.LayoutParams.WRAP_CONTENT);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredLibCoin = et_lib_coin.getText().toString().trim();
                if (!enteredLibCoin.equals("") || enteredLibCoin.equals("0")){
                    if (Integer.parseInt(libCoin) >= Integer.parseInt(enteredLibCoin)){
                        if (FinalTotalAmount >= Integer.parseInt(enteredLibCoin)) {
                            FinalTotalAmount = FinalTotalAmount - Integer.parseInt(enteredLibCoin);
                            tv_order_total_price.setText("\u20B9 " + (FinalTotalAmount + TotalShippingCharge));
                            libCoin = String.valueOf(Integer.parseInt(libCoin) - Integer.parseInt(enteredLibCoin));
                            tv_coins.setText(libCoin);
                            alertDialog.dismiss();
                            deductedLibCoins = enteredLibCoin;
                            v.setVisibility(View.INVISIBLE);
                            tv_applyed_coins.setText("- \u20B9 "+enteredLibCoin);
                            tv_applyed_coins.setVisibility(View.VISIBLE);
                        }else {
                            if (FinalTotalAmount == 0){
                                Toast toast = Toast.makeText(CheckoutActivity.this, "You can't use LibCoins on shipping charge.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,100);
                                toast.show();
                                //Toast.makeText(CheckoutActivity.this, "You can't use LibCoins on shipping charge.", Toast.LENGTH_SHORT).show();
                            }else {
                                tv_coins.setText("");
                                tv_coins.setText(String.valueOf(FinalTotalAmount));
                                Toast toast = Toast.makeText(CheckoutActivity.this, "You need only " +FinalTotalAmount+" LibCoins.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,100);
                                toast.show();
                                //Toast.makeText(CheckoutActivity.this, "You need only " +FinalTotalAmount+" LibCoins.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        tv_coins.setText("");
                        tv_coins.setText(libCoin);
                        Toast toast = Toast.makeText(CheckoutActivity.this, "You have only "+libCoin + " Lib coins.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,100);
                        toast.show();
                    }
                }
            }
        });
    }

    public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CartListHolder> {
        List<CheckoutModel.ResDataBooks.CheckoutList> mList;
        Context mCtx;
        List<CheckoutModel.ResDataBooks.CheckoutList.ItemList> mSubList = new ArrayList<>();
        CheckoutSubAdapter subAdapter;
        int selfPickUpSize = 0;
        List<Integer> selectedChecked = new ArrayList<>();
        //int count = 0;

        public CheckoutAdapter(List<CheckoutModel.ResDataBooks.CheckoutList> mList, Context mCtx) {
            this.mList = mList;
            this.mCtx = mCtx;
        }

        @NonNull
        @Override
        public CartListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(mCtx).inflate(R.layout.checkout_row, parent, false);
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
            TextView tvLibraryName, tvTotalPrice,tvShippingCost;
            RecyclerView rvBooks;
            CheckBox cbShefPickup;

            public CartListHolder(@NonNull View itemView) {
                super(itemView);
                tvLibraryName = itemView.findViewById(R.id.tvLibraryName);
                tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
                tvShippingCost = itemView.findViewById(R.id.tvShippingCost);
                rvBooks = itemView.findViewById(R.id.rvBooks);
                cbShefPickup = itemView.findViewById(R.id.cbShefPickup);
            }

            public void bind(int pos) {
                tvLibraryName.setText(mList.get(getAdapterPosition()).getLibrary_name());

                if (mList.get(getAdapterPosition()).getLibrary_price().equals("0")){
                    tvTotalPrice.setText("Total Price : Free");
                }else {
                    tvTotalPrice.setText("Total Price : \u20B9 "+mList.get(getAdapterPosition()).getLibrary_price());
                }

                mSubList = mList.get(getAdapterPosition()).getCart_list();

                if (mList.get(getAdapterPosition()).getIsSelfPickup().equals("0")){
                    ShipingCharge = 50;
                    cbShefPickup.setVisibility(View.GONE);
                    tvShippingCost.setText("Shipping Charge : \u20B9 "+ ShipingCharge);
                    selfPickUpSize += ShipingCharge;
                }else if (mList.get(getAdapterPosition()).getIsSelfPickup().equals("3")){
                    ShipingCharge = 0;
                    cbShefPickup.setVisibility(View.GONE);
                    tvShippingCost.setText("Shipping Charge : \u20B9 "+ ShipingCharge);
                    selfPickUpSize += ShipingCharge;
                }else {
                    ShipingCharge = 50;
                    cbShefPickup.setVisibility(View.VISIBLE);
                    tvShippingCost.setText("Shipping Charge : \u20B9 "+ ShipingCharge);
                    selfPickUpSize += ShipingCharge;
                }

                TotalShippingCharge = selfPickUpSize;
                tv_shipping_price.setText("\u20B9 " + ""+ TotalShippingCharge);
                tv_order_total_price.setText("\u20B9 "+ (FinalTotalAmount + TotalShippingCharge));

                if (mSubList.size() > 0) {
                    subAdapter = new CheckoutSubAdapter(mSubList, mCtx);
                    LinearLayoutManager verticalManager = new LinearLayoutManager(mCtx, LinearLayoutManager.VERTICAL, false);
                    rvBooks.setLayoutManager(verticalManager);
                    rvBooks.setAdapter(subAdapter);
                }

                cbShefPickup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final boolean isChecked = cbShefPickup.isChecked();
                        if (isChecked) {
                            //selfPickUpSize += ShipingCharge;
                            ShipingCharge = 50;
                            tvShippingCost.setText("Shipping Charge : \u20B9 "+ String.valueOf(0));
                            Log.d("selfPick",String.valueOf(selfPickUpSize));
                            TotalShippingCharge = TotalShippingCharge - ShipingCharge;
                            tv_shipping_price.setText("\u20B9 " + ""+ TotalShippingCharge);
                            tv_order_total_price.setText("\u20B9 "+ (FinalTotalAmount + TotalShippingCharge));
                        } else {
                            //selfPickUpSize -= ShipingCharge;
                            ShipingCharge = 50;
                            tvShippingCost.setText("Shipping Charge : \u20B9 "+ String.valueOf(50));
                            TotalShippingCharge = TotalShippingCharge + ShipingCharge;
                            tv_shipping_price.setText("\u20B9 " + ""+ TotalShippingCharge);
                            //FinalTotalAmount = ;
                            tv_order_total_price.setText("\u20B9 "+ (FinalTotalAmount + TotalShippingCharge));
                            Log.d("selfPick",String.valueOf(selfPickUpSize));
                        }
                    }
                });
            }
        }
    }

    public class CheckoutSubAdapter extends RecyclerView.Adapter<CheckoutSubAdapter.CheckoutHolder> {
        List<CheckoutModel.ResDataBooks.CheckoutList.ItemList> mSubList;
        Context mCtx;

        public CheckoutSubAdapter(List<CheckoutModel.ResDataBooks.CheckoutList.ItemList> mSubList, Context mCtx) {
            this.mSubList = mSubList;
            this.mCtx = mCtx;
        }

        @NonNull
        @Override
        public CheckoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(mCtx).inflate(R.layout.checkout_sub_row_new, parent, false);
            return new CheckoutHolder(mView);
        }

        @Override
        public void onBindViewHolder(@NonNull CheckoutHolder holder, int position) {
            holder.bind();
        }

        @Override
        public int getItemCount() {
            return mSubList.size();
        }

        public class CheckoutHolder extends RecyclerView.ViewHolder {
            TextView tv_book, tv_author, tv_price,tv_security_price;
            ImageView vi_book;

            public CheckoutHolder(@NonNull View itemView) {
                super(itemView);
                tv_book = itemView.findViewById(R.id.tv_book);
                tv_author = itemView.findViewById(R.id.tv_author);
                tv_price = itemView.findViewById(R.id.tv_price);
                vi_book = itemView.findViewById(R.id.vi_book);
                tv_security_price = itemView.findViewById(R.id.tv_security_price);
            }

            public void bind() {
                tv_book.setText(mSubList.get(getAdapterPosition()).getBook_name());
                tv_author.setText("By " + mSubList.get(getAdapterPosition()).getAuthor_name());

                if (mSubList.get(getAdapterPosition()).getBook_image().equals("")) {
                    Glide.with(mCtx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(vi_book);
                } else {
                    Glide.with(mCtx).load(mSubList.get(getAdapterPosition()).getBook_image()).into(vi_book);
                }

                //tv_price.setText("\u20B9 " + mSubList.get(getAdapterPosition()).getBook_price());
                if (mSubList.get(getAdapterPosition()).getBook_price().equals("0")){
                    tv_price.setText("Free");
                    tv_book_price.setText("Free");
                    tvBookPriceNewRent.setText("Book Price");
                }else {
                    if (mSubList.get(getAdapterPosition()).getCart_for().equals("rent")){
                        tv_price.setText("\u20B9 "+mSubList.get(getAdapterPosition()).getBook_price() + " (Rent for " + mSubList.get(getAdapterPosition()).getRent_duration() +" days)");
                        tv_book_price.setText("\u20B9 "+mSubList.get(0).getBook_price());
                        tvBookPriceNewRent.setText("Book Price (Rent for " + mSubList.get(0).getRent_duration() +" days)");
                    }else {
                        tv_price.setText("\u20B9 "+mSubList.get(getAdapterPosition()).getBook_price());
                        tv_book_price.setText("\u20B9 "+mSubList.get(0).getBook_price());
                        tvBookPriceNewRent.setText("Book Price (Purchase)");
                    }
                }

                if (mSubList.get(getAdapterPosition()).getSecurity_money().equals("0")){
                    tv_security_price_main.setText("\u20B9 0");
                    tv_security_price.setVisibility(View.GONE);
                }else {
                    if (mSubList.get(getAdapterPosition()).getCart_for().equals("rent")){
                        //tv_security_price.setVisibility(View.VISIBLE);
                        tv_security_price.setVisibility(View.GONE);
                        tv_security_price.setText("Security Price - \u20B9 "+mSubList.get(getAdapterPosition()).getSecurity_money());
                        tv_security_price_main.setText("\u20B9 "+mSubList.get(0).getSecurity_money());
                    }else {
                        tv_security_price_main.setText("\u20B9 0");
                        tv_security_price.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void makeOnlinePayment() {
        Checkout checkout = new Checkout();
        //checkout.setImage(R.drawable.logo);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "LibLibGo");
            options.put("description", "is a one stop solution for your library management.");
            //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_9A33XWu170gUtm");
            options.put("currency", "INR");

            double amount = FinalTotalAmount + TotalShippingCharge;
            amount = amount * 100;

            options.put("amount", amount);

            JSONObject preFill  = new JSONObject();
            preFill .put("email",userEmail);
            preFill .put("contact",tv_phone.getText().toString());

            options.put("prefill",preFill );

            Log.d("deductedLibCoins",deductedLibCoins);

            checkout.open(activity, options);
        } catch(Exception e) {
            Toast.makeText(CheckoutActivity.this, "Error in payment : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("ErrorMessage", "Error in starting Razorpay Checkout", e);
        }

    }

    @Override
    public void onPaymentSuccess(String s) {
        transactionId = s;
        createOrder(transactionId);
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(this, "Payment Failed. Please Try Again.", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void createOrder(String transId) {
        FinalTotalAmount = FinalTotalAmount + TotalShippingCharge;
        final ProgressDialog loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Log.d("bbb",jsArray.toString());
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> call = holders.createUserOrder(database.getUserId(),jsArray.toString(),transId,"success",
                tvUserName.getText().toString().trim(),tv_phone.getText().toString().trim(),tvAddress.getText().toString().trim(),
                String.valueOf(FinalTotalAmount),deductedLibCoins);

        call.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        loginDialog.dismiss();
                        editor.putInt("cart_count",0);
                        editor.commit();
                        Intent intent = new Intent(CheckoutActivity.this,ThankyouActivity.class);
                        startActivity(intent);
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(CheckoutActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loginDialog.dismiss();
                    Toast.makeText(CheckoutActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                loginDialog.dismiss();
                Toast.makeText(CheckoutActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });

    }
}