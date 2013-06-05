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
package chat.menu;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChatDialog extends JDialog {
	JFrame parent;

	public ChatDialog(JFrame parent, String title, boolean modal) {
		super(parent, title, modal);
		this.parent = parent;

		addWindowListener (
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {doClose();}
			}
		);
	}

    protected void doClose() {dispose();}

	protected void center() {
		int x = parent.getLocation().x+(parent.getSize().width/2)-getSize().width/2;
		int y = parent.getLocation().y+(parent.getSize().height/2)-getSize().height/2;
		setLocation(x,y);
	}
}