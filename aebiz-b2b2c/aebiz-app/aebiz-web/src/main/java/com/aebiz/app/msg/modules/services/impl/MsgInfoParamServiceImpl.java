package com.aebiz.app.msg.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.msg.modules.models.Msg_info_param;
import com.aebiz.app.msg.modules.services.MsgInfoParamService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MsgInfoParamServiceImpl extends BaseServiceImpl<Msg_info_param> implements MsgInfoParamService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
