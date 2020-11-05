# Authentication and Authorization - OAuth 2 

In this project, I have demonstrated the use of OAuth 2 for Authorization. The context or example that I've used for this is of a meal tracking applicaton, which allows users to log what they ate and calories consumed in the meal log(database).

## Bird's eye view of the architecture
![Image of scenarios](https://github.com/s1mar/OAuth2-Demo/blob/main/imgs/architecture.jpeg?raw=true)

##### Salient Features
- The **Controller** ensures that only users with admin privilege can delete records(meal logs).
- The **Service** Layer ensures that a user can only log meals for their own account and not any other.
- The **Repository** layer ensures that data can only be retrieved for authenticated users.
## Setup
##### [Keycloak](https://www.keycloak.org/) 
- An open source identity and access management solution.
- Import the realm-export.json file inside the folder keycloak of this repository.
- After importing the realm, please add these users:

    | Username | Password |
    | ------ | ------ |
    | simar | bez123@ |
    | amreen | qwerty123@ |
- For the User Simar, unde Role Mappings, please assign him the **sevenfive_admin** role.
- For the User Amreen, under Role Mappings, please assign the **sevenfive_user** role.

##### [MySql](https://dev.mysql.com/downloads/) 
I have used MySql 8.0 for this project. Please create a database named sevenfivefive_oauth in it.

##### The Project
 - Add your data source(MySql) password and username.
 - I've set the project to run on port 4567, for keycloak runs on port 8080
 ![Image of application.properties](https://github.com/s1mar/OAuth2-Demo/blob/main/imgs/project_properties.jpg?raw=true)
-Finally, run the Main.java file to launch this Spring based application.

## Scenarios!
 I've demonstrated the efficacy of this solution via 4 scenarios that I wrote in the test file DemoTests.
  - **Scenario 1** : An Authenticated user has access to only their records and their alone. They can add records(log meals) for themselves only.
  - **Scenario 2** : An authenticated user can not record or log an entry for a different user.
  - **Scenario 3** : A user can only fetch their logs from the resource server. They'll run into 403 forbidden if they try to get records of a different user.
  - **Scenario 4** : Only admin users can delete the meal logs in the meal database.
 ![Image of scenarios](https://github.com/s1mar/OAuth2-Demo/blob/main/imgs/scenarios.jpg?raw=true)

## Security Tactics
 - **Authentication** : Keycloak is managing our users and the authentication flow.
 - **Authorization** : OAuth 2.0 protocol is being implemented via our keycloak server and users are only able to access resources that they are authorized to do so. *Please look at the Scenarios.
 - **Limit Exposure** : In this demonstration, only admin level users can delete logs, hence I've limited the exposure by exluding normal users from being able to do this.
 - **Maintain data confidentiality** : An Authenticated user can see only their logs and no one else's. 