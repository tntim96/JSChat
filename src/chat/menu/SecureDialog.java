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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SecureDialog extends JDialog implements Runnable {
	JFrame parent;
	JCheckBox jCheckBoxSK, jCheckBoxIV, jCheckBoxEx, jCheckBoxSec;
	JButton jb;
	Thread thread;

	public SecureDialog(final JFrame parent) {
		super(parent, "Securing Channel", true);
		this.parent = parent;

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(1,4,1,4);
		getContentPane().setLayout(gridbag);

		//Generate Session
		JLabel jLabelSK = new JLabel("Generating session key half");
		c.gridx = 0;c.gridy = 0;
		getContentPane().add(jLabelSK, c);

		jCheckBoxSK = new JCheckBox();
		jCheckBoxSK.setEnabled(false);
		c.gridx = 1;c.gridy = 0;
		getContentPane().add(jCheckBoxSK, c);

		//Generate IV
		JLabel jLabelIV = new JLabel("Generating session IV");
		c.gridx = 0;c.gridy = 1;
		getContentPane().add(jLabelIV, c);

		jCheckBoxIV = new JCheckBox();
		jCheckBoxIV .setEnabled(false);
		c.gridx = 1;c.gridy = 1;
		getContentPane().add(jCheckBoxIV , c);

		//Key/IV Exchanged
		JLabel jLabelEx = new JLabel("Session Key/IV exchanged");
		c.gridx = 0;c.gridy = 2;
		getContentPane().add(jLabelEx, c);

		jCheckBoxEx = new JCheckBox();
		jCheckBoxEx .setEnabled(false);
		c.gridx = 1;c.gridy = 2;
		getContentPane().add(jCheckBoxEx , c);

		//Channel Secured
		JLabel jLabelSec = new JLabel("Channel Secured");
		c.gridx = 0;c.gridy = 3;
		getContentPane().add(jLabelSec, c);

		jCheckBoxSec = new JCheckBox();
		jCheckBoxSec .setEnabled(false);
		c.gridx = 1;c.gridy = 3;
		getContentPane().add(jCheckBoxSec , c);

		jb = new JButton(" Close ");
		jb.setEnabled(false);
		c.gridwidth = 2;
		c.gridx = 0;c.gridy = 4;
		getContentPane().add(jb, c);

		jb.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			}
		);

		pack();
		setResizable(false);
		center();
		thread = new Thread(this);
	}
	
	public synchronized void start() {
		thread.start();
	}

	public void run() {
		setVisible(true);
	}

	protected void center() {
		int x = parent.getLocation().x+(parent.getSize().width/2)-getSize().width/2;
		int y = parent.getLocation().y+(parent.getSize().height/2)-getSize().height/2;
		setLocation(x,y);
	}

	public void doClose() {dispose();}

	public void generatedKey() {
		jCheckBoxSK.setSelected(true);
		repaint();
	}

	public void generatedIV() {
		jCheckBoxIV.setSelected(true);
		repaint();
	}

	public void keyExchanged() {
		jCheckBoxEx.setSelected(true);
		repaint();
	}

	public void channelSecured() {
		jCheckBoxSec.setSelected(true);
		jb.setEnabled(true);
		repaint();
	}
}
