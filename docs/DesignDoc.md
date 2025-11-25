---
geometry: margin=1in
---
# PROJECT Design Documentation

> _The following template provides the headings for your Design
> Documentation.  As you edit each section make sure you remove these
> commentary 'blockquotes'; the lines that start with a > character
> and appear in the generated PDF in italics but do so only **after** all team members agree that the requirements for that section and current Sprint have been met. **Do not** delete future Sprint expectations._

## Team Information
* Team name: Team 06 Fantastech4
* Team members
  * Nolan York
  * Tommy Bell
  * James Halt
  * Brayden Castro

## Executive Summary
This project is a web-based platform for managing school supplies and school supplies needs efficiently. It supports helpers by organizing and tracking items (Needs) through a centralized system. The goal is to streamline school supply allocation, prevent errors, and provide visibility into available and required supplies.

### Purpose
>  _**[Sprint 2 & 4]** Provide a very brief statement about the project and the most
> important user group and user goals._

The purpose of this project is to provide users with a centralized, and friendly to use application for managing school supplies allocation. The primary users, helpers,
can easily organize baskets for needed supplies, while the secondary users, admins, can monitor and validate activities. The system helps reduce manual tracking,
prevent errors, and makes communication between helpers and admins easier and quicker.

### Glossary and Acronyms
> _**[Sprint 2 & 4]** Provide a table of terms and acronyms._

| Term | Definition |
|------|------------|
| SPA | Single Page |
| REST | Representational State Transfer |
| MVP | Minimum Viable Product |
| Helper | Class of user that can contribute to goals |
| Manager | Class of user that manages goals |
| Need | An item requested by a Helper |
| Basket | A collection of Needs that a Helper wants to contribute to |
| Cupboard | A collection of Needs available for Helpers to contribute to |

## Requirements

This section describes the features of the application.

> _In this section you do not need to be exhaustive and list every
> story.  Focus on top-level features from the Vision document and
> maybe Epics and critical Stories._

### Definition of MVP
> _**[Sprint 2 & 4]** Provide a simple description of the Minimum Viable Product._
The MVP of this project:
1. User management
2. User login capability
2. Creation, viewing, and management of school supplies basket (Funding baskets)
3. Finalization to lock completed baskets. 
4. Interface that allows for smooth navigation and interaction. 

### MVP Features
>  _**[Sprint 4]** Provide a list of top-level Epics and/or Stories of the MVP._

1. Admin Login (5) - As a U-fund Manager, I want to log in using the reserved username admin so that I can manage the organization's needs cupboard.

2. Helper Login (8) - As a Helper I want to login to the UFund app so that I can access the needs cupboard and view my basket.

3. Search Needs in cupboard (8) - As a Helper I want to search for a particular need so that I can easily find what things exist or do not exist.

4. Create New Need (8) - As a Manager/Admin I want to submit a request to create a new need so that it is added to the cupboard.

5. Edit an Existing Need(5) - As a Helper I want to edit the details of an existing need in the cupboard 

6. Remove Needs from Cupboard(5) - As a Helper I want to add/remove needs from my funding basket so that I can change the status of available needs.

7. Browse Needs (5) - As a Helper I want to see a list of needs so that I choose which ones to contribute to.

8. Checkout Needs(5) - As a Helper I want to add/remove needs from my funding basket so that I can change the status of available needs.

9. Populate Cupboard(5) - As a Helper I want to add needs from my funding basket so that I can change the status of available needs.

10. Modify Funding Basket(5) - As a Helper I want to review all the needs currently in my funding basket so that I can confirm, update, or remove them before finalizing their status.


### Enhancements
> _**[Sprint 4]** Describe what enhancements you have implemented for the project._

**MAJOR:** A real time notification system that shows Helper and Managers/Admins when needs have status changes (created, deleted, etc.). Appears as a bell icon on the webpage. This system was implemented to improve communication between Helpers and Managers. Benefits and features of this system include:
- Automatic refresh of notification count without reloading the page.
- Scalable design allowing additional notification additions in the future. This follows the Open-Closed principle.

