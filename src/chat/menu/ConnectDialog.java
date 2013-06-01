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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ConnectDialog extends ChatDialog {
	String serverIP;//, numConnectionsS;
	JTextField serverIP_text;//, numConnectionsText;
	
	public ConnectDialog(JFrame parent) {
		super(parent,  "Enter Server IP Address", true);

		//numConnectionsS = String.valueOf(State.numConnections);

		//getContentPane().setLayout(new GridLayout(4,1));
		getContentPane().setLayout(new GridLayout(3,1));
		JPanel level1 = new JPanel();
		level1.add(new JLabel(" Enter Server IP Address: "));
		getContentPane().add(level1);

		JPanel level2 = new JPanel();
		serverIP_text = new JTextField(State.serverIP, 20);
		serverIP_text.setBackground(Color.white);
		serverIP_text.setEditable(true);
		serverIP_text.addKeyListener(
			new KeyAdapter(){
				public void keyPressed(KeyEvent e) {
					if (((int)e.getKeyChar() == 10) || ((int)e.getKeyChar() == 13))
					doClose();
				}
			}
		);
		level2.add(serverIP_text);
		getContentPane().add(level2);
/*
		JPanel level3 = new JPanel();
		level3.add(new JLabel(" Number of connections to accept (0 for infinite): "));
		numConnectionsText = new JTextField(String.valueOf(State.numConnections), 3);
		numConnectionsText.addKeyListener(
			new KeyAdapter(){
				public void keyPressed(KeyEvent e) {
					if (((int)e.getKeyChar() == 10) || ((int)e.getKeyChar() == 13))
					doClose();
				}
			}
		);
		level3.add(numConnectionsText);
		getContentPane().add(level3);
*/
		JPanel level4 = new JPanel();
		JButton ok = new JButton("     OK     ");    			
		level4.add(ok);
		ok.addActionListener( 
			new ActionListener(){public void actionPerformed(ActionEvent e) {doClose();}}
		);
		getContentPane().add(level4);

		pack();
		setResizable(false);
		center();
		setVisible(true);
	}

	public void doClose() {
		try {
			State.serverIP = serverIP_text.getText();
			//State.numConnections = Integer.parseInt(numConnectionsText.getText());
			dispose();
		} catch (Exception e) {}
	}
} // End of class IPDialog
