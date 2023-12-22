import socket
import datetime
import os
os.environ["USER"]
BUFTIME = 1024
def server(port):
    sock = socket.socket(type=socket.SOCK_DGRAM)
    # sock.bind(('192.168.43.8', port))
    sock.bind(('192.168.43.54', port))
    while True:
        data, addr = sock.recvfrom(BUFTIME)
        if data.decode() == "user":
            data = os.environ["USER"]
        elif data.decode() == "date":
            data = datetime.datetime.now()
        else:
            data = input()
        data = data.encode() + b"\n"
        sock.sendto(data, addr)

server(5555)