package tech.mamontov.blackradish.ssh.listeners

import com.sshtools.common.ssh.Channel
import com.sshtools.common.ssh.ChannelEventListener
import java.io.UnsupportedEncodingException
import java.nio.ByteBuffer
import java.nio.charset.Charset

class OutputListener(private var output: StringBuffer) : ChannelEventListener {
    override fun onChannelDataIn(channel: Channel, buffer: ByteBuffer) {
        this.recordOutput(buffer)
    }

    override fun onChannelExtendedData(channel: Channel, buffer: ByteBuffer, type: Int) {
        this.recordOutput(buffer)
    }

    @Synchronized
    private fun recordOutput(buffer: ByteBuffer) {
        val tmp = ByteArray(buffer.remaining())
        buffer[tmp]
        try {
            output.append(String(tmp, Charset.defaultCharset()))
        } catch (e: UnsupportedEncodingException) {
            throw IllegalStateException(e.message, e)
        }
    }
}
