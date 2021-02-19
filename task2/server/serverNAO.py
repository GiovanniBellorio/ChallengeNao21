# -- doc
"""
Nome programma: serverNAO.py
Descrizione   : server per il controllo dei comandi di spostamento testa di NAO.
Autore        : NaoNexus Team
"""

import socket

# -- function
def on_new_client(clientSocket, msg):
    message = clientSocket.recv(1024).decode("utf-8")
    message = message.split("_")

    if "nao" in message:
        print("Request from NAO:", message)
        if msg[0] != "":
            clientSocket.sendall(msg[0].encode("utf-8"))
            print("Send to NAO:", msg[0])
        msg = ["",""]
    elif "pho" in message:
        msg[0] = message[0]
        print("Recevive from APP:", message)
    else:
        msg = ["",""]
    
    return msg
   
# -- input
s    = socket.socket()                                 # Create a socket object
#HOST = "192.168.0.107"                                 # Get local machine name
HOST = "127.0.0.1"
PORT = 5050                                            # Reserve a port for your service

print("HOST:", HOST)
print("PORT:", PORT) 
print ("Server started!")
print ("Waiting for clients...")

s.bind((HOST, PORT))                                   # Bind to the port
s.listen()                                             # Now wait for client connection

msg = ["",""]

# -- execute
while True:
    c, addr = s.accept()                               # Establish connection with client
    msg = on_new_client(c, msg)
    c.close()
