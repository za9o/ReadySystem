# ReadySystem
A system to indicate readiness of two different teams participating in some kind of activity

The project contains a REST service which is run on a Tomcat server.
The server has a main POST and GET method at the moment. 
The post request waits for input from the arduino (or postman) containing information about two teams. "redTeamStatus" and "blueTeamStatus", true or false. When posted, the system will check if both teams are ready.
This information is stored in a file and a database.
The GET request is the information passed on to the html part. The server side code will, for now, not do anything but start a pie timer [4] if both teams are ready, this is all on done on the HTML client side.
The information the GET request is acquiring, is derived from the text file/database mentioned in the POST request.
Support for SQL Database (MariaDB) has been added.
A POST reset is also created, which will set both team values to false.
The code is derived from examples given by mkyoung. The code is reflecting this.

The Arduino side, consists of a D1 mini (wemos) and is using the ESP8266WiFi library. [3]
The arduino code, issues a POST request to the server, telling it which team is ready and which is not. A GET request is also being tested out, but at the moment it is faulty, and not supplying adequate feedback. (namely panic loop_task, since GET requests are posted to the server every 3 seconds).
The arduino uses a rest client [1], and a JSON parser [2].
For now, each unit must be hard coded with team name... THe plan is to make this dynamic, so teams can be chosen independently of which unit is being used.

The html page uses javascript and jquery to call different GET and POST methods from the server.
When both teams are ready, a pie timer will start counting [4], sounding the all ready after 10 seconds.

There are a few error handling methods in the code, but it is far from complete here.
There is a lack of a basic logging/debugging feature on the server
Missing database row/colum checks on the server
The arduino will sometimes at boot, cause the POST message to be sent, without the button being pressed.
The javascript will throw an 500 error if the SQL database is not running. 
etc.

<bold>References:</bold>
[1] - https://github.com/CanTireInnovations/esp8266-arduino-rest-client
[2] - http://blog.benoitblanchon.fr/arduino-json-v5-0/
[3] - https://github.com/ekstrand/ESP8266wifi/blob/master/ESP8266wifi.h
[4] - https://github.com/knorthfield/pietimer/blob/master/jquery.pietimer.js
[5] - http://tympanus.net/codrops/2012/09/13/button-switches-with-checkboxes-and-css3-fanciness/


Updates:
2016/07/15 - added menu bar, separated gps and main page. Added new visual representation of team status[5]. User can chose gps values to view. Moved the reset button.

2016/07/30 - Removed the GET to aquire gpsID, and instead created a possibility to create new users. The post of latitude and longitude was switched, so fixed this. Minor text changes. A checkConnectivity GET method created. Code cleanup and status message handling. An android app has been created as well. The user is able to Create new user, log in, and send positioning data to the server. Passwords are also encrypted. See android app for a more detailed explanation.
A live test has been performed, and the system is recieving values perfectly. 
