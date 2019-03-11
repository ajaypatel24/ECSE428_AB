# Test automation of sending an image attachment via Gmail

### SOME IMPORTANT REQUIRMENTS 

* The drive account included in the package must have storage at 14.9GB for the error flow to work (and below 14.95gb to ensure normal flows do not fail).
* Extract the Quebec image file in images. It could not be uploaded as a jpg as it was too large. If you do not extract it, the error flow will fail
* Tested on IntelliJ ultimate, cannot verify whether it will work the same on Eclipse or VS Code

### DEPENDENCIES

* Build with Maven (simply run as cucumber test)
* Cucumber Version 2.3.1
* Junit Version 4.12
* Selenium Version 3.141.59
* Chromedriver (OS Dependent, you must [download](http://chromedriver.chromium.org/downloads) this in your own, the one in this project is for macOS). NOTICE: If you are on windows, the extension will be .exe ad you must modify the reference in the scenariodefinition class!


```
  <dependencies>
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-java</artifactId>
            <version>2.3.1</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-junit</artifactId>
            <version>2.3.1</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>3.141.59</version>
        </dependency>

```
