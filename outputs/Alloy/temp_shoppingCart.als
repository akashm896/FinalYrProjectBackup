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
//__modelattribute__shoppingCart.user.userPaymentList : mu___modelattribute__shoppingCart_user_userPaymentList
//__modelattribute__shoppingCart.user.userRoles : mu___modelattribute__shoppingCart_user_userRoles
//__modelattribute__shoppingCart.user.userShippingList : mu___modelattribute__shoppingCart_user_userShippingList
//__modelattribute__shoppingCart.user.username : mu___modelattribute__shoppingCart_user_username
sig FieldData {}
one sig u_this_shoppingCart_user_lastName in FieldData {}
one sig u_this_shoppingCart_user_phone in FieldData {}
one sig u_this_shoppingCart_user_email in FieldData {}
one sig u_this_shoppingCart_user_enabled in FieldData {}
one sig u_this_shoppingCart_user_userRoles in FieldData {}
one sig u_this_shoppingCart_user_id in FieldData {}
one sig u_this_shoppingCart_user_password in FieldData {}
one sig u_this_shoppingCart_user_username in FieldData {}
one sig u_this_shoppingCart_user_userPaymentList in FieldData {}
one sig u_this_shoppingCart_user_userShippingList in FieldData {}
one sig u_this_shoppingCart_user_firstName in FieldData {}
one sig u_this_shoppingCart_user_shoppingCart in FieldData {}
one sig u_principalusername in FieldData {}
sig u_CartItem {
u_qty : FieldData,
u_id : FieldData,
u_subtotal : FieldData,
}
sig u_ShoppingCart {
u_id : FieldData,
u_GrandTotal : FieldData,
u_user : u_User,
u_cartItemList : u_CartItem,
}
sig u_CartItemRepository {
}
sig u_User {
u_firstName : FieldData,
u_enabled : FieldData,
u_id : FieldData,
u_phone : FieldData,
u_email : FieldData,
u_password : FieldData,
u_lastName : FieldData,
u_shoppingCart : u_ShoppingCart,
u_username : FieldData,
}
sig u_Sel___ClassRef_com_b2 in u_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b2[x: u_User] {
x.u_username = u_principalusername
}
fact { all y:u_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b2[y] <=> y in u_Sel___ClassRef_com_b2 }
sig u_Sel___ClassRef_com_b7 in u_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b7[x: u_User] {
x.u_username = u_principalusername
}
fact { all y:u_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b7[y] <=> y in u_Sel___ClassRef_com_b7 }
sig u_Sel___ClassRef_com_b9 in u_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b9[x: u_User] {
x.u_username = u_principalusername
}
fact { all y:u_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b9[y] <=> y in u_Sel___ClassRef_com_b9 }
sig u_Sel___ClassRef_com_b5 in u_User {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b5[x: u_User] {
x.u_username = u_principalusername
}
fact { all y:u_User | meets_selection_criteria_of_u_Sel___ClassRef_com_b5[y] <=> y in u_Sel___ClassRef_com_b5 }
sig u_shoppingCart in u_ShoppingCart {}

sig u_Pi___Join_____Sel___8 in u_ShoppingCart {}

sig u_user in u_User {}

lone sig u_UnknownNode4 in FieldData {}

sig u_Join_u_Sel___ClassRef_com_b5_u_ShoppingCart in u_ShoppingCart {}
fact { u_Join_u_Sel___ClassRef_com_b5_u_ShoppingCart = u_Sel___ClassRef_com_b5.u_ShoppingCart_c }

sig u_Join_u_shoppingCart_u_CartItemRepository in u_CartItemRepository {}
fact { u_Join_u_shoppingCart_u_CartItemRepository = u_shoppingCart.u_CartItemRepository_c }

sig u_Join_u_Sel___ClassRef_com_b9_u_ShoppingCart in u_ShoppingCart {}
fact { u_Join_u_Sel___ClassRef_com_b9_u_ShoppingCart = u_Sel___ClassRef_com_b9.u_ShoppingCart_c }

sig u_Join_u_Join_u_Sel___ClassRef_com_b9_u_ShoppingCart_u_User in u_User {}
fact { u_Join_u_Join_u_Sel___ClassRef_com_b9_u_ShoppingCart_u_User = u_Join_u_Sel___ClassRef_com_b9_u_ShoppingCart.u_User_c }

sig u_Join_u_Sel___ClassRef_com_b2_u_ShoppingCart in u_ShoppingCart {}
fact { u_Join_u_Sel___ClassRef_com_b2_u_ShoppingCart = u_Sel___ClassRef_com_b2.u_ShoppingCart_c }

fact { u_Pi___Join_____Sel___8 = u_Join_u_Sel___ClassRef_com_b7_u_ShoppingCart }
sig u_Join_u_Join_u_Sel___ClassRef_com_b5_u_ShoppingCart_u_CartItem in u_CartItem {}
fact { u_Join_u_Join_u_Sel___ClassRef_com_b5_u_ShoppingCart_u_CartItem = u_Join_u_Sel___ClassRef_com_b5_u_ShoppingCart.u_CartItem_c }

sig u_Join_u_Sel___ClassRef_com_b7_u_ShoppingCart in u_ShoppingCart {}
fact { u_Join_u_Sel___ClassRef_com_b7_u_ShoppingCart = u_Sel___ClassRef_com_b7.u_ShoppingCart_c }

sig u_ShoppingCart_cartIte6 in u_CartItem {}

sig mu___modelattribute__shoppingCart_user_username in univ {}
fact { mu___modelattribute__shoppingCart_user_username = u_this_shoppingCart_user_username }
sig mu___modelattribute__shoppingCart_user_firstName in univ {}
fact { mu___modelattribute__shoppingCart_user_firstName = u_this_shoppingCart_user_firstName }
sig mu___modelattribute__shoppingCart_cartItemList in univ {}
fact { mu___modelattribute__shoppingCart_cartItemList = u_ShoppingCart_cartIte6 }
sig mu___modelattribute__shoppingCart_id in univ {}
fact { mu___modelattribute__shoppingCart_id = u_Pi___Join_____Sel___8.u_id }
sig mu___modelattribute__shoppingCart_user_userRoles in univ {}
fact { mu___modelattribute__shoppingCart_user_userRoles = u_this_shoppingCart_user_userRoles }
sig mu___modelattribute__shoppingCart_user_userShippingList in univ {}
fact { mu___modelattribute__shoppingCart_user_userShippingList = u_this_shoppingCart_user_userShippingList }
sig mu___modelattribute__shoppingCart_user_lastName in univ {}
fact { mu___modelattribute__shoppingCart_user_lastName = u_this_shoppingCart_user_lastName }
sig mu___modelattribute__cartItemList in univ {}
fact { mu___modelattribute__cartItemList = u_Join_u_shoppingCart_u_CartItemRepository }
sig mu___modelattribute__shoppingCart_user_password in univ {}
fact { mu___modelattribute__shoppingCart_user_password = u_this_shoppingCart_user_password }
sig mu___modelattribute__shoppingCart_user_shoppingCart in univ {}
fact { mu___modelattribute__shoppingCart_user_shoppingCart = u_this_shoppingCart_user_shoppingCart }
sig mu___modelattribute__shoppingCart_user_id in univ {}
fact { mu___modelattribute__shoppingCart_user_id = u_this_shoppingCart_user_id }
sig mu___modelattribute__shoppingCart_user_phone in univ {}
fact { mu___modelattribute__shoppingCart_user_phone = u_this_shoppingCart_user_phone }
sig mu___modelattribute__shoppingCart_GrandTotal in univ {}
fact { mu___modelattribute__shoppingCart_GrandTotal = u_UnknownNode4 }
sig mu___modelattribute__shoppingCart_user_userPaymentList in univ {}
fact { mu___modelattribute__shoppingCart_user_userPaymentList = u_this_shoppingCart_user_userPaymentList }
sig mu___modelattribute__shoppingCart_user_email in univ {}
fact { mu___modelattribute__shoppingCart_user_email = u_this_shoppingCart_user_email }
sig mu___modelattribute__shoppingCart_user_enabled in univ {}
fact { mu___modelattribute__shoppingCart_user_enabled = u_this_shoppingCart_user_enabled }
sig mu___modelattribute__shoppingCart in univ {}
fact { mu___modelattribute__shoppingCart = u_shoppingCart }
sig mu___modelattribute__shoppingCart_user in univ {}
fact { mu___modelattribute__shoppingCart_user = u_user }



sig BottomNode in FieldData {}
