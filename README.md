This is my final year clean coding and concurrent programming project. Here is the explanation and requiremrnts for the system:

Synex Outlet Store (SYOS) is an up-and-coming grocery store in Colombo. SYOS is a large store where 
customers can choose their household needs. After selecting items, customers check out the items via the 
single point of sales managed by a SYOS employee. At the point of sale, the employee writes down the 
items to be purchased in their quantities and calculates the amount owed by the customer to SYOS. The 
employee prepares and issues the final bill with the necessary change upon payment.  

The management of SYOS has observed that during rush hour, the customers spend a lot of time queuing 
at the point of sale, resulting in store congestion and customer dissatisfaction. Also, the manual methods 
are prone to calculation errors, are time-consuming, and are tedious. To alleviate the problem, the 
management has decided to automate the billing system at SYOS and integrate it with the stock system 
to manage stocks and reorder levels efficiently. 

Following are the functional requirements of the system that need to be developed. 

1. The items to be purchased by the customer must be entered into the system via a code. The codes
can be defined in suitable forms if they uniquely identify the item. Once the thing has been keyed
into the system, the quantity is inserted, and the following item is keyed. Upon completing the
insertion of the items to be purchased into the system, the system shall calculate the dues and
display them to the employee.

  a. SYOS only accepts cash payments. Hence, when cash is tendered, the amount so tendered
  is inserted into the system, and the change to be returned is calculated and displayed.
  
  b. The bill is created with the item name (not code), the quantity, the total price for the item,
  full price, discount, cash tendered, and the change amount. The bill should have the serial
  number (running number starting from 1) and the bill date. Bill should be saved separately
  with the bill serial number for each customer transaction performed.
  
2. Upon the customer checking out (the bill has been generated and saved), the number of items on
shelves should be reduced by the amount the customer has purchased.

  a. The items are first bought at the SYOS store and are stocked according to the code, date
  of purchase, amount of quantity received, and date of expiry of items.

  b. Items are moved to the shelf from the store. The stock should be reduced from the oldest 
  batch of items and put on the shelf. However, when the expiry date of another set is 
  closer than the one in the oldest batch of items, the newer batch is chosen to stock the 
  SYOS shelves. 
  
3. Apart from the over-the-counter sales, SYOS provides an interface where sales are made through 
the Internet. The user needs to be registered with the system first and, upon completing the 
registration process, can purchase items from SYOS's website.

  a. The website inventory must also be maintained separately from the store's shelf.
  
  b. The transactions that have happened online and over the counter should be identified 
  separately. 
  
4. The following reports are needed to be displayed on the screen. The reports should be generated 
combinedly and severally for each transaction type and store type.

  a. The total sale for a given day. This report should display the items sold (with name and 
  code), the total quantity, and the total revenue for the given day. 
  
  b. The total number of items with code, name, and quantity must be reshelved at the end 
  of the day. 
  
  c. Reorder levels of stock. If any stock for a given item falls below 50 items, that item should 
  appear on the report. 
  
  d. Stock report. Provides details of the current stock batch-wise with the same information 
  as mentioned in 2(a) above. 
  
  e. Bill report. This would contain all the customer transactions that have taken place in the 
  SYOS system
