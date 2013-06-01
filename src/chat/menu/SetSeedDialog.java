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

public class SetSeedDialog extends ChatDialog {
	JTextField seedText;
	long tempSeed = State.seed;
	
	public SetSeedDialog(JFrame parent) {
		super(parent, "Enter A Seed", true);
		getContentPane().setLayout(new GridLayout(3,1));
		JPanel top = new JPanel();
		JPanel middle = new JPanel();
		JPanel bottom = new JPanel();
		getContentPane().add(top);
		getContentPane().add(middle);
		getContentPane().add(bottom);
		setSize(140, 140);
		setResizable(false);

		JLabel labelName = new JLabel(" Enter Seed:   ");
		seedText = new JTextField(Integer.toHexString((int)State.seed).toUpperCase(), 10);
		seedText.setBackground(Color.white);
		seedText.setEditable(true);

		JButton ok = new JButton("     OK     ");    			
		top.add(labelName);
		middle.add(seedText);
		bottom.add(ok);
		ok.addActionListener( 
			new ActionListener(){public void actionPerformed(ActionEvent e) {doClose();}}
		);
		seedText.addKeyListener(
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
		try {
			tempSeed = Long.parseLong(seedText.getText(),16);
			if (tempSeed >=0 && tempSeed <= 0xFFFFFFFFL) {
				State.seed = tempSeed;
				dispose();
			} else {
				seedText.setText(Integer.toHexString((int)State.seed).toUpperCase());
			}
		} catch (NumberFormatException nfe) {
			seedText.setText(Integer.toHexString((int)State.seed).toUpperCase());
		}
	}
} // End of class SetNameDialog
