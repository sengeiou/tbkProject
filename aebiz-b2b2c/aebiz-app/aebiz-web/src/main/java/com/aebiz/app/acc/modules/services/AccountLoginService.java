package com.aebiz.app.acc.modules.services;

import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.acc.modules.models.Account_login;

import javax.servlet.http.HttpServletRequest;

public interface AccountLoginService extends BaseService<Account_login>{

    void doLogin(String accountId, String cookieSkuStr, String cookieNumStr, Account_login accountLogin);


    void memberRegister(Account_user accountUser);

    /**
     * 登录时生成登录日志
     * @param request
     * @param accountId
     * @param loginType
     */
    void login(HttpServletRequest request, String accountId, String loginType);
}
