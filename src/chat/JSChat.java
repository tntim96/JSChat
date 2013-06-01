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
package chat;

/** This simply creates the State and Gui classes */
public class JSChat {

	/** Calls new JSChat() */
	public static void main (String[] args)	{
		new JSChat();
	}
	
	/** Instatiates the State and Gui classes */
	public JSChat() {
		new State();
		new Gui();
	}
}	
