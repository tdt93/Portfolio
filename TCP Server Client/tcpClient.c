#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <string.h>
#include <netdb.h>

int main(​int​ argCount, ​char​ *argv[]) {
    int ​socketDesc, portNum, connectAddr, n;
    struct ​sockaddr_in servAddr;
    struct​ hostent *server;
    char​ buffer[​80​];
    
    if (argCount < ​3​){
      printf(​"Error input for: %s.\n"​, argv[​0​]);
      exit(​1​);
    } 

//Getting port number:
    portNum = atoi(argv[​2​]);

//Getting socket :
    socketDesc = socket(AF_INET, SOCK_STREAM,​ 0​);

    if (socketDesc <​0​){
      printf(​"Error opening socket.\n"​);
      exit(​1​);
    } else printf(​“Server socket created.\n”​);

//Getting host address:
    server = gethostbyname(argv[​1​]);

    if (server ==​ NULL​){
      printf("​Error, no such host.\n​");
      exit (​1​);
    } else printf(“​Getting address from server...\n​”);

//Clear server address before use:
    bzero((​char​ *) &servAddr, sizeof(servAddr));
    servAddr.sin_family = AF_INET;
    servAddr.sin_port = htons(portNum);

//Copy address from the server address to the client:
    bcopy((​char ​*)server->h_addr, (​char ​*)&servAddr.sin_addr.s_addr, server->h_length);

//Connect to address:
    connectAddr = connect(socketDesc, (​struct ​sockaddr *)&servAddr, sizeof(servAddr));
    
    if (connectAddr <​ 0​){
      printf(​"Error connecting.\n"​);
      exit(​1​);
    } else printf(​“Connecting to server successfully.\n”​);

    printf(​"Please enter message: "​);

//Clear buffer:
    bzero (buffer, ​80​);

//Get request from user:
    fgets(buffer, ​80​, stdin);
    n = write(socketDesc, buffer, ​80​);

    if (n<​0​){
      printf(​"Error writing to socket.\n"​);
      exit(​1​);
    } else printf(​“Writing successfully.\n”​);

//Get response from the server:
    bzero(buffer,​ 80​);
    n = read(socketDesc, buffer, ​80​);

    If (n<​0​){
      printf(​"Error reading from socket.\n"​);
      exit(​1​);
    } 
    puts (buffer);

    return ​0​;
    }
