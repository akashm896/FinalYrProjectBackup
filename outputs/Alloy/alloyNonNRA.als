//__modelattribute__products : mu___modelattribute__products
sig FieldData {}
one sig NullNode extends FieldData {}
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
NullNode
}
fact { all y:u_com_shakeel_repository_ProductRepository | meets_selection_criteria_of_u_Sel___ClassRef_com_s1[y] <=> y in u_Sel___ClassRef_com_s1 }
fact {
all s:u_Sel___ClassRef_com_s1|some p:u_Pi___Sel_____ClassRe2 {
p.u_productid = u_productId
p.u_productname = u_productName
p.u_productprice = u_productPrice
p.u_orderid = u_orderId
}
all p:u_Pi___Sel_____ClassRe2|some s:u_Sel___ClassRef_com_s1 {
p.u_productid = u_productId
p.u_productname = u_productName
p.u_productprice = u_productPrice
p.u_orderid = u_orderId
}}
sig u_Pi___Sel_____ClassRe2 in u_com_shakeel_repository_ProductRepository {}

sig mu___modelattribute__products in univ {}
fact { mu___modelattribute__products = u_Pi___Sel_____ClassRe2 }
sig BottomNode in FieldData {}
