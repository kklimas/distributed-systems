import socket;

serverPort = 9009
serverSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
serverSocket.bind(('', serverPort))
buff = []
while True:
    buff, address = serverSocket.recvfrom(1024)
print("received msg: " + str(buff, 'utf-8'))
