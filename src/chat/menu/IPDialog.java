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
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPDialog extends ChatDialog {
	String serverIP = "";
	JTextField serverIP_text;
	
	public IPDialog(JFrame parent) {
		super(parent, "Your IP Address", true);

		try{
			InetAddress Address = InetAddress.getLocalHost();
			State.currentIP=Address.toString();
		} catch(UnknownHostException uh) {State.currentIP = "IP address not found";}

		getContentPane().setLayout(new GridLayout(3,1));
		JPanel top = new JPanel();
		JPanel middle = new JPanel();
		JPanel bottom = new JPanel();
		getContentPane().add(top);
		getContentPane().add(middle);
		getContentPane().add(bottom);

		JLabel label = new JLabel(" Your IP Address is: ");
		serverIP_text = new JTextField(State.currentIP, 20);
		serverIP_text.setBackground(Color.white);
		serverIP_text.setEditable(false);
      
		JButton ok = new JButton("     OK     ");    			
		top.add(label);
		middle.add(serverIP_text);
		bottom.add(ok);
		ok.addActionListener( 
			new ActionListener(){public void actionPerformed(ActionEvent e) {doClose();}}
		);
		serverIP_text.addKeyListener(
			new KeyAdapter(){
				public void keyPressed(KeyEvent e) {
					if (((int)e.getKeyChar() == 10) || ((int)e.getKeyChar() == 13))
					doClose();
				}
			}
		);

		//setSize(300, 140);
		pack();
		setResizable(false);
		center();
		setVisible(true);
	}

	public void doClose() {
		dispose();     	
	}
} // End of class IPDialog
