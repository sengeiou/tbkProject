package com.aebiz.app.web.modules.controllers.open.H5;

import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.goods.modules.models.Goods_image;
import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.services.GoodsImageService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.goods.modules.services.GoodsService;
import com.aebiz.app.member.modules.models.Member_address;
import com.aebiz.app.member.modules.services.MemberAddressService;
import com.aebiz.app.order.modules.models.Order_goods;
import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.app.order.modules.services.OrderGoodsService;
import com.aebiz.app.order.modules.services.OrderMainService;
import com.aebiz.app.sys.modules.services.SysDictService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.DateUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Cnd;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chengzhuming
 * @date 2019/4/1 19:52
 */
@Controller
@RequestMapping("/open/h5/order")
public class OrderController {
    private static final Log log = Logs.get();
    @Autowired
    private MemberAddressService memberAddressService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsImageService goodsImageService;

    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private OrderMainService orderMainService;

    @Autowired
    private OrderGoodsService orderGoodsService;


    /**
     * 进入订单确认页
     */
    @RequestMapping("/orderConfirmation.html")
    public String orderConfirmation(HttpServletRequest request) {
        String ids = request.getParameter("id");
        String num = request.getParameter("num");

        Subject subject = SecurityUtils.getSubject();
        Account_user accountUser = (Account_user) subject.getPrincipal();
        if(accountUser==null){
            return "pages/front/h5/niantu/login";
        }
        Goods_main good = goodsService.fetch(ids);
        Order_main order_main = new Order_main();
        Order_goods order_goods = new Order_goods();

        order_main.setAccountId(accountUser.getAccountId());
        order_main.setStoreId(good.getStoreId());
        Cnd proCnd = Cnd.NEW();
        proCnd.and("goodsId","=",good.getId());
        List<Goods_product> gpList = goodsProductService.query(proCnd);
        if(gpList!=null&&gpList.size()>0){
            Goods_product goods_product = gpList.get(0);
            Integer salePrice = goods_product.getSalePrice();
            int n = Integer.parseInt(num);
            int totalMoney = salePrice*n;
            order_main.setGoodsMoney(totalMoney);
            order_main.setGoodsFreeMoney(0);
            String freight = sysDictService.getNameByCode("freight");
            int freightMoney = Integer.parseInt(freight)*100;
            int freeMoney = 0;
            order_main.setPayMoney(totalMoney+freightMoney);
            order_main.setFreightMoney(freightMoney);
            order_main.setFreeMoney(0);
            order_main.setPayStatus(0);
            order_main.setOrderStatus(0);
            order_main.setOrderAt(DateUtil.getTime(new Date()));
            Order_main order = orderMainService.insert(order_main);
            order_goods.setOrderId(order.getId());
            order_goods.setAccountId(order.getAccountId());
            order_goods.setGoodsId(good.getId());
            order_goods.setStoreId(order_main.getStoreId());
            order_goods.setGoodsName(good.getName());
            order_goods.setProductId(goods_product.getId());
            order_goods.setSku(goods_product.getSku());
            order_goods.setName(goods_product.getName());
            order_goods.setBuyNum(n);
            order_goods.setSalePrice(goods_product.getSalePrice());
            order_goods.setBuyPrice(goods_product.getSalePrice()-freeMoney);
            order_goods.setTotalMoney(goods_product.getSalePrice()*n);
            order_goods.setFreeMoney(freeMoney);
            order_goods.setPayMoney(goods_product.getSalePrice()*n-freeMoney);
            orderGoodsService.insert(order_goods);
            request.setAttribute("orderId",order.getId());
        }



        return "pages/front/h5/niantu/orderConfirmation";
    }
    /**
     * 获取订单信息
     */
    @RequestMapping("/getOrderInfo.html")
    @SJson
    public Result getOrderInfo(HttpServletRequest request) {
        String id = request.getParameter("id");
        request.setAttribute("orderId",id);
        try {
        Subject subject = SecurityUtils.getSubject();
        Account_user accountUser = (Account_user) subject.getPrincipal();
        if(accountUser==null){
            return Result.error(-1,"请先登录");
        }
        //查询收获地址
        Cnd cnd = Cnd.NEW();
        cnd.and("accountId", "=", accountUser.getId() );
        cnd.and("defaultValue","=",true);
        List<Member_address> list = memberAddressService.query(cnd);


        //查询商品信息
        Cnd pCnd = Cnd.NEW();

        Order_main order = orderMainService.fetch(id);
        if(list!=null&&list.size()>0){
            order.setAddresses(list.get(0));
        }
        Cnd ogCnd = Cnd.NEW();
        ogCnd.and("orderId","=",order.getId());
            List<Order_goods> ogList = orderGoodsService.query(ogCnd);
            for (Order_goods og :
                    ogList) {
                Cnd imgCnd = Cnd.NEW();
                imgCnd.and("goodsId","=",og.getGoodsId());
                List<Goods_image> imgList = goodsImageService.query(imgCnd);
                og.setImgUrl(imgList.get(0).getImgAlbum());
            }
            order.setGoodsList(ogList);
        return Result.success("ok",order);
        }catch (Exception e){
            log.error("获取订单信息失败",e);
            return Result.error("fail");
        }
    }
}
