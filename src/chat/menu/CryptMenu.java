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

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.*;
 
/** Utilities Menu */
public class CryptMenu extends JMenu {
	chat.Gui mainFrame;
	/** Used for file encryption */
	static public Key publicKey, privateKey, sessionKey;

	public static Cipher getAsymmetricCipher() throws Exception {
		return Cipher.getInstance("RSA","BC");// RSA/ECB/PKCS1Padding,RSA
	}

	public static SecretKey getSecretKey(byte[] keyBytes) throws Exception {
		return new SecretKeySpec(keyBytes, "RC4");
	}

	public static Cipher[] getStreamCiphers(SecretKey sessionKey) throws Exception {
		Cipher encrypter = Cipher.getInstance("RC4","BC");
		encrypter.init(Cipher.ENCRYPT_MODE, sessionKey);
		Cipher decrypter = Cipher.getInstance("RC4","BC");
		decrypter.init(Cipher.DECRYPT_MODE, sessionKey);
		return new Cipher[]{encrypter,decrypter};
	}

	public static SecretKey getFileSessionKey(byte[] keyBytes) throws Exception {
		return new SecretKeySpec(keyBytes, "Blowfish");
	}

	public static Cipher[] getBlockCiphers() throws Exception {
		Cipher encrypter = Cipher.getInstance("Blowfish/CBC/PKCS7Padding","BC");
		Cipher decrypter = Cipher.getInstance("Blowfish/CBC/PKCS7Padding","BC");
		return new Cipher[]{encrypter,decrypter};
	}

	/** Used for file encryption */
	public Key symmetricKey, asymmetricKey;
	private int bufSize = 8192;
	int asymmetricStrength = 1024;
	int symmetricStrength = 128;
	IvParameterSpec ivSpec = new IvParameterSpec(new byte[64]);


	JMenuItem secureChannel = new JMenuItem("Secure Channel");
	JMenuItem generateAKey = new JMenuItem("Generate Asymmetric Keys");
	JMenuItem loadPrivateKey = new JMenuItem("Load Private Key");
	JMenuItem loadPublicKey = new JMenuItem("Load Public Key");

	JMenuItem encryptPrivFile = new JMenuItem("Encrypt File (Private)");
	JMenuItem decryptPrivFile = new JMenuItem("Decrypt File (Private)");
	JMenuItem encryptPubFile = new JMenuItem("Encrypt File (Public)");
	JMenuItem decryptPubFile = new JMenuItem("Decrypt File (Public)");

	JMenuItem generateSKey = new JMenuItem("Generate Symmetric Key");
	JMenuItem loadSymmetricKey = new JMenuItem("Load Symmetric Key");
	JMenuItem decryptSFile = new JMenuItem("Decrypt file");
	JMenuItem encryptSFile = new JMenuItem("Encrypt file");
	JMenuItem getSHA = new JMenuItem("Message Digest (SHA-1)");
	JMenuItem getMD5 = new JMenuItem("Message Digest (MD5)");
	JMenuItem getBase64 = new JMenuItem("Base 64");