**MINOR:** A set of filtering/sorting utilities was added to the Needs Cupboard to enhance the systems usability for Helpers. This includes options to:
- Sort needs by highest funding percentage
- Sort by highest need quantity
- Sort by lowest need quantity
- Filter Searches based on relevant criteria


## Application Domain

This section describes the application domain.

![Domain Model](domainmodels4.png)

> _**[Sprint 2 & 4]** Provide a high-level overview of the domain for this application. You
> can discuss the more important domain entities and their relationship
> to each other._

The main entities and relationships of the project:
1. Helper: Primary user, can manage their own basket and Needs.
2. Funding Basket: Group of needs asscoiated with a helper.
3. Need: Individual resource item with description, quantity, and other metadata.
4. Manager: Admin user, can modify current needs in the Cupboard, or create new ones. Has access to updated information about the initiative (# of needs, users, etc).
5. Cupboard: Needs posted by Managers available for Helpers to browse, checkout, etc.

## Architecture and Design

This section describes the application architecture.

### Summary

The following Tiers/Layers model shows a high-level view of the webapp's architecture. 
**NOTE**: detailed diagrams are required in later sections of this document.
> _**[Sprint 1]** (Augment this diagram with your **own** rendition and representations of sample system classes, placing them into the appropriate M/V/VM (orange rectangle) tier section. Focus on what is currently required to support **Sprint 1 - Demo requirements**. Make sure to describe your design choices in the corresponding _**Tier Section**_ and also in the _**OO Design Principles**_ section below.)_

![The Tiers & Layers of the Architecture](architecture-tiers-and-layers.png)

The web application, is built using the Model–View–ViewModel (MVVM) architecture pattern. 

The Model stores the application data objects including any functionality to provide persistance. 

The View is the client-side SPA built with Angular utilizing HTML, CSS and TypeScript. The ViewModel provides RESTful APIs to the client (View) as well as any logic required to manipulate the data objects from the Model.

Both the ViewModel and Model are built using Java and Spring Framework. Details of the components within these tiers are supplied below.


### Overview of User Interface

This section describes the web interface and flow; this is how the user views and interacts with the web application.
>_For the reference below, provide an initial draft image/sketch of possible layout of a mayor page of your User Interface and a brief description of the elements it contains **[Sprint 1]**_

![Replace with your First concept of a layout for a mayor page in the User Interface](draft-layout-ui.png)

### 
> _Provide a summary of the application's user interface.  Describe, from the user's perspective, the flow of the pages/navigation in the web application.
>  (Add low-fidelity mockups prior to initiating your **[Sprint 2]**  work so you have a good idea of the user interactions.) Eventually replace with representative screen shots of your high-fidelity results as these become available and finally include future recommendations improvement recommendations for your **[Sprint 4]** )_

![](screenshots/Homepageloggedout.png)
When a user first visits the "Books for the Future" application, they are greeted by a simple landing page serving the local Rochester community. The navigation bar provides clear entry points to either view the cupboard immediately or log in to the system.

![](screenshots/Login.png)
To interact with the system—whether to fund a need or manage inventory—users must authenticate. The login interface is presented as a focused, centered card requiring a username and password, ensuring security before granting access to role-specific features.

![](screenshots/HelperCupboard.png)
Once logged in as a Helper, the user is directed to the Cupboard. This view displays a list of all available needs. It features a search bar for finding specific items (e.g., "14 needs...") and a filter dropdown. Each item card shows the progress of the need and includes an "Add To Basket" button for immediate selection.

![](screenshots/HelperBasket.png)
After selecting items, the Helper navigates to their Funding Basket. This page lists the specific needs the user intends to support, displaying progress bars for each. Users have the control to review their selections, remove items if they change their mind, and click "Checkout" to finalize their funding.

