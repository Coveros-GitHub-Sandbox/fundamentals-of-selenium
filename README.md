# Fundamentals of Selenium

## Selenium

* Clone the sample project from git

	```git clone https://github.com/Coveros/fundamentals-of-selenium.git```

* Navigate to the selenium project directory
* Make directory lib
* Download the latest geckodriver

	```https://github.com/mozilla/geckodriver/releases```

* Import the project into your IDE
* Edit the sample test at src/test/java/GoogleIT.java
  * Uncomment out either line 14 or 16, depending on your system to add the correct geckodriver
* Execute the test

	```mvn clean verify```

* Make some changes and run from the IDE

## Selenified

* In your previously cloned project, navigate to the selenified project directory
* Execute the test

	```mvn clean verify -Dbrowser=Firefox```

* Review the results
* Import the project into your IDE
* Make some changes and run from the IDE

