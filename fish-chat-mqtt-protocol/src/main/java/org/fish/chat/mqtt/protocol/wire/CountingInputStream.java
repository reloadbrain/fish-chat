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

import java.io.IOException;
import java.io.InputStream;

/**
 * An input stream that counts the bytes read from it.
 */
public class CountingInputStream extends InputStream {
	private InputStream in;
	private int counter;

	/**
	 * Constructs a new <code>CountingInputStream</code> wrapping the supplied
	 * input stream.
	 */
	public CountingInputStream(InputStream in) {
		this.in = in;
		this.counter = 0;
	}
	
	public int read() throws IOException {
		int i = in.read();
		if (i != -1) {
			counter++;
		}
		return i;
	}

	/**
	 * Returns the number of bytes read since the last reset.
	 */
	public int getCounter() {
		return counter;
	}
	
	/**
	 * Resets the counter to zero.
	 */
	public void resetCounter() {
		counter = 0;
	}
}
