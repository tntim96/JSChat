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
package chat.io;

import java.util.EventListener;

/** Interface for sending socket connection events.
		@see ConnectionServer
		@see ConnectionEvent
 */
public interface ConnectionListener extends EventListener {
	/** Non-fatal information ie - listening on port X */
	byte INFO = 0;
	/** Connection established - pass socket in {@link ConnectionEvent} */
	byte ESTABLISHED = 1;
	/** Connection couldn't be established - port busy? */
	byte FAILED = 2;
	/** No longer listening for connections */
	byte NOLISTEN = 3;

	void connectionEvent(ConnectionEvent e);
}
