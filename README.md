# Compose News App
Modular news app written in jetpack-compose. The main aim of this app to make an api call using retrofit and storing it to room 
database and integrate all with compose-ui using livedata, viewmodel and observer.

There is two module in this app 
1. app :- It will contain all the ui and viewmodel code.
2. data :- It will handle database and api call.

I also added dark and light mode for this app which is really simple to deal in compose-ui.

<img src="/doc_image/darkModeNewsApp.jpeg" width="30%" height="500px">   <img src="/doc_image/lightModeNewsApp.jpeg" width="30%" height="500px">

# Code Setup
You will require latest version of android studio. Currently, I am using android studio android studio 4.0 Canary 1. 


<b>API KEY setup</b>
1. Login/Register to [newsapi.org](https://newsapi.org/) and then generate your api key.
2. Create a properties file in project root folder and name it credential.properties.
3. Add your api key as show below in your credential.properties file<br>
        `` NEWS_API_KEY="Your_Api_Key_Goes_Here" ``
4. Build the app and you are good to go.

