package tech.mamontov.blackradish.ssh.properties

import com.jcraft.jsch.Session
import tech.mamontov.blackradish.core.utils.Logged

class ThreadSessionProperty : Logged {
    companion object {
        private val session: ThreadLocal<Session?> = ThreadLocal()

        fun set(sshSession: Session) {
            session.set(sshSession)
        }

        fun get(): Session? {
            return session.get()
        }

        fun close() {
            if (session.get() !== null) {
                session.get()!!.disconnect()
                session.set(null)
            }
        }
    }
}
