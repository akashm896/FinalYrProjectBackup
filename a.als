//__modelattribute__cartItemList : mu___modelattribute__cartItemList
//__modelattribute__shoppingCart : mu___modelattribute__shoppingCart
//__modelattribute__shoppingCart.GrandTotal : mu___modelattribute__shoppingCart_GrandTotal
//__modelattribute__shoppingCart.cartItemList : mu___modelattribute__shoppingCart_cartItemList
//__modelattribute__shoppingCart.id : mu___modelattribute__shoppingCart_id
//__modelattribute__shoppingCart.user : mu___modelattribute__shoppingCart_user
//__modelattribute__shoppingCart.user.email : mu___modelattribute__shoppingCart_user_email
//__modelattribute__shoppingCart.user.enabled : mu___modelattribute__shoppingCart_user_enabled
//__modelattribute__shoppingCart.user.firstName : mu___modelattribute__shoppingCart_user_firstName
//__modelattribute__shoppingCart.user.id : mu___modelattribute__shoppingCart_user_id
//__modelattribute__shoppingCart.user.lastName : mu___modelattribute__shoppingCart_user_lastName
//__modelattribute__shoppingCart.user.password : mu___modelattribute__shoppingCart_user_password
//__modelattribute__shoppingCart.user.phone : mu___modelattribute__shoppingCart_user_phone
//__modelattribute__shoppingCart.user.shoppingCart : mu___modelattribute__shoppingCart_user_shoppingCart
//__modelattribute__shoppingCart.user.shoppingCart.GrandTotal : mu___modelattribute__shoppingCart_user_shoppingCart_GrandTotal
//__modelattribute__shoppingCart.user.shoppingCart.cartItemList : mu___modelattribute__shoppingCart_user_shoppingCart_cartItemList
//__modelattribute__shoppingCart.user.shoppingCart.id : mu___modelattribute__shoppingCart_user_shoppingCart_id
//__modelattribute__shoppingCart.user.shoppingCart.user : mu___modelattribute__shoppingCart_user_shoppingCart_user
//__modelattribute__shoppingCart.user.userPaymentList : mu___modelattribute__shoppingCart_user_userPaymentList
//__modelattribute__shoppingCart.user.userRoles : mu___modelattribute__shoppingCart_user_userRoles
//__modelattribute__shoppingCart.user.userShippingList : mu___modelattribute__shoppingCart_user_userShippingList
//__modelattribute__shoppingCart.user.username : mu___modelattribute__shoppingCart_user_username
sig FieldData {}
one sig u_this_shoppingCart_user_shoppingCart_id in FieldData {}
one sig u_this_shoppingCart_user_shoppingCart_user in FieldData {}
one sig u_this_shoppingCart_user_shoppingCart_GrandTotal in FieldData {}
one sig u__r1_shoppingCart_user_shoppingCart in FieldData {}
one sig u_this_shoppingCart_user_shoppingCart_cartItemList in FieldData {}
one sig u_principalusername in FieldData {}
sig u_com_bookstore_repository_CartItemRepository {
}
sig u_com_bookstore_domain_User {
u_firstName : FieldData,
u_firstname : FieldData,
u_enabled : FieldData,
u_id : FieldData,
u_phone : FieldData,
u_email : FieldData,
u_password : FieldData,
u_lastName : FieldData,
u_username : FieldData,
u_lastname : FieldData,
}
sig u_com_bookstore_domain_UserShipping {
}
sig u_com_bookstore_domain_ShoppingCart {
u_id : FieldData,
u_GrandTotal : FieldData,
}
sig u_com_bookstore_domain_CartItem {
u_qty : FieldData,
u_id : FieldData,
u_subtotal : FieldData,
}
sig u_com_bookstore_domain_security_UserRole {
}
sig u_com_bookstore_domain_UserPayment {
}
sig u_Sel___ClassRef_com_b53 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b53[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b53[y] <=> y in u_Sel___ClassRef_com_b53 }
sig u_Sel___ClassRef_com_b44 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b44[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b44[y] <=> y in u_Sel___ClassRef_com_b44 }
sig u_Sel___ClassRef_com_b60 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b60[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b60[y] <=> y in u_Sel___ClassRef_com_b60 }
sig u_Sel___ClassRef_com_b24 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b24[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b24[y] <=> y in u_Sel___ClassRef_com_b24 }
sig u_Sel___ClassRef_com_b4 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b4[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b4[y] <=> y in u_Sel___ClassRef_com_b4 }
sig u_Sel___ClassRef_com_b57 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b57[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b57[y] <=> y in u_Sel___ClassRef_com_b57 }
sig u_Sel___ClassRef_com_b20 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b20[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b20[y] <=> y in u_Sel___ClassRef_com_b20 }
sig u_Sel___ClassRef_com_b40 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b40[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b40[y] <=> y in u_Sel___ClassRef_com_b40 }
sig u_Sel___ClassRef_com_b32 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b32[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b32[y] <=> y in u_Sel___ClassRef_com_b32 }
sig u_Sel___ClassRef_com_b12 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b12[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b12[y] <=> y in u_Sel___ClassRef_com_b12 }
sig u_Sel___ClassRef_com_b49 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b49[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b49[y] <=> y in u_Sel___ClassRef_com_b49 }
sig u_Sel___ClassRef_com_b16 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b16[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b16[y] <=> y in u_Sel___ClassRef_com_b16 }
sig u_Sel___ClassRef_com_b9 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b9[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b9[y] <=> y in u_Sel___ClassRef_com_b9 }
sig u_Sel___ClassRef_com_b36 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b36[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b36[y] <=> y in u_Sel___ClassRef_com_b36 }
sig u_Sel___ClassRef_com_b28 in u_com_bookstore_domain_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b28[x: u_com_bookstore_domain_User] {
x.u_username = u_principalusername
}
fact { all y:u_com_bookstore_domain_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b28[y] <=> y in u_Sel___ClassRef_com_b28 }
sig u_Join___Sel_____Class52 in u_com_bookstore_domain_ShoppingCart {}
fact { u_Join___Sel_____Class52 = u_Sel___ClassRef_com_b53.u_com_bookstore_domain_ShoppingCart_c }

