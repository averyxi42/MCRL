import socket

action_sock = socket.socket()

action_sock.connect(('localhost',8083))
# md_sock.setblocking(True)
import time
while True:
   
    try:
        msg = input("what to send:")
        action_sock.send((msg+('\n')).encode('ascii'))

        # print(len(msg))
    except KeyboardInterrupt:

        exit(0)

#1639680