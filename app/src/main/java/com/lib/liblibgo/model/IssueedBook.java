package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IssueedBook {
    @SerializedName("issue_id")
    @Expose
    private String issueId;
    @SerializedName("book_id")
    @Expose
    private String bookId;
    @SerializedName("book_name")
    @Expose
    private String bookName;
    @SerializedName("author_name")
    @Expose
    private String authorName;
    @SerializedName("issue_date")
    @Expose
    private String issueDate;

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }
}
