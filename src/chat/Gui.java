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
package chat;

import chat.io.ConnectionEvent;
import chat.io.ConnectionListener;
import chat.io.ConnectionServer;
import chat.io.file.FileThread;
import chat.io.file.TransferEvent;
import chat.io.file.TransferListener;
import chat.io.text.Message;
import chat.io.text.MessageEvent;
import chat.io.text.MessageListener;
import chat.io.text.TalkThread;
import chat.menu.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/** Handles Gui and events */
public class Gui extends JFrame implements ActionListener, ConnectionListener, MessageListener, TransferListener {
	public static JFrame jFrame;
	ConnectionServer connectionServer;

	TalkThread tt;
	FileThread ft;
	//SecuringDialog securingDialog;

	/** Indicates if local keys are loaded */
	private boolean localKeysLoaded;
	/** Indicates if remote user has loaded keys so we can secure the channel */
	public boolean remoteKeysLoaded;

	private JMenuBar menuBar = new JMenuBar();
	private CryptMenu cryptMenu;
	private SkinsMenu skinsMenu;
	private JMenuItem miListen,miStopListen,miConnect,miTerminate,miSend,miWake,miQuit;
	
	//Text Area
	private JScrollPane scrollPaneText;
	private JTextArea display = new JTextArea();
	//Text Field
	private JTextField send = new JTextField("Type here when connected");

	/** Creates menu bars, display and send boxes, adds window listener */
	public Gui() {
		super("JSChat");
		jFrame = this;
		addMenu();
		setConnected(false);
		addContent();
		setBounds(State.x,State.y,State.width,State.height);
		skinsMenu.setUI(State.currentUI);
		setConnected(false);
		setVisible(true);
		
		addWindowListener (
			new WindowListener() {
				public void windowActivated(WindowEvent e) {
    			send.requestFocus();
				}
				public void windowClosed(WindowEvent e) {}
				public void windowClosing(WindowEvent e) {
					setDimensions();
					State.shutDown();
				}
				public void windowDeactivated(WindowEvent e) {}
				public void windowDeiconified(WindowEvent e) {
    			send.requestFocus();
				}
				public void windowIconified(WindowEvent e) {}
				public void windowOpened(WindowEvent e) {
    			send.requestFocus();
				}
			}
		);
	}

	/** Set these values before saving state */
	private void setDimensions() {
		State.x = getLocation().x;
		State.y = getLocation().y;
		State.width = getSize().width;
		State.height = getSize().height;
	}
	
	/** Add all menu bars */
	private void addMenu() {
		setJMenuBar(menuBar);
		
		//Network Menu
		JMenu menu_network = new JMenu("Network");
		menu_network.add(miListen = new JMenuItem("Listen for Connection"));
		menu_network.add(miStopListen = new JMenuItem("Stop Listening"));
		menu_network.add(miConnect = new JMenuItem("Connect to Server"));
		menu_network.add(miTerminate = new JMenuItem("Terminate ConnectionServer"));
		menu_network.addSeparator();
		menu_network.add(miSend = new JMenuItem("Send a File"));
		menu_network.add(miWake = new JMenuItem("Wake Remote User"));
		menu_network.addSeparator();
		menu_network.add(miQuit = new JMenuItem("Quit"));
		miListen.addActionListener(this);
		miStopListen.addActionListener(this);
		miConnect.addActionListener(this);
		miTerminate.addActionListener(this);
		miStopListen.setEnabled(false);
		miSend.addActionListener(this);
		miWake.addActionListener(this);
		miQuit.addActionListener(this);
		menuBar.add(menu_network);		

		menuBar.add(cryptMenu = new CryptMenu(this));
		menuBar.add(new SetupMenu(this));
		menuBar.add(new UtilitiesMenu(this));
		menuBar.add(skinsMenu = new SkinsMenu(this));
		menuBar.add(new HelpMenu(this));
	}
	
