//__modelattribute__orders : mu___modelattribute__orders
//__modelattribute__products : mu___modelattribute__products
sig FieldData {}
one sig u_productName in FieldData {}
one sig u_productId in FieldData {}
one sig u_productPrice in FieldData {}
one sig u_orderId in FieldData {}
sig u_com_shakeel_repository_OrderRepository {
u_productId : FieldData,
u_total : FieldData,
u_orderId : FieldData,
u_products : u_Product,
u_customer : u_Customer,
}
sig u_Product {
u_productName : FieldData,
u_productId : FieldData,
u_productPrice : FieldData,
u_orderId : FieldData,
}
sig u_Customer {
u_firstName : FieldData,
u_customerOrder : u_CustomerOrder,
u_customerId : FieldData,
u_lastName : FieldData,
}
sig u_CustomerOrder {
u_total : FieldData,
u_orderId : FieldData,
}
sig u_com_shakeel_repository_ProductRepository {
u_productprice : FieldData,
u_productname : FieldData,
u_productid : FieldData,
u_orderid : FieldData,
}
sig u_Sel___ClassRef_com_s2 in u_com_shakeel_repository_ProductRepository {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_s2[x: u_com_shakeel_repository_ProductRepository] {
}
fact { all y:u_com_shakeel_repository_ProductRepository | meets_selection_criteria_of_u_Sel___ClassRef_com_s2[y] <=> y in u_Sel___ClassRef_com_s2 }
sig u_Sel___ClassRef_com_s1 in u_com_shakeel_repository_OrderRepository {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_s1[x: u_com_shakeel_repository_OrderRepository] {
}
fact { all y:u_com_shakeel_repository_OrderRepository | meets_selection_criteria_of_u_Sel___ClassRef_com_s1[y] <=> y in u_Sel___ClassRef_com_s1 }
fact {
all s:u_Sel___ClassRef_com_s2|some p:u_Pi___Sel_____ClassRe3 {
p.u_productid = s
p.u_productname = s
p.u_productprice = s
p.u_orderid = s
}
all p:u_Pi___Sel_____ClassRe3|some s:u_Sel___ClassRef_com_s2 {
p.u_productid = s
p.u_productname = s
p.u_productprice = s
p.u_orderid = s
}}
sig u_Pi___Sel_____ClassRe3 in u_com_shakeel_repository_ProductRepository {}

fact {  all v0 : u_com_shakeel_repository_OrderRepository |  all v1 : u_Customer |  all v2 : u_CustomerOrder | v1.u_customerId = v2.u_orderId <=> v2 in v1.u_customerOrder }
fact {  all v0 : u_com_shakeel_repository_OrderRepository |  all v1 : u_Product | v0.u_productId = v1.u_orderId <=> v1 in v0.u_products }
fact {  all v0 : u_com_shakeel_repository_OrderRepository |  all v1 : u_Customer | v0.u_orderId = v1.u_customerId <=> v1 in v0.u_customer }
sig mu___modelattribute__orders in univ {}
fact { mu___modelattribute__orders = u_Sel___ClassRef_com_s1 }
sig mu___modelattribute__products in univ {}
fact { mu___modelattribute__products = u_Pi___Sel_____ClassRe3 }
sig BottomNode in FieldData {}
