package cn.adelyn.framework.database.pojo.dto;

import cn.adelyn.framework.core.execption.AdelynException;
import cn.adelyn.framework.core.pojo.dto.BasePageDTO;
import cn.adelyn.framework.core.pojo.dto.PageOrder;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @Author chengzelee
 * @Date 2022/10/4 21:28
 * @Desc 分页入参
 */
public class PageDTO extends BasePageDTO {

    /**
     * 最大分页大小，如果分页大小大于50，则用50作为分页的大小。防止有人直接传入一个较大的数，导致服务器内存溢出宕机
     */
    protected static final int MAX_PAGE_SIZE = 50;

    @Override
    public long getPageNum() {
        return pageNum;
    }

    @Override
    public long getPageSize() {
        return pageSize > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : pageSize;
    }

    public List<OrderItem> getOrderList() {
        if (Objects.isNull(pageOrderList) || pageOrderList.isEmpty()) {
            return null;
        }

        List<OrderItem> orderItemList = new ArrayList<>();
        for (PageOrder pageOrder: pageOrderList) {
            String column = pageOrder.getColumn();

            if (!isField(column)) {
                throw new AdelynException("排序字段名称不符合要求");
            }

            column = humpConversionUnderscore(column);

            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(column);
            orderItem.setAsc(pageOrder.isAsc());

            orderItemList.add(orderItem);
        }

        return orderItemList;
    }

    private boolean isField(String value) {
        if (!StringUtils.hasLength(value)) {
            return false;
        }
        return Pattern.matches("([a-zA-Z0-9_]+)", value);
    }

    private String humpConversionUnderscore(String value) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = value.toCharArray();
        for (char character : chars) {
            if (Character.isUpperCase(character)) {
                stringBuilder.append("_");
                character = Character.toLowerCase(character);
            }
            stringBuilder.append(character);
        }
        return stringBuilder.toString();
    }
}