![](screenshots/ManagerDashboard.png)
Managers logging into the system see a specialized Dashboard view. This provides a high-level administrative overview of the organization's health, displaying key statistics such as the Total Users, Total Needs in Cupboard, and Total Funding Collected.

![](screenshots/Cupboard.png)
The Manager's view of the Cupboard is similar to the Helper's but includes administrative capabilities. Notably, a "Create Need" button is visible at the top left, allowing the manager to add new items to the inventory directly from this screen.

![](screenshots/CreateNeed.png)
When a Manager chooses to create a new item, a modal appears at the bottom of the website. This allows the manager to input the Name and Funding Goal for the new need without navigating away from the cupboard context.

![](screenshots/EditNeed.png)
Managers can access a detailed view of individual needs. This interface displays the target goal and current collection status, while providing specific action buttons to "Edit Need" details or "Delete Need" from the system entirely.

#### Future Improvements:
* When the manager goes to create a new need, the popup could be made into an overlay that appears over the rest of the interface. As it is now, the popup appears at the bottom of the screen after the user presses the button, which they have to scroll to see. This could be made less confusing and more intuitively.
* The Cupboard view layout could be remade from a list view to a card grid view. This would allow for a more visually appealing and more functional interface while also utilizing wasted whitespace.
* There could be some visual cues added to the Login and Create Need modals to instantly inform users of invalid inputs. Such visual cues could be red borders or helper text.

### View Tier
> _**[Sprint 4]** Provide a summary of the View Tier UI of your architecture.
> Describe the types of components in the tier and describe their
> responsibilities.  This should be a narrative description, i.e. it has
> a flow or "story line" that the reader can follow._

The application is anchored by the AppComponent, which initializes the global Header and a real-time Notification System that polls for inventory updates using interval polling.
Navigation is handled by the AppRoutingModule, guiding users to the HomepageComponent for general information or the LoginComponent for authentication via the AuthService.
The core functional area for Helpers is the CupboardComponent, which allows users to browse, search, and filter needs fetched by the CupboardService.
This component utilizes the reusable NeedListComponent to render individual items and navigates to NeedInfoComponent for detailed views and basket additions.
The BasketComponent manages the user's cart, enabling checkout operations through the UsersService.
For Managers, the ManagerDashboardComponent provides statistical oversight, while the NeedEditComponent (often presented via ModalService) facilitates the creation and modification of needs.

> _**[Sprint 4]** You must  provide at least **2 sequence diagrams** as is relevant to a particular aspects 
> of the design that you are describing.  (**For example**, in a shopping experience application you might create a 
> sequence diagram of a customer searching for an item and adding to their cart.)
> As these can span multiple tiers, be sure to include an relevant HTTP requests from the client-side to the server-side 
> to help illustrate the end-to-end flow._

![](SequenceDiagramViewModel1.png)
![](sequenceDiagram-AddNeed.png)

> _**[Sprint 4]** To adequately show your system, you will need to present the **class diagrams** where relevant in your design. Some additional tips:_
 >* _Class diagrams only apply to the **ViewModel** and **Model** Tier_
>* _A single class diagram of the entire system will not be effective. You may start with one, but will be need to break it down into smaller sections to account for requirements of each of the Tier static models below._
 >* _Correct labeling of relationships with proper notation for the relationship type, multiplicities, and navigation information will be important._
 >* _Include other details such as attributes and method signatures that you think are needed to support the level of detail in your discussion._

### ViewModel Tier
> _**[Sprint 1]** List the classes supporting this tier and provide a description of there purpose._

Core Classes: 
  * UserController - Handles User account data (user/password) and account creation. 
  * CupboardController - Handles Cupboard data, methods to search, CRUD operations, etc. 
  * UserService - Handles User app data (for basket, checking out), contains reference to HelperService for actions. 
  * HelperService - Handles User actions like searching for, creating, checking out needs, etc. 
  * AuthService - Authenticates user accounts for logging into the site and performing actions.

