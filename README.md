# iyzico payment integration

In here there is an implementation of iyzico payment service on a dubbed e-commerce simulation. 
In this simulation there are members and they have their baskets. They add items into the baskets and then they can pay with iyzico.

## Credit Card Masking and Validation:
There are several type of card masking.
```
4729150000000005 --> 472915******0005
4729-1500-0000-0005 --> 4729-15**-****-0005
```

Also there are some limitations for a valid credit card:

* Card number length must be 15 or 16.
* Not mask the first 6 and last 4 chars.
* Not mask if the number is not a valid one.

## Simulated e-commerce purchase:
* API should CRUD items in e-commerce.
* API should CRUD members.
* API should CRUD a basket.
* API should CRUD items in the basket.
* API should provide the item name, stock and description.
* API for iyzico payment.
* Items should be sold if they are exist in e-commerce.

## Testing:
* Unit test.
* Integration test with iyzico sandbox.

**NOTE:** This project contains only backend codes. No ui is created.






