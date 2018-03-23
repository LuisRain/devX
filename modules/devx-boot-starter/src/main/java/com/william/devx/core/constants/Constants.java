package com.william.devx.core.constants;

/**
 * Created with IntelliJ IDEA.
 * Date: 2016/6/14
 * Time: 14:23
 * To change this template use File | Settings | File Templates.
 */
public interface Constants {

    /**
     * 操作名称
     */
    String OP_NAME = "op";
    /**
     * 消息key
     */
    String MESSAGE = "message";

    /**
     * 错误key
     */
    String ERROR = "error";

    /**
     * 上个页面地址
     */
    String BACK_URL = "BackURL";

    String IGNORE_BACK_URL = "ignoreBackURL";

    /**
     * 当前请求的地址 带参数
     */
    String CURRENT_URL = "currentURL";

    /**
     * 当前请求的地址 不带参数
     */
    String NO_QUERYSTRING_CURRENT_URL = "noQueryStringCurrentURL";


    String CONTEXT_PATH = "ctx";

    /**
     * 当前登录的用户
     */
    String CURRENT_USER = "user";

    /**
     * 当前登录的CRM用户
     */
    String MEMBER_USER = "memberUser";

    /**
     * 当前门店id
     */
    String CURRENT_SHOP_ID = "shop_id";

    /**
     * 机器地址
     */
    String MAC_IP = "127.0.0.1";
    /**
     * 当前登录用户名
     */
    String CURRENT_USERNAME = "username";
    /**
     * 系统编码
     */
    String ENCODING = "UTF-8";

    /**
     * 默认分页起始值
     */
    public static final int DEFAULT_PAGE_NUMBER = 1;
    /**
     * 默认分页大小值
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * 分页起始参数名称
     */
    public static final String PAGE_NUMBER_PARAM_NAME = "pageNumber";
    /**
     * 分页大小参数名称
     */
    public static final String PAGE_SIZE_PARAM_NAME = "pageSize";
}
