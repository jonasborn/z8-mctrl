package z8.mctrl.companion.device

import z8.mctrl.db.RDS
import z8.mctrl.jooq.tables.pojos.Internaldevice

class InternalDevices {

    companion object {
        fun getPassword(id: String): String {
            return ""
        }

        fun get(id: String): Internaldevice? {
            val list = RDS.internalDevice().fetchById(id)
            if (list.isEmpty()) return null
            return list.first()
        }

        fun add(ind: Internaldevice) {
            RDS.internalDevice().insert(ind)
        }

        fun update(ind: Internaldevice) {
            RDS.internalDevice().update(ind)
        }

    }

}