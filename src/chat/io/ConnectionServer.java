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
package chat.io;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/** Socket connection manager (client and server) */
public class ConnectionServer implements Runnable
{
	/** IP address of server to connect to if we are client */
	protected String serverIP = "127.0.0.1";
	/** Port to connect on if we're client/Port to listen on if we're server */
	protected int port;
	/** Number of connections to accept */
	protected int numberConnections;
	/** Number of connections accepted */
	protected int connectionCount;
	/** Application's IP address */
	protected String currentIP = "127.0.0.1";
	/** Indicates if we still listening for connections */
	protected boolean listening = false;
	/** Indicates if we are a server or not */
	protected boolean weAreServer = false;

	/** Event Listeners */
	protected EventListenerList listenerList = new EventListenerList();
	/** Communications Socket (Client AND Server) */
	protected Socket socket;
	/** Server Socket */
	protected ServerSocket serverSocket;

	/** Server connection constructor */
	public ConnectionServer(int port, int numberConnections){
		this.port = port;
		this.numberConnections = numberConnections;
		weAreServer = true;
		listening = true;
	}

	/** Client connection constructor */
	public ConnectionServer(String serverIP, int port){
		this.serverIP = serverIP;
		this.port = port;
		weAreServer = false;
	}

	/** Try to make socket connection (server and client) */
	public void run() {
		try{
			currentIP = InetAddress.getLocalHost().toString();
		}catch(UnknownHostException uhe){}
		if (weAreServer) {
			try{
				serverSocket = new ServerSocket(port);
				//serverSocket.setSoTimeout(100000);	//100 seconds
			}catch(IOException e){
				disconnect();
				fireConnectionEvent(ConnectionListener.FAILED, "Unable to listen on port:"+port, null);
				fireConnectionEvent(ConnectionListener.NOLISTEN, "Server 1 stopped listening for connections", null);
				return;
			}
			while (listening) {
				if (numberConnections>0 && connectionCount>=numberConnections) {
					disconnectServerSocket();
					socket = null;
					break;
				}
				fireConnectionEvent(ConnectionListener.INFO, "Listening for connection at "+currentIP+":"+port, null);
				try{
					socket=serverSocket.accept();
					System.out.println("Accepted connection");
					fireConnectionEvent(ConnectionListener.ESTABLISHED, "Server connection made to "+socket.getInetAddress(), socket);
					connectionCount++;
				}catch(IOException e){
					disconnect();
					if (listening)
						fireConnectionEvent(ConnectionListener.FAILED, "Stopped listening! (socket=serverSocket.accept() failed)", null);
				}
			}
		} else {	//Create client socket
			fireConnectionEvent(ConnectionListener.INFO, "Attempting to connect to IP Address: "+serverIP+" On port: "+port, null);
			try{
				socket = new Socket(serverIP,port);
				fireConnectionEvent(ConnectionListener.ESTABLISHED, "Client connection made to "+socket.getInetAddress(), socket);
			}catch(UnknownHostException uhe){
				disconnect();
				fireConnectionEvent(ConnectionListener.FAILED, "UnknownHost: "+serverIP, null);
			}catch(IOException uhe){
				disconnect();
				fireConnectionEvent(ConnectionListener.FAILED, "Connection to "+serverIP+":"+port+" failed", null);
			}
		}
	}

	/* This method leaves port bound on WinNT, Win2000.
	** Fixed JDK1.4. Bug# 4476378
	*/
	public void disconnectServerSocket() {
		try {
			if (weAreServer) {
				if (listening)
					fireConnectionEvent(ConnectionListener.NOLISTEN, "Server stopped listening for connections", null);
				listening = false;
				if (serverSocket!=null) {
					serverSocket.close();
					serverSocket = null;
				}
			}
		}catch(IOException ioe){}
	}

	/** Disconnect */
	public void disconnect() {
		disconnectServerSocket();
		try {
			if (socket!=null) {
				socket.close();
				socket = null;
			}
		}catch(IOException ioe){}
	}		

	/** Listen for connection events - see {@link ConnectionListener} and {@link ConnectionEvent} */
	public void addConnectionListener(ConnectionListener l) {
		listenerList.add(ConnectionListener.class, l);
	}

	/** Stop listening for connection events - see {@link ConnectionListener} and {@link ConnectionEvent} */
	public void removeConnectionListener(ConnectionListener l) {
		listenerList.remove(ConnectionListener.class, l);
	}

	protected void fireConnectionEvent(byte type, String description, Socket socket) {
		Object[] listeners = listenerList.getListenerList();
		ConnectionEvent e = new ConnectionEvent(this,type,description,socket);
		for (int i = listeners.length - 2;i >= 0;i-=2) {
			((ConnectionListener)listeners[i+1]).connectionEvent(e);
		}
	}

	public boolean stillListening() {return listening;}
}