sig u_User_shoppingCart=Pi2 in u_com_bookstore_domain_ShoppingCart {}

sig u_Join___Join_____Sel_30 in u_com_bookstore_domain_User {}
fact { u_Join___Join_____Sel_30 = u_Join___Sel_____Class31.u_com_bookstore_domain_User_c }

sig u_Pi___Join_____Join__29 in u_com_bookstore_domain_User {}

fact { u_Pi___Join_____Join__33 = u_Join___Join_____Sel_30 }
sig u_Pi___Join_____Join__25 in u_com_bookstore_domain_User {}

sig u_Join___Sel_____Class8 in u_com_bookstore_domain_ShoppingCart {}
fact { u_Join___Sel_____Class8 = u_Sel___ClassRef_com_b9.u_com_bookstore_domain_ShoppingCart_c }

fact { u_Pi___Join_____Join__29 = u_Join___Join_____Sel_26 }
sig u_Join___Sel_____Class31 in u_com_bookstore_domain_ShoppingCart {}
fact { u_Join___Sel_____Class31 = u_Sel___ClassRef_com_b32.u_com_bookstore_domain_ShoppingCart_c }

fact { u_Pi___Join_____Join__21 = u_Join___Join_____Sel_18 }
sig u_Join___Join_____Sel_58 in u_com_bookstore_domain_User {}
fact { u_Join___Join_____Sel_58 = u_Join___Sel_____Class59.u_com_bookstore_domain_User_c }

sig u_Pi___Join_____Join__21 in u_com_bookstore_domain_User {}

sig u_Join___Sel_____Class11 in u_com_bookstore_domain_ShoppingCart {}
fact { u_Join___Sel_____Class11 = u_Sel___ClassRef_com_b12.u_com_bookstore_domain_ShoppingCart_c }

sig u_Join___Sel_____Class43 in u_com_bookstore_domain_ShoppingCart {}
fact { u_Join___Sel_____Class43 = u_Sel___ClassRef_com_b44.u_com_bookstore_domain_ShoppingCart_c }

sig u_Join___Join_____Sel_42 in u_com_bookstore_domain_User {}
fact { u_Join___Join_____Sel_42 = u_Join___Sel_____Class43.u_com_bookstore_domain_User_c }

fact { u_Pi___Join_____Sel___13 = u_Join___Sel_____Class11 }
sig u_Join___Join_____Sel_51 in u_com_bookstore_domain_User {}
fact { u_Join___Join_____Sel_51 = u_Join___Sel_____Class52.u_com_bookstore_domain_User_c }

sig u_Pi___Join_____Join__37 in u_com_bookstore_domain_User {}

sig u_Join___Join_____Sel_55 in u_com_bookstore_domain_User {}
fact { u_Join___Join_____Sel_55 = u_Join___Sel_____Class56.u_com_bookstore_domain_User_c }

sig u_Join___Sel_____Class27 in u_com_bookstore_domain_ShoppingCart {}
fact { u_Join___Sel_____Class27 = u_Sel___ClassRef_com_b28.u_com_bookstore_domain_ShoppingCart_c }

sig u_Join___Sel_____Class56 in u_com_bookstore_domain_ShoppingCart {}
fact { u_Join___Sel_____Class56 = u_Sel___ClassRef_com_b57.u_com_bookstore_domain_ShoppingCart_c }

sig u_Join___Join_____Sel_7 in u_com_bookstore_domain_CartItem {}
fact { u_Join___Join_____Sel_7 = u_Join___Sel_____Class8.u_com_bookstore_domain_CartItem_c }

lone sig u_UnknownNode6 in FieldData {}

sig u_Join___Join_____Join50 in u_com_bookstore_domain_security_UserRole {}
fact { u_Join___Join_____Join50 = u_Join___Join_____Sel_51.u_com_bookstore_domain_security_UserRole_c }

sig u_Pi___Join_____Join__33 in u_com_bookstore_domain_User {}

sig u_Pi___Join_____Join__41 in u_com_bookstore_domain_User {}

sig u_Join___Join_____Join46 in u_com_bookstore_domain_UserPayment {}
fact { u_Join___Join_____Join46 = u_Join___Join_____Sel_47.u_com_bookstore_domain_UserPayment_c }

sig u_Pi___Join_____Join__45 in u_com_bookstore_domain_User {}

sig u_Join___Join_____Sel_18 in u_com_bookstore_domain_User {}
fact { u_Join___Join_____Sel_18 = u_Join___Sel_____Class19.u_com_bookstore_domain_User_c }

sig u_Join___Join_____Sel_14 in u_com_bookstore_domain_User {}
fact { u_Join___Join_____Sel_14 = u_Join___Sel_____Class15.u_com_bookstore_domain_User_c }

sig u_Join___Sel_____Class19 in u_com_bookstore_domain_ShoppingCart {}
fact { u_Join___Sel_____Class19 = u_Sel___ClassRef_com_b20.u_com_bookstore_domain_ShoppingCart_c }

sig u_ShoppingCart_user=Pi17 in u_com_bookstore_domain_User {}

sig u_Join___Join_____Sel_47 in u_com_bookstore_domain_User {}
fact { u_Join___Join_____Sel_47 = u_Join___Sel_____Class48.u_com_bookstore_domain_User_c }

sig u_User_shoppingCart=Pi5 in u_com_bookstore_domain_ShoppingCart {}

