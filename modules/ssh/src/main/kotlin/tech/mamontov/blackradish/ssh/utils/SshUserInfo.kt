package tech.mamontov.blackradish.ssh.utils

import com.jcraft.jsch.UserInfo

class SshUserInfo : UserInfo {
    override fun getPassword(): String {
        return ""
    }

    override fun promptYesNo(str: String): Boolean {
        return true
    }

    override fun getPassphrase(): String? {
        return null
    }

    override fun promptPassphrase(message: String): Boolean {
        return true
    }

    override fun promptPassword(message: String): Boolean {
        return true
    }

    override fun showMessage(message: String) {}
}
