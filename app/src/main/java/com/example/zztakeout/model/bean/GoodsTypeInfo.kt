package com.example.zztakeout.model.bean

class GoodsTypeInfo {
    //商品类型id
    var id  = 0
    //商品类型名称
    var name : String = ""
    //特价信息
    var info : String = ""
    //商品列表
    var list : List<GoodsInfo> = listOf()
    //小红点数量
    var tvRedDotCount : Int = 0

    constructor() : super() {}
    constructor(id: Int, name: String, info: String, list: List<GoodsInfo>) : super() {
        this.id = id
        this.name = name
        this.info = info
        this.list = list
    }

    override fun toString(): String {
        return "GoodsTypeInfo [id=$id, name=$name, info=$info, list=$list]"
    }
}