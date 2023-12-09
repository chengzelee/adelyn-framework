package cn.adelyn.framework.core.pojo.dto;

import java.util.List;

public class BasePageDTO {

    /**
     * 当前页
     */
    protected long pageNum = 1;

    /**
     * 页大小
     */
    protected long pageSize = 10;

    /**
     * 排序规则
     */
    protected List<PageOrder> pageOrderList;

    public long getPageNum() {
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public List<PageOrder> getPageOrderList() {
        return pageOrderList;
    }

    public void setPageOrderList(List<PageOrder> pageOrderList) {
        this.pageOrderList = pageOrderList;
    }
}
