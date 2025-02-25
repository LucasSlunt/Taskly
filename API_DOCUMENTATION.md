api_documentation_content = """# API Documentation

# API Documentation

This document provides an overview of the API endpoints available in the system. Each section corresponds to a controller and details the available routes, methods, parameters, and expected responses.

## Introduction

Welcome to the **Task Manager API**. This API allows developers to manage admins, teams, tasks, and assignments within the system. It is designed to be RESTful, using standard HTTP methods and response codes.

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

---

## 1. AdminController

**Base URL:** `/api/admin`

### Endpoints
- **Create Admin:** `POST /`
    - Params: `name`, `email`
- **Delete Admin:** `DELETE /{adminId}`
- **Modify Admin Name:** `PUT /{adminId}/update-name`
    - Params: `newName`
- **Modify Admin Email:** `PUT /{adminId}/update-email`
    - Params: `newEmail`
- **Create Team Member:** `POST /team-member`
    - Params: `name`, `email`
- **Modify Team Member Name:** `PUT /team-member/{teamMemberId}/update-name`
    - Params: `newName`
- **Modify Team Member Email:** `PUT /team-member/{teamMemberId}/update-email`
    - Params: `newEmail`
- **Delete Team Member:** `DELETE /team-member/{teamMemberId}`
- **Assign Team Member to a Team:** `POST /team-member/{teamMemberId}/assign-to-team/{teamId}`
- **Promote Team Member to Admin:** `POST /team-member/{teamMemberId}/promote`
- **Lock a Task:** `PUT /tasks/{taskId}/lock`
- **Unlock a Task:** `PUT  /tasks/{taskId}/unlock`

---

## 2. AuthInfoController

**Base URL:** `/api/auth-info`

### Endpoints
- (Currently no documented endpoints)

---

## 3. IsAssignedController

**Base URL:** `/api/assignments`

### Endpoints
- **Assign a Team Member to a Task:** `POST /{teamMemberId}/task/{taskId}`
- **Unassign a Team Member from a Task:** `DELETE /{teamMemberId}/task/{taskId}`
- **Check if Assigned:** `GET /{teamMemberId}/task/{taskId}`

---

## 4. IsMemberOfController

**Base URL:** `/api/memberships`

### Endpoints
- **Add a Member to a Team:** `POST /{teamMemberId}/team/{teamId}`
- **Remove a Member from a Team:** `DELETE /{teamMemberId}/team/{teamId}`
- **Check if a Member is assigned to a Team:** `GET /{teamMemberId}/team/{teamId}`

---

## 5. TaskController

**Base URL:** `/api/tasks`

### Endpoints
- **Notify Members of a Task:** `POST /{taskId}/notify`
    - Param: `message`

---

## 6. TeamController

**Base URL:** `/api/teams`

### Endpoints
- **Create a Team:** `POST`
    - Params: `teamName`, `teamLeadId`
- **Delete a Team:** `DELETE /{teamId}`
- **Change Team Lead:** `PUT /{teamId}/change-lead`
    - Params: `teamLeadId`
- **Get Team Members:** `GET /{teamId}/members`

---

## 7. TeamMemberController

**Base URL:** `api/tasks`

### Endpoints
- **Create a Task:** `POST`
    - Params: `title`, `description`, `isLocked`, `status`, `teamId`
- **Delete a Task:** `DELETE /{taskId}`
- **Edit a Task:** `PUT /{taskId}`
    - **Request Body:** (TaskDTO, JSON)
    ```json
    {
        "title": "New Task Title",
        "description": "Updated Description",
        "isLocked": false,
        "status": "In Progress",
        "dueDate": "2025-03-01"
    }
- **Assign Member to a Task:** `POST /{taskId}/assign/{teamMemberId}`
- **Change Password:** `POST /team-members/{teamMemberId}/change-password`
    Params: `oldPassword`, `newPassword`

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
    - `title` (string, required): The title of the task
    - `description` (string, required): A description of the task
    - `isLocked` (boolean, required): Indicates if the task is locked
    - `status` (string, required): The status of the task (e.g. "To-Do", "In Progress", "Done")
    - `dueDate` (string, optional): The due date of the task in YYYY-MM-DD format

- **TeamDTO**
    - `id` (integer, required): The unique ID of the team  
    - `name` (string, required): The name of the team  
    - `teamLeadId` (integer, required): The ID of the team leader  

- **TeamMemberDTO**
    - `id` (integer, required): The unique ID of the team member  
    - `name` (string, required): The name of the team member  
    - `email` (string, required): The email of the team member  

- **IsAssignedDTO**
    - `taskId` (integer, required): The ID of the task  
    - `teamMemberId` (integer, required): The ID of the team member  
    - `isAssigned` (boolean, required): Indicates whether the team member is assigned to the task

---

## **Request and Response Examples**

Below are examples of some API requests and responses.

---


---

### **Best Practies**


---

## Error Codes
Code    | Description
_______________________________________________________
200     | OK - Request succeeded
204     | No Content - Resource deleted
400     | Bad Request - Invalid input
404     | Not Found - Resource not found
500     | Internal Server Error - Unexpected error

---

## Example Error Response
```json
{
    "message": "Resource not found"
}