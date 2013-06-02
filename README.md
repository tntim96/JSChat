JSChat - A Secure Chat and File Transfer Program
================================================

JSChat is a platform independent secure chat and file transfer program written in Java Swing.

JSCover is free software, distributed under the
[GNU General Public License version 3](http://www.gnu.org/licenses/gpl.txt).

Features
--------
* Real-time text based chat
* File transfer
* Text chat and file transfers can be encrypted
* Encryption utilities (Key Generation/File encryption|decryption/SHA/MD5/Base64)
* Resume file transfer (for interrupted or cancelled transfers)
* CRC32 File integrity check
* DNS Host / IP lookup
* Multi-threading (continue chatting while your files transfer)
* Save last used settings
* Skins (as available for JRE for your platform)

What do I need to run JSChat?
-----------------------------
* JSChat.jar binary (run `ant jar`)
* Install a Java Runtime Environment (minimum Java 1.5)
* Save Bouncy Castle's JCA Provider [bcprov-ext-jdk15on-148.jar](http://www.bouncycastle.org/download/bcprov-ext-jdk15on-148.jar) to JSChat's lib directory
* Install [Oracle's unlimited strength JCE](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* Run `run.bat` or `run.sh` (or manually `java "dist/JSChat.jar:lib/bcprov-ext-jdk15on-148.jar" chat.JSChat`)

Encryption Details
------------------
* Public/Private Keys are RSA, 1024 bit
* Text chat uses RC4, 218 bit
* File transfer uses AES, 218 bit, CBC, PKCS#5

Future changes are likely to make encryption implementations configurable and allow for session keys longer than
double the RSA block size.

Session Key Exchange Process
----------------------------
* Person 1 generates 1st half of session key and complete initialisation vector
* Person 1 then encrypts these with the Person 2's public key
* Person 1 then sends the encrypted data to Person 2
* Person 2 decrypts 1st half key and vector using their private key
* Person 2 generates 2nd half of the session key
* Person 2 then encrypts these with the Person 1's public key
* Person 2 then sends the encrypted data to Person 1
* Person 1 decrypts 2nd half key using their private key
* Each end now has 1st and 2nd half of key and initialisation vector

Key loading via a password protected key-store will be added next

Development
-----------
JSChat is a standard Ant and Java build.

History
-------
This program is a based on a non-encrypted program, version written in 1999 in a couple of sittings. Encryption was
added using the [ABA JCE](http://web.archive.org/web/20010217155013/http://www.wumpus.com.au/crypto/aba.html) in 2001.
It has recently been open-sourced and updated to work with Bouncy Castle.