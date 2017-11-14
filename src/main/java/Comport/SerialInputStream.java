/* 
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Comport;

import jssc.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 *
 * @author Jan.Adamczyk
 */
public class SerialInputStream extends InputStream {

    private final SerialPort serialPort;
    private int defaultTimeout = 0;

    /**
     *
     * @param sp
     */
    public SerialInputStream(SerialPort sp) {
        serialPort = sp;
    }

    /**
     *
     * @param time
     */
    public void setTimeout(int time) {
        defaultTimeout = time;
    }

    @Override
    public int read() throws IOException {
        return read(defaultTimeout);
    }

    /**
     *
     * @param timeout
     * @return
     * @throws IOException
     */
    public int read(int timeout) throws IOException {
        byte[] buf;
        try {
            if (timeout > 0) {
                buf = serialPort.readBytes(1, timeout);
            } else {
                buf = serialPort.readBytes(1);
            }
            return (int) buf[0];
        } catch (SerialPortException | SerialPortTimeoutException e) {
            throw new IOException(e);
        }
    }

    @Override
    public int read(byte[] buf) throws IOException {
        return read(buf, 0, buf.length);
    }

    @Override
    public int read(byte[] buf, int offset, int length) throws IOException {

        if (buf.length < offset + length) {
            length = buf.length - offset;
        }

        int available = this.available();

        if (available > length) {
            available = length;
        }

        try {
            byte[] readBuf = serialPort.readBytes(available);
            System.arraycopy(readBuf, 0, buf, offset, length);
            return readBuf.length;
        } catch (SerialPortException e) {
            throw new IOException(e);
        }
    }

    /**
     *
     * @param buf
     * @return
     * @throws IOException
     */
    public int blockingRead(byte[] buf) throws IOException {
        return blockingRead(buf, 0, buf.length, defaultTimeout);
    }

    /**
     *
     * @param buf
     * @param timeout
     * @return
     * @throws IOException
     */
    public int blockingRead(byte[] buf, int timeout) throws IOException {
        return blockingRead(buf, 0, buf.length, timeout);
    }

    /**
     *
     * @param buf
     * @param offset
     * @param length
     * @return
     * @throws IOException
     */
    public int blockingRead(byte[] buf, int offset, int length) throws IOException {
        return blockingRead(buf, offset, length, defaultTimeout);
    }

    /**
     *
     * @param buf
     * @param offset
     * @param length
     * @param timeout
     * @return
     * @throws IOException
     */
    public int blockingRead(byte[] buf, int offset, int length, int timeout) throws IOException {
        if (buf.length < offset + length) {
            throw new IOException("Not enough buffer space for serial data");
        }

        if (timeout < 1) {
            return read(buf, offset, length);
        }

        try {
            byte[] readBuf = serialPort.readBytes(length, timeout);
            System.arraycopy(readBuf, 0, buf, offset, length);
            return readBuf.length;
        } catch (SerialPortException | SerialPortTimeoutException e) {
            throw new IOException(e);
        }
    }

    @Override
    public int available() throws IOException {
        int ret;
        try {
            ret = serialPort.getInputBufferBytesCount();
            if (ret >= 0) {
                return ret;
            }
            throw new IOException("Error checking available bytes from the serial port.");
        } catch (IOException | SerialPortException e) {
            throw new IOException("Error checking available bytes from the serial port.");
        }
    }

}