> _**[Sprint 4]** Provide a summary of this tier of your architecture. This
> section will follow the same instructions that are given for the View
> Tier above._

> _At appropriate places as part of this narrative provide **one** or more updated and **properly labeled**
> static models (UML class diagrams) with some details such as associations (connections) between classes, and critical attributes and methods. (**Be sure** to revisit the Static **UML Review Sheet** to ensure your class diagrams are using correct format and syntax.)_
> 
![](view-modelClassDiagram.png)

### Model Tier
> _**[Sprint 1]** List the classes supporting this tier and provide a description of there purpose._

Core classes:
  * UserRole - Enumeration containing both types of Users on the UFund site: helper and manager
  * User - Represents a user on the UFund site, contains username/password, basket and role data. Performs actions specific to the user.
  * UserFileDAO - Handles persistence of User data to the json database.
  * UserDAO - Interface that is implemented by UserFileDAO and defines DAO behavior.
  * Need - Represents a Need on the UFund site, contains quantity, id #, name and funding amount data.
  * Cupboard - Represents the Cupboard on the UFund site that holds Needs available for funding, stores Need objects in a List data structure.
  * FileCupboardDAO - Handles persistence of Cupboard data to the json database.
  * CupboardDAO - Interface that is implemented by FileCupboardDAO and defines DAO behavior.
  * Authenticator - Represents the authentication information of Users on the UFund website. Contains session key, username of the user it authenticates, and expiry timestamp data. 
  * FileAuthDAO - Handles persistence of User Authentication data to the json database.
  * AuthDAO - Interface that is implemented by FileAuthDAO and defines DAO behavior.

> _**[Sprint 2, 3 & 4]** Provide a summary of this tier of your architecture. This
> section will follow the same instructions that are given for the View
> Tier above._

The Model Tier contains domain level classes that represent the data, some business rules, and the state of the UFund system.
These classes do NOT handle UI logic or HTTP requests. Instead, these classes define the main entities used throughout the backend. These classes are the foundation for the controllers and services.



> _At appropriate places as part of this narrative provide **one** or more updated and **properly labeled**
> static models (UML class diagrams) with some details such as associations (connections) between classes, and critical attributes and methods. (**Be sure** to revisit the Static **UML Review Sheet** to ensure your class diagrams are using correct format and syntax.)_
> 
![](modelClassDiagram.png)

## OO Design Principles

> _**[Sprint 1]** Name and describe the initial OO Principles that your team has considered in support of your design (and implementation) for this first Sprint._

> _**[Sprint 2, 3 & 4]** Will eventually address upto **4 key OO Principles** in your final design. Follow guidance in augmenting those completed in previous Sprints as indicated to you by instructor. Be sure to include any diagrams (or clearly refer to ones elsewhere in your Tier sections above) to support your claims._
> _**[Sprint 3 & 4]** OO Design Principles should span across **all tiers.**_

1. Information Expert
  - Responsibility is assigned to the class that has the necessary information to complete fulfill it.
  - The Need class, for example, is responsible for maintaining its own quantity, fund status, and metadata rather than having another class track it.
  - Another example is the DashboardStats class. This class handles important statistics about users and cupboard contents because it has access to all relevant data to do so.

2. Single Responsibility Principle (SRP)
 - Each class has a it's own clear purpose:
 - For example, the Controller Classes:
     CupboardController only handles HTTP requests related to cupboards.
     Notification Controller is soley responsible for managing notification data.
     NotificationService is responsible for sending notifications between the backend and frontend.
 - DAO classes:
      Each DAO handles data for a single entity. If the strategy of storing data changes, only this class needs to be updated.

3. Low Coupling
- Each service and controller only interacts through clear itnerfaces (REST endpoints).
- For example: The NotificationService doesn't directly modify any controllers state. It only makes HTTP requests, so changing its implementation will not break other classes.
- This design principle allows our classes to be cohesive but still loosely connected.

