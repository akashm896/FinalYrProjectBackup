//__modelattribute__orders : mu___modelattribute__orders
//__modelattribute__products : mu___modelattribute__products
sig FieldData {}
one sig u_productName in FieldData {}
one sig u_productId in FieldData {}
one sig u_productPrice in FieldData {}
one sig u_orderId in FieldData {}
sig u_Customer {
firstName : FieldData,
lastName : FieldData,
orderId : FieldData,
customerId : FieldData,
u_customerOrder : u_CustomerOrder,
}
sig u_Product {
productId : FieldData,
orderId : FieldData,
productName : FieldData,
productPrice : FieldData,
}
sig u_CustomerOrder {
total : FieldData,
productId : FieldData,
orderId : FieldData,
customerId : FieldData,
u_products : u_Product,
u_customer : u_Customer,
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


 sig u_Order1 in u_Order {} 

 fact { all v1 : u_Product | alpha.productId = v1.u_orderId <=> v1 in alpha.u_products } 

 fact { all v1 : u_Customer | alpha.orderId = v1.u_customerId <=> v1 in alpha.u_customer } 

 fact { all alpha : u_customer |  all v2 : u_CustomerOrder | alpha.customerId = v2.u_orderId <=> v2 in alpha.u_customerOrder } 

sig mu___modelattribute__orders in univ {}
fact { mu___modelattribute__orders =  }
sig mu___modelattribute__products in univ {}
fact { mu___modelattribute__products = u_Pi___Sel_____ClassRe3 }
sig BottomNode in FieldData {}
