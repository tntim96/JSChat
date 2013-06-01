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

/** Help Menu */
public class HelpMenu extends JMenu {

	public HelpMenu(final JFrame mainFrame) {
		super("Help");

		JMenuItem miInstructions = new JMenuItem("Instructions");
		add(miInstructions);
		miInstructions.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new InstructionsDialog(mainFrame);
				}
			}
		);

		JMenuItem miAbout = new JMenuItem("About");
		add(miAbout);
		miAbout.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new AboutDialog(mainFrame);
				}
			}
		);
	}
}