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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Setup Menu */
public class SetupMenu extends JMenu {

	public SetupMenu(final JFrame mainFrame) {
		super("Setup");

		JMenuItem miSetName = new JMenuItem("Set Your Name");
		add(miSetName);
		miSetName.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new SetNameDialog(mainFrame);
				}
			}
		);

		JMenuItem miSetPorts = new JMenuItem("Set Ports");
		add(miSetPorts);
		miSetPorts.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new SetPortDialog(mainFrame);
				}
			}
		);

		JMenuItem miSetIP = new JMenuItem("Connect Details");
		add(miSetIP);
		miSetIP.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new ConnectDialog(mainFrame);
				}
			}
		);
	}
}