	/** Add display text area and send text field */
	private void addContent() {
		getContentPane().setLayout(new BorderLayout());

		display.setBorder(BorderFactory.createLineBorder(getContentPane().getBackground(),4));
		display.setLineWrap(true);
		display.setBackground(Color.white);
		display.setForeground(Color.blue);
		display.setEditable(false);
		scrollPaneText = new JScrollPane(display,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add("Center", scrollPaneText);

		send.setBorder(BorderFactory.createLineBorder(getContentPane().getBackground(),4));
		send.setBackground(Color.white);
		send.setForeground(Color.blue);
		send.addActionListener (
			new ActionListener() {		
				public void actionPerformed(ActionEvent e) {
					if (connected()) {
							tt.sendMessage(State.yourName+":"+send.getText());
						if (State.yourName.compareTo(State.yourNameInit) != 0) {
							updateDisplay(State.yourName + ": " + send.getText());
						} else {
							updateDisplay("You: " + send.getText());
						}
						send.selectAll();
						send.setText("");
					}
				}
			}
		);
		getContentPane().add("South", send);
	}

	/** Add text String to display text area and scroll the pane to bottom */
	public void updateDisplay(String text) {
		display.append(text+"\n");
		scrollPaneText.getViewport().setViewPosition(new Point(0,scrollPaneText.getViewport().getViewSize().height-scrollPaneText.getViewport().getViewRect().height));
	}

	public boolean connected() {
		return tt==null?false:tt.isConnected();
	}

	/** Set Network menu according to whether we are connected or not */
	public void setConnected(boolean connected) {
		miWake.setEnabled(connected);
		miSend.setEnabled(connected);
		miTerminate.setEnabled(connected);
		//if (securingDialog!=null) securingDialog.setEnabled(connected);
	}
	
	/** Set File send menu according to whether we are transferring a file or not */
	public void setFileXfer(boolean sending) {
		miSend.setEnabled(!sending);
	}
	
	/** Bla */
	public void localKeysLoaded() {
		localKeysLoaded = true;
		if (connected())
			tt.keysLoaded();
	}
	
	/** Action Listener for all menu events */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Listen for Connection")) {
			State.weAreServer = true;
			connectionServer = new ConnectionServer(State.talkPort,1);
			connectionServer.addConnectionListener(this);
			Thread t = new Thread(connectionServer);
			t.start();
			updateDisplay("Listening for connections on "+State.talkPort);
			miListen.setEnabled(false);
			miStopListen.setEnabled(true);
		}
		if (e.getActionCommand().equals("Stop Listening")) {
			connectionServer.disconnectServerSocket();
			miListen.setEnabled(true);
			miStopListen.setEnabled(false);
		}
		if (e.getActionCommand().equals("Connect to Server")) {
			State.weAreServer = false;
			connectionServer = new ConnectionServer(State.serverIP,State.talkPort);
			connectionServer.addConnectionListener(this);
			Thread t = new Thread(connectionServer);
			t.start();
			updateDisplay("Attempting to connect to "+State.serverIP+":"+State.talkPort);
		}
		if (e.getActionCommand().equals("Terminate ConnectionServer")) {
			connectionServer.disconnectServerSocket();
			if (tt!=null) tt.disconnect();
			setConnected(false);
			updateDisplay("Closing connectionServer");
		}
		if (e.getActionCommand().equals("Send a File")) {
			JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
			int returnValue = fc.showOpenDialog(Gui.jFrame.getContentPane());
			if (returnValue==JFileChooser.APPROVE_OPTION) {
				miSend.setEnabled(false);
				tt.sendFile(fc.getSelectedFile());
			}
		}
		if (e.getActionCommand().equals("Wake Remote User")) {
			tt.wakeUp();
		}
		if (e.getActionCommand().equals("Quit")) {
			setDimensions();
			State.shutDown();
		}
		if (e.getActionCommand().equals("Secure Channel")) {
			if (remoteKeysLoaded) {
				updateDisplay("Securing Channel");
				tt.secureChannelInit();
				//securingDialog = new SecuringDialog(this);
			} else
				JOptionPane.showMessageDialog(this,
    			"Remote user has not loaded keys",
    			"Secure Channel Error",
    			JOptionPane.ERROR_MESSAGE);
		}
	}

	public synchronized void connectionEvent(ConnectionEvent e) {
		System.out.println(e.getDescription());
		ConnectionServer connectionServer = (ConnectionServer)e.getSource();
		switch(e.getType()){
			case ConnectionListener.INFO:
				updateDisplay(e.getDescription());
				break;
			case ConnectionListener.ESTABLISHED:
				tt = new TalkThread(e.getSocket());
				tt.addMessageListener(this);
				Thread t = new Thread(tt);
				t.start();
				break;
			case ConnectionListener.FAILED:
				connectionServer.removeConnectionListener(this);
				connectionServer = null;
				updateDisplay(e.getDescription());
				Toolkit.getDefaultToolkit().beep();
				break;
			case ConnectionListener.NOLISTEN:
				miListen.setEnabled(true);
				miStopListen.setEnabled(false);
				connectionServer.removeConnectionListener(this);
				updateDisplay(e.getDescription());
				connectionServer = null;
				Toolkit.getDefaultToolkit().beep();
				break;
			default: System.err.println("Invalid ConnectionEvent type "+e.getType());
		}
	}

	public synchronized void messageEvent(MessageEvent e) {
		TalkThread tt = (TalkThread)e.getSource();
		System.out.println(tt.getRemoteIP()+":"+e.getDescription());
		switch(e.getType()){
			case MessageListener.MESSAGE:
				Message message = (Message)e.getObject();
				switch(message.code) {
					case  Message.MESSAGE:
						System.out.println(tt.getRemoteIP()+":"+message.text);
						//updateDisplay(tt.getRemoteIP()+":"+message.text);
						updateDisplay(message.text);
						break;
					case Message.WAKE:
						toFront();
						requestFocus();
						updateDisplay("Wake up request from "+tt.getRemoteIP());
						Toolkit.getDefaultToolkit().beep();
						break;
					case Message.CANCEL_FXFER:
						miSend.setEnabled(true);
						updateDisplay("File xfer cancelled remotely");
						ft.removeTransferListener(this);
						ft.cancel();
						Toolkit.getDefaultToolkit().beep();
						break;
					case Message.KEYS_LOADED:
						remoteKeysLoaded = true;
						updateDisplay("Keys loaded by "+tt.getRemoteIP());
						break;
					case Message.SECURE_INIT:
						//securingDialog = new SecuringDialog(this);
						updateDisplay("Securing channel with "+tt.getRemoteIP());
						break;
					default: System.err.println("Invalid Message code "+e.getType());
				}
				break;
			case MessageListener.INFO:
				System.out.println("Info: "+e.getDescription());
				updateDisplay("Info: "+e.getDescription());
				break;
			case MessageListener.ESTABLISHED:
				setConnected(true);
				if (localKeysLoaded)
					tt.keysLoaded();
				updateDisplay("Connected to "+tt.getRemoteIP());
				Toolkit.getDefaultToolkit().beep();
				break;
			case MessageListener.FAILED://Connected but never made I/O Streams
				tt.removeMessageListener(this);
				updateDisplay("Connection failed");
				Toolkit.getDefaultToolkit().beep();
				break;
			case MessageListener.LOST:
				setConnected(false);
				tt.removeMessageListener(this);
				updateDisplay("Connection lost");
				Toolkit.getDefaultToolkit().beep();
				break;
			case MessageListener.SECURED:
				updateDisplay("Channel secured");
				//if (securingDialog!=null) securingDialog.setEnabled(true);
				Toolkit.getDefaultToolkit().beep();
				break;
			case MessageListener.FILE_XFER:
				miSend.setEnabled(false);
				ft = (FileThread)e.getObject();
				ft.addTransferListener(this);
				break;
			default: System.err.println("Invalid MessageEvent type "+e.getType());
		}
	}

	public synchronized void transferEvent(TransferEvent e) {
		FileThread ft = (FileThread)e.getSource();
		System.out.println("XFer event "+e.getDescription());
		switch(e.getType()){
			case TransferListener.FAILED:
				updateDisplay(e.getDescription());
				miSend.setEnabled(true);
				System.out.println("Xfer failed");
				ft.removeTransferListener(this);
				tt.cancelXfer();
				break;
			case TransferListener.CANCELLED_LOCAL_DURING:
				tt.cancelXfer();
			case TransferListener.CANCELLED_REMOTE:
			case TransferListener.CANCELLED_LOCAL:
			case TransferListener.COMPLETE:
				miSend.setEnabled(true);
				updateDisplay(e.getDescription());
				System.out.println(e.getDescription());
				ft.removeTransferListener(this);
				break;
			default: System.err.println("Invalid TransferEvent type "+e.getType());
		}
		Toolkit.getDefaultToolkit().beep();
	}
}
