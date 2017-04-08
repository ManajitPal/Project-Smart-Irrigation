#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>   // Include the Wi-Fi library

const char* ssid     = "AP_name";         // The SSID (name) of the Wi-Fi network you want to connect to
const char* password = "AP_password";     // The password of the Wi-Fi network
const char* host = "ROOT_URL_HERE";       // Name of the DNS where you want to store your data. Mention only the main url. example: http://manajitpal.online
int switchdatatrue = 43;
int switchdatafalse = 13;
byte a1;
byte a2;
int moisturedata;
void setup() {
  // put your setup code here, to run once:
  WiFi.begin(ssid, password);             // Connect to the network
  
     while (WiFi.status() != WL_CONNECTED) { // Wait for the Wi-Fi to connect
    delay(1000);
    Serial.begin(19200);
  }
}

void loop() {
  // put your main code here, to run repeatedly:
   if(Serial.available())   // Reading the moisture data sent by arduino. This is not the best way to convert byte to int in interfacinig. but it will work in most cases.
   {
    
    a1=Serial.read();
    a2=Serial.read();

     if( a2 > a1 )
       {
            moisturedata = ((int)a1)*256+ a2;
  
        }
        if( a1 > a2 )
        {
            moisturedata = ((int)a2)*256+ a1;
   
        }
     }

   
   if (WiFi.status() == WL_CONNECTED) {

    HTTPClient http;  //Declare an object of class HTTPClient
    
    http.begin("http://root_url.com/smartirrigation/parseswitch.php");//Specify request destination from where you are getting the app data json
   
    int httpCode = http.GET();   //Send the request
   
    if (httpCode > 0) { //Check the returning code
 
      String payload = http.getString();   //Get the request response payload
      Serial.println(payload);                     //Print the response payload
      
       StaticJsonBuffer<300> jsonBuffer;        //Static memory allocation to JSONbuffer 
       JsonObject& root = jsonBuffer.parseObject(payload);    //Parsing JSON object. {"switch":"value"}

        if (!root.success())
        {
          //Serial.println("parseObject() failed");
          return;
        }

        int toggle= root["switch"];       // Storing the value of switch in integer

        if(toggle == 1)     // Check
        {
         Serial.write((byte)switchdatatrue/256);
         Serial.write((byte)switchdatatrue%256);
        }
        else
        {
          Serial.write((byte)switchdatafalse/256);
          Serial.write((byte)switchdatafalse%256);
        }
    }
        http.end();   //Close connection

// Sending the moisture data to the server

        String url = "/smartirrigation/updatemoisture.php"; // The receive destination
        url += "?data=";
        url += moisturedata;

        WiFiClient client;

        if (client.connect(host, 80))
        {
       client.print(String("GET ") + url + " HTTP/1.1\r\n" +
                 "Host: " + host + "\r\n" +
                 "Connection: close\r\n" +
                 "\r\n"
                );

      client.stop();
       }
       else
      {
   
      client.stop();
      }
      
    }

    delay(2000);   // Update every two second. deleting this line may result in DoS. We don't want that now do we? 

}
