package com.gnosis.sign;

import okhttp3.OkHttpClient;

public class SafeInstance {
    private final OkHttpClient client = new OkHttpClient.Builder().build();
    private final String address;

    SafeInstance(String address) {
        this.address = address;
    }

    
}
