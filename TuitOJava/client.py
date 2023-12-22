import socket

BUFSIZE = 1024
def client(host:str,port:int):
    sock = socket.socket(type=socket.SOCK_DGRAM)
    addr = (host,port)
    sock.sendto(b"",addr)
    data = sock.recv(BUFSIZE)
    print(data.decode(), end="")

client("localhost",5555)