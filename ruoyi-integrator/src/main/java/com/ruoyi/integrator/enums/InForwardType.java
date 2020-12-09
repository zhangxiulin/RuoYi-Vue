package com.ruoyi.integrator.enums;

public enum InForwardType {

    REST("000000"), FORM("000111"), SQL_QUERY("200001"), SQL_DML("200011");

    String code;

    InForwardType(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static InForwardType getEnumByCode(String code){
        for (InForwardType inForwardType : InForwardType.values()){
            if (inForwardType.getCode().equals(code)){
                return inForwardType;
            }
        }
        return null;
    }
}
