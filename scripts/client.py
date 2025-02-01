import socket

sock = socket.socket()

sock.connect(('localhost',8888))


while True:
    try:
        msg = sock.recv(10000000000)
        print(len(msg))
    except KeyboardInterrupt:
        print ("exiting")
        sock.close()
        exit(0)

