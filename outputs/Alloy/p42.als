//__modelattribute__products : mu___modelattribute__products
sig FieldData {}
one sig u_productName in FieldData {}
one sig u_productId in FieldData {}
one sig u_productPrice in FieldData {}
one sig u_orderId in FieldData {}
sig u_com_shakeel_repository_ProductRepository {
u_productprice : FieldData,
u_productname : FieldData,
u_productid : FieldData,
u_orderid : FieldData,
}
sig u_Sel___ClassRef_com_s1 in u_com_shakeel_repository_ProductRepository {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_s1[x: u_com_shakeel_repository_ProductRepository] {
}
fact { all y:u_com_shakeel_repository_ProductRepository | meets_selection_criteria_of_u_Sel___ClassRef_com_s1[y] <=> y in u_Sel___ClassRef_com_s1 }
fact {
all s:u_Sel___ClassRef_com_s1|some p:u_Pi___Sel_____ClassRe2 {
p.u_productid = s
p.u_productname = s
p.u_productprice = s
p.u_orderid = s
}
all p:u_Pi___Sel_____ClassRe2|some s:u_Sel___ClassRef_com_s1 {
p.u_productid = s
p.u_productname = s
p.u_productprice = s
p.u_orderid = s
}}
sig u_Pi___Sel_____ClassRe2 in u_com_shakeel_repository_ProductRepository {}

sig mu___modelattribute__products in univ {}
fact { mu___modelattribute__products = u_Pi___Sel_____ClassRe2 }
sig BottomNode in FieldData {}
