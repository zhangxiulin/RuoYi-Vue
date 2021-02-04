package com.ruoyi.common.enums;

public enum ExecutionOrder {

    SEQUENCE("1", "顺序"), PARALLEL("0", "并行");

    private final String code;
    private final String name;

    ExecutionOrder(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
