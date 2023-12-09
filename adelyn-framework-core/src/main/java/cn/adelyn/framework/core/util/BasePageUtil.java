package cn.adelyn.framework.core.util;

public class BasePageUtil {

    private static final int firstPageNo = 1;

    /**
     * 获取页数
     */
    public static Integer getPages(long total, Integer pageSize) {

        if (total == -1) {
            return 1;
        }
        if (pageSize > 0) {
            return  (int) (total / pageSize + ((total % pageSize == 0) ? 0 : 1));
        }
        return  0;
    }

    /**
     * 获取查询起始位置
     */
    public static int getStart(int pageNo, int pageSize) {
        if (pageNo < firstPageNo) {
            pageNo = firstPageNo;
        }

        if (pageSize < 1) {
            pageSize = 0;
        }

        return (pageNo - firstPageNo) * pageSize;
    }

    /**
     * 获取查询结尾位置
     */
    public static int getEnd(int pageNo, int pageSize) {
        int start = getStart(pageNo, pageSize);
        return start + pageSize;
    }
}
