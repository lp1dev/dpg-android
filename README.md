## Deterministic Password Generator - Android Version

DPG is a deterministic password generator that does not store data or keep state. Its output is based purely on user input. 

This version is based on https://github.com/w8rbt/dpg. 
Please read this [project's readme](https://github.com/w8rbt/dpg/blob/master/readme.md) before using DPG on Android, or to understand why DPG is different than the other password generators.


## Dowload the Latest Build

[Version 0.0.1](https://raw.githubusercontent.com/lp1dev/dpg-android/master/app/app-release.apk)
        
## Nota bene

Android API < 26 doesn't support PBKDF2WithHmacSHA512 encryption, 
that's why this app switches to PBKDF2WithHmacSHA1 if you are using a device with an older Android version than O.
