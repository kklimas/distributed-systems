import socket

serverIP = "127.0.0.1"
serverPort = 9876
msg = "Żółta gęś"
client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
client.sendto(bytes(msg, 'utf-8'), (serverIP, serverPort))
