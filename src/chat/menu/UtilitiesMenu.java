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

import chat.State;
import chat.crc.CRC32;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Utilities Menu */
public class UtilitiesMenu extends JMenu {

	public UtilitiesMenu(final JFrame mainFrame) {
		super("Utilities");

		JMenuItem miObtainIP = new JMenuItem("Obtain Your IP Address");
		add(miObtainIP);
		miObtainIP.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new IPDialog(mainFrame);
				}
			}
		);

		JMenuItem miDNSLookup = new JMenuItem("Host / IP Lookup");
		add(miDNSLookup);
		miDNSLookup.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new DNSLookupDialog(mainFrame);
				}
			}
		);

		addSeparator();

		JMenuItem miCRC32 = new JMenuItem("CRC32");
		add(miCRC32);
		miCRC32.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					CRC32 dialog = new CRC32(mainFrame, State.seed);
					int x = mainFrame.getLocation().x+(mainFrame.getSize().width/2)-dialog.getSize().width/2;
					int y = mainFrame.getLocation().y+(mainFrame.getSize().height/2)-dialog.getSize().height/2;
					dialog.setLocation(x,y);
					dialog.setVisible(true);
					Thread t = new Thread(dialog);
					//t.setPriority(Thread.MIN_PRIORITY); //May never run on Preemptive Systems (Unix)
					t.start();
				}
			}
		);

		JMenuItem miSeed = new JMenuItem("Set CRC Seed");
		add(miSeed);
		miSeed.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new SetSeedDialog(mainFrame);
				}
			}
		);
	}
}