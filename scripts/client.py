import socket

video_sock = socket.socket()
md_sock = socket.socket()


md_sock.connect(('localhost',8000))
video_sock.connect(('localhost',8888))
import cv2
# md_sock.setblocking(True)
import numpy as np
while True:
    try:
        md = md_sock.recv(8)
        # print(md)
        if(len(md)!=8):
            print("wrong length")
            continue

        w = int.from_bytes(md[0:4],'big')
        h = int.from_bytes(md[4:8],'big')
        # print("w %d, h %d" %(w,h))
        msg = video_sock.recv(10000000000)
        if(len(msg)!=w*h*4):
            print("oops wrong frame")
        else:
            arr = np.frombuffer(msg,dtype=np.uint8).reshape((h,w,4))[:,:,[2,1,0,3]]
            cv2.imshow("stream",np.flip(arr,axis=0))
            

        # print(len(msg))
    except KeyboardInterrupt:
        print ("exiting")
        video_sock.close()
        exit(0)
    cv2.waitKey(1)

#1639680