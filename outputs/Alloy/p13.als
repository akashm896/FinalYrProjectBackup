//__modelattribute__classActiveBilling : mu___modelattribute__classActiveBilling
//__modelattribute__listOfCreditCards : mu___modelattribute__listOfCreditCards
//__modelattribute__listOfShippingAddresses : mu___modelattribute__listOfShippingAddresses
//__modelattribute__user : mu___modelattribute__user
//__modelattribute__user.email : mu___modelattribute__user_email
//__modelattribute__user.enabled : mu___modelattribute__user_enabled
//__modelattribute__user.firstName : mu___modelattribute__user_firstName
//__modelattribute__user.id : mu___modelattribute__user_id
//__modelattribute__user.lastName : mu___modelattribute__user_lastName
//__modelattribute__user.password : mu___modelattribute__user_password
//__modelattribute__user.phone : mu___modelattribute__user_phone
//__modelattribute__user.shoppingCart : mu___modelattribute__user_shoppingCart
//__modelattribute__user.userOrderList : mu___modelattribute__user_userOrderList
//__modelattribute__user.userPaymentList : mu___modelattribute__user_userPaymentList
//__modelattribute__user.userRoles : mu___modelattribute__user_userRoles
//__modelattribute__user.userShippingList : mu___modelattribute__user_userShippingList
//__modelattribute__user.username : mu___modelattribute__user_username
//__modelattribute__userPaymentList : mu___modelattribute__userPaymentList
//__modelattribute__userShippingList : mu___modelattribute__userShippingList
sig FieldData {}
one sig u_1 extends FieldData {}
one sig u_principalusername in FieldData {}
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
sig u_com_bookstore_domain_User {
u_firstName : FieldData,
u_enabled : FieldData,
u_id : FieldData,
u_phone : FieldData,
u_email : FieldData,
u_password : FieldData,
u_lastName : FieldData,
u_username : FieldData,
u_principalusername : FieldData,
}
sig u_BillingAddress {
billingAddressState : FieldData,
u_order : u_Order,
billingAddressCity : FieldData,
billingAddressName : FieldData,
billingAddressgStreet1 : FieldData,
billingAddressgStreet2 : FieldData,
billingAddressZipcode : FieldData,
id : FieldData,
billingAddressCountry : FieldData,
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
sig u_UserBilling {
userBillingZipcode : FieldData,
userBillingCity : FieldData,
userBillingCountry : FieldData,
userBillingName : FieldData,
userBilling_id : FieldData,
id : FieldData,
userBillingStreet2 : FieldData,
u_userPayment : u_UserPayment,
userBillingStreet1 : FieldData,
userBillingState : FieldData,
u_payment : u_Payment,
}
sig u_Role {
u_userRoles : u_UserRole,
role_id : FieldData,
roleId : FieldData,
name : FieldData,
}
sig u_ShippingAddress {
shippingAddressCity : FieldData,
u_order : u_Order,
shippingAddressState : FieldData,
shippingAddressStreet2 : FieldData,
shippingAddressStreet1 : FieldData,
user_id : FieldData,
shippingAddressName : FieldData,
id : FieldData,
u_user : u_User,
shippingAddressZipcode : FieldData,
shippingAddressCountry : FieldData,
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
sig u_Payment {
cvc : FieldData,
holderName : FieldData,
u_userBilling : u_UserBilling,
u_order : u_Order,
cardName : FieldData,
expiryMonth : FieldData,
id : FieldData,
expiryYear : FieldData,
type : FieldData,
cardNumber : FieldData,
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
sig u_Sel___ClassRef_com_b21 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b21[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b21[y] <=> y in u_Sel___ClassRef_com_b21 }
sig u_Sel___ClassRef_com_b10 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b10[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b10[y] <=> y in u_Sel___ClassRef_com_b10 }
sig u_Sel___ClassRef_com_b2 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b2[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b2[y] <=> y in u_Sel___ClassRef_com_b2 }
sig u_Sel___ClassRef_com_b4 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b4[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b4[y] <=> y in u_Sel___ClassRef_com_b4 }
sig u_Sel___ClassRef_com_b8 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b8[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b8[y] <=> y in u_Sel___ClassRef_com_b8 }
sig u_Sel___ClassRef_com_b1 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b1[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b1[y] <=> y in u_Sel___ClassRef_com_b1 }
sig u_Sel___ClassRef_com_b14 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b14[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b14[y] <=> y in u_Sel___ClassRef_com_b14 }
sig u_Sel___ClassRef_com_b6 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b6[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b6[y] <=> y in u_Sel___ClassRef_com_b6 }
sig u_Sel___ClassRef_com_b12 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b12[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b12[y] <=> y in u_Sel___ClassRef_com_b12 }

 sig u_User19 in u_User {} 
fact{all v0 : u_User | v0.u_username = MethodWontHandleOp <=> v0 in u_User19} 

 fact { all alpha : u_User19 |  all v2 : u_UserRole | alpha.id = v2.u_user_id <=> v2 in alpha.u_userRoles } 

 fact { all v0 : u_User19 | all alpha : v0.u_userRoles |  all v3 : u_User | alpha.user_id = v3.u_userRoleId <=> v3 in alpha.u_user } 

 fact { all v0 : u_User19 | all v1 : v0.u_userRoles | all alpha : v1.u_user |  all v4 : u_ShoppingCart | alpha.id = v4.u_user_id <=> v4 in alpha.u_shoppingCart } 

 fact { all v0 : u_User19 | all v1 : v0.u_userRoles | all alpha : v1.u_user |  all v4 : u_UserShipping | alpha.id = v4.u_user_id <=> v4 in alpha.u_userShippingList } 

 fact { all v0 : u_User19 | all v1 : v0.u_userRoles | all alpha : v1.u_user |  all v4 : u_Order | alpha.id = v4.u_user_id <=> v4 in alpha.u_userOrderList } 

 fact { all v0 : u_User19 | all v1 : v0.u_userRoles | all alpha : v1.u_user |  all v4 : u_UserPayment | alpha.id = v4.u_user_id <=> v4 in alpha.u_userPaymentList } 

 fact { all v0 : u_User19 | all v1 : v0.u_userRoles | all alpha : v1.u_user |  all v4 : u_UserRole | alpha.id = v4.u_user_id <=> v4 in alpha.u_userRoles } 

 fact { all v0 : u_User19 | all alpha : v0.u_userRoles |  all v3 : u_Role | alpha.role_id = v3.u_userRoleId <=> v3 in alpha.u_role } 

 fact { all v0 : u_User19 | all v1 : v0.u_userRoles | all alpha : v1.u_role |  all v4 : u_UserRole | alpha.roleId = v4.u_role_id <=> v4 in alpha.u_userRoles } 

fact { u_Pi___Sel_____ClassRe9 = u_Sel___ClassRef_com_b8 }
sig u_Pi___Sel_____ClassRe22 in u_com_bookstore_domain_User {}


 sig u_User24 in u_User {} 
fact{all v0 : u_User | v0.u_username = MethodWontHandleOp <=> v0 in u_User24} 

 fact { all alpha : u_User24 |  all v2 : u_UserShipping | alpha.id = v2.u_user_id <=> v2 in alpha.u_userShippingList } 

 fact { all v0 : u_User24 | all alpha : v0.u_userShippingList |  all v3 : u_User | alpha.user_id = v3.u_id <=> v3 in alpha.u_user } 

 fact { all v0 : u_User24 | all v1 : v0.u_userShippingList | all alpha : v1.u_user |  all v4 : u_ShoppingCart | alpha.id = v4.u_user_id <=> v4 in alpha.u_shoppingCart } 

 fact { all v0 : u_User24 | all v1 : v0.u_userShippingList | all alpha : v1.u_user |  all v4 : u_UserShipping | alpha.id = v4.u_user_id <=> v4 in alpha.u_userShippingList } 

 fact { all v0 : u_User24 | all v1 : v0.u_userShippingList | all alpha : v1.u_user |  all v4 : u_Order | alpha.id = v4.u_user_id <=> v4 in alpha.u_userOrderList } 

 fact { all v0 : u_User24 | all v1 : v0.u_userShippingList | all alpha : v1.u_user |  all v4 : u_UserPayment | alpha.id = v4.u_user_id <=> v4 in alpha.u_userPaymentList } 

 fact { all v0 : u_User24 | all v1 : v0.u_userShippingList | all alpha : v1.u_user |  all v4 : u_UserRole | alpha.id = v4.u_user_id <=> v4 in alpha.u_userRoles } 

fact { u_Pi___Sel_____ClassRe15 = u_Sel___ClassRef_com_b14 }

 sig u_User16 in u_User {} 
fact{all v0 : u_User | v0.u_username = MethodWontHandleOp <=> v0 in u_User16} 

 fact { all alpha : u_User16 |  all v2 : u_ShoppingCart | alpha.id = v2.u_user_id <=> v2 in alpha.u_shoppingCart } 

 fact { all v0 : u_User16 | all alpha : v0.u_shoppingCart |  all v3 : u_CartItem | alpha.id = v3.u_shoppingCart_id <=> v3 in alpha.u_cartItemList } 

 fact { all v0 : u_User16 | all v1 : v0.u_shoppingCart | all alpha : v1.u_cartItemList |  all v4 : u_Book | alpha.id = v4.u_cartItem_id <=> v4 in alpha.u_book } 

 fact { all v0 : u_User16 | all v1 : v0.u_shoppingCart | all alpha : v1.u_cartItemList |  all v4 : u_BookToCartItem | alpha.id = v4.u_cart_tem_id <=> v4 in alpha.u_bookToCartItemList } 

 fact { all v0 : u_User16 | all v1 : v0.u_shoppingCart | all alpha : v1.u_cartItemList |  all v4 : u_ShoppingCart | alpha.shoppingCart_id = v4.u_id <=> v4 in alpha.u_shoppingCart } 

 fact { all v0 : u_User16 | all v1 : v0.u_shoppingCart | all alpha : v1.u_cartItemList |  all v4 : u_Order | alpha.order_id = v4.u_id <=> v4 in alpha.u_order } 

 fact { all v0 : u_User16 | all alpha : v0.u_shoppingCart |  all v3 : u_User | alpha.lhs = v3.u_rhs <=> v3 in alpha.u_user } 

 fact { all v0 : u_User16 | all v1 : v0.u_shoppingCart | all alpha : v1.u_user |  all v4 : u_ShoppingCart | alpha.id = v4.u_user_id <=> v4 in alpha.u_shoppingCart } 

 fact { all v0 : u_User16 | all v1 : v0.u_shoppingCart | all alpha : v1.u_user |  all v4 : u_UserShipping | alpha.id = v4.u_user_id <=> v4 in alpha.u_userShippingList } 

 fact { all v0 : u_User16 | all v1 : v0.u_shoppingCart | all alpha : v1.u_user |  all v4 : u_Order | alpha.id = v4.u_user_id <=> v4 in alpha.u_userOrderList } 

 fact { all v0 : u_User16 | all v1 : v0.u_shoppingCart | all alpha : v1.u_user |  all v4 : u_UserPayment | alpha.id = v4.u_user_id <=> v4 in alpha.u_userPaymentList } 

 fact { all v0 : u_User16 | all v1 : v0.u_shoppingCart | all alpha : v1.u_user |  all v4 : u_UserRole | alpha.id = v4.u_user_id <=> v4 in alpha.u_userRoles } 

sig u_Pi___Sel_____ClassRe3 in u_com_bookstore_domain_User {}

sig u_Pi___Sel_____ClassRe5 in u_com_bookstore_domain_User {}

fact { u_Pi___Sel_____ClassRe7 = u_Sel___ClassRef_com_b6 }
sig u_Pi___Sel_____ClassRe11 in u_com_bookstore_domain_User {}

sig u_Pi___Sel_____ClassRe13 in u_com_bookstore_domain_User {}

fact { u_Pi___Sel_____ClassRe5 = u_Sel___ClassRef_com_b4 }

 sig u_User20 in u_User {} 
fact{all v0 : u_User | v0.u_username = MethodWontHandleOp <=> v0 in u_User20} 

 fact { all alpha : u_User20 |  all v2 : u_UserShipping | alpha.id = v2.u_user_id <=> v2 in alpha.u_userShippingList } 

 fact { all v0 : u_User20 | all alpha : v0.u_userShippingList |  all v3 : u_User | alpha.user_id = v3.u_id <=> v3 in alpha.u_user } 

 fact { all v0 : u_User20 | all v1 : v0.u_userShippingList | all alpha : v1.u_user |  all v4 : u_ShoppingCart | alpha.id = v4.u_user_id <=> v4 in alpha.u_shoppingCart } 

 fact { all v0 : u_User20 | all v1 : v0.u_userShippingList | all alpha : v1.u_user |  all v4 : u_UserShipping | alpha.id = v4.u_user_id <=> v4 in alpha.u_userShippingList } 

 fact { all v0 : u_User20 | all v1 : v0.u_userShippingList | all alpha : v1.u_user |  all v4 : u_Order | alpha.id = v4.u_user_id <=> v4 in alpha.u_userOrderList } 

 fact { all v0 : u_User20 | all v1 : v0.u_userShippingList | all alpha : v1.u_user |  all v4 : u_UserPayment | alpha.id = v4.u_user_id <=> v4 in alpha.u_userPaymentList } 

 fact { all v0 : u_User20 | all v1 : v0.u_userShippingList | all alpha : v1.u_user |  all v4 : u_UserRole | alpha.id = v4.u_user_id <=> v4 in alpha.u_userRoles } 


 sig u_User18 in u_User {} 
fact{all v0 : u_User | v0.u_username = MethodWontHandleOp <=> v0 in u_User18} 

 fact { all alpha : u_User18 |  all v2 : u_UserPayment | alpha.id = v2.u_user_id <=> v2 in alpha.u_userPaymentList } 

 fact { all v0 : u_User18 | all alpha : v0.u_userPaymentList |  all v3 : u_User | alpha.user_id = v3.u_id <=> v3 in alpha.u_user } 

 fact { all v0 : u_User18 | all v1 : v0.u_userPaymentList | all alpha : v1.u_user |  all v4 : u_ShoppingCart | alpha.id = v4.u_user_id <=> v4 in alpha.u_shoppingCart } 

 fact { all v0 : u_User18 | all v1 : v0.u_userPaymentList | all alpha : v1.u_user |  all v4 : u_UserShipping | alpha.id = v4.u_user_id <=> v4 in alpha.u_userShippingList } 

 fact { all v0 : u_User18 | all v1 : v0.u_userPaymentList | all alpha : v1.u_user |  all v4 : u_Order | alpha.id = v4.u_user_id <=> v4 in alpha.u_userOrderList } 

 fact { all v0 : u_User18 | all v1 : v0.u_userPaymentList | all alpha : v1.u_user |  all v4 : u_UserPayment | alpha.id = v4.u_user_id <=> v4 in alpha.u_userPaymentList } 

 fact { all v0 : u_User18 | all v1 : v0.u_userPaymentList | all alpha : v1.u_user |  all v4 : u_UserRole | alpha.id = v4.u_user_id <=> v4 in alpha.u_userRoles } 

 fact { all v0 : u_User18 | all alpha : v0.u_userPaymentList |  all v3 : u_UserBilling | alpha.id = v3.u_userPayment_id <=> v3 in alpha.u_userBilling } 

 fact { all v0 : u_User18 | all v1 : v0.u_userPaymentList | all alpha : v1.u_userBilling |  all v4 : u_UserPayment | alpha.id = v4.u_userBilling_id <=> v4 in alpha.u_userPayment } 

 fact { all v0 : u_User18 | all v1 : v0.u_userPaymentList | all alpha : v1.u_userBilling |  all v4 : u_Payment | alpha.id = v4.u_userBilling_id <=> v4 in alpha.u_payment } 

fact { u_Pi___Sel_____ClassRe3 = u_Sel___ClassRef_com_b2 }

 sig u_User17 in u_User {} 
fact{all v0 : u_User | v0.u_username = MethodWontHandleOp <=> v0 in u_User17} 

 fact { all alpha : u_User17 |  all v2 : u_Order | alpha.id = v2.u_user_id <=> v2 in alpha.u_userOrderList } 

 fact { all v0 : u_User17 | all alpha : v0.u_userOrderList |  all v3 : u_CartItem | alpha.id = v3.u_order_id <=> v3 in alpha.u_cartItemList } 

 fact { all v0 : u_User17 | all v1 : v0.u_userOrderList | all alpha : v1.u_cartItemList |  all v4 : u_Book | alpha.id = v4.u_cartItem_id <=> v4 in alpha.u_book } 

 fact { all v0 : u_User17 | all v1 : v0.u_userOrderList | all alpha : v1.u_cartItemList |  all v4 : u_BookToCartItem | alpha.id = v4.u_cart_tem_id <=> v4 in alpha.u_bookToCartItemList } 

 fact { all v0 : u_User17 | all v1 : v0.u_userOrderList | all alpha : v1.u_cartItemList |  all v4 : u_ShoppingCart | alpha.shoppingCart_id = v4.u_id <=> v4 in alpha.u_shoppingCart } 

 fact { all v0 : u_User17 | all v1 : v0.u_userOrderList | all alpha : v1.u_cartItemList |  all v4 : u_Order | alpha.order_id = v4.u_id <=> v4 in alpha.u_order } 

 fact { all v0 : u_User17 | all alpha : v0.u_userOrderList |  all v3 : u_ShippingAddress | alpha.id = v3.u_order_id <=> v3 in alpha.u_shippingAddress } 

 fact { all v0 : u_User17 | all v1 : v0.u_userOrderList | all alpha : v1.u_shippingAddress |  all v4 : u_User | alpha.user_id = v4.u_id <=> v4 in alpha.u_user } 

 fact { all v0 : u_User17 | all v1 : v0.u_userOrderList | all alpha : v1.u_shippingAddress |  all v4 : u_Order | alpha.lhs = v4.u_rhs <=> v4 in alpha.u_order } 

 fact { all v0 : u_User17 | all alpha : v0.u_userOrderList |  all v3 : u_Payment | alpha.id = v3.u_order_id <=> v3 in alpha.u_payment } 

 fact { all v0 : u_User17 | all v1 : v0.u_userOrderList | all alpha : v1.u_payment |  all v4 : u_Order | alpha.lhs = v4.u_rhs <=> v4 in alpha.u_order } 

 fact { all v0 : u_User17 | all v1 : v0.u_userOrderList | all alpha : v1.u_payment |  all v4 : u_UserBilling | alpha.lhs = v4.u_rhs <=> v4 in alpha.u_userBilling } 

 fact { all v0 : u_User17 | all alpha : v0.u_userOrderList |  all v3 : u_BillingAddress | alpha.id = v3.u_order_id <=> v3 in alpha.u_billingAddress } 

 fact { all v0 : u_User17 | all v1 : v0.u_userOrderList | all alpha : v1.u_billingAddress |  all v4 : u_Order | alpha.lhs = v4.u_rhs <=> v4 in alpha.u_order } 

 fact { all v0 : u_User17 | all alpha : v0.u_userOrderList |  all v3 : u_User | alpha.user_id = v3.u_id <=> v3 in alpha.u_user } 

 fact { all v0 : u_User17 | all v1 : v0.u_userOrderList | all alpha : v1.u_user |  all v4 : u_ShoppingCart | alpha.id = v4.u_user_id <=> v4 in alpha.u_shoppingCart } 

 fact { all v0 : u_User17 | all v1 : v0.u_userOrderList | all alpha : v1.u_user |  all v4 : u_UserShipping | alpha.id = v4.u_user_id <=> v4 in alpha.u_userShippingList } 

 fact { all v0 : u_User17 | all v1 : v0.u_userOrderList | all alpha : v1.u_user |  all v4 : u_Order | alpha.id = v4.u_user_id <=> v4 in alpha.u_userOrderList } 

 fact { all v0 : u_User17 | all v1 : v0.u_userOrderList | all alpha : v1.u_user |  all v4 : u_UserPayment | alpha.id = v4.u_user_id <=> v4 in alpha.u_userPaymentList } 

 fact { all v0 : u_User17 | all v1 : v0.u_userOrderList | all alpha : v1.u_user |  all v4 : u_UserRole | alpha.id = v4.u_user_id <=> v4 in alpha.u_userRoles } 


 sig u_User23 in u_User {} 
fact{all v0 : u_User | v0.u_username = MethodWontHandleOp <=> v0 in u_User23} 

 fact { all alpha : u_User23 |  all v2 : u_UserPayment | alpha.id = v2.u_user_id <=> v2 in alpha.u_userPaymentList } 

 fact { all v0 : u_User23 | all alpha : v0.u_userPaymentList |  all v3 : u_User | alpha.user_id = v3.u_id <=> v3 in alpha.u_user } 

 fact { all v0 : u_User23 | all v1 : v0.u_userPaymentList | all alpha : v1.u_user |  all v4 : u_ShoppingCart | alpha.id = v4.u_user_id <=> v4 in alpha.u_shoppingCart } 

 fact { all v0 : u_User23 | all v1 : v0.u_userPaymentList | all alpha : v1.u_user |  all v4 : u_UserShipping | alpha.id = v4.u_user_id <=> v4 in alpha.u_userShippingList } 

 fact { all v0 : u_User23 | all v1 : v0.u_userPaymentList | all alpha : v1.u_user |  all v4 : u_Order | alpha.id = v4.u_user_id <=> v4 in alpha.u_userOrderList } 

 fact { all v0 : u_User23 | all v1 : v0.u_userPaymentList | all alpha : v1.u_user |  all v4 : u_UserPayment | alpha.id = v4.u_user_id <=> v4 in alpha.u_userPaymentList } 

 fact { all v0 : u_User23 | all v1 : v0.u_userPaymentList | all alpha : v1.u_user |  all v4 : u_UserRole | alpha.id = v4.u_user_id <=> v4 in alpha.u_userRoles } 

 fact { all v0 : u_User23 | all alpha : v0.u_userPaymentList |  all v3 : u_UserBilling | alpha.id = v3.u_userPayment_id <=> v3 in alpha.u_userBilling } 

 fact { all v0 : u_User23 | all v1 : v0.u_userPaymentList | all alpha : v1.u_userBilling |  all v4 : u_UserPayment | alpha.id = v4.u_userBilling_id <=> v4 in alpha.u_userPayment } 

 fact { all v0 : u_User23 | all v1 : v0.u_userPaymentList | all alpha : v1.u_userBilling |  all v4 : u_Payment | alpha.id = v4.u_userBilling_id <=> v4 in alpha.u_payment } 

fact { u_Pi___Sel_____ClassRe11 = u_Sel___ClassRef_com_b10 }
sig u_Pi___Sel_____ClassRe15 in u_com_bookstore_domain_User {}

sig u_Pi___Sel_____ClassRe7 in u_com_bookstore_domain_User {}

fact { u_Pi___Sel_____ClassRe22 = u_Sel___ClassRef_com_b21 }
sig u_Pi___Sel_____ClassRe9 in u_com_bookstore_domain_User {}

fact { u_Pi___Sel_____ClassRe13 = u_Sel___ClassRef_com_b12 }
sig mu___modelattribute__user_username in univ {}
fact { mu___modelattribute__user_username = u_Pi___Sel_____ClassRe22.u_principalusername }
sig mu___modelattribute__listOfShippingAddresses in univ {}
fact { mu___modelattribute__listOfShippingAddresses = u_1 }
sig mu___modelattribute__user_userRoles in univ {}
fact { mu___modelattribute__user_userRoles =  }
sig mu___modelattribute__user_firstName in univ {}
fact { mu___modelattribute__user_firstName = u_Pi___Sel_____ClassRe7.u_firstName }
sig mu___modelattribute__user_phone in univ {}
fact { mu___modelattribute__user_phone = u_Pi___Sel_____ClassRe15.u_phone }
sig mu___modelattribute__user_enabled in univ {}
fact { mu___modelattribute__user_enabled = u_Pi___Sel_____ClassRe5.u_enabled }
sig mu___modelattribute__user_userOrderList in univ {}
fact { mu___modelattribute__user_userOrderList =  }
sig mu___modelattribute__user_email in univ {}
fact { mu___modelattribute__user_email = u_Pi___Sel_____ClassRe3.u_email }
sig mu___modelattribute__classActiveBilling in univ {}
fact { mu___modelattribute__classActiveBilling = u_1 }
sig mu___modelattribute__user_lastName in univ {}
fact { mu___modelattribute__user_lastName = u_Pi___Sel_____ClassRe11.u_lastName }
sig mu___modelattribute__userShippingList in univ {}
fact { mu___modelattribute__userShippingList =  }
sig mu___modelattribute__user in univ {}
fact { mu___modelattribute__user = u_Sel___ClassRef_com_b1 }
sig mu___modelattribute__user_password in univ {}
fact { mu___modelattribute__user_password = u_Pi___Sel_____ClassRe13.u_password }
sig mu___modelattribute__user_shoppingCart in univ {}
fact { mu___modelattribute__user_shoppingCart =  }
sig mu___modelattribute__user_id in univ {}
fact { mu___modelattribute__user_id = u_Pi___Sel_____ClassRe9.u_id }
sig mu___modelattribute__userPaymentList in univ {}
fact { mu___modelattribute__userPaymentList =  }
sig mu___modelattribute__listOfCreditCards in univ {}
fact { mu___modelattribute__listOfCreditCards = u_1 }
sig mu___modelattribute__user_userPaymentList in univ {}
fact { mu___modelattribute__user_userPaymentList =  }
sig mu___modelattribute__user_userShippingList in univ {}
fact { mu___modelattribute__user_userShippingList =  }
sig BottomNode in FieldData {}
