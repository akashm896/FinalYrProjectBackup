//__modelattribute__cartItemList : mu___modelattribute__cartItemList
//__modelattribute__shoppingCart : mu___modelattribute__shoppingCart
//__modelattribute__shoppingCart.GrandTotal : mu___modelattribute__shoppingCart_GrandTotal
//__modelattribute__shoppingCart.cartItemList : mu___modelattribute__shoppingCart_cartItemList
//__modelattribute__shoppingCart.id : mu___modelattribute__shoppingCart_id
//__modelattribute__shoppingCart.user : mu___modelattribute__shoppingCart_user
sig FieldData {}
one sig u_this_shoppingCart_cartItemList in FieldData {}
one sig u_this_shoppingCart_user in FieldData {}
one sig u_this_shoppingCart_id in FieldData {}
one sig u_this_shoppingCart_GrandTotal in FieldData {}
sig u_Order {
orederStatus : FieldData,
shippingMethod : FieldData,
u_user : u_User,
orderTotal : FieldData,
u_payment : u_Payment,
u_cartItemList : u_CartItem,
u_shippingDate : u_Date,
user_id : FieldData,
u_billingAddress : u_BillingAddress,
u_orderDate : u_Date,
id : FieldData,
order_id : FieldData,
u_shippingAddress : u_ShippingAddress,
}
sig u_UserRole {
user_id : FieldData,
role_id : FieldData,
u_role : u_Role,
userRoleId : FieldData,
u_user : u_User,
}
sig u_UserPayment {
holderName : FieldData,
u_userBilling : u_UserBilling,
cardName : FieldData,
userPayment_id : FieldData,
expiryMonth : FieldData,
expiryYear : FieldData,
type : FieldData,
u_user : u_User,
cvc : FieldData,
defaultPayment : FieldData,
user_id : FieldData,
id : FieldData,
cardNumber : FieldData,
}
sig u_ShoppingCart {
GrandTotal : FieldData,
shoppingCart_id : FieldData,
id : FieldData,
u_user : u_User,
u_cartItemList : u_CartItem,
}
sig u_BookToCartItem {
cart_tem_id : FieldData,
u_cartItem : u_CartItem,
id : FieldData,
book_id : FieldData,
u_book : u_Book,
}
sig u_CartItem {
u_bookToCartItemList : u_BookToCartItem,
u_order : u_Order,
cartItem_id : FieldData,
subtotal : FieldData,
cart_tem_id : FieldData,
qty : FieldData,
shoppingCart_id : FieldData,
id : FieldData,
u_book : u_Book,
order_id : FieldData,
u_shoppingCart : u_ShoppingCart,
}
sig u_Book {
inStockNumber : FieldData,
author : FieldData,
isbn : FieldData,
format : FieldData,
active : FieldData,
description : FieldData,
u_cartItem : u_CartItem,
language : FieldData,
book_id : FieldData,
title : FieldData,
numberOfPages : FieldData,
u_bookImage : u_MultipartFile,
shippingWeight : FieldData,
publisher : FieldData,
id : FieldData,
category : FieldData,
u_bookToCartItemsList : u_BookToCartItem,
publicationDate : FieldData,
ourPrice : FieldData,
listPrice : FieldData,
}
sig u_UserShipping {
user_id : FieldData,
userShippingDefault : FieldData,
userShippingName : FieldData,
userShippingStreet2 : FieldData,
userShippingStreet1 : FieldData,
id : FieldData,
userShippingCity : FieldData,
userShippingZipcode : FieldData,
u_user : u_User,
userShippingState : FieldData,
userShippingCountry : FieldData,
}
sig u_User {
u_userPaymentList : u_UserPayment,
lastName : FieldData,
u_userRoles : u_UserRole,
enabled : FieldData,
firstName : FieldData,
password : FieldData,
u_userShippingList : u_UserShipping,
phone : FieldData,
user_id : FieldData,
id : FieldData,
u_userOrderList : u_Order,
email : FieldData,
u_shoppingCart : u_ShoppingCart,
username : FieldData,
}


 sig u_User1 in u_User {} 
