package com.aebiz.app.sys.modules.services;

import com.aebiz.app.sys.modules.models.Sys_dict;
import com.aebiz.baseframework.base.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * Created by 王怀先 on 2017/1/19.
 */
public interface SysDictService extends BaseService<Sys_dict> {
    void clearCache();
    String getNameByCode(String code);
    Sys_dict getSysDictByCode(String code,String storeId);
    String getNameById(String id);
    List<Sys_dict> getSubListByPath(String path);
    List<Sys_dict> getSubListById(String id);
    List<Sys_dict> getSubListByCode(String code);
    Map getSubMapByPath(String path);
    Map getSubMapById(String id);
    Map getSubMapByCode(String code);
    void save(Sys_dict dict, String pid);
    void deleteAndChild(Sys_dict dict);
}
