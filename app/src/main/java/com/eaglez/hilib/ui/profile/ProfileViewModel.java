package com.eaglez.hilib.ui.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eaglez.hilib.components.Core;
import com.eaglez.hilib.components.Users;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<Users> mUser;

    public ProfileViewModel() {
        mUser = new MutableLiveData<>();

        update();
    }

    public MutableLiveData<Users> getmUser() {
        return mUser;
    }

    public void update() {
        mUser.setValue(Core.getUserData());
    }
}