barcodefuzzer
=============
A fairly basic fuzzer designed to generate barcodes based on user input and random strings.

Features:
  * Supports QR codes and Code128 barcodes
  * Generate random strings based on any combination of four character sets: uppercase, lowercase, digits, and special chars
  * Save successful barcodes and their associated strings
  
TODO:
  * QR codes have a maximum length depending on the characters used, still need to add logic for limiting length based on selected char sets
  * Allow for custom character spaces
  * Add option for random mutation of user input strings
  * Add regex string generation for smarter SQL fuzzing
  * Smart character substitions like ASCII code for special SQL characters
  * Add dynamic resizing for different devices.  Currently sized for my Galaxy S3's screen.
  
Challenges:
  * Spent a majority of my time just learning how to create an Android app.
  * Originally made it a two activity app, but that was clunky so I spent a lot of time streamlining the interface
    and making it just update the barcode image on a single screen.
  
