/* 
 * Copyright (c) 2009, 2012 IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Dave Locke - initial API and implementation and/or initial documentation
 */
package org.fish.chat.mqtt.protocol.wire;


import org.fish.chat.mqtt.protocol.MqttException;
import org.fish.chat.mqtt.protocol.MqttMessage;

import java.io.*;

/**
 * An on-the-wire representation of an MQTT SUBSCRIBE message.
 */
public class MqttSubscribe extends MqttWireMessage {

    /**
     * 
     */
    private static final long serialVersionUID = -3248577810277443043L;

    private String[] names;

    private int[] qos;

    private int count;

    public MqttSubscribe() {
        super(MqttWireMessage.MESSAGE_TYPE_SUBSCRIBE);
    }

    /**
     * Constructor for an on the wire MQTT subscribe message
     * 
     * @param info
     * @param data
     */
    public MqttSubscribe(byte info, byte[] data) throws IOException {
        super(MqttWireMessage.MESSAGE_TYPE_SUBSCRIBE);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        msgId = dis.readUnsignedShort();

        count = 0;
        names = new String[10];
        qos = new int[10];
        boolean end = false;
        while (!end) {
            try {
                names[count] = decodeUTF8(dis);
                qos[count++] = dis.readByte();
            } catch (Exception e) {
                end = true;
            }
        }
        dis.close();
    }

    /**
     * Constructor for an on the wire MQTT subscribe message
     * 
     * @param names - one or more topics to subscribe to
     * @param qos - the max QoS that each each topic will be subscribed at
     */
    public MqttSubscribe(String[] names, int[] qos) {
        super(MqttWireMessage.MESSAGE_TYPE_SUBSCRIBE);
        this.names = names;
        this.qos = qos;

        if (names.length != qos.length) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < qos.length; i++) {
            MqttMessage.validateQos(qos[i]);
        }
    }

    /**
     * @return string representation of this subscribe packet
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(super.toString());
        sb.append(" names:[");
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("\"" + names[i] + "\"");
        }
        sb.append("] qos:[");
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(qos[i]);
        }
        sb.append("]");

        return sb.toString();
    }

    protected byte getMessageInfo() {
        return (byte) (2 | (duplicate ? 8 : 0));
    }

    protected byte[] getVariableHeader() throws MqttException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeShort(msgId);
            dos.flush();
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new MqttException(ex);
        }
    }

    public byte[] getPayload() throws MqttException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            for (int i = 0; i < names.length; i++) {
                encodeUTF8(dos, names[i]);
                dos.writeByte(qos[i]);
            }
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new MqttException(ex);
        }
    }

    public boolean isRetryable() {
        return true;
    }

    /**
     * @return the names
     */
    public String[] getNames() {
        return names;
    }

    /**
     * @return the qos
     */
    public int[] getQos() {
        return qos;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

}
