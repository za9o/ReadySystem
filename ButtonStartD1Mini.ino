/* Blink
  Turns on the onboard LED on for one second, then off for one second, repeatedly.
  This uses delay() to pause between LED toggles.
*/
#include "pins_arduino.h"
#include <ESP8266WiFi.h>
#include <WiFiRestClient.h>

const char* ssid = "SSID";
const char* password = "PASSWORD";
const char* server = "192.168.1.8";
const int port = 8080;

int redLED = 14;
int greenLED = 12;
int buttonState = 0;
int timeoutCounter = 0;

const char* path = "/rest/test/json/metallica/get";
const char* body = "{\"singer\":\"Sverre\",\"title\":\"On top of the rain\"}";

void setup() {

  Serial.begin(115200);
  delay(10);

  pinMode(D5, OUTPUT);  // initialize onboard LED as output
  pinMode(D6, OUTPUT);
  pinMode(D3, INPUT);

  connectToWifi();
}

void loop() {

  buttonState = digitalRead(D3);
  if (buttonState == HIGH) {
    
    digitalWrite(D5, LOW);  // turn on LED with voltage HIGH
    digitalWrite(D6, HIGH);
  } else {
    delay(200);
    /*if(counter == 0) {
      color = "Blue";
      counter++;
      //delay(500);
      } else if (counter == 1) {
      color = "Red";
      counter = 0;
      //delay(500);
      }*/
    //postToServer();
    getFromServer();
    digitalWrite(D5, HIGH);   // turn off LED with voltage LOW
    digitalWrite(D6, LOW);
  }
}

/*void postToServer() {
  
  WiFiClient client;
  if (!client.connect(server, port)) {
    Serial.println("Connection failed");
    return;
  }
  Serial.print("Sending string: ");
  Serial.println(insertString);

  client.println(String("POST ") +  path + " HTTP/1.1");
  client.print("Host: ");
  client.print("192.168.1.8");
  client.print(":");
  client.println(port);
  client.println("Content-Type: application/json");
  client.print("Content-Length: ");
  client.println(insertString.length());
  client.println();
  client.print(insertString);
  delay(10);
}*/
void connectToWifi() {
  
  // Connect to WiFi network
  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");
  delay(500);
}
void postToServer() {
  
  WiFiRestClient restClient( server, port );

  Serial.print( "Posting to http://" );
  Serial.print( server );
  Serial.println( path );

  String response;
  int statusCode = restClient.post( path, body , &response );
  
  Serial.print( statusCode );
  Serial.print( " " );
  Serial.println( response );

  if(statusCode = '201') {
    Serial.print("Status code OK");
  }
}

void getFromServer() {
  
  WiFiRestClient restClient(server, port);
  
  Serial.print( "Posting to http://" );
  Serial.print( server );
  Serial.println( path );
  
  String response;
  int statusCode = restClient.get( path, &response );

  Serial.print(statusCode);
  Serial.print(" ");
  Serial.println(response);

  Serial.println(find_text("Linn", response));
  
  if (find_text("Linn", response) > 0) {
    Serial.println("GAME ON!!!");
    timeoutCounter = 0;
  } else {
      
      Serial.println("Waiting to start...");
      timeoutCounter = timeoutCounter + 1;
      Serial.println(timeoutCounter);
      delay(3000);
        if(timeoutCounter <= 4){
          Serial.println("Waiting for ready signal");
          getFromServer();
        } else if (timeoutCounter > 4) {
          Serial.println("Timeout");
          return;
        }
    
  }
}

  int find_text(String needle, String haystack) {
  int foundpos = -1;
  for (int i = 0; i <= haystack.length() - needle.length(); i++) {
    if (haystack.substring(i,needle.length()+i) == needle) {
      foundpos = i;
    }
  }
  return foundpos;
}


