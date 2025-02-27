# API Documentation

This document provides an overview of the API endpoints available in the system. Each section corresponds to a controller and details the available routes, methods, parameters, and expected responses.

## Introduction

Welcome to the **Task Manager API**. This API allows developers to manage admins, teams, tasks, and assignments within the system. It is designed to be RESTful, using standard HTTP methods and response codes.

A REST API allows different systems to communicate over the internet using standard HTTP methods. Each request interacts with the API's resources, such as "admins" and "tasks".

### Base URL
All API requests should be made to the following base URL (Spring Boot's default settings): `http://localhost:8080` 

---

## Table of Contents
1. [AdminController](#admincontroller)
2. [AuthInfoController](#authinfocontroller)
3. [IsAssignedController](#isassignedcontroller)
4. [IsMemberOfController](#ismemberofcontroller)
5. [TaskController](#taskcontroller)
6. [TeamController](#teamcontroller)
7. [TeamMemberController](#teammembercontroller)
8. [Path Variables](#path-variables)
9. [DTO References](#dto-references)
10. [Request and Response Examples](#request-and-response-examples)
11. [Best Practices](#best-practices)
12. [Error Codes](#error-codes)
13. [Example Error Response](#example-error-response)

---

## **AdminController**

**Base URL:** `/api/admin`

### **Endpoints**

- **Create Admin:** `POST /`
    - **Parameters:**  
        - `name` (string, required): The name of the new admin.  
        - `email` (string, required): The email of the new admin.  
    - **Description:** Creates a new admin in the system.

- **Delete Admin:** `DELETE /{adminId}`
    - **Parameters:**
        - `adminId` (integer, required): The unique ID of the admin to be deleted.  
    - **Description:** Removes an admin from the system permanently.

- **Modify Admin Name:** `PUT /{adminId}/update-name`
    - **Parameters:**  
        - `newName` (string, required): The updated name for the admin.  
    - **Description:** Updates the admin's **name** field in the database.

- **Modify Admin Email:** `PUT /{adminId}/update-email`
    - **Parameters:**  
        - `newEmail` (string, required): The updated email for the admin.  
    - **Description:** Updates the admin's **email** field in the database.

- **Create Team Member:** `POST /team-member`
    - **Parameters:**  
        - `name` (string, required): The name of the new team member.  
        - `email` (string, required): The email of the new team member.  
    - **Description:** Adds a new team member to the system.

- **Modify Team Member Name:** `PUT /team-member/{teamMemberId}/update-name`
    - **Parameters:**  
        - `newName` (string, required): The updated name for the team member.  
    - **Description:** Updates the **name** field of the specified team member in the database.

- **Modify Team Member Email:** `PUT /team-member/{teamMemberId}/update-email`
    - **Parameters:**  
        - `newEmail` (string, required): The updated email for the team member.  
    - **Description:** Updates the **email** field of the specified team member in the database.

- **Delete Team Member:** `DELETE /team-member/{teamMemberId}`
    - **Parameters:**
        - `teamMemberId` (integer, required): The unique ID of the team member to be deleted.  
    - **Description:** Removes a team member from the system permanently.

- **Assign Team Member to a Team:** `POST /team-member/{teamMemberId}/assign-to-team/{teamId}`
    - **Parameters:**  
        - `teamMemberId` (integer, required): The ID of the team member to assign.  
        - `teamId` (integer, required): The ID of the team to assign them to.  
    - **Description:** Assigns a team member to a specified team.

- **Promote Team Member to Admin:** `POST /team-member/{teamMemberId}/promote`
    - **Parameters:**  
        - `teamMemberId` (integer, required): The ID of the team member to be promoted.  
    - **Description:** Upgrades a team member to an admin role, granting them full administrative privileges.

- **Lock a Task:** `PUT /tasks/{taskId}/lock`
    - **Parameters:**  
        - `taskId` (integer, required): The ID of the task to lock.  
    - **Description:** Locks a task to prevent any modifications. Once locked, updates and deletions are restricted.

- **Unlock a Task:** `PUT /tasks/{taskId}/unlock`
    - **Parameters:**  
        - `taskId` (integer, required): The ID of the task to unlock.  
    - **Description:** Unlocks a task, allowing updates and modifications.

---

## **AuthInfoController**

**Base URL:** `/api/auth-info`

### Endpoints
- (Currently no documented endpoints)

---

## **IsAssignedController**

**Base URL:** `/api/assignments`

### Endpoints
- **Assign a Team Member to a Task:** `POST /{teamMemberId}/task/{taskId}`
    - **Description:** Assigns a team member to a task.

- **Unassign a Team Member from a Task:** `DELETE /{teamMemberId}/task/{taskId}`
    - **Description:** Unassigns a team member from a task.

- **Check if Assigned:** `GET /{teamMemberId}/task/{taskId}`
    - **Description:** Returns a boolean of whether a team member is currently assigned to a task.

---

## **IsMemberOfController**

**Base URL:** `/api/memberships`

### Endpoints
- **Add a Member to a Team:** `POST /{teamMemberId}/team/{teamId}`
    - **Description:** Adds a team member to a team.

- **Remove a Member from a Team:** `DELETE /{teamMemberId}/team/{teamId}`
    - **Description:** Removes a team member from a team.

- **Check if a Member is assigned to a Team:** `GET /{teamMemberId}/team/{teamId}`
    - **Description:** Returns a boolean of whether a team member is assigned to a team. 

---

## **TaskController**

**Base URL:** `/api/tasks`

### Endpoints
- **Notify Members of a Task:** `POST /{taskId}/notify`
    - **Parameters:**:         
        - `message` (string, required): The message sent to all team members.
    - **Description:** Not yet implemented in backend. 

---

## **TeamController**

**Base URL:** `/api/teams`

### Endpoints
- **Create a Team:** `POST`
    - **Parameters:**: 
        - `teamName` (string, required): The name of the new team.
        - `teamLeadId` (integer, required): The ID of the team lead of the new team.
            - This parameter **must be included** in the request, but it can be set to `null` if no team lead is assigned.
    - **Description:** Creates a team in the database.

- **Delete a Team:** `DELETE /{teamId}`
    - **Description:** Deletes a team from the database.
    
- **Change Team Lead:** `PUT /{teamId}/change-lead`
    - **Parameters:**: 
        - `teamLeadId` (integer, required): The ID of the team member that is becoming the team lead.
    - **Description:** Updates the team lead for a specified team by assigning a different team member.

- **Get Team Members:** `GET /{teamId}/members`
    - **Description:** Returns a list of every team member in a team. Each list item contains the team member's ID, name, and email.

---

## **TeamMemberController**

**Base URL:** `/api/tasks`

### Endpoints
- **Create a Task:** `POST`
    - **Parameters:**:
        - `title` (string, required): The title of the new task.
        - `description` (string, optional): A description of the task.
        - `isLocked` (boolean, optional): Whether the task is locked (`true`) or unlocked (`false`). Defaults to `null` if not provided.
        - `status` (string, optional): The status of the task (e.g., "To-Do", "In Progress", "Done"). Defaults to `null` if not provided.
        - `teamId` (integer, required): The ID of the team being assigned to the task.

- **Delete a Task:** `DELETE /{taskId}`
    - **Description:** Deletes a task from the database.

- **Edit a Task:** `PUT /{taskId}`
    - **Description:** Updates the details of a task.
    - **Request Body:** (TaskDTO, JSON)
    ```json
    {
        "title": "New Task Title",
        "description": "Updated Description",
        "isLocked": false,
        "status": "In Progress",
        "dueDate": "2025-03-01"
    }
    ```
    - **Note:** Only the provided fields will be updated. Missing fields will remain the same.

- **Assign Member to a Task:** `POST /{taskId}/assign/{teamMemberId}`
    - **Description:** Assigns a member (admin or team member) to a task.

- **Change Password:** `POST /team-members/{teamMemberId}/change-password`
    - **Parameters:**
        - `oldPassword` (string, required): The current password of the user.
        - `newPassword` (string, optional): The new password of the user.
    - **Description:** Updates the password field for a team member.

---

## **Path Variables**

- **AdminController**
    - `{adminId}` (integer, required): The ID of the admin
    - `{teamMemberId}` (integer, required): The ID of the team member
    - `{teamId}` (integer, required): The ID of the team
    - `{taskId}` (integer, required): The ID of the task
- **IsAssigned Controller**
    - `{teamMemberId}` (integer, required): The ID of the team member
    - `{taskId}` (integer, required): The ID of the task
- **IsMemberOfController**
    - `{teamMemberId}` (integer, required): The ID of the team member
    - `{teamId}` (integer, required): The ID of the team
- **TaskController**
    - `{taskId}` (integer, required): The ID of the task
- **TeamController**
    - `{teamId}` (integer, required): The ID of the team
- **TeamMemberController**
    - `{taskId}` (integer, required): The ID of the task
    - `{teamMemberId}` (integer, required): The ID of the team member

---

## **DTO References**

- **TaskDTO**
    - `title` (string, required): The title of the task.
    - `description` (string, optional): A description of the task.
    - `isLocked` (boolean, optional): Whether the task is locked. Defaults to `null` if not provided.
    - `status` (string, optional): The task status (e.g., "To-Do", "In Progress", "Done"). Defaults to `null` if not provided.
    - `dueDate` (string, optional): The due date of the task in `YYYY-MM-DD` format.

---

## **Request and Response Examples**

Below are some examples of API requests and responses.

---

#### **CreateAdmin**
```http
POST /api/admin?name=John Doe&email=john.doe@example.com
```

- **Request Parameters**
    - `name` (string, required): The name of the new admin.
    - `email` (string, required): The email of the new admin.

- **Request Response**
```json 
{
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
}
```

#### **ModifyAdminName**
```http
PUT /api/admin/1/update-name?newName=Jane Doe
```

- **Request Parameters**
    - `adminId` (integer, required): The ID of the admin changing the members name.
    - `newName` (string, required): The new name of the member.

- **Response Body**
```json
{
    "id": 1,
    "name": "Jane Doe",
    "email": "john.doe@example.com"
}
```

#### **DeleteAdmin**
```http
DELETE /api/admin/1
```

- **Notes:** 
    - No request body required. 
    - No response body returned.


#### **CreateTask**
```http
POST /api/tasks?title=The Task Title&description=This is the description, it could get long.&isLocked=false&status=To-Do&teamId=3
```

- **Request Parameters**
    - `title` (string, required): The title of the new task.
    - `description` (string, required): The description of the new task.
    - `isLocked` (boolean, required): A boolean of whether the new task is locked or not.
    - `status` (string, required): A string of the current status of the task.
    - `teamId` (integer, required): The ID of the team being assigned to the task.

- **Response Body**
```json
{
    "taskId": 4,
    "title": "Design Database Schema",
    "description": "Create the database structure.",
    "isLocked": false,
    "status": "To-Do",
    "teamId": 3
}
```

---

## **Request and Response Testing Examples**

Below are examples of some API requests and responses using Cypress.

---

- **AdminController**
    - **CreateAdmin**
    ```javascript
    cy.request({
        method: 'POST',
        url: 'http://localhost:8080/api/admin',
        qs: {
            name: 'John Doe',
            email: 'john@example.com'
        }
    }).then((response) => {
        expect(response.status).to.eq(200);
        expect(response.body.name).to.eq('John Doe');
    });
    ```

    - **DeleteAdmin**
    ```javascript
    cy.request({
        method: 'DELETE',
        url: 'http://localhost:8080/api',
        qs: {
            adminId: 1
        }
    }).then((response) => {
        expect(response.status).to.eq(204);
    });
    ```

- **TeamMemberController**
    - **Assign Member To Task**
    ```javascript
    const taskId = 5;
    const teamMemberId = 7;

    cy.request({
        method: 'POST',
        url: 'http://localhost:8080/api/tasks/${taskId}/assign/${teamMemberId}'
    }).then((response) => {
        expect(response.status).to.eq(200);
        expect(response.body).to.have.property('taskId', taskId);
        expect(response.body).to.have.property('teamMemberId', teamMemberId);
    });
    ```

---

## **Best Practices**

Follow these practices to ensure efficiency and accuracy with all API requests.

### **Correct HTTP Methods**
- **GET**: Used to retrieve resources. Cannot modify data.
- **POST**: Used to create resources.
- **PUT**: Used to update existing resources.
- **DELETE**: Used to delete resources.

### **Request Formatting**
- Ensure that required parameters are included in the request body or URL path.
- Avoid sending unnecessary fields in API requests.

---

## Error Codes
| Code      | Description                               |
|-----------|-------------------------------------------|
| 200       | OK - Request succeeded                    |
| 201       | Created - Resource successfully created   |
| 204       | No Content - Resource deleted             |
| 400       | Bad Request - Invalid input               |
| 401       | Unauthorized - Authentication required    |
| 403       | Forbidden - Insufficient permissions      |
| 404       | Not Found - Resource not found            |
| 500       | Internal Server Error - Unexpected error  |

---

## Example Error Response
```json
{
    "message": "Resource not found",
    "error": "NotFoundException",
    "statusCode": 404
}