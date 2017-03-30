# GEO TESTING ANDROID EMULATOR

to programatically change the android emulators GPS position you need to connect to it (the emulator) via _telnet_ through port 5554 (default)

this can be done in the test code using a library or a socket that communicates using the _telnet_ protocoll manually, the code that we made manually connects
( LINK TO GITHUB)

the issue with connecting to the emulator using telnet from a tests is that the emulator in recent versions requires an authentication token in order to allow you to execute commands (such as setting the GPS manually) the auth token is written in a hidden file in the host computer (the computer that runs the android emulator) an example is _/Users/<Username>/.emulator_console_auth_token_

telnet does provide you with the path to this file which differ on diffrent operating systems, however since the android tests that will connect through telnet to the emulator runs on the emulator itself, it cannot access any files on the host computer

the solution to this is to have a program on the host computer connect to the emulator via _telnet_ to get the path to the file, then use ADB to _push_ the auth file onto the amulator, allowing the tests to read the file from its emulated device to get the auth token and connect via telnet

once connected to the emulator with telnet, authenticate by sending the string
auth <auth token>

example:
auth 54kj32re09

afterwards it will unlock all the commands include the geo command which is used to tell the emulator to fake its GPS position
geo fix <lon> <lat>

example:
geo fix 34.28 -24.01

