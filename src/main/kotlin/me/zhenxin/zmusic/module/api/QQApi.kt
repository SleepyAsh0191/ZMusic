package me.zhenxin.zmusic.module.api

import com.google.gson.JsonObject
import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.module.Api

class QQApi : Api {
    override val api = Config.Api.qq
    override fun search(key: String, page: Int, count: Int): JsonObject {
        TODO("Not yet implemented")
    }

    override fun info(id: String): JsonObject {
        TODO("Not yet implemented")
    }

    override fun url(id: String): JsonObject {
        TODO("Not yet implemented")
    }

    override fun lyric(id: String): JsonObject {
        TODO("Not yet implemented")
    }
}
