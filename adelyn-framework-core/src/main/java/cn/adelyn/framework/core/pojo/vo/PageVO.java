package cn.adelyn.framework.core.pojo.vo;

import java.util.List;

/**
 * @Author chengzelee
 * @Date 2022/10/4 18:27
 * @Desc
 */
public class PageVO<T> {

    /**
     * 总页数
     */
    private long pages;

    /**
     * 总条数
     */
    private long total;

    private List<T> list;

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
