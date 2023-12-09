package cn.adelyn.framework.database.util;

import cn.adelyn.framework.core.cglib.CglibUtil;
import cn.adelyn.framework.core.pojo.vo.PageVO;
import cn.adelyn.framework.core.util.BasePageUtil;
import cn.adelyn.framework.database.pojo.dto.PageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author chengzelee
 * @Date 2022/10/4 21:28
 * @Desc 分页工具
 */
public class PageUtil extends BasePageUtil {

    public static <T> Page<T> getPage(PageDTO pageDTO) {
        if (Objects.isNull(pageDTO)) {
            pageDTO = new PageDTO();
        }

        Page<T> page = new Page();
        page.setCurrent(pageDTO.getPageNum());
        page.setSize(pageDTO.getPageSize());
        page.setOrders(pageDTO.getOrderList());
        return page;
    }

    public static <T> PageVO<T> getPageVO(Page<T> page) {
        PageVO<T> pageVO = new PageVO();
        pageVO.setTotal(page.getTotal());
        pageVO.setPages(page.getPages());
        pageVO.setList(page.getRecords());
        return pageVO;
    }

    public static <T, V> PageVO<V> getPageVO(Page<T> page, Class<V> targetClass) {
        PageVO<V> pageVO = new PageVO();
        pageVO.setTotal(page.getTotal());
        pageVO.setPages(page.getPages());

        List<V> list = new ArrayList<>();
        for (T source : page.getRecords()) {
            list.add(CglibUtil.copy(source, targetClass));
        }
        pageVO.setList(list);

        return pageVO;
    }
}
