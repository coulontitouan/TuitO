import socket

def client(host, port):
    sock = socket.socket()
    sock.connect((host, port))
    f = sock.makefile(mode="rw")
    while(True):
        txt = input(">> ")
        if txt == "quit":
            f.write("quit\n")
            f.flush()
            f.close()
            break
        elif txt in ["inc", "sub"]:
            f.write(f"{txt.strip()}\n")
            f.flush()
            chiffre = input('entre un chiffre ')
            f.write(f"{chiffre.strip()}\n")
        elif txt == "get":
            f.write("get\n")
        else:
            print("err")
        f.flush()
        print(f.readline(), end="")
    f.close()
    sock.shutdown(socket.SHUT_RDWR)
    sock.close()

client("localhost", 5555)