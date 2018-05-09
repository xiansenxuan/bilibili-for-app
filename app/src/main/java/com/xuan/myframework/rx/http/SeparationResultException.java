package com.xuan.myframework.rx.http;

/**
 * Created by xuan on 2017/6/5.
 */

public class SeparationResultException extends RuntimeException {
    /**
     * 请求成功
     */
    public static final int SUCCESS_REQUEST = 0;
    /**
     * 系统错误
     */
    public static final int SYSTEM_ERROR    = 1;
    /**
     * 未登录/登录超时
     */
    public static final int NOT_LOGIN       = 2;
    /**
     * 没有权限
     */
    public static final int NOT_PERMISSIONS = 3;

    public SeparationResultException(String detailMessage) {
        super(detailMessage);
    }

    public SeparationResultException(int resultCode, String detailMessage) {
        this(getSeparationResultMessage(resultCode, detailMessage));
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     */
    private static String getSeparationResultMessage(int resultCode, String detailMessage) {
        String message = detailMessage;
        switch (resultCode) {
            case SYSTEM_ERROR:
                break;
            case NOT_PERMISSIONS:
                detailMessage = "无访问权限，请联系管理员";
                break;
            default:
                break;

        }
        return detailMessage;
    }

}
