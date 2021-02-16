package com.eaglez.hilib.components.material;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class MyMaterial {
    private String Borrower, Title;
    private @ServerTimestamp Date borrowDate;
    private DocumentReference MaterialRef;

    public MyMaterial() {}

    public MyMaterial(String uid, String title, DocumentReference reference) {
        this.Borrower = uid;
        this.Title = title;
        this.MaterialRef = reference;
        this.borrowDate = new Date();
    }

    public String getBorrower() {
        return Borrower;
    }

    public void setBorrower(String borrower) {
        Borrower = borrower;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public DocumentReference getMaterialRef() {
        return MaterialRef;
    }

    public void setMaterialRef(DocumentReference materialRef) {
        MaterialRef = materialRef;
    }
}
