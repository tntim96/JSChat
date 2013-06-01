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

public class SetNameDialog extends ChatDialog
{
	JTextField yourNameText;
	public SetNameDialog(JFrame parent) {
		super(parent, "Enter Your Name", true);
		getContentPane().setLayout(new GridLayout(3,1));
		JPanel top = new JPanel();
		JPanel middle = new JPanel();
		JPanel bottom = new JPanel();
		getContentPane().add(top);
		getContentPane().add(middle);
		getContentPane().add(bottom);
		setSize(220, 140);
		setResizable(false);

		JLabel labelName = new JLabel("   Enter Your Name:   ");
		yourNameText = new JTextField(State.yourName, 16);
		yourNameText.setBackground(Color.white);
		yourNameText.setEditable(true);

		JButton ok = new JButton("     OK     ");    			
		top.add(labelName);
		middle.add(yourNameText);
		bottom.add(ok);
		ok.addActionListener(
			new ActionListener(){public void actionPerformed(ActionEvent e) {doClose();}}
		);
		yourNameText.addKeyListener(
			new KeyAdapter(){
				public void keyPressed(KeyEvent e) {
					if (((int)e.getKeyChar() == 10) || ((int)e.getKeyChar() == 13))
					doClose();
				}
			}
		);

		center();
		setVisible(true);
	}

	public void doClose() {
		State.yourName = yourNameText.getText();
		dispose();     	
	}
} // End of class SetNameDialog
