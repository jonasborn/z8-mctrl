package z8.mctrl.companion.device

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import z8.mctrl.db.RDS
import z8.mctrl.jooq.tables.pojos.Internaldevice

@Component
class InternalDevices @Autowired constructor(val rds: RDS) {

    fun getPassword(id: String): String {
        return ""
    }

    fun get(id: String): Internaldevice? {
        val list = rds.internalDevice().fetchById(id)
        if (list.isEmpty()) return null
        return list.first()
    }

    fun add(ind: Internaldevice) {
        rds.internalDevice().insert(ind)
    }

    fun update(ind: Internaldevice) {
        rds.internalDevice().update(ind)
    }

    
}