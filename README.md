# PwnedPasswords
Pwned Passwords are hundreds of millions of real world passwords exposed in data breaches. This exposure makes them unsuitable for ongoing use as they're at much greater risk of being used to take over other accounts.
This library checks for hacked passwords using [Pwned Passwords](https://haveibeenpwned.com/Passwords) service provided by [Have I been pwned?](https://haveibeenpwned.com/About).
# Table of content
- [Download](#download)
- [Usage](#usage)
- [Links](#links)
## Download
[![](https://jitpack.io/v/RandomAdversary/PwnedPasswords.svg)](https://jitpack.io/#RandomAdversary/PwnedPasswords)

### Maven
To use it in your Maven build add:
```xml
  <repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
  </repositories>
```

and the dependency:

```xml
	<dependency>
	    <groupId>com.github.RandomAdversary</groupId>
	    <artifactId>PwnedPasswords</artifactId>
	    <version>1.0</version>
	</dependency>
```
### Gradle
Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Add the dependency
```
	dependencies {
	        compile 'com.github.RandomAdversary:PwnedPasswords:1.0'
	}

```


## Usage
PwnedPasswords.pwn() will send the password directly to the api.
```java
        String myPassword = "admin";
        boolean pwned = PwnedPasswords.pwn(myPassword);
        if(pwned){
            System.out.println( "Oh no — pwned!");
        }
        else {
            System.out.println("Good news — no pwnage found!");
        }
```
PwnedPasswords.hashAndPwn() will send hash(SHA1) instead of plaintext to the api.

```java
        String myPassword = "admin";
        boolean pwned = PwnedPasswords.hashAndPwn(myPassword);
        if(pwned){
            System.out.println( "Oh no — pwned!");
        }
        else {
            System.out.println("Good news — no pwnage found!");
        }
```

# Links
* [Pwned Passwords web service](https://haveibeenpwned.com/Passwords)
* [Pwned Passwords API](https://haveibeenpwned.com/API/v2#PwnedPasswords)
