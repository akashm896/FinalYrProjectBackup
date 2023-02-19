//__modelattribute__orders : mu___modelattribute__orders
//__modelattribute__products : mu___modelattribute__products
sig FieldData {}
one sig NullNode extends FieldData {}
one sig u_productName in FieldData {}
one sig u_productId in FieldData {}
one sig u_productPrice in FieldData {}
one sig u_orderId in FieldData {}
sig u_com_shakeel_model_Product {
u_productprice : FieldData,
u_productname : FieldData,
u_productid : FieldData,
u_orderid : FieldData,
}
sig u_Customer {
u_firstName : FieldData,
u_customerOrder : u_CustomerOrder,
u_orderId : FieldData,
u_customerId : FieldData,
u_lastName : FieldData,
}
sig u_Product {
u_productName : FieldData,
u_productId : FieldData,
u_productPrice : FieldData,
u_orderId : FieldData,
}
sig u_CustomerOrder {
u_productId : FieldData,
u_total : FieldData,
u_orderId : FieldData,
u_customerId : FieldData,
u_products : u_Product,
u_customer : u_Customer,
}
sig u_Sel___ClassRef_com_s2 in u_com_shakeel_model_Product {}
pred meets_selection_criteria_of_u_Sel___ClassRef_com_s2[x: u_com_shakeel_model_Product] {
NullNode
}
fact { all y:u_com_shakeel_model_Product | meets_selection_criteria_of_u_Sel___ClassRef_com_s2[y] <=> y in u_Sel___ClassRef_com_s2 }
fact {
all s:u_Sel___ClassRef_com_s2|some p:u_Pi___Sel_____ClassRe3 {
p.u_productid = u_productId
p.u_productname = u_productName
p.u_productprice = u_productPrice
p.u_orderid = u_orderId
}
all p:u_Pi___Sel_____ClassRe3|some s:u_Sel___ClassRef_com_s2 {
p.u_productid = u_productId
p.u_productname = u_productName
p.u_productprice = u_productPrice
p.u_orderid = u_orderId
}}
sig u_Pi___Sel_____ClassRe3 in u_com_shakeel_model_Product {}


 sig u_CustomerOrder1 in u_CustomerOrder { NullNode } 

 fact { all alpha : u_CustomerOrder1 |  all v2 : u_Product | alpha.u_productId = v2.u_orderId <=> v2 in alpha.u_products } 

 fact { all alpha : u_CustomerOrder1 |  all v2 : u_Customer | alpha.u_orderId = v2.u_customerId <=> v2 in alpha.u_customer } 

 fact { all v0 : u_CustomerOrder1 | all alpha : v0.u_customer |  all v3 : u_CustomerOrder | alpha.u_customerId = v3.u_orderId <=> v3 in alpha.u_customerOrder } 

sig mu___modelattribute__orders in univ {}
fact { mu___modelattribute__orders =  }
sig mu___modelattribute__products in univ {}
fact { mu___modelattribute__products = u_Pi___Sel_____ClassRe3 }
sig BottomNode in FieldData {}
