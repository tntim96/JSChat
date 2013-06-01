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

public class SecuringDialog extends JDialog {
	Frame parent;
	JButton ok = new JButton("OK");

	public SecuringDialog(JFrame parent)	{
		super(parent, "JSChat", false);
		this.parent = parent;
		getContentPane().setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;gbc.gridy = 0;
		
		JLabel label = new JLabel("Securing Channel");
		label.setFont(new Font("Arial", Font.BOLD, 18));
		getContentPane().add(label);
		ok.setEnabled(false);
		gbc.gridy++;
		getContentPane().add(ok);
		
		ok.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                }
        );
		ok.addKeyListener(
                new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        if (ok.isEnabled())
                            if (((int) e.getKeyChar() == 10) || ((int) e.getKeyChar() == 13))
                                dispose();
                    }
                }
        );

		pack();
		setResizable(false);
		center();
		setVisible(true);
	}

	public void setEnabled(boolean enabled) {
		ok.setEnabled(enabled);
	}

	protected void center() {
		int x = parent.getLocation().x+(parent.getSize().width/2)-getSize().width/2;
		int y = parent.getLocation().y+(parent.getSize().height/2)-getSize().height/2;
		setLocation(x,y);
	}
}