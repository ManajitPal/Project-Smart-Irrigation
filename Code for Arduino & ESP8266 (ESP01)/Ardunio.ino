SoftwareSerial ESPserial(10, 11); // RX | TX 
int pin = A0;       
int relaypin = 12;
int switchpin;
byte b1;
byte b2;
const int MOISTURE_LEVEL = 700; // Threshold for the sensor for automatic watering

void setup() {
  // put your setup code here, to run once:
  Serial.begin(19200);
   pinMode(pin,INPUT);
   pinMode(relaypin,OUTPUT);
  // Start the software serial for communication with the ESP8266
  ESPserial.begin(19200);

}

void loop() {
  // put your main code here, to run repeatedly:

  int moisture = analogRead(pin); //reading sensor data and storing it in moisture
  Serial.println(moisture);

  // listen for communication from the ESP8266. For proper synchronization we need convert int  to byte and sensd n convert it back to int.
  if ( ESPserial.available() )   
    {  
      
      b1=ESPserial.read();
      b2=ESPserial.read();
       if(b2>b1)    // yup i know this is not a best method especially when incoming data gets lost. If anyone has a better strategy feel free to change it and commit :)
       {
           switchpin = ((int)b1)*256+b2;
           
  
        }
        if(b1>b2)
        {
            switchpin = ((int)b2)*256+b1;
   
        }
   

      if ( switchpin == 43 || moisture >= MOISTURE_LEVEL ) 
      digitalWrite(relaypin,HIGH);
      else
      digitalWrite(relaypin,LOW);


      // Sending moisture data to ESP8266 for sending it to the app 
      if ( Serial.available() )  
    {
       ESPserial.write( (byte)moisture/256 );
       ESPserial.write( (byte)moisture%256 );
    }

   
      }


