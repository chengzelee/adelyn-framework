package cn.adelyn.framework.core.pojo.dto;

public class PageOrder {

    private String column;

    /**
     * 排序字段方式，true 正序，false倒序
     */
    private boolean asc = false;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }
}
