package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookModel {
     private Integer id;
//    String returnDate;
    @SerializedName("book_id")
    @Expose
    private String bookId;
    @SerializedName("book_number")
    @Expose
    private String bookNumber;
    @SerializedName("book_name")
    @Expose
    private String bookName;
    @SerializedName("author_name")
    @Expose
    private String bookAuthor;
    @SerializedName("publish_date")
    @Expose
    private String publishDate;
    @SerializedName("isbn_no")
    @Expose
    private String isbnNo;
    @SerializedName("apartment_id")
    @Expose
    private String apartId;
    @SerializedName("apartment_name")
    @Expose
    private String apartName;
    @SerializedName("issue_id")
    @Expose
    private String issueId;
//    @SerializedName("issue_id")
//    @SerializedName("book_issue_id")
    @Expose
    private String booIssueId;

    private boolean isIssue;

    @SerializedName("issue_date")
//    @SerializedName("book_issue_date")
    @Expose
    public String issueDate;
    @SerializedName("return_date")
//    @SerializedName("book_return_date")
    @Expose
    public String returnDate;

    public String getReturn_date() {
        return returnDate;
    }

    public void setReturn_date(String return_date) {
        this.returnDate = return_date;
    }

    public String getBooIssueId() {
        return booIssueId;
    }

    public void setBooIssueId(String booIssueId) {
        this.booIssueId = booIssueId;
    }

    public boolean isIssue() {
        return isIssue;
    }

    public void setIssue(boolean issue) {
        isIssue = issue;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public BookModel() {
    }

    public BookModel(String bookId, String bookName,
                     String bookAuthor, String issueDate, String returnDate) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
    }

    public BookModel(String bookName, String bookAuthor,
                     String issueDate, String returnDate) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
    }

    public BookModel(Integer id, String bookId, String bookNumber, String bookName,
                     String bookAuthor, String publishDate, String isbnNo, String apartId,
                     String apartName, String issueDate, String returnDate) {
        this.id = id;
        this.bookId = bookId;
        this.bookNumber = bookNumber;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.publishDate = publishDate;
        this.isbnNo = isbnNo;
        this.apartId = apartId;
        this.apartName = apartName;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getIsbnNo() {
        return isbnNo;
    }

    public void setIsbnNo(String isbnNo) {
        this.isbnNo = isbnNo;
    }

    public String getApartId() {
        return apartId;
    }

    public void setApartId(String apartId) {
        this.apartId = apartId;
    }

    public String getApartName() {
        return apartName;
    }

    public void setApartName(String apartName) {
        this.apartName = apartName;
    }

    public String getBookId() {
        return bookId;
    }


    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