4. Open/Closed
- Our software entities (classes, modules, functions) are open for extension but closed for modification. This means we could easily add new functionality without changing exisiting, tested code.
- The notification system is designed to be extensible. New types of notifications can be added without changing the existing backend or UI logic.
- The Filtering and Sorting utilities in the Needs cupboard can be extended to include new criteria without modifying the substance of the CupboardComponent or service logic.
- DAO classes are designed so that changed to the persistence do not require modifications to main business logic.

5. Separation of Conserns (SoC)
    Each layer of our projects architecture has a distinct role.
      Model: Handles data and business logic (Ex: Cupboard, Item, NotifictionService)
      Controller/ViewModel: Communicates between the frontend and backend, processing user input and organizing updates.
      View/UFund-UI: Displays data to the user (Ex: Angular components like cupboard.component.ts)
    This seperation od concerns allows each tier of the arhitecture to be modified independently without affecting others.

    Answer from Sprint 2:
    Front-End  
      All user interaction and request handling logic is isolated in the REST controllers. This tier only formats requests/responses and doesn't directly access the business like logic or data persistence.
    Business Logic
      Contrains all core application logic, such as validations.
    Data Access (DAO)
      Responsible solely for CRUD operations and persistence. Handle reading/writing to JSON files. These files are unaware of how the data is used in business logic.

## Static Code Analysis/Future Design Improvements
> _**[Sprint 4]** With the results from the Static Code Analysis exercise, 
> **Identify 3-4** areas within your code that have been flagged by the Static Code 
> Analysis Tool (SonarQube) and provide your analysis and recommendations.  
> Include any relevant screenshot(s) with each area._

> _**[Sprint 4]** Discuss **future** refactoring and other design improvements your team would explore if the team had additional time._

## Testing
> _This section will provide information about the testing performed
> and the results of the testing._

### Acceptance Testing
> _**[Sprint 2 & 4]** Report on the number of user stories that have passed all their
> acceptance criteria tests, the number that have some acceptance
> criteria tests failing, and the number of user stories that
> have not had any testing yet. Highlight the issues found during
> acceptance testing and if there are any concerns._
- All unit tests pass.
- View the Acceptance Test Plan document for more information.

### Unit Testing and Code Coverage
> _**[Sprint 4]** Discuss your unit testing strategy. Report on the code coverage
> achieved from unit testing of the code base. Discuss the team's
> coverage targets, why you selected those values, and how well your
> code coverage met your targets._

>_**[Sprint 2, 3 & 4]** **Include images of your code coverage report.** If there are any anomalies, discuss
> those._
(___.png)

## Ongoing Rationale
>_**[Sprint 1, 2, 3 & 4]** Throughout the project, provide a time stamp **(yyyy/mm/dd): Sprint # and description** of any _**mayor**_ team decisions or design milestones/changes and corresponding justification._

Mainly on Slack

(2025/10/10) Sprint 2: Working on now:
Tommy: Edit needs in Cupboard, Search needs in cupboard user stories
James: Admin logic, modify funding basket user stories
Nolan: Refactoring, Populate/remove/browse needs in cupboard user stories
Brayden: Checkout needs to funding basket, edit needs in cupboard user stories
Working on next:
Tommy: User stories in progress
James: Researching, User stories in progress
Nolan: User stories that are in progress
Brayden: User stories that are in progress
BLOCKERS:
Tommy: N/A
James: Funding basket creation
Nolan: Need to refactor before everyone continues
Brayden: Funding basket creation

(2025/11/07) Sprint 3: 
Working on now:
ALL: Sorting out bugs from all the merges
Working on next:
ALL: Testing and code coverage
BLOCKERS:
Tommy: N/A
James: Filter for basket
Nolan: N/A
Brayden: N/A
During Standup:
Merged all our separate feature branches to sprint3-dev
Worked on fixing merge conflicts and bugs that arose
Discussed plan moving forward