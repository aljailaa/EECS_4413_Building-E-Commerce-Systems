# Executive Summary
As a precursor to Project-B, you will create a TCP server using an agile development methodology. 
The server will adopt a custom protocol that was designed to expose a number of concepts, such as basic networking, json/xml marshaling, and cloud computing.
In addition, the project will address scalability issues, such as multithreading and throttling, as well as security concerns, such as vulnerabilities and authentication. 
# The Server 
- Developed as a POJO.
- Listens on a port of your choosing. 
- Is TCP-based and restful. Is multithreaded. 
- Has a log. Has a firewall. 
# The Protocol
- Is text based and case insensitive. 
- Its request and response strings are EOL-terminated. 
- Its request tokens are whitespace delimited (use regex to parse). 
- Has 7 methods: M1 thru M7. 

# The Methods
See attached ProjA.pdf
