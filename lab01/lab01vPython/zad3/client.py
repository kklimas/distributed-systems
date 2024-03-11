import socket

number = 300
msg_bytes = (number).to_bytes(4, byteorder='big')

serverIP = "127.0.0.1"
serverPort = 9876
print(f'Sending number {number}')
client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
client.sendto(msg_bytes, (serverIP, serverPort))

buff = client.recvmsg(100)
print(int.from_bytes(buff[0], byteorder='big'))
