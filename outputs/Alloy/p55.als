//$r1.cartItemRepository : mu__r1_cartItemRepository
sig FieldData {}
one sig u_id in FieldData {}
sig u_ts__r1_cartItemRepository {
u_qty : FieldData,
u_id : FieldData,
u_subtotal : FieldData,
}
sig u_com_bookstore_domain_CartItem {
u_qty : FieldData,
u_id : FieldData,
u_subtotal : FieldData,
}
sig u__r1_cartItemRepository in u_ts__r1_cartItemRepository {
}
sig u_Sel___ClassRef_com_b4 in u_com_bookstore_domain_CartItem {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b4[x: u_com_bookstore_domain_CartItem] {
x.u_id = u_id
}
fact { all y:u_com_bookstore_domain_CartItem | meets_selection_criteria_of_u_Sel___ClassRef_com_b4[y] <=> y in u_Sel___ClassRef_com_b4 }
sig u_Sel___ClassRef_com_b6 in u_com_bookstore_domain_CartItem {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b6[x: u_com_bookstore_domain_CartItem] {
x.u_id = u_id
}
fact { all y:u_com_bookstore_domain_CartItem | meets_selection_criteria_of_u_Sel___ClassRef_com_b6[y] <=> y in u_Sel___ClassRef_com_b6 }
sig u_Sel___ClassRef_com_b2 in u_com_bookstore_domain_CartItem {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_b2[x: u_com_bookstore_domain_CartItem] {
x.u_id = u_id
}
fact { all y:u_com_bookstore_domain_CartItem | meets_selection_criteria_of_u_Sel___ClassRef_com_b2[y] <=> y in u_Sel___ClassRef_com_b2 }
sig u_Pi___Sel_____ClassRe3 in u_com_bookstore_domain_CartItem {}

fact { u_Pi___Sel_____ClassRe3 = u_Sel___ClassRef_com_b2 }
sig u_Pi___Sel_____ClassRe7 in u_com_bookstore_domain_CartItem {}

fact { u_Pi___Sel_____ClassRe7 = u_Sel___ClassRef_com_b6 }
one sig u_List___Pi_____Sel___1 in u_ts__r1_cartItemRepository {}fact { u_List___Pi_____Sel___1.u_id = u_Pi___Sel_____ClassRe3.u_id }
fact { u_List___Pi_____Sel___1.u_qty = u_Pi___Sel_____ClassRe5.u_qty }
fact { u_List___Pi_____Sel___1.u_subtotal = u_Pi___Sel_____ClassRe7.u_subtotal }
sig u_RelationalMinus____r8 in u__r1_cartItemRepository {}
fact { u_RelationalMinus____r8 = u__r1_cartItemRepository - u_List___Pi_____Sel___1 }
fact { u_Pi___Sel_____ClassRe5 = u_Sel___ClassRef_com_b4 }
sig u_Pi___Sel_____ClassRe5 in u_com_bookstore_domain_CartItem {}

sig mu__r1_cartItemRepository in univ {}
fact { mu__r1_cartItemRepository = u_RelationalMinus____r8 }
sig BottomNode in FieldData {}
