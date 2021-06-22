# SWE573-Project
This repository is prepared for educational purpose in the scope of SWE-573 Software Development Practice Course. The repository includes project code, documentation and additional resources for backend part of project. 

Project description,requirements, user scenarios, mockups and detailed information is placed at Wikipage of project.

 * [WikiPage](https://github.com/ugurtosun/SWE573-Project/wiki)

For Angular front-end project please visit repository:

 * [Front-end Repository](https://github.com/ugurtosun/SWE573-Project-frontend)

Project is running at AWS Server with URL: (http://ec2-18-191-214-254.us-east-2.compute.amazonaws.com/)

### Important Bug

Unfortunately, project has a crucial known bug. Because of CROSS Origin Policy of Spring Boot requests are not handled with default browser. 

To test project, you will need run chrome with command; Otherwise requests will be blocked by Spring Security.

```
google-chrome --disable-web-security --user-data-dir="/"
```

