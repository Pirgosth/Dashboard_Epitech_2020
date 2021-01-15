# T-JAV-501-LIL-5-1-dashboard-anthony.anicotte

# Build

Simply run `./gradlew build` at the root directory of the project to generate your .war file.

# Installation

This application require a Tomcat server to run our war application, and a MariaDB database to host user and cache data.
To perform database request, you need to run a SQL server listening on port `localhost:3306`, with the following SQL account:
`user: dashboard`, `password: qps4<PAsj:e*`. The account must of course have access to the dashboard table at least.

To install our application into the Tomcat server, you have to paste the previously generated war file into the `webapps` Tomcat folder, and wait for it to load it.

# Services
This Dashboard project provides 3 different services:
- Steam
- Youtube
- Github

## Steam
The Steam service contains 3 widgets:
> Each of them takes a single (String) parameter, representing the url/pseudo of the requested Steam User
> Three forms are possible:
> - https://steamcommunity.com/profiles/76561199115353715
> - https://steamcommunity.com/id/cvxfous
> - cvxfous (if the user has a custom url like above)

Profile: It displays the Pseudo/Real Name, profile picture, level and number of friends of the user requested  
Library Value: It displays the total amount of the library (all the games) possessed by the user.  
Time Played: It calculated the number of hours (with a .1 precision) played by a user on his games (account related)  

## Youtube
The Youtube service contains 2 widgets:
> Each of them takes a single (String) parameter, representing the url/pseudo of the requested Youtube Channel
> - https://www.youtube.com/channel/UCXF0YCBWewAj3RytJUAivGA
> - https://www.youtube.com/user/aMOODIEsqueezie
> - aMOODIEsqueezie (if the user has a custom url like above)

Channel Stats: Displays statistics on the given channel, as profile picture, current pseudo, number of total views, subscribers and videos uploaded  
Latest Video: Generates a card with a Youtube Player frame, containing the latest video of the channel entered as parameter  

## Github
The Github service contains 2 widgets:
> Each of them takes a single (String) parameter, representing the pseudo of the requested Github User
> - AnthonyAnicotte

List Repositories: Lists the repositories of the given user, also contains a link to look on Github and to clone the project  
> If the current user is connected to the Dashboard with Github and is looking for himself, also his private repositories will be displayed
Followers: Lists the followers of the given user  
