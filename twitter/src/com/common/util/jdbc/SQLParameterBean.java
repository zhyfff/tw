package com.common.util.jdbc;

import java.sql.Types;

public class SQLParameterBean {
    
    private int type;
    private String value;
    
    public SQLParameterBean() {
        type = Types.VARCHAR;
        value = "";
    }
    
    public SQLParameterBean(int type, Object value) {
        this.type = type;
        this.value = value.toString();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
