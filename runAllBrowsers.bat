@echo off
start cmd /k mvn test -Dbrowser=chromium -Dcucumber.plugin="pretty,json:target/cucumber-reports/chromium.json"
start cmd /k mvn test -Dbrowser=firefox -Dcucumber.plugin="pretty,json:target/cucumber-reports/firefox.json"
start cmd /k mvn test -Dbrowser=webkit -Dcucumber.plugin="pretty,json:target/cucumber-reports/webkit.json"
start cmd /k mvn test -Dbrowser=edge -Dcucumber.plugin="pretty,json:target/cucumber-reports/edge.json"
start cmd /k mvn test -Dbrowser=chrome -Dcucumber.plugin="pretty,json:target/cucumber-reports/chrome.json"
pause
