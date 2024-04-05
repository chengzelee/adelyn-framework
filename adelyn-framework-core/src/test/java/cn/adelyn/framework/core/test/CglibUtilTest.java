package cn.adelyn.framework.core.test;

import cn.adelyn.framework.core.cglib.BeanCopierUtil;
import cn.adelyn.framework.core.pojo.dto.BasePageDTO;
import cn.adelyn.framework.core.pojo.dto.PageOrder;
import org.junit.jupiter.api.Test;

public class CglibUtilTest {

    @Test
    void getBeanCopier() {
        BeanCopierUtil.copy(BasePageDTO.class, PageOrder.class);
    }
}
