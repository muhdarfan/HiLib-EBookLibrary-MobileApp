package com.eaglez.hilib.ui.library;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eaglez.hilib.components.Core;
import com.eaglez.hilib.components.material.MyMaterial;

import java.util.ArrayList;

public class MaterialFileListModel extends ViewModel {
    private static MutableLiveData<ArrayList<MyMaterial>> mMyFile;

    public MaterialFileListModel() {
        mMyFile = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<MyMaterial>> getmMyFile() {
        return mMyFile;
    }

    public static void update() {
        if (mMyFile != null)
            mMyFile.setValue(Core.getUserMaterial());
    }
}
