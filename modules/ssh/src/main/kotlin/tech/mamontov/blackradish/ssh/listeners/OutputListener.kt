package tech.mamontov.blackradish.ssh.listeners

import com.sshtools.common.ssh.Channel
import com.sshtools.common.ssh.ChannelEventListener
import java.io.UnsupportedEncodingException
import java.nio.ByteBuffer
import java.nio.charset.Charset

/**
 * Ssh command output listener.
 *
 * @author Dmitry Mamontov
 *
 * @property output StringBuffer
 * @constructor
 */
class OutputListener(private var output: StringBuffer) : ChannelEventListener {
    /**
     * On channel data in.
     *
     * @param channel Channel
     * @param buffer ByteBuffer
     */
    override fun onChannelDataIn(channel: Channel, buffer: ByteBuffer) {
        this.recordOutput(buffer)
    }

    /**
     * On channel extended data.
     *
     * @param channel Channel
     * @param buffer ByteBuffer
     * @param type Int
     */
    override fun onChannelExtendedData(channel: Channel, buffer: ByteBuffer, type: Int) {
        this.recordOutput(buffer)
    }

    /**
     * Record output to buffer.
     *
     * @param buffer ByteBuffer
     */
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
