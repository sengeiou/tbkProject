package com.aebiz.app.goods.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 商品类型-属性表
 * Created by wizzer on 2016/9/27.
 */
@Table("goods_type_props")
public class Goods_type_props extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("类型ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String typeId;

    @Column
    @Comment("类型名称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String name;

    @Column
    @Comment("类型 select input")
    @ColDefine(type = ColType.VARCHAR, width = 10)
    private String type;

    @Column
    @Comment("排序字段")
    @Prev({
            @SQL(db = DB.MYSQL, value = "SELECT IFNULL(MAX(location),0)+1 FROM goods_type_props"),
            @SQL(db = DB.ORACLE, value = "SELECT COALESCE(MAX(location),0)+1 FROM goods_type_props")
    })
    private Integer location;

    @One(target = Goods_type.class, field = "typeId")
    private Goods_type goodsType;

    @Many(target = Goods_type_props_values.class, field = "propsId")
    private List<Goods_type_props_values> propsValues;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Goods_type getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Goods_type goodsType) {
        this.goodsType = goodsType;
    }

    public List<Goods_type_props_values> getPropsValues() {
        return propsValues;
    }

    public void setPropsValues(List<Goods_type_props_values> propsValues) {
        this.propsValues = propsValues;
    }
}
