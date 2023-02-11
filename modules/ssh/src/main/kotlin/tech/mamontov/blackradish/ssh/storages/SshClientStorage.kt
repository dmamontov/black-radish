package tech.mamontov.blackradish.ssh.storages

import com.sshtools.client.SshClient
import tech.mamontov.blackradish.core.interfaces.Logged

/**
 * Ssh client storage
 *
 * @author Dmitry Mamontov
 */
class SshClientStorage : Logged {
    companion object {
        private val client: ThreadLocal<SshClient?> = ThreadLocal()

        /**
         * Set ssh client.
         *
         * @param ssh SshClient
         */
        fun set(ssh: SshClient) {
            client.set(ssh)
        }

        /**
         * Get ssh client.
         *
         * @return SshClient?
         */
        fun get(): SshClient? {
            return client.get()
        }

        /**
         * Close ssh connection.
         */
        fun close() {
            if (client.get() !== null) {
                client.get()!!.disconnect()
                client.set(null)
            }
        }
    }
}
