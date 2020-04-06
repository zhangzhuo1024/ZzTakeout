package com.example.zztakeout.model.bean

class GoodsInfo {
    //商品id
    var id = 0
    //商品名称
    var name : String = ""
    //商品图片
    var icon : String = ""
    //组成
    var form : String = ""
    //月销售量
    var monthSaleNum = 0
    //特价
    var isBargainPrice  = false
    //是否是新产品
    var isNew  = false
    //新价
    var newPrice: String = ""
    //原价
    var oldPrice  = 0
    var sellerId = 0
    var typeId = 0
    var count = 0
    var typeName: String = ""

    constructor() : super() {}
    constructor(sellerId: Int, id: Int, name: String, icon: String, form: String, monthSaleNum: Int, bargainPrice: Boolean, isNew: Boolean, newPrice: String, oldPrice: Int) : super() {
        this.id = id
        this.name = name
        this.icon = icon
        this.form = form
        this.monthSaleNum = monthSaleNum
        isBargainPrice = bargainPrice
        this.isNew = isNew
        this.newPrice = newPrice
        this.oldPrice = oldPrice
        this.sellerId = sellerId
    }

    override fun toString(): String {
        return ("GoodsInfo [id=" + id + ", name=" + name + ", count=" + count +", icon=" + icon + ", form=" + form + ", monthSaleNum="
                + monthSaleNum + ", bargainPrice=" + isBargainPrice + ", isNew=" + isNew + ", newPrice=" + newPrice
                + ", oldPrice=" + oldPrice + "]")
    }
}