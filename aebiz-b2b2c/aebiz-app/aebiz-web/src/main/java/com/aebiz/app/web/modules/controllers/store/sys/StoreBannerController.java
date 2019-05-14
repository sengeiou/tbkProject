package com.aebiz.app.web.modules.controllers.store.sys;

import com.aebiz.app.shop.modules.models.Shop_adv_main;
import com.aebiz.app.shop.modules.services.ShopAdvMainService;
import com.aebiz.app.shop.modules.services.ShopAdvPositionService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Auther: zenghaorong
 * @Date: 2019/5/20  20:11
 * @Description: 首页 banner
 */
@Controller
@RequestMapping("/store/shop/adv/main")
public class StoreBannerController {

    private static final Log log = Logs.get();

    @Autowired
    private ShopAdvMainService shopAdvMainService;

    //广告位
    @Autowired
    private ShopAdvPositionService shopAdvPositionService;

    @RequestMapping("")
    @RequiresPermissions("store.shop.adv.main")
    public String index() {
        return "pages/store/shop/adv/main/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("store.shop.adv.main")
    public Object data(@RequestParam(value = "title",required = false) String title, DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(title)) {
            cnd.and("title", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(title) + "%"));
        }
        NutMap re = shopAdvMainService.data(dataTable.getLength(), dataTable.getStart(),
                dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
        List<Shop_adv_main> mains = (List<Shop_adv_main>)re.get("data");
//        if(mains !=null && mains.size()>0){
//            for(int i=0;i<mains.size();i++){
//                Shop_adv_main main = mains.get(i);
//                String name = shopAdvPositionService.fetch(main.getPositionId()).getName();
//                main.setPositionId(name);
//                mains.set(i,main);
//            }
//        }
        re.put("data",mains);
        return re;
    }

    @RequestMapping("/add")
    @RequiresPermissions("store.shop.adv.main")
    public String add(HttpServletRequest req) {
        req.setAttribute("list", shopAdvPositionService.query(Cnd.where("1", "=", "1")));
        return "pages/store/shop/adv/main/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Shop_adv_main")
    @RequiresPermissions("store.shop.adv.main.add")
    public Object addDo(@RequestParam String at,@RequestParam String end,Shop_adv_main shopAdvMain, HttpServletRequest req) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int star= (int) (sdf.parse(at).getTime() / 1000);
            int endAt= (int) (sdf.parse(end).getTime() / 1000);
            shopAdvMain.setStartAt(star);
            shopAdvMain.setEndAt(endAt);
            shopAdvMainService.insert(shopAdvMain);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.shop.adv.main")
    public String edit(@PathVariable String id,HttpServletRequest req) {
        req.setAttribute("list", shopAdvPositionService.query(Cnd.where("1", "=", "1")));
        req.setAttribute("obj", shopAdvMainService.fetch(id));
        return "pages/store/shop/adv/main/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Shop_adv_main")
    @RequiresPermissions("store.shop.adv.main.edit")
    public Object editDo(@RequestParam String at, @RequestParam String end, Shop_adv_main shopAdvMain, HttpServletRequest req) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int star= (int) (sdf.parse(at).getTime() / 1000);
            int endAt= (int) (sdf.parse(end).getTime() / 1000);
            shopAdvMain.setStartAt(star);
            shopAdvMain.setEndAt(endAt);
            shopAdvMain.setOpBy(StringUtil.getUid());
            shopAdvMain.setOpAt((int) (System.currentTimeMillis() / 1000));
            shopAdvMainService.updateIgnoreNull(shopAdvMain);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Shop_adv_main")
    @RequiresPermissions("store.shop.adv.main.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
        try {
            if(ids!=null&&ids.length>0){
                shopAdvMainService.delete(ids);
                req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
            }else{
                shopAdvMainService.delete(id);
                req.setAttribute("id", id);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("store.shop.adv.main")
    public String detail(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            req.setAttribute("obj", shopAdvMainService.fetch(id));
        }else{
            req.setAttribute("obj", null);
        }
        return "pages/store/shop/adv/main/detail";
    }
    /**
     * 广告管理排序
     *
     * @param pk
     * @param name
     * @param value
     * @return
     */
    @RequestMapping("/location")
    @SJson
    @SLog(description = "Shop_adv_main")
    @RequiresPermissions("store.shop.adv.main")
    public Object location(@RequestParam(required = false, value = "pk") String pk, @RequestParam(required = false, value = "name") String name, @RequestParam(required = false, value = "value") Integer value) {
        shopAdvMainService.update(Chain.make("location", value), Cnd.where("id", "=", pk));
        NutMap nutMap = new NutMap();
        nutMap.addv("name", name);
        nutMap.addv("pk", pk);
        nutMap.addv("value", value);
        return nutMap;
    }

    @RequestMapping("/getAllImages")
    @SJson
    public List<String> getAllImages(){
        return null;
    }

}
