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

import java.util.EventObject;

public class TransferEvent extends EventObject {
	protected byte type;
	protected String description;

	public TransferEvent(Object source, byte type, String description) {
		super(source);
		this.type = type;
		this.description = description;
	}

	public byte getType() {return type;}
	public String getDescription() {return description;}
}
