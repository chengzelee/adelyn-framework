package cn.adelyn.framework.core.execption;

import cn.adelyn.framework.core.response.ResponseEnum;

/**
 * @author chengze
 * @date 2022/8/24
 * @desc 自定义异常类
 */
public class AdelynException extends RuntimeException{

    private Object data;

    private ResponseEnum responseEnum;

    public AdelynException(String msg) {
        super(msg);
    }

    public AdelynException(String msg, Object data) {
        super(msg);
        this.data = data;
    }

    public AdelynException(Throwable cause) {
        super(cause);
    }

    public AdelynException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public AdelynException(ResponseEnum responseEnum) {
        super(responseEnum.getMsg());
        this.responseEnum = responseEnum;
    }

    public AdelynException(ResponseEnum responseEnum, Object data) {
        super(responseEnum.getMsg());
        this.responseEnum = responseEnum;
        this.data = data;
    }


    public Object getObject() {
        return data;
    }

    public ResponseEnum getResponseEnum() {
        return responseEnum;
    }
}
