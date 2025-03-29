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
API in use: No
Page Status: Almost complete just needs to use the api

All pages still need tweaks with CSS and styling. We also need to make pages dynamic to properly adjust for mobile screens.

##Backend and Tests
The database and backend powering frontend are both fully complete, and have their functionality tested by 213 unit tests, as well as continuous integration that runs these tests each time the project has a pull request or a push to main.

The backend is split into 5 different layers, each with their own uses, and all have been fully implemented. These layers are:
- Controller -> handles http requests
- Entity -> determines the behaviour of tables and relationships in the database
- DTO -> handles data transfer between backend and frontend
- Repository -> class specific methods that involve database querying
- Service -> class specific methods that involve data gathered from repository level

## Known Issues

### Issue #224: TeamLead Does not stay teamLead when roles swap 
When swaping roles if user is team lead they do not remain team lead. This happens due to a userId change when roles change(this is intended behavior), but the service method on the backend does not check if user is a teamLead to bring that over to the new user (admin or teamMember) 
### Solution
Fix backend service logic to check if user is team lead if so ensure to set the newly created (admin or teamMember) to teamLead.

### Issue #244 Crash When Click Logo In Header On State Pages
If you are on a page that relies on the state from another page ie teamTasks, viewTasks, editTask. And you click the logo in the header the web site bugs out as this gets rid of the state, its href = '#'. This causes the state to be undefined which causes issues.
### Solution
Either get rid of the href on the logo or make it link to '/home'

### Issue #207 On Task Creation Due Date Does Not Show
When a task is created due date does not go into the due date column of the data base it goes into expect completion date. This is wrong and cause by a typo in the service for createTask
### Solution
Fix typo in service layer of backend for create task. It should set response.getDueDate to task.dueDate not task.expectCompletionDate


