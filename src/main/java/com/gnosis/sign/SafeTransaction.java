package com.gnosis.sign;

public class SafeTransaction {
    public String to;
    public String value;
    public String data;
    public Integer operation;
    public String safeTxGas;
    public String baseGas;
    public String gasPrice;
    public String gasToken;
    public String refundReceiver;
    public String nonce;

    SafeTransaction(String to) {
        this(
            to,
            "0",
            "0x",
            0,
            "0",
            "0",
            "0",
            "0x0000000000000000000000000000000000000000",
            "0x0000000000000000000000000000000000000000",
            "0"
        );
    }

    SafeTransaction(
        String to,
        String value,
        String data,
        Integer operation,
        String safeTxGas,
        String baseGas,
        String gasPrice,
        String gasToken,
        String refundReceiver,
        String nonce
    ) {
        this.to = to;
        this.value = value;
        this.data = data;
        this.operation = operation;
        this.safeTxGas = safeTxGas;
        this.baseGas = baseGas;
        this.gasPrice = gasPrice;
        this.gasToken = gasToken;
        this.refundReceiver = refundReceiver;
        this.nonce = nonce;
    }
}
