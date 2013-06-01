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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AboutDialog extends ChatDialog {
	Color orange = new Color(255,204,153);
	JButton ok = new JButton("OK");    			

	public AboutDialog(JFrame parent)	{
		super(parent, "JSChat", true);
		getContentPane().setLayout(new BorderLayout());

		JPanel main = new JPanel();
		main.setLayout(new GridLayout(5,1));
		JPanel lev1 = new JPanel();
		JPanel lev2 = new JPanel();
		JPanel lev3 = new JPanel();
		JPanel lev4 = new JPanel();
		JPanel lev5 = new JPanel();
		JPanel lev6 = new JPanel();
		main.setBackground(orange);
		lev1.setBackground(orange);
		lev2.setBackground(orange);
		lev3.setBackground(orange);
		lev4.setBackground(orange);
		lev5.setBackground(orange);

		main.add(lev1);
		main.add(lev2);
		main.add(lev3);
		main.add(lev4);
		main.add(lev5);

        JLabel text1 = new JLabel("JSChat");
        text1.setFont(new Font("TimesRoman", Font.BOLD, 18));
		lev1.add(text1);
        JLabel text2 = new JLabel("By tntim96");
        lev2.add(text2);
        JLabel text3 = new JLabel("First Release: 17th Sep 2001");
        lev3.add(text3);
        JLabel text4 = new JLabel("Last Release : 15th Mar 2002");
        lev4.add(text4);
        JLabel text5 = new JLabel("Resurrected  :  2nd Jun 2013");
        lev5.add(text5);

		lev6.add(ok);
		getContentPane().add("Center",main);
		getContentPane().add("South",lev6);
		
		ok.addActionListener( 
			new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					dispose();     	
				}
			}
		);
		ok.addKeyListener( 
			new KeyAdapter(){
				public void keyPressed(KeyEvent e) {
					if (((int)e.getKeyChar() == 10) || ((int)e.getKeyChar() == 13))
					dispose();     	
				}
			}
		);

		setSize(250, 200);
		setResizable(false);
		center();
        setVisible(true);
	}
}