fact{all v0 : u_User | v0.u_username = MethodWontHandleOp <=> v0 in u_User1} 

 fact { all alpha : u_User1 |  all v2 : u_ShoppingCart | alpha.id = v2.u_user_id <=> v2 in alpha.u_shoppingCart } 

 fact { all v0 : u_User1 | all alpha : v0.u_shoppingCart |  all v3 : u_CartItem | alpha.id = v3.u_shoppingCart_id <=> v3 in alpha.u_cartItemList } 

 fact { all v0 : u_User1 | all v1 : v0.u_shoppingCart | all alpha : v1.u_cartItemList |  all v4 : u_Book | alpha.id = v4.u_cartItem_id <=> v4 in alpha.u_book } 

 fact { all v0 : u_User1 | all v1 : v0.u_shoppingCart | all alpha : v1.u_cartItemList |  all v4 : u_BookToCartItem | alpha.id = v4.u_cart_tem_id <=> v4 in alpha.u_bookToCartItemList } 

 fact { all v0 : u_User1 | all v1 : v0.u_shoppingCart | all alpha : v1.u_cartItemList |  all v4 : u_ShoppingCart | alpha.shoppingCart_id = v4.u_id <=> v4 in alpha.u_shoppingCart } 

 fact { all v0 : u_User1 | all v1 : v0.u_shoppingCart | all alpha : v1.u_cartItemList |  all v4 : u_Order | alpha.order_id = v4.u_id <=> v4 in alpha.u_order } 

 fact { all v0 : u_User1 | all alpha : v0.u_shoppingCart |  all v3 : u_User | alpha.lhs = v3.u_rhs <=> v3 in alpha.u_user } 

 fact { all v0 : u_User1 | all v1 : v0.u_shoppingCart | all alpha : v1.u_user |  all v4 : u_ShoppingCart | alpha.id = v4.u_user_id <=> v4 in alpha.u_shoppingCart } 

 fact { all v0 : u_User1 | all v1 : v0.u_shoppingCart | all alpha : v1.u_user |  all v4 : u_UserShipping | alpha.id = v4.u_user_id <=> v4 in alpha.u_userShippingList } 

 fact { all v0 : u_User1 | all v1 : v0.u_shoppingCart | all alpha : v1.u_user |  all v4 : u_Order | alpha.id = v4.u_user_id <=> v4 in alpha.u_userOrderList } 

 fact { all v0 : u_User1 | all v1 : v0.u_shoppingCart | all alpha : v1.u_user |  all v4 : u_UserPayment | alpha.id = v4.u_user_id <=> v4 in alpha.u_userPaymentList } 

 fact { all v0 : u_User1 | all v1 : v0.u_shoppingCart | all alpha : v1.u_user |  all v4 : u_UserRole | alpha.id = v4.u_user_id <=> v4 in alpha.u_userRoles } 

sig mu___modelattribute__shoppingCart_cartItemList in univ {}
fact { mu___modelattribute__shoppingCart_cartItemList = u_this_shoppingCart_cartItemList }
sig mu___modelattribute__shoppingCart_id in univ {}
fact { mu___modelattribute__shoppingCart_id = u_this_shoppingCart_id }
sig mu___modelattribute__shoppingCart_GrandTotal in univ {}
fact { mu___modelattribute__shoppingCart_GrandTotal = u_this_shoppingCart_GrandTotal }
sig mu___modelattribute__cartItemList in univ {}
fact { mu___modelattribute__cartItemList =  }
sig mu___modelattribute__shoppingCart in univ {}
fact { mu___modelattribute__shoppingCart =  }
sig mu___modelattribute__shoppingCart_user in univ {}
fact { mu___modelattribute__shoppingCart_user = u_this_shoppingCart_user }
sig BottomNode in FieldData {}