	public CryptMenu(final chat.Gui mainFrame) {
		super("Crypt");

		java.security.Security.addProvider(new BouncyCastleProvider());
		this.mainFrame = mainFrame;

		secureChannel.setEnabled(false);
		add(secureChannel);
		secureChannel.addActionListener(mainFrame);

		add(generateAKey);
		generateAKey.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						System.out.println("Initializing the KeyPairGenerator...");
						KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA","BC");
						kpg.initialize(asymmetricStrength, new SecureRandom());
						//kpg.initialize(asymmetricStrength);
						System.out.println("Generating the key pair...");
						KeyPair pair = kpg.genKeyPair();
						Key publicKey = pair.getPublic();
						Key privateKey = pair.getPrivate();

/*
            FileOutputStream fos = new FileOutputStream("private.key");
            fos.write(privateKey.getEncoded());
            fos.close();
            fos = new FileOutputStream("public.key");
            fos.write(publicKey.getEncoded());
            fos.close();
//*/
/**/
						ObjectOutputStream out;
						out = new ObjectOutputStream(new FileOutputStream("private.key"));
						out.writeObject(privateKey);
//						RSAPrivateKey rsaPrivKey = (RSAPrivateKey)privateKey;
//						out.writeObject(rsaPrivKey.getModulus());
//						out.writeObject(rsaPrivKey.getPrivateExponent());
						out.close();
				
						out = new ObjectOutputStream(new FileOutputStream("public.key"));
						out.writeObject(publicKey);
//                        RSAPrivateKey rsaPubKey = (RSAPrivateKey)publicKey;
//						out.writeObject(rsaPubKey.getModulus());
//						out.writeObject(rsaPubKey.getPublicExponent());
						out.close();
//*/
					} catch (Exception exc) {
						exc.printStackTrace();
					}
				}
			}
		);

		add(loadPrivateKey);
		loadPrivateKey.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
					fc.setDialogTitle("Load Private Key");
					int returnValue = fc.showOpenDialog(mainFrame.getContentPane());
					if (returnValue==JFileChooser.APPROVE_OPTION) {
						try {
/*
							FileInputStream fis = new FileInputStream(fc.getSelectedFile());
							byte[] keyBytes = new byte[(int)fc.getSelectedFile().length()];
							int offSet = 0;
							int numRead;
							while ((numRead=fis.read(keyBytes, offSet, keyBytes.length-offSet))!=-1)
								offSet += numRead;
							fis.close();
							//BigInteger bi[] = RSAKeyFactory.parseKey(keyBytes);
							//privateKey = new RSAPrivKey(bi[0],bi[1]);
							privateKey = new RSAPrivKeyCrt(keyBytes);
//*/
/**/
							ObjectInputStream in = new ObjectInputStream(
									new FileInputStream(fc.getSelectedFile()));
							privateKey = (Key)in.readObject();
//							privateKey = (Key)(new RSAPrivateKey((BigInteger)in.readObject(),
//                                    (BigInteger)in.readObject(),(BigInteger)in.readObject(),
//                                    (BigInteger)in.readObject(),(BigInteger)in.readObject(),
//                                    (BigInteger)in.readObject(),(BigInteger)in.readObject(),
//                                    (BigInteger)in.readObject()));
							in.close();
							if (publicKey!=null) {
								mainFrame.localKeysLoaded();
								secureChannel.setEnabled(true);
							}
//*/
							encryptPrivFile.setEnabled(true);
							decryptPrivFile.setEnabled(true);
						} catch (Exception exc) {
							exc.printStackTrace();
						}
					}
				}
			}
		);

		add(loadPublicKey);
		loadPublicKey.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
					fc.setDialogTitle("Load Public Key");
					int returnValue = fc.showOpenDialog(mainFrame.getContentPane());
					if (returnValue==JFileChooser.APPROVE_OPTION) {
						try {
/*
							FileInputStream fis = new FileInputStream(fc.getSelectedFile());
							byte[] keyBytes = new byte[(int)fc.getSelectedFile().length()];
							int offSet = 0;
							int numRead;
							while ((numRead=fis.read(keyBytes, offSet, keyBytes.length-offSet))!=-1)
								offSet += numRead;
							fis.close();
							//BigInteger bi[] = RSAKeyFactory.parseKey(keyBytes);
							//publicKey = new RSAPubKey(bi[0],bi[1]);
							publicKey = new RSAPubKey(keyBytes);
//*/
/**/
							ObjectInputStream in = new ObjectInputStream(
									new FileInputStream(fc.getSelectedFile()));
							publicKey = (Key)in.readObject();
//							publicKey = (Key)(new RSAPublicKey((BigInteger)in.readObject(),(BigInteger)in.readObject()));
							in.close();
							if (privateKey!=null) {
								mainFrame.localKeysLoaded();
								secureChannel.setEnabled(true);
							}
//*/
							encryptPubFile.setEnabled(true);
							decryptPubFile.setEnabled(true);
						} catch (Exception exc) {
							exc.printStackTrace();
						}
					}
				}
			}
		);

		addSeparator();

		encryptPrivFile.setEnabled(false);
		add(encryptPrivFile);
		encryptPrivFile.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enCodeIt("RSA", true, privateKey);
				}
			}
		);

		decryptPrivFile.setEnabled(false);
		add(decryptPrivFile);
		decryptPrivFile.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enCodeIt("RSA", false, privateKey);
				}
			}
		);

		encryptPubFile.setEnabled(false);
		add(encryptPubFile);
		encryptPubFile.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enCodeIt("RSA", true, publicKey);
				}
			}
		);

		decryptPubFile.setEnabled(false);
		add(decryptPubFile);
		decryptPubFile.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enCodeIt("RSA", false, publicKey);
				}
			}
		);

		addSeparator();

		add(generateSKey);
		generateSKey.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
					fc.setDialogTitle("Save symmetric key as");
					int returnValue = fc.showSaveDialog(mainFrame.getContentPane());
					if (returnValue==JFileChooser.APPROVE_OPTION) {
						try {
                            KeyGenerator generator = KeyGenerator.getInstance("Blowfish","BC");
                            generator.init(symmetricStrength, new SecureRandom());
                            symmetricKey = generator.generateKey();
                            FileOutputStream fos = new FileOutputStream(fc.getSelectedFile());
                            fos.write(symmetricKey.getEncoded());
                            fos.close();
                            /*
							ObjectOutputStream out = new ObjectOutputStream(
									new FileOutputStream(fc.getSelectedFile()));
							out.writeObject(symmetricKey);
							out.close();
							*/
						} catch (Exception exc) {
							exc.printStackTrace();
						}
					}
				}
			}
		);

		add(loadSymmetricKey);

		loadSymmetricKey.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
					fc.setDialogTitle("Load Symmetric Key");
					int returnValue = fc.showOpenDialog(mainFrame.getContentPane());
					if (returnValue==JFileChooser.APPROVE_OPTION) {
						try {
							FileInputStream fis = new FileInputStream(fc.getSelectedFile());
							byte[] keyBytes = new byte[(int)fc.getSelectedFile().length()];
							int offSet = 0;
							int numRead;
							while (offSet!=keyBytes.length && (numRead=fis.read(keyBytes, offSet, keyBytes.length-offSet))!=-1) {
								offSet += numRead;
								System.out.println("Read "+numRead);
							}
							fis.close();
							System.out.println("Read in key");
                            SecretKeyFactory skf = SecretKeyFactory.getInstance("Blowfish","BC");
                            symmetricKey = skf.generateSecret(new SecretKeySpec(keyBytes, "Blowfish"));
							System.out.println("Converted to BlowfishKey");
							/*
							ObjectInputStream in = new ObjectInputStream(
									new FileInputStream(fc.getSelectedFile()));
								symmetricKey = (Key)in.readObject();
							in.close();
							*/
							encryptSFile.setEnabled(true);
							decryptSFile.setEnabled(true);
						} catch (Exception exc) {
							exc.printStackTrace();
						}
					}
				}
			}
		);

		encryptSFile.setEnabled(false);
		add(encryptSFile);
		encryptSFile.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enCodeIt("Blowfish/CBC/PKCS7Padding", true, symmetricKey, ivSpec);
