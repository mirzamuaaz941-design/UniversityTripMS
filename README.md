# University Trip Management System

Course: Software Design & Analysis
Group: Mirza Muaz Baig (24P-0684)_only, Rizwan Akbar 24p-0722 (Drop The Course)
Section: BCS 4C

## How to Compile and Run

Open terminal inside the src folder, then run:

javac -d . model/*.java service/*.java gui/*.java Main.java
java Main

## Login Credentials
| Role    | Email            | Password |
|---------|------------------|----------|
| Student | ali@fast.edu     | 123      |
| Student | sara@fast.edu    | 123      |
| Teacher | umar@fast.edu    | 123      |
| Admin   | admin@fast.edu   | admin    |

## Features
- Role-based login
- Student: browse and enroll in trips
- Teacher: view trips, submit trip requests
- Admin: approve/reject trips, assign vehicles, add new trips

## Design Patterns
- Singleton: DataStore
- MVC: model / gui / service separation
- State: Trip lifecycle (DRAFT to COMPLETED)
