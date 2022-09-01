package com.lib.liblibgo.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.lib.liblibgo.model.BookModel;

public class DatabaseHandler extends SQLiteOpenHelper {
    private final Context context;

    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BABY_TABLE = " CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID   + " INTEGER PRIMARY KEY,"
                + Constants.BOOK_ID + " INTEGER,"
                + Constants.BOOK_NUMBER + " INTEGER,"
                + Constants.BOOK_NAME + " TEXT,"
                + Constants.BOOK_AUTHOR + " TEXT,"
                + Constants.PUBLISH_DATE + " TEXT,"
                + Constants.ISBN_NO + " TEXT,"
                + Constants.APART_ID + " TEXT,"
                + Constants.APART_NAME + " TEXT)";

        db.execSQL(CREATE_BABY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        onCreate(db);
    }

    // CRUD Operation
    public void addBook(BookModel book){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.BOOK_ID,book.getBookId());
        values.put(Constants.BOOK_NUMBER,book.getBookNumber());
        values.put(Constants.BOOK_NAME,book.getBookName());
        values.put(Constants.BOOK_AUTHOR,book.getBookAuthor());
        values.put(Constants.PUBLISH_DATE,book.getPublishDate());
        values.put(Constants.ISBN_NO,book.getIsbnNo());
        values.put(Constants.APART_ID,book.getApartId());
        values.put(Constants.APART_NAME,book.getApartName());

        //Insert the row
        db.insert(Constants.TABLE_NAME,null,values);
        Log.d("DBHandler", "addItem: ");
    }


    //Get an Item
    public BookModel getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID,
                Constants.BOOK_ID,
                Constants.BOOK_NUMBER,
                Constants.BOOK_NAME,
                Constants.BOOK_AUTHOR,
                Constants.PUBLISH_DATE,
                Constants.ISBN_NO,
                Constants.APART_ID,
                Constants.APART_NAME,
                },
                Constants.KEY_ID + "+?" ,
                new String[]{String.valueOf(id)}, null,null,null,null);

        BookModel item = new BookModel();
        if (cursor != null){
            cursor.moveToFirst();
            item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
//            item.setBookId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.BOOK_ID))));
//            item.setBookId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.BOOK_NUMBER))));
            item.setBookName(cursor.getString(cursor.getColumnIndex(Constants.BOOK_NAME)));
//            item.setItemSize(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ITEM_SIZE)));

            //convert Timestamp to something readable
//            DateFormat dateFormat = DateFormat.getDateInstance();
//            String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
//            .getTime()); // June 7, 2020
//
//            item.setDateItemAdded(formatedDate);
        }

        return item;
    }

    //Get all items

//    public List<BabyItem> getAllItems() {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        List<BabyItem> itemList = new ArrayList<>();
//
//        Cursor cursor = db.query(Constants.TABLE_NAME,
//                new String[]{Constants.KEY_ID,
//                        Constants.KEY_BABY_ITEM,
//                        Constants.KEY_COLOR,
//                        Constants.KEY_QTY_NUMBER,
//                        Constants.KEY_ITEM_SIZE,
//                        Constants.KEY_DATE_NAME},
//                null, null, null, null,
//                Constants.KEY_DATE_NAME + " DESC");
//
//        if (cursor.moveToFirst()) {
//            do {
//                BabyItem item = new BabyItem();
//                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
//                item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.KEY_BABY_ITEM)));
//                item.setItemColor(cursor.getString(cursor.getColumnIndex(Constants.KEY_COLOR)));
//                item.setItemQuantity(cursor.getInt(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));
//                item.setItemSize(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ITEM_SIZE)));
//
//                //convert Timestamp to something readable
//                DateFormat dateFormat = DateFormat.getDateInstance();
//                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
//                        .getTime()); // Feb 23, 2020
//                item.setDateItemAdded(formattedDate);
//
//                //Add to arraylist
//                itemList.add(item);
//            } while (cursor.moveToNext());
//        }
//        return itemList;
//    }

        //Todo: Add Update Item
//    public int updateItem(BabyItem item){
//
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(Constants.KEY_BABY_ITEM, item.getItemName());
//        values.put(Constants.KEY_COLOR, item.getItemColor());
//        values.put(Constants.KEY_QTY_NUMBER,item.getItemQuantity());
//        values.put(Constants.KEY_ITEM_SIZE,item.getItemSize());
//        values.put(Constants.KEY_DATE_NAME, System.currentTimeMillis()); // timestamp of the system
//
//        //Update the row
//        return  db.update(Constants.TABLE_NAME, values,
//                Constants.KEY_ID + "=?",
//                new String[]{String.valueOf(item.getId())});
//    }

    //Todo: Add Delete Item
    public void deleteItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Constants.TABLE_NAME,
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)});

        //close
        db.close();

    }

    //Todo: getItemCount
    public int getItemCount(){
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery,null);
        return cursor.getCount();
    }
}
