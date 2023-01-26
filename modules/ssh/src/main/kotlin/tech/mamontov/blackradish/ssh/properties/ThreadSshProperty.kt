package tech.mamontov.blackradish.ssh.properties

import com.sshtools.client.SshClient
import tech.mamontov.blackradish.core.interfaces.Logged

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
