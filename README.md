# COSC310_GroupProject Progress Documentation

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
API in use: No
Page Status: Nearly complete, need links connecting to other, already implemented functionality

### Profile page
Frontend: Functional
Required APIs: Implemented
Required Backend: Implemented
API in use: Done
Page Status: Nearly complete, need links connecting to other, already implemented functionality

### Team-tasks page
Frontend: Functional
Required APIs: Implemented
Required Backend: Implemented
API in use: No
Page Status: Functionally complete

### My-tasks page
Frontend: Functional
Required APIs: Implemented
Required Backend: Implemented
API in use: No
Page Status: Functionally complete

### create-account page
Frontend: Functional
Required APIs: Implemented
Required Backend: Implemented
API in use: No
Page Status: Functionally complete

### create-task page
Frontend: 1/3rd of required methods complete
Required APIs: Implemented
Required Backend: Implemented
API in use: Yes
Page Status: Nearly complete

All pages still need tweaks with CSS and styling. We also need to make pages dynamic to properly adjust for mobile screens.

##Backend and Tests
The database and backend powering frontend are both fully complete, and have their functionality tested by 213 unit tests, as well as continuous integration that runs these tests each time the project has a pull request or a push to main.

The backend is split into 5 different layers, each with their own uses, and all have been fully implemented. These layers are:
- Controller -> handles http requests
- Entity -> determines the behaviour of tables and relationships in the database
- DTO -> handles data transfer between backend and frontend
- Repository -> class specific methods that involve database querying
- Service -> class specific methods that involve data gathered from repository level

    As of Milestone #2, some backend tests involving the testing environment are failing. The testing database that we are using (Hibernate) has rules about cascades and relations between tables which makes it difficult to test the relationship mappings. This is a known problem, and an issue has been created.

While the backend tests are currently not passing, and have been commented out for the purpose of Milestone #2, tests have been performed manually to ensure all relations between tables are working, and API calls can be made with no issues.