/*
					JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
					fc.setDialogTitle("Choose file to encrypt");
					int returnValue = fc.showOpenDialog(mainFrame.getContentPane());
					if (returnValue==JFileChooser.APPROVE_OPTION) {
						try {
							Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS7Padding","BC");
							IvParameterSpec spec = new IvParameterSpec(iv);
							cipher.init(Cipher.ENCRYPT_MODE, symmetricKey, spec);
							FileInputStream fis = new FileInputStream(fc.getSelectedFile());
							FileOutputStream fos = new FileOutputStream(fc.getSelectedFile()+".enc");
							CipherOutputStream cos = new CipherOutputStream(fos, cipher);
							byte[] buf = new byte[bufSize];
							int numRead;
							while ((numRead=fis.read(buf))!=-1)
								cos.write(buf,0,numRead);
							fis.close();
							cos.close();
						} catch (Exception exc) {
							exc.printStackTrace();
						}
					}
*/
				}
			}
		);

		decryptSFile.setEnabled(false);
		add(decryptSFile);
		decryptSFile.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enCodeIt("Blowfish/CBC/PKCS7Padding", false, symmetricKey, ivSpec);
/*
					JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
					fc.setDialogTitle("Choose file to decrypt");
					int returnValue = fc.showOpenDialog(mainFrame.getContentPane());
					if (returnValue==JFileChooser.APPROVE_OPTION) {
						try {
							Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS7Padding","BC");
							IvParameterSpec spec = new IvParameterSpec(iv);
							cipher.init(Cipher.DECRYPT_MODE, symmetricKey, spec);
							FileInputStream fis = new FileInputStream(fc.getSelectedFile());
							FileOutputStream fos = new FileOutputStream(fc.getSelectedFile()+".dec");
							CipherOutputStream cos = new CipherOutputStream(fos, cipher);
							byte[] buf = new byte[bufSize];
							int numRead;
							while ((numRead=fis.read(buf))!=-1)
								cos.write(buf,0,numRead);
							fis.close();
							cos.close();
						} catch (Exception exc) {
							exc.printStackTrace();
						}
					}
*/
				}
			}
		);

		addSeparator();

		add(getSHA);
		getSHA.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
					fc.setDialogTitle("Choose file");
					int returnValue = fc.showOpenDialog(mainFrame.getContentPane());
					if (returnValue==JFileChooser.APPROVE_OPTION) {
						try {
							MessageDigest md = MessageDigest.getInstance("SHA-1","BC");
							FileInputStream fis = new FileInputStream(fc.getSelectedFile());
							DigestInputStream dis = new DigestInputStream(fis, md);
							byte[] buf = new byte[bufSize];
							while (dis.read(buf)!=-1);
							byte[] raw = md.digest();
							fis.close();

        			new SelectDialog(mainFrame, "SHA-1", false, "SHA-1",
        				fc.getSelectedFile(), "sha", raw);
						} catch (Exception exc) {
							exc.printStackTrace();
						}
					}
				}
			}
		);

		add(getMD5);
		getMD5.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
					fc.setDialogTitle("Choose file");
					int returnValue = fc.showOpenDialog(mainFrame.getContentPane());
					if (returnValue==JFileChooser.APPROVE_OPTION) {
						try {
							MessageDigest md = MessageDigest.getInstance("MD5","BC");
							FileInputStream fis = new FileInputStream(fc.getSelectedFile());
							DigestInputStream dis = new DigestInputStream(fis, md);
							byte[] buf = new byte[bufSize];
							while (dis.read(buf)!=-1);
							byte[] raw = md.digest();
							fis.close();

        			new SelectDialog(mainFrame, "MD5", false, "MD5",
        				fc.getSelectedFile(), "md5", raw);
						} catch (Exception exc) {
							exc.printStackTrace();
						}
					}
				}
			}
		);

		add(getBase64);
		getBase64.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
					fc.setDialogTitle("Choose file");
					int returnValue = fc.showOpenDialog(mainFrame.getContentPane());
					if (returnValue==JFileChooser.APPROVE_OPTION) {
						try {
							FileInputStream fis = new FileInputStream(fc.getSelectedFile());
							OutputStreamWriter osw = new OutputStreamWriter(
								new FileOutputStream(fc.getSelectedFile()+".b64"),"UTF8");
							BASE64Encoder enc = new BASE64Encoder();
                            String base64;
                            byte[] bufRead;
							byte[] buf = new byte[bufSize];
							int numRead;
							while ((numRead=fis.read(buf))!=-1) {
								bufRead = new byte[numRead];
								System.arraycopy(buf,0,bufRead,0,numRead);
								base64 = enc.encode(bufRead);
								osw.write(base64);
							}
							fis.close();
							osw.close();
						} catch (Exception exc) {
							exc.printStackTrace();
						}
					}
				}
			}
		);
	}

	void enCodeIt(String algorithm, boolean encrypt, Key key) {
		enCodeIt(algorithm, encrypt, key, null);
	}

	void enCodeIt(String algorithm, boolean encrypt, Key key, IvParameterSpec ivSpec) {
		JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
		fc.setDialogTitle("Choose file to "+(encrypt?"encrypt.":"decrypt."));
		int returnValue = fc.showOpenDialog(mainFrame.getContentPane());
		if (returnValue==JFileChooser.APPROVE_OPTION) {
			try {
				Cipher cipher = Cipher.getInstance(algorithm,"BC");
				int mode = encrypt?Cipher.ENCRYPT_MODE:Cipher.DECRYPT_MODE;
				if (ivSpec!=null)
					cipher.init(mode, key, ivSpec);
				else
					cipher.init(mode, key);
				FileInputStream fis = new FileInputStream(fc.getSelectedFile());
				FileOutputStream fos = new FileOutputStream(fc.getSelectedFile()+(encrypt?".enc":".dec"));
				CipherOutputStream cos = new CipherOutputStream(fos, cipher);
				byte[] buf = new byte[bufSize];
				int numRead;
				while ((numRead=fis.read(buf))!=-1)
					cos.write(buf,0,numRead);
				fis.close();
				cos.close();
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	}
}