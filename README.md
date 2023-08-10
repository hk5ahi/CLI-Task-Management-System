# CLI-Task-Management-System

Entities:

  1-User (with subclasses Employee, Manager, Supervisor)
  2-Task
  3-Comment
  4-TaskHistory
Relationships:

  Employee (1) - (n) Task
  Manager (1) - (n) Task
  Supervisor (1) - (n) Task
  Task (1) - (n) Comment
  Comment (n) - (1) User (createdBy)
  Comment (n) - (1) Task
  Task (1) - (n) TaskHistory
  User (1) - (n) TaskHistory
