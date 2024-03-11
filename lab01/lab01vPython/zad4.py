import socket

serverIP = "127.0.0.1"
serverPort = 9876
msg = "PPing from python"
client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
client.sendto(bytes(msg, 'utf-8'), (serverIP, serverPort))

recMessage = client.recvmsg(100)[0]
print(recMessage)