sig u_Join___Sel_____Class48 in u_com_bookstore_domain_ShoppingCart {}
fact { u_Join___Sel_____Class48 = u_Sel___ClassRef_com_b49.u_com_bookstore_domain_ShoppingCart_c }

sig u_Join___Join_____Sel_26 in u_com_bookstore_domain_User {}
fact { u_Join___Join_____Sel_26 = u_Join___Sel_____Class27.u_com_bookstore_domain_User_c }

sig u_Join___Sel_____Class15 in u_com_bookstore_domain_ShoppingCart {}
fact { u_Join___Sel_____Class15 = u_Sel___ClassRef_com_b16.u_com_bookstore_domain_ShoppingCart_c }

fact { u_Pi___Join_____Join__37 = u_Join___Join_____Sel_34 }
sig u_Pi___Join_____Sel___13 in u_com_bookstore_domain_ShoppingCart {}

sig u_Join___Join_____Sel_22 in u_com_bookstore_domain_User {}
fact { u_Join___Join_____Sel_22 = u_Join___Sel_____Class23.u_com_bookstore_domain_User_c }

fact { u_Pi___Join_____Join__61 = u_Join___Join_____Sel_58 }
sig u_ShoppingCart_cartIte10 in u_com_bookstore_domain_CartItem {}

sig u_Join___Sel_____Class23 in u_com_bookstore_domain_ShoppingCart {}
fact { u_Join___Sel_____Class23 = u_Sel___ClassRef_com_b24.u_com_bookstore_domain_ShoppingCart_c }

sig u_Join___Sel_____Class3 in u_com_bookstore_domain_ShoppingCart {}
fact { u_Join___Sel_____Class3 = u_Sel___ClassRef_com_b4.u_com_bookstore_domain_ShoppingCart_c }

sig u_Join___Sel_____Class39 in u_com_bookstore_domain_ShoppingCart {}
fact { u_Join___Sel_____Class39 = u_Sel___ClassRef_com_b40.u_com_bookstore_domain_ShoppingCart_c }

sig u_Join___Join_____Join54 in u_com_bookstore_domain_UserShipping {}
fact { u_Join___Join_____Join54 = u_Join___Join_____Sel_55.u_com_bookstore_domain_UserShipping_c }

fact { u_Pi___Join_____Join__45 = u_Join___Join_____Sel_42 }
sig u_Join___User_shopping1 in u_com_bookstore_repository_CartItemRepository {}
fact { u_Join___User_shopping1 = u_User_shoppingCart=Pi2.u_com_bookstore_repository_CartItemRepository_c }

sig u_Pi___Join_____Join__61 in u_com_bookstore_domain_User {}

sig u_Join___Join_____Sel_34 in u_com_bookstore_domain_User {}
fact { u_Join___Join_____Sel_34 = u_Join___Sel_____Class35.u_com_bookstore_domain_User_c }

fact { u_Pi___Join_____Join__41 = u_Join___Join_____Sel_38 }
sig u_Join___Sel_____Class35 in u_com_bookstore_domain_ShoppingCart {}
fact { u_Join___Sel_____Class35 = u_Sel___ClassRef_com_b36.u_com_bookstore_domain_ShoppingCart_c }

sig u_Join___Join_____Sel_38 in u_com_bookstore_domain_User {}
fact { u_Join___Join_____Sel_38 = u_Join___Sel_____Class39.u_com_bookstore_domain_User_c }

fact { u_Pi___Join_____Join__25 = u_Join___Join_____Sel_22 }
sig u_Join___Sel_____Class59 in u_com_bookstore_domain_ShoppingCart {}
fact { u_Join___Sel_____Class59 = u_Sel___ClassRef_com_b60.u_com_bookstore_domain_ShoppingCart_c }

sig mu___modelattribute__shoppingCart_user_username in univ {}
fact { mu___modelattribute__shoppingCart_user_username = u_Pi___Join_____Join__61.u_username }
sig mu___modelattribute__shoppingCart_user_firstName in univ {}
fact { mu___modelattribute__shoppingCart_user_firstName = u_Pi___Join_____Join__29.u_firstname }
sig mu___modelattribute__shoppingCart_cartItemList in univ {}
fact { mu___modelattribute__shoppingCart_cartItemList = u_ShoppingCart_cartIte10 }
sig mu___modelattribute__shoppingCart_id in univ {}
fact { mu___modelattribute__shoppingCart_id = u_Pi___Join_____Sel___13.u_id }
sig mu___modelattribute__shoppingCart_user_shoppingCart_user in univ {}
fact { mu___modelattribute__shoppingCart_user_shoppingCart_user = u_this_shoppingCart_user_shoppingCart_user }
sig mu___modelattribute__shoppingCart_user_userRoles in univ {}
fact { mu___modelattribute__shoppingCart_user_userRoles = u_Join___Join_____Join50 }
sig mu___modelattribute__shoppingCart_user_userShippingList in univ {}
fact { mu___modelattribute__shoppingCart_user_userShippingList = u_Join___Join_____Join54 }
sig mu___modelattribute__shoppingCart_user_lastName in univ {}
fact { mu___modelattribute__shoppingCart_user_lastName = u_Pi___Join_____Join__37.u_lastname }
sig mu___modelattribute__cartItemList in univ {}
fact { mu___modelattribute__cartItemList = u_Join___User_shopping1 }
sig mu___modelattribute__shoppingCart_user_password in univ {}
fact { mu___modelattribute__shoppingCart_user_password = u_Pi___Join_____Join__41.u_password }
sig mu___modelattribute__shoppingCart_user_shoppingCart_cartItemList in univ {}
fact { mu___modelattribute__shoppingCart_user_shoppingCart_cartItemList = u_this_shoppingCart_user_shoppingCart_cartItemList }
sig mu___modelattribute__shoppingCart_user_shoppingCart in univ {}
fact { mu___modelattribute__shoppingCart_user_shoppingCart = u__r1_shoppingCart_user_shoppingCart }
sig mu___modelattribute__shoppingCart_user_shoppingCart_GrandTotal in univ {}
fact { mu___modelattribute__shoppingCart_user_shoppingCart_GrandTotal = u_this_shoppingCart_user_shoppingCart_GrandTotal }
sig mu___modelattribute__shoppingCart_user_id in univ {}
fact { mu___modelattribute__shoppingCart_user_id = u_Pi___Join_____Join__33.u_id }
sig mu___modelattribute__shoppingCart_user_phone in univ {}
fact { mu___modelattribute__shoppingCart_user_phone = u_Pi___Join_____Join__45.u_phone }
sig mu___modelattribute__shoppingCart_GrandTotal in univ {}
fact { mu___modelattribute__shoppingCart_GrandTotal = u_UnknownNode6 }
sig mu___modelattribute__shoppingCart_user_userPaymentList in univ {}
fact { mu___modelattribute__shoppingCart_user_userPaymentList = u_Join___Join_____Join46 }
sig mu___modelattribute__shoppingCart_user_shoppingCart_id in univ {}
fact { mu___modelattribute__shoppingCart_user_shoppingCart_id = u_this_shoppingCart_user_shoppingCart_id }
sig mu___modelattribute__shoppingCart_user_email in univ {}
fact { mu___modelattribute__shoppingCart_user_email = u_Pi___Join_____Join__21.u_email }
sig mu___modelattribute__shoppingCart_user_enabled in univ {}
fact { mu___modelattribute__shoppingCart_user_enabled = u_Pi___Join_____Join__25.u_enabled }
sig mu___modelattribute__shoppingCart in univ {}
fact { mu___modelattribute__shoppingCart = u_User_shoppingCart=Pi2 }
sig mu___modelattribute__shoppingCart_user in univ {}
fact { mu___modelattribute__shoppingCart_user = u_ShoppingCart_user=Pi17 }
sig BottomNode in FieldData {}
