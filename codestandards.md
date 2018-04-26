# Code standards
## Introduction
For this project we will be using the [Sun code conventions](http://www.oracle.com/technetwork/java/codeconvtoc-136057.html).
We use Codestyle in our IDE's and CI environment to enforce this code standard.
 However this code standard is from 1999 and is outdated, which is why we will make some adjustments to it.
 
 All changes in the checkstyle.xml file are marked with comment that starts with 'CHANGED: '.
 
## Adjustments
 - Disabled variable shadowing check since it causes for awkward naming of variables.
 - Increased the max line length from 80 to 120 characters since screen sizes have increased since 1999
 - Disabled DesignForExtension since in our opinion there was no good reason to enforce this