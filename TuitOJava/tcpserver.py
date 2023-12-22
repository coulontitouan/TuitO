import socket


class Server:
    def __init__(self) -> None:
        self.counter = 0

    def mainServer(self, port):
        sock = socket.socket()
        sock.bind(("0.0.0.0", port))
        sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR,1)
        sock.listen(10)
        while True:
            cli, addr = sock.accept()
            sess = Session(self,cli)
            sess.mainSession()

class Session:
    def __init__(self, server, sock) -> None:
        self.server = server
        self.socket = sock
        self.file = sock.makefile(mode="rw")
    
    def mainSession(self):
        while True:
            line = self.file.readline().strip()
            if "get" in line:
                self.file.write(f"val {self.server.counter} \n")
                self.file.flush()
            elif "quit" in line:
                self.file.write("quit\n")
                self.file.flush()
                break
            elif "inc" in line or "sub" in line:
                if "inc" in line:
                    self.server.counter += 1
                else:
                    self.server.counter -= 1
                self.file.write(f"val {self.server.counter} \n")
                self.file.flush()
            elif "add" in line:
                line = self.file.readline().strip()
                if line.isnumeric():
                    self.server.counter -= int(line)
                    self.file.write(f"val {self.server.counter} \n")
                else:
                    self.file.write("pas un nombre")
                self.file.flush()
            else:
                self.file.write("err\n")
                self.file.flush()
        self.file.close()
        self.socket.shutdown(socket.SHUT_RDWR)
        self.socket.close()

Server().mainServer(5555)
