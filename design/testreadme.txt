sehr sehr sehr frackelig:

java Servertest startet den Server, welcher einen Channel "Virtuelle Konferenz" 
anlegt

java Clienttest foo - meldet "foo" beim Server an, betritt den Channel und sendet
"hello world". Danach liest Clienttest von der Konsole und sendet die eingegebene
Nachricht an alle anderen Clients

weitere Clients mit java Clienttest foo2 etc anmelden

crashed sofort, wenn: 
-ein Client gekillt wird
-ein Client einen null-String sendet (einfach nur return an der Console gedrückt)
- ...


btw: evtl. in Client::Client.java noch IP anpassen und neu compilieren

have fun
