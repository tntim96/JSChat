package chat.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class SelectKeyDialog extends ChatDialog {
    protected JComboBox jComboBox;

    /**
     * Command line startup
     */
    public static void main(String[] args) throws IOException {
        //Make test API Calls
        JFrame frame = new JFrame();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - frame.getSize().width) / 2;
        int y = (d.height - frame.getSize().height) / 2;
        frame.setLocation(x, y);
        new SelectKeyDialog(frame, new String[]{"a", "b", "c"});
    } // End of main

    public SelectKeyDialog(JFrame parent, String[] list) {
        super(parent, "Select Public Key", false);
        setModal(true);
        getContentPane().setLayout(new GridLayout(3, 1));
        JPanel top = new JPanel();
        JPanel middle = new JPanel();
        JPanel bottom = new JPanel();
        getContentPane().add(top);
        getContentPane().add(middle);
        getContentPane().add(bottom);
        setSize(220, 140);
        setResizable(false);

        JLabel labelPassword = new JLabel(" Key Alias: ");
        jComboBox = new JComboBox(list);
        jComboBox.setBackground(Color.white);

        JButton ok = new JButton("     OK     ");
        top.add(labelPassword);
        middle.add(jComboBox);
        bottom.add(ok);
        ok.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        doClose();
                    }
                }
        );
        jComboBox.addKeyListener(
                new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        if (((int) e.getKeyChar() == 10) || ((int) e.getKeyChar() == 13))
                            doClose();
                    }
                }
        );

        center();
        setVisible(true);
    }

    public void doClose() {
        dispose();
    }
}
