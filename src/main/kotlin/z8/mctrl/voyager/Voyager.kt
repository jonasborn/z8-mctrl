package z8.mctrl.voyager

import z8.mctrl.db.KVS

class Voyager {

    companion object {
        fun getStatus(id: String): VoyagerStatus {
            return VoyagerStatus(
                KVS.get<Boolean>("devices", id, "active") ?: false,
                KVS.get<String>("devices", id, "ip"),
                KVS.get<Long>("devices", id, "lastseen") ?: -1

            )
        }
    }

}

class VoyagerStatus(var active: Boolean, var ip: String?, var lastSeen: Long) {
}