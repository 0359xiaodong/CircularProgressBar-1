CircularProgressBar
===== 
![image](demo.gif)

##Importing to your project
Add this dependency to your build.gradle file:
```java
dependencies {
    compile 'com.hrules:circularprogressbar:2.0.0'
}
```
##Usage
CircularProgressBar:
```java
CircularProgressBar circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
circularProgressBar.setPercentValue(.75f, true); // 75% with animation

// ...

circularProgressBar.reset(false); // no animation
```

CircularProgressIndeterminateBar:
```java
CircularProgressIndeterminateBar circularProgressIndeterminateBar = (CircularProgressIndeterminateBar) findViewById(R.id.circularProgressIndeterminateBar);
circularProgressIndeterminateBar.start(); 

// ...

circularProgressIndeterminateBar.stop(); 
```

Developed by
-------
Héctor de Isidro - hrules6872 [![Twitter](http://img.shields.io/badge/contact-@h_rules-blue.svg?style=flat)](http://twitter.com/h_rules)

License
-------
    Copyright 2015 Héctor de Isidro - hrules6872

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
