SoftwareEngineering
--------


> UPDATE.
* CheckersLobby_new.java is the file that I am editing the lobby with.
* CheckersLobbyOriginal is the file that Derek gave us.


HOW TO
-------------

	SERVER COMPILE in Visual Studio
---------

1. Download and install visual studio (express or pro..) c#.
2. Install the sql file (seserver/mysql-connector-net-6.9.4.msi)
3. open seserver/se server.sln
4. Build project
	ServerGUI is the one you want to use.
	There should be the IP to connect to the server in the GUi window at the top.

> Note: You shoulden't need to make changes to this project since the basic one is in his office.




CLIENT
----
1. Download eclipse
2. Open up eclipse.
3. Launch the project. 
4. Install window builder/swt in eclipse
5. start rmiregistry in the project's bin folder (see the project 0 documentation for questions)
6. start RMICONNECTION ENGINE
7. start the CheckersLobby.java  (or whatever one I'm working on at the moment.. one of them is working. I'll update the readme when it's more official.)

**To edit the project, OPEN WITH client/checkers lobby.java AS WINDOW BUILDER

==From Scratch: 

==SERVER COMPILE in Visual Studio:

Download and install visual studio (express or pro..) c#.

install the sql file (TestingGUI/gui/mysql-connector-net-6.9.4.msi)

New project

Drag and drop the files from derek doran (the one from bit bucket) server files

Add reference (right click project, add reference) in the project
reference is at c:\program files\mysql\binaries\4.0 \ mysql data

The program should compile+run

The serverGUI window will pop up and give you the IP to connect to the server

Client: 

see the project0.pdf documentation from pilot
