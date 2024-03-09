MOVIE TICKET BOOKING APPLICATION
PROJECT OVERVIEW:
STATEMENT :
To design a web application for the movie ticket booking, and this has to be
shared among the different theatres.
FEATURES:
1. Primarily, there are three users for this application.
2. The first one is customers (who are going to use this application) and the second
one is managers, and the final one is super admin.
3. The role of the manager is to add movies and add shows to the theatre, and
he/she can be able to cancel the show.
4. Moreover, the manager will also be able to view the available and booked tickets
for the show and the total price collected for the show.
5. The users can see the theatres and can filter the theatres based on the location
and the date and time.
6. The users can book the tickets for the show.
7. If they want to cancel, it should be done before 24 hours for the show time or
else they can’t.Timing can be varied, and that should be dynamic.
8. And also, they can’t cancel a single ticket if they have booked more than one
ticket.If they want to cancel, it has to be cancelled as a whole.The super admin
can be able to assign a manager to the theatre and add theatres.DATABASE SCHEMA:
API DESIGN :APPLICATION FLOW :
FRONT END : Ember js , SCSS .
BACK END
: Java
DATABASE : PostgreSQL
CODE FLOW :
First of all , all the request that we will make will come to the filter , which is a Auth
Filter again which is a java class and there are some methods that authenticate the
valid api and authorization for the valid resource.
After the filter it will be redirected to the Main Servlet and based on the api
endpoints through the reflection it will be redirected to the controller and based on the
request method it will be handled and also there is a POJO class for the every data
model for this application.For fetching the data from the database , I have done two
separate class one for the connecting to the database and the other one for the query
generation part.
While fetching data from the database,it usually returns the resultset and then I have
converted it into JSONArray to easily handle the data in the front end.
For entire code,refer the below github repository
