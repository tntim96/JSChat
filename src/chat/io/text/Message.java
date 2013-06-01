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

/** Message.<br>
		Signalling codes indicate codes that are used for	signalling between the two
		applications. These codes usually require processing by the talk thread and
		never reach the Listeners. Instead a new MessageListener code is used to
		alert the Listeners after processing is complete.
 */
public class Message implements java.io.Serializable {

	/** Send normal text message */
	public static final byte MESSAGE = 0;
	/** Wake code */
	public static final byte WAKE = 1;
	/** Secure Channel Initiation code - not seen by Gui */
	public static final byte SECURE_INIT = 2;
	/** Secure Channel Response code - signalling code */
	public static final byte SECURE_RESP = 3;
	/** Send file code - signalling code */
	public static final byte SEND_FILE = 4;
	/** Send file length code - signalling code */
	public static final byte SEND_FILE_LENGTH = 5;
	/** Send file cancel code - signalling code */
	public static final byte CANCEL_FXFER = 6;
	/** Signals remote user is listening for conneections - signalling code */
	public static final byte FILE_LISTEN = 7;
	/** Keys loaded code */
	public static final byte KEYS_LOADED = 8;

	/** Message Type */
	public byte code;
	/** Text message */
	public String text;
}
