package cn.adelyn.framework.database.handler;

import cn.adelyn.framework.core.context.UserInfoContext;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class FillMetaInfoHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Date currentDate = new Date();
        Long currentUserId = UserInfoContext.getUserId();

        this.setFieldValByName("createUser",  currentUserId, metaObject);
        this.setFieldValByName("updateUser", currentUserId, metaObject);
        this.setFieldValByName("createTime", currentDate, metaObject);
        this.setFieldValByName("updateTime", currentDate, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateUser", UserInfoContext.getUserId(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
