## Deterministic Password Generator - Android Version

DPG is a deterministic password generator that does not store data or keep state. Its output is based purely on user input. 

This version is based on https://github.com/w8rbt/dpg. 
Please read this project's readme before using DPG.

## Nota bene

Android API < 26 doesn't support PBKDF2WithHmacSHA512 encryption, 
that's why this app switches to PBKDF2WithHmacSHA1 if you are using a device with an older Android version than O.
