#!/usr/bin/python
# -*- coding: utf-8 -*-

import socket
import time


HOST = "127.0.0.1"  # The server's hostname or IP address
PORT = 5050        # The port used by the server

for i in range(1):
	with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
	    s.connect((HOST, PORT))
	    message = "res" + "_pho"
	    s.send(message.encode("utf-8"))
	    time.sleep(0.2)