package me.zhenxin.zmusic.api.qq

import com.alibaba.fastjson.JSONObject
import me.zhenxin.zmusic.api.Api
import me.zhenxin.zmusic.config.Config

class QQApi : Api {
    override val api = Config.Api.qq
    override fun search(key: String, page: Int, count: Int): JSONObject {
        TODO("Not yet implemented")
    }

    override fun info(id: String): JSONObject {
        TODO("Not yet implemented")
    }

    override fun url(id: String): JSONObject {
        TODO("Not yet implemented")
    }

    override fun lyric(id: String): JSONObject {
        TODO("Not yet implemented")
    }
}
