package com.simmya.easyui;

import java.util.List;

public class DataGrid {

    private int total;//总记录数
    private List<?> rows;//所有记录
    
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public List<?> getRows() {
        return rows;
    }
    public void setRows(List<?> rows) {
        this.rows = rows;
    }
    
    
}
