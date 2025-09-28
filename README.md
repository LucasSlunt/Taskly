# COSC310_GroupProject

## Frontend
This project has 8 different pages, each on varying degrees of completeness.
Summary of progress on each page:

### Login page
Frontend: Functional
Required APIs: Implemented
Required Backend: Implemented
API in use: Done
Page Status: Functionally complete

### Home page
Frontend: Functional
Required APIs: Implemented
Required Backend: Implemented
API in use: Done
Page Status: Functionally complete

### Profile page
Frontend: Functional
Required APIs: Implemented
Required Backend: Implemented
API in use: Done
Page Status: Functionally complete

### Team-tasks page
Frontend: Functional
Required APIs: Implemented
Required Backend: Implemented
API in use: Done
Page Status: Functionally complete

### My-tasks page
Frontend: Functional
Required APIs: Implemented
Required Backend: Implemented
API in use: Done
Page Status: Functionally complete

### create-account page
Frontend: Functional
Required APIs: Implemented
Required Backend: Implemented
API in use: Done
Page Status: Functionally complete

### create-task page
Frontend: Functional
Required APIs: Implemented
Required Backend: Implemented
API in use: Yes
Page Status: Functionally complete

### notfications
Frontend: Functional
Required APIs: Implemented
Required Backend: Implemented
API in use: Yes
Page Status: Functionally complete

##Backend and Tests
The database and backend powering frontend are both fully complete, and have their functionality tested by 213 unit tests, as well as continuous integration that runs these tests each time the project has a pull request or a push to main.

The backend is split into 5 different layers, each with their own uses, and all have been fully implemented. These layers are:
- Controller -> handles http requests
- Entity -> determines the behaviour of tables and relationships in the database
- DTO -> handles data transfer between backend and frontend
- Repository -> class specific methods that involve database querying
- Service -> class specific methods that involve data gathered from repository level


## Coverage Testing Report
Extensively detailed coverage report can be found by opening "COSC310_GroupProject\coverageTesting\jacoco\index.html" in a browser.
Low coverage percentage in TaskManagerApplication.java is due to it being a single class to hold the main method.
Low coverage percentage in Controller and Entity classes is due to setters, getters, and error throwing/printing code which is simple enough that it is unnecessary and time-consuming to test.

