package com.aebiz.app.goods.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 商品规格值表
 * Created by wizzer on 2016/9/27.
 */
@Table("goods_spec_values")
@TableIndexes({@Index(name = "INDEX_GOODS_SPEC_VALUES", fields = {"specId"}, unique = false)})
public class Goods_spec_values extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("规格ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String specId;

    @Column
    @Comment("规格值")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String spec_value;

    @Column
    @Comment("规格值别名")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String spec_alias;

    @Column
    @Comment("规格图片")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String spec_picurl;

    @Column
    @Comment("排序字段")
    @Prev({
            @SQL(db = DB.MYSQL, value = "SELECT IFNULL(MAX(location),0)+1 FROM goods_spec_values"),
            @SQL(db = DB.ORACLE, value = "SELECT COALESCE(MAX(location),0)+1 FROM goods_spec_values")
    })
    private Integer location;

    @One(target = Goods_spec.class, field = "specId")
    private Goods_spec goodsSpec;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getSpec_value() {
        return spec_value;
    }

    public void setSpec_value(String spec_value) {
        this.spec_value = spec_value;
    }

    public String getSpec_alias() {
        return spec_alias;
    }

    public void setSpec_alias(String spec_alias) {
        this.spec_alias = spec_alias;
    }

    public String getSpec_picurl() {
        return spec_picurl;
    }

    public void setSpec_picurl(String spec_picurl) {
        this.spec_picurl = spec_picurl;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Goods_spec getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(Goods_spec goodsSpec) {
        this.goodsSpec = goodsSpec;
    }
}
