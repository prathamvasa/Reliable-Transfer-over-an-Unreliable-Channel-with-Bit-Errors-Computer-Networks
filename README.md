•Tools and technologies used: 
Java, socket programming for UDP, Eclipse IDE.

•This project consists of building a Stop and Wait (S&W) reliable protocol. The S&W is built on top of UDP and provides a reliable transport service to the SFTP application. 

•Messages are sent one at a time and each message is acknowledged when received, before a new message can be sent.

•The S&W consists of a client and a server. Communication is unidirectional, i.e., data flows from the client to the server. The server starts first and waits for messages. The client starts the communication. Messages have seq number 0 or 1. 

•Before sending each message, a checksum is calculated and added to the S&W header. 

•After sending each message, the client waits for a corresponding ACK. When it arrives, if it is not the corresponding ACK (or if the checksum does not match), the message is sent again. If it is the corresponding ACK, the client changes state and returns to the application, which can now send one more message. This means that the S&W blocks on writes.

•The server, after receiving a message, checks its checksum. If the message is correct and has the right seq number, the server sends an ACK0 or ACK1 message (according to the seq number) to the client, changes state accordingly, and deliver data to the application.

•The protocol deals properly with duplicate data messages and duplicate ACK messages.The S&W message contains the header and the application data.
