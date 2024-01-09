import socket
import datetime

BUFSIZE = 1024
def server(port:int):
    sock = socket.socket(type=socket.SOCK_DGRAM)
    sock.bind(("0.0.0.0",port))
    while True:
        data, addr = sock.recvfrom(BUFSIZE)
        data = datetime.datetime.now()
        data = f"{data}\n"
        sock.sendto(data.encode(),addr)

server(5555)