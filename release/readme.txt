This software currently uses the ABA JCE from Australian Business Access.
You'll need to check your local laws yourself to see if you can legally use this
software.

Installation instructions:

1 - Download this jar and extract preserving path
2 - Dowload ABA JCE (http://www.wumpus.com.au/crypto/aba.html)
		and put jce.zip in lib directory with JSChat
3 - {Unix} Configure java_unix.policy to match your install directories
4 - {Windows} Configure java_win32.policy to match your install directories
5 - {Unix} Configure run.sh to match your installation paths
6 - {Windows} Configure run.bat to match your installation paths
7 - Execute the script or batch file

Starting an encrypted conversation:

1 - Generate public and private key pair
2 - Send your public key to your friend*
3 - Start JSChat
4 - Load your private key
5 - Load your friend's public key
6 - One of you then has to click "Crypt->Secure Channel"
7 - After some time a session key and initialisation vector will be generated
    and exchanged and you are ready to chat

*This step is should be done by physical disks to exchange public keys. This
 will stop the man in the middle attack. But what you saying/sending probably
 isn't important enough to warrant such lengths from an attacker.