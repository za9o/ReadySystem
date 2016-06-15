# ReadySystem
A system to indicate readiness of two different teams participating in some kind of activity

The project contains a REST service which is run on a Tomcat server.
Enclosed is also an index html file, a css(which is rather poorly done) and a javascript which is issuing both GET and POST request to the server.

The arduino code, issues a POST request to the server, telling it which team is ready and which is not. A GET request is also being tested out, but at the moment it is faulty, and not supplying an adequate feedback.

The code is derived from examples given by mkyoung. The code is reflecting this.
