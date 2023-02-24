//__modelattribute__products : mu___modelattribute__products
sig FieldData {}
one sig NullNode extends FieldData {}
sig u_ProductRepository {
u_productName : FieldData,
u_productId : FieldData,
u_productPrice : FieldData,
u_orderId : FieldData,
}
sig u_Sel1 in u_ProductRepository {}
pred meets_selection_criteria_of_u_Sel1[x: u_ProductRepository] {
NullNode
}
fact { all y:u_ProductRepository | meets_selection_criteria_of_u_Sel1[y] <=> y in u_Sel1 }
sig u_Pi2 in u_ProductRepository {}

u_Product{
productId : FieldData
orderId : FieldData
productName : FieldData
productPrice : FieldData
}
sig mu___modelattribute__products in univ {}
fact { mu___modelattribute__products = u_Pi2 }
sig BottomNode in FieldData {}
