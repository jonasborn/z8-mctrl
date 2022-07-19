package z8.mctrl.db

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.client.RedisClient
import org.redisson.config.Config

class KVS {

    //TODO https://github.com/redisson/redisson/wiki/7.-Distributed-collections

    companion object {

        var client: RedissonClient? = null

        fun init() {
            val config = Config()
            config.useClusterServers()
                .addNodeAddress(
                    "redis://"
                            + z8.mctrl.config.Config.get("redis.host")
                            + ":" + z8.mctrl.config.Config.get("redis.port")
                )
            client = Redisson.create(config)
        }


    }

}