package com.example.anandparmeetsingh.books;

public class Word {

    private String mTitle;
    private String mDate;
    private String[] mDescription;
    private String mAuthor;
    private String mBookImage;
    private String mBookUrl;

    public Word(String title, String date, String[] description, String author, String bookImage, String bookUrl) {
        mTitle = title;
        mDate = date;
        mAuthor = author;
        mDescription = description;
        mBookImage = bookImage;
        mBookUrl = bookUrl;


    }

    public Word(String title, String date, String[] description, String author) {
        mTitle = title;
        mDate = date;
        mDescription = description;
        mAuthor = author;


    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String[] getDescription() {
        return mDescription;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getBookImage(){
        return mBookImage;
    }
    public String getUrl() {
        return mBookUrl;
    }
}
