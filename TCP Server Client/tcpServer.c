#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <time.h>
#include <string.h>
#include <stdlib.h>

int main(​int​ argCount, ​char​ *argv[] ){

//Getting time:
    time_t timer;
    struct ​tm* tm_info;
    time(&timer);
    tm_info = localtime(&timer);

// Creating socket:
    int ​socketDesc, portNum, bindAddr, newSocketDesc, cliLen, n;
    char ​buffer[​80​]; // to read command from client and give back response
    struct​ sockaddr_in servAddr, cliAddr;

    if (argCount <​ 2​){
      printf( ​"Error, no port provided.\n"​);
      exit(​1​);
    }
    
    socketDesc = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

    if (socketDesc < ​0​){
      printf(​"Error opening socket.\n"​);
      exit(​1​);
    } else printf(​“Opening socket...\n”​);

//Clear server address before use:
    bzero((​char​*) &servAddr, sizeof(servAddr));

//Getting port number:
    portNum = atoi(argv[​1​]);            //convert char at the argument to int type
    servAddr.sin_family = AF_INET;        //sevAddr have IP addr concept type
    servAddr.sin_addr.s_addr = INADDR_ANY;    //servAddr gets its own addr, this PC addr only
    servAddr.sin_port = htons(portNum);       //convert int to network format of portNum for servAddr

//Binding socket address and local host (server address):
    bindAddr = bind(socketDesc, (​struct ​sockaddr *) &servAddr, sizeof(servAddr));

    if (bindAddr < ​0​){
      printf(​"Error on binding.\n"​);
      exit(​1​);
    } else printf(​“Binding successfully.\n”​);

//Receiving connection states:
    listen(socketDesc, ​5​); //max number of connection requests at a time is 5

    printf(​“Listening...\n”​);

//Getting client address:
    cliLen = sizeof(cliAddr);

//Accept and create new socket for client:
    newSocketDesc = accept(socketDesc, (​struct​ sockaddr *) &cliAddr, &cliLen);

    if (newSocketDesc < ​0​){
      printf(​"Error on accept.\n"​);
      exit(​1​);
    } else printf(​“Getting new socket from client...\n”​);

//Clear buffer:
    bzero(buffer, ​80​);

//Read request from client:
    n = read(newSocketDesc, buffer, ​80​);

    if (n<​0​){
      printf(​"Error reading from socket.\n"​);
      exit(​1​);
    } 
    printf(​"Here is the message: %s\n"​, buffer);
    
//Response to the request:
    char​ tmp[] = ​“get time”​;
    buffer[ strcspn (buffer, ​“\n”​)] = ​0​;
    cmp = strcmp (buffer, tmp);

    if (cmp == ​0​){
      bzero(buffer, ​80​);
      strftime(buffer, ​80​, ​"The time is: %d-%m-%Y %H:%M:%S\n"​, tm_info);
    } else {
      bzero(buffer, 80);
      strncpy (buffer,​ "I got your message\n"​, ​80​);
    } 

    printf (​“Responded.\n”​);
    n = write(newSocketDesc, buffer, ​80​);

    if (n<​0​){
      printf(​"Error writing to socket\n"​);
      exit(​1​);
    } 
    
    printf(​“Closing connection.\n”​);

    return ​0​;
}
