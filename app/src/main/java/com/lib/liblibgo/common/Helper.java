package com.lib.liblibgo.common;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.liblibgo.model.NotificationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Helper {

    public static void showToast(Context mCtx, String mMessage) {
        Toast.makeText(mCtx, "" + mMessage, Toast.LENGTH_SHORT).show();
    }

    public static void showCustomToast(Context mCtx, String mMessage, int clr) {
        Toast toast = Toast.makeText(mCtx,
                mMessage,
                Toast.LENGTH_SHORT);

        View toastView = toast.getView();
        toastView.setBackgroundResource(clr);
        TextView text = (TextView) toastView.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
        toast.show();
    }

    public static void spinnerError(Spinner spinner, String message){
        TextView errorText = (TextView) spinner.getSelectedView();
        errorText.setError("");
        errorText.setTextColor(Color.RED);//just to highlight that this is an error
        errorText.setText(message);
    }


    public static ArrayList<String> getApartment() {
        ArrayList<String> mList = new ArrayList<>();
        mList.add("Select Apartment");
        mList.add("BCA");
        mList.add("MCA");
        mList.add("BBA");
        mList.add("MBA");
        mList.add("B.TECH");
        mList.add("M.TECH");
        return mList;
    }
    public static String BOOK_JSON = "[\n" +
            "{\n" +
            "\"book_name\":\"Data Structures\",\n" +
            "\"author_name\":\"Robert Sedgewick, Kevin Wayne\",\n" +
            "\"issue_date\":\"01/01/2021\",\n" +
            "\"return_date\":\"10/01/2021\"\n" +
            "},\n" +
            "{\n" +
            "\"book_name\":\"Object-Oriented Programming With C++\",\n" +
            "\"author_name\":\"E. Balagurusamy\",\n" +
            "\"issue_date\":\"05/01/2021\",\n" +
            "\"return_date\":\"12/01/2021\"\n" +
            "},\n" +
            "{\n" +
            "\"book_name\":\"Business Accounting\",\n" +
            "\"author_name\":\"M E Thukaram Rao\",\n" +
            "\"issue_date\":\"10/01/2021\",\n" +
            "\"return_date\":\"18/01/2021\"\n" +
            "},\n" +
            "{\n" +
            "\"book_name\":\"Data Structures\",\n" +
            "\"author_name\":\"Robert Sedgewick, Kevin Wayne\",\n" +
            "\"issue_date\":\"01/01/2021\",\n" +
            "\"return_date\":\"10/01/2021\"\n" +
            "},\n" +
            "{\n" +
            "\"book_name\":\"Object-Oriented Programming With C++\",\n" +
            "\"author_name\":\"E. Balagurusamy\",\n" +
            "\"issue_date\":\"05/01/2021\",\n" +
            "\"return_date\":\"12/01/2021\"\n" +
            "},\n" +
            "{\n" +
            "\"book_name\":\"Business Accounting\",\n" +
            "\"author_name\":\"M E Thukaram Rao\",\n" +
            "\"issue_date\":\"10/01/2021\",\n" +
            "\"return_date\":\"18/01/2021\"\n" +
            "}\n" +
            "\n" +
            "\n" +
            "\n" +
            "]";

    public static final String NOTIFICATION_DUMMY = "[\n" +
            "{\n" +
            "\"title\":\"Title Here\",\n" +
            "\"date\":\"24 Feb 2021 02:16:30\",\n" +
            "\"message\":\"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s\"\n" +
            "},\n" +
            "{\n" +
            "\"title\":\"Title Here\",\n" +
            "\"date\":\"23 Feb 2021 14:10:35\",\n" +
            "\"message\":\"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s\"\n" +
            "},\n" +
            "{\n" +
            "\"title\":\"Title Here\",\n" +
            "\"date\":\"21 Jan 2021 15:16:30\",\n" +
            "\"message\":\"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s\"\n" +
            "},\n" +
            "{\n" +
            "\"title\":\"Title Here\",\n" +
            "\"date\":\"20 Jan 2021 12:10:12\",\n" +
            "\"message\":\"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s\"\n" +
            "},\n" +
            "{\n" +
            "\"title\":\"Title Here\",\n" +
            "\"date\":\"18 Jan 2021 11:09:50\",\n" +
            "\"message\":\"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s\"\n" +
            "},\n" +
            "{\n" +
            "\"title\":\"Title Here\",\n" +
            "\"date\":\"20 Dec 2020 09:19:30\",\n" +
            "\"message\":\"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s\"\n" +
            "}\n" +
            "]";

    public static ArrayList<NotificationModel> getNotificationDummy() {
        ArrayList<NotificationModel> mList = new ArrayList<>();
        try {
            JSONArray mRoot=new JSONArray(Helper.NOTIFICATION_DUMMY);
            for (int i = 0; i < mRoot.length(); i++) {
                JSONObject index=mRoot.getJSONObject(i);
                NotificationModel mModel = new NotificationModel();
                /*mModel.setTitle(index.getString("title"));
                mModel.setDate(index.getString("date"));;
                mModel.setMessage(index.getString("message"));*/
                mList.add(mModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mList;
    }


}
