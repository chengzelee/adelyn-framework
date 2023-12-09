package cn.adelyn.framework.core.test;

import cn.adelyn.framework.core.cglib.BeanCopierCache;
import cn.adelyn.framework.core.pojo.dto.BasePageDTO;
import cn.adelyn.framework.core.pojo.dto.PageOrder;
import org.junit.jupiter.api.Test;

public class CglibUtilTest {

    @Test
    void getBeanCopier() {
        BeanCopierCache.INSTANCE.get(BasePageDTO.class, PageOrder.class, null);
    }
}
