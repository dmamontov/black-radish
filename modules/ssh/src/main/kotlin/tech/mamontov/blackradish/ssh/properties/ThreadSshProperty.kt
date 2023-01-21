package tech.mamontov.blackradish.ssh.properties

import com.jcraft.jsch.Session
import com.sshtools.client.SshClient
import tech.mamontov.blackradish.core.utils.Logged

class ThreadSshProperty : Logged {
    companion object {
        private val client: ThreadLocal<SshClient?> = ThreadLocal()

        fun set(ssh: SshClient) {
            client.set(ssh)
        }

        fun get(): SshClient? {
            return client.get()
        }

        fun close() {
            if (client.get() !== null) {
                client.get()!!.disconnect()
                client.set(null)
            }
        }
    }
}
