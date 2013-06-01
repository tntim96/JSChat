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

import java.util.EventObject;

public class MessageEvent extends EventObject {
	protected byte type;
	protected String description;
	protected Object o;

	public MessageEvent(Object source, byte type, String description, Object o) {
		super(source);
		this.type = type;
		this.description = description;
		this.o = o;
	}

	public byte getType() {return type;}
	public String getDescription() {return description;}
	public Object getObject() {return o;}
}
