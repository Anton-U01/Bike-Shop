# Online-Bike-Shop
This is web platform for the sale of bicycles and bicycle parts.
There are two types of roles available - user and administrator. Authorization and authentication mechanisms are integrated via Spring Security.
Users have the opportunity to create their own profile by registering. After successful registration with valid data, the user has the opportunity to log into his profile and, depending on his role, can perform certain actions.
The user has the opportunity to view the page with all products, where it is possible to sort the products in several directions - price in ascending and descending order,
as well as by relevance - newest or oldest products. The project includes filtering and search functionality.
The search engine allows users to search for products by name by entering keywords related to the product.
The user can add/remove a product to/from the list of favorites which he can find in the navigation part of the site. On the page with all products there are two more buttons for each offer - Read more and Buy button.
Upon clicking the first button, the application navigates to a page where all the information about a given product is located and there is a functionality to leave a review under the corresponding product.
When choosing to buy, the user can also choose the desired quantity. After confirmation, the product is added to the "Cart".
There are options to change the quantity, remove the product from the "Cart" or proceed to the next step, where the user is asked for the delivery details of the purchase.
It is possible to choose between the data already provided by the user during registration or to enter new ones. After all of the above, the user reaches the stage of making a payment,
where he is redirected to a page (hosted from Stripe) requiring the provision of bank card details and, as a result of a successful payment, the user receives an order confirmation email with details about it.
Each user has a profile section that contains: a page with profile details - offers the possibility to view the data and change the username; page with the orders made – here you can see the history of all the orders made, with details of each purchase, date, status, etc.
Admin can add new products by filling necessary forms with product information. Additionally, he has the ability to edit existing product information and to delete prodcuts.
After an order is processed and shipped, he can mark it as delivered, and upon marking an order as delivered, the system automatically sends an email notification to the user informing him that the order has been successfully delivered.
After successful delivery, admin can archive the orders. The administrator has the ability to add and remove user roles, giving them control over the access rights and actions each user can perform in the application. 
Additionally, the administrator can delete users from the system.
