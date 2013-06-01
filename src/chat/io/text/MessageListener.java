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
package chat.io.text;

import java.util.EventListener;

//All vars are implicitly public static final
public interface MessageListener extends EventListener {
	/** Normal text message received */
	byte MESSAGE = 0;
	/** Non-fatal information ie - bad packets received */
	byte INFO = 1;
	/** Connection Established - Made IO Streams OK */
	byte ESTABLISHED = 2;
	/** Connection never made - Failed to make IO Streams */
	byte FAILED = 3;
	/** Connection Lost */
	byte LOST = 4;
	/** Got file thread */
	byte FILE_XFER = 5;
	/** Channel secured */
	byte SECURED = 6;

	void messageEvent(MessageEvent e);
}
