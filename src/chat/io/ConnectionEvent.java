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

import java.net.Socket;
import java.util.EventObject;

/** ConnectionEvent - see {@link ConnectionListener} and {@link ConnectionEvent} */
public class ConnectionEvent extends EventObject {
	protected byte type;
	protected String description;
	protected Socket socket;

	/** ConnectionEvent constructor
	***	@param type Type of connection event
	*** ({@link chat.io.ConnectionListener#INFO INFO},
	*** {@link chat.io.ConnectionListener#ESTABLISHED ESTABLISHED},
	*** {@link chat.io.ConnectionListener#FAILED FAILED})
	***	@param description String describing event
	***	@param socket Socket with live connection
	 */
	public ConnectionEvent(Object source, byte type, String description, Socket socket) {
		super(source);
		this.type = type;
		this.description = description;
		this.socket = socket;
	}

	public byte getType() {return type;}
	public String getDescription() {return description;}
	public Socket getSocket() {return socket;}
}
