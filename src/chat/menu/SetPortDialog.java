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

public class SetPortDialog extends ChatDialog {
	String talkPortS, filePortS;
	JTextField talkPortText,filePortText;

	public SetPortDialog(JFrame parent) {
		super(parent, "Enter Port Numbers", true);
		setSize(260, 200);
		setResizable(false);

		talkPortS = String.valueOf(State.talkPort);
		filePortS = String.valueOf(State.filePort);

		getContentPane().setLayout(new GridLayout(3,1));
		JPanel level1 = new JPanel();
		level1.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(level1);
		level1.add(new JLabel("Enter Talk Port Number:"));
		talkPortText = new JTextField(talkPortS, 6);
		talkPortText.setBackground(Color.white);
		talkPortText.setEditable(true);
		talkPortText.addKeyListener(
			new KeyAdapter(){
				public void keyPressed(KeyEvent e) {
					if (((int)e.getKeyChar() == 10) || ((int)e.getKeyChar() == 13))
						doClose();
				}
			}
		);
		level1.add(talkPortText);

		JPanel level2 = new JPanel();
		level2.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(level2);
		level2.add(new JLabel("Enter File Port Number:"));
		filePortText = new JTextField(filePortS, 6);
		filePortText.setBackground(Color.white);
		filePortText.setEditable(true);
		filePortText.addKeyListener(
			new KeyAdapter(){
				public void keyPressed(KeyEvent e) {
					if (((int)e.getKeyChar() == 10) || ((int)e.getKeyChar() == 13))
						doClose();
				}
			}
		);
		level2.add(filePortText);

		JPanel level3 = new JPanel();
		getContentPane().add(level3);
		JButton ok = new JButton("     OK      ");    			
		level3.add(ok);
		ok.addActionListener(
			new ActionListener(){public void actionPerformed(ActionEvent e) {doClose();}}
		);

		pack();
		center();
		setVisible(true);
	}

	public void doClose() {
		try {
			int temp;
			talkPortS = talkPortText.getText();
			temp = Integer.valueOf(talkPortS).intValue();
			if(temp>65535 || temp<0) {
				JOptionPane.showMessageDialog(this, "Port must be between 1 and 65535", "Port error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			State.talkPort = temp;
	
			filePortS = filePortText.getText();
			temp = Integer.valueOf(filePortS).intValue();
			if(temp>65535 || temp<0) {
				JOptionPane.showMessageDialog(this, "Port must be between 1 and 65535", "Port error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			State.filePort = temp;
	
			dispose();
		} catch (Exception e) {JOptionPane.showMessageDialog(this, "Port must be between 1 and 65535", "Port error", JOptionPane.ERROR_MESSAGE);}
	}
} // End of class SetPortDialog
