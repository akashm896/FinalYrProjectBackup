//__modelattribute__products : mu___modelattribute__products
sig FieldData {}
one sig NullNode extends FieldData {}
one sig u_productName in FieldData {}
one sig u_productId in FieldData {}
one sig u_productPrice in FieldData {}
sig u_com_shakeel_model_Product {
u_productprice : FieldData,
u_productname : FieldData,
u_productid : FieldData,
}
sig u_Sel___ClassRef_com_s1 in u_com_shakeel_model_Product {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_s1[x: u_com_shakeel_model_Product] {
NullNode
}
fact { all y:u_com_shakeel_model_Product | meets_selection_criteria_of_u_Sel___ClassRef_com_s1[y] <=> y in u_Sel___ClassRef_com_s1 }
fact {
all s:u_Sel___ClassRef_com_s1|some p:u_Pi___Sel_____ClassRe2 {
p.u_productid = u_productId
p.u_productname = u_productName
p.u_productprice = u_productPrice
}
all p:u_Pi___Sel_____ClassRe2|some s:u_Sel___ClassRef_com_s1 {
p.u_productid = u_productId
p.u_productname = u_productName
p.u_productprice = u_productPrice
}}
sig u_Pi___Sel_____ClassRe2 in u_com_shakeel_model_Product {}

sig mu___modelattribute__products in univ {}
fact { mu___modelattribute__products = u_Pi___Sel_____ClassRe2 }
sig BottomNode in FieldData {}
