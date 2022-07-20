package z8.mctrl.voyager

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import z8.mctrl.db.KVS

@Component
class Voyager @Autowired constructor(val kvs: KVS) {


    fun getStatus(id: String): VoyagerStatus {
        return VoyagerStatus(
            kvs.get<Boolean>("devices", id, "active") ?: false,
            kvs.get<String>("devices", id, "ip"),
            kvs.get<Long>("devices", id, "lastseen") ?: -1

        )
    }
 

}

class VoyagerStatus(var active: Boolean, var ip: String?, var lastSeen: Long)