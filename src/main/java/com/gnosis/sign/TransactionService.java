package com.gnosis.sign;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TransactionService {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SigningService.class);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient.Builder().build();

    private static class PostSafeTransaction extends SafeTransaction {
        public String sender;
        public String signature;
        public String contractTransactionHash;

        PostSafeTransaction(SafeTransaction tx, String sender, String signature, String safeTxHash) {
            super(tx.to);
            this.sender = sender;
            this.signature = signature;
            this.contractTransactionHash = safeTxHash;
        }
    }

    public static void submitTx(String safe, String sender, String hash, String signature, SafeTransaction transaction) throws IOException {
        String content = new ObjectMapper().writeValueAsString(new PostSafeTransaction(transaction, sender, signature, hash));
        log.info(content);
        RequestBody body = RequestBody.create(JSON, content);
        Request request = new Request.Builder().url(
            "https://safe-transaction.rinkeby.gnosis.io/api/v1/safes/" + safe + "/multisig-transactions/"
        ).post(body).build();
        Response resp = TransactionService.client.newCall(request).execute();
        log.info(resp.body().string());
    }
}
