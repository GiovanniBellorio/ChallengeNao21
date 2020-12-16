# -- doc

'''
Nome programma: conta_le_vocali.py
Descrizione   : programma che conta le vocali di una stringa
Autore        : Giovanni Bellorio
'''

# -- start
nome_programma = "conta_le_vocalii.py"
print("Inizio esecuzione", nome_programma)

# -- input
s = input("Inserisci una stringa: ")

vocali = 0

# -- execute
i = 0
while i < len(s):
    t = s.lower()
    if t[i] == 'a' or t[i] == 'e' or t[i] == 'i' or t[i] == 'o' or t[i] == 'u':
        vocali += 1
    i += 1

# -- output
print("La stringa inserita contiene:", vocali, "vocali.")

# -- end
print("Fine esecuzione", nome_programma)