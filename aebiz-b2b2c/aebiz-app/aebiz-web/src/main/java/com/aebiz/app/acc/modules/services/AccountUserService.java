package com.aebiz.app.acc.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.acc.modules.models.Account_user;

public interface AccountUserService extends BaseService<Account_user> {
    void clearAccount(String accountId);

    Account_user getAccount(String accountId);

    Account_user getAccountByLoginname(String loginname);

    //包月到期给用户发送验证码
    boolean sendMsg(String content,String mobile);
}
