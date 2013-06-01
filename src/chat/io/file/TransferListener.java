/*
This file is part of JSChat.

JSChat is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

JSChat is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with JSChat.  If not, see <http://www.gnu.org/licenses/>.
*/
package chat.io.file;

import java.util.EventListener;

public interface TransferListener extends EventListener {
	/** Normal data packet received */
	public static final byte DATA = 0;
	/** Non-fatal information ie - bad packets received */
	public static final byte INFO = 1;
	/** Connection Established - Made IO Streams OK */
	public static final byte ESTABLISHED = 2;
	/** Receiving file */
	public static final byte RECEIVING = 3;
	/** Connection never made - Failed to make IO Streams */
	public static final byte FAILED = 4;
	/** Connection Lost */
	public static final byte LOST = 5;
	/** Transfer complete */
	public static final byte COMPLETE = 6;
	/** Transfer cancelled remotely */
	public static final byte CANCELLED_REMOTE = 7;
	/** Transfer cancelled before starting */
	public static final byte CANCELLED_LOCAL = 8;
	/** Transfer stopped during transfer */
	public static final byte CANCELLED_LOCAL_DURING = 9;

	void transferEvent(TransferEvent e);
}
