The objective was to design and implement a simple chat server application that would allow two-way communication, with a 
potential for expansion to a multi-user system with threads. I decided to give that a shot, so I kept that in mind in the early 
stages and opted for things that were going to work in a multi-threaded sense and not need complete overhauling. The key to this 
as mentioned above, for me, was keeping things as simple and as separate as possible. Sometimes I had to try to hack away to get 
certain functionality to see if it was possible, but it always came to a point where that hacking was going to be so much easier #
if you just remove the complication, so you can see what’s happening, and split things out into logical units (as much as possible).
That’s a good idea but also you need the coupling stuff, I remember one constructor has an instance of a class passed in as a 
parameter that is never used – but in the end I decided to leave it there as I am reminding myself of possible usefulness of 
having a handle on it for future expansion. The concern of having separate chat clients and what you want to keep 
separate and what you want to share while trying to keep things simple and resources reasonable and not trip yourself up with 
threads, was an excellent exercise to get the mind around that type of stuff. 

Design

Ultimately this chat application is made up of several classes, some of them representing Threads. The Server Class initializes 
by starting up with some state – notably we use Sets to enforce uniqueness in our HashSet of ClientHandler class instances. The 
server starts a write thread (for console in) and then enters the connection listen loop for new connections on the chosen port.
On accepting a connection, it generates a new ClientHandler instance from the thread pool which is set at 20 (allowing 20 
clients and associated overhead), hands off effectively and continues listening. We have eliminated the need for a server read 
thread by a well-placed method call in the ClientHandler which calls a method in the server to print client output on the 
server. This significantly reduced complexity with different threads all doing their business, so I thought why a complicated 
solution when a simple one will do. The server write thread handles server communication to all clients and eliminates the need
for n more threads by using server instance methods which can broadcast to all users or take target parameters to an iterative 
function, we can broadcast to all clients or hone in on just one. Clients can DM (direct message) to each other, again due to 
selectors in an input parsing function where we use the @ delimeter to split input into target and message with a simple 
array.split(”@”). The server while not party to the DM’s will get a notification that a direct message has been sent between 
the users. The server can receive DM’s from users also with the @admin selector syntax: messagetexttosend@admin. If any user 
misbehaves the admin can kick the user by triggering the command switch option “kline” located in the serverTargetMessage() 
method – case insensitive syntax: kline@usernametokick . Note: This will result in a null pointer exception being thrown at 
the other end – but that’s what you get for misbehaving and I didn’t have time to write a client-side graceful exit for 
irresponsible chat citizens. Well behaved users remaining in the chat will be informed of the departure.  

Instructions

ChatServer Syntax:
Compile the source code by navigating to the src directory and typing: javac ie/gmit/dip/*.java
The ChatServer instance itself can be started using default params (port 63519) by typing: java ie.gmit.dip.ChatServer  
from the project src directory, or by specifying a port with the syntax below.

ChatServer <PortNumber>
Direct Message:
String of words to message@username
Kick User:
kline@username

ChatClient Syntax:
The ChatClient can be run in default mode by typing: java ie.gmit.dip.ChatClient , or by using the below syntax:
ChatServer <PortNumber>
ChatServer <INetAddress, PortNumber> - note INetAddress is string format such as e.g. “localhost”.
Chat Client Direct Message:
To send a chat client direct message please follow the syntax below:
stringofwords with or without spaces@username 
To message the admin use:
Stringof words@admin
