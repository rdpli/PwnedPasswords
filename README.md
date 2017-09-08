# PwnedPasswords
Checks for hacked passwords using have I been pwned API

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
