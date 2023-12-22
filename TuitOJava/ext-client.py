import socket
import sys

BUFSIZE = 1024
def client(host, port, data:str):
    sock = socket.socket(type=socket.SOCK_DGRAM)
    addr = (host, port)
    sock.sendto(data.encode(), addr)
    data = sock.recv(BUFSIZE)
    print(data.decode(), end="")

# client('192.168.43.54', 5555)
client(sys.argv[1], 5555, sys.argv[2])