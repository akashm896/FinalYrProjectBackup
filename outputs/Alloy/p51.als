//__modelattribute__bookList : mu___modelattribute__bookList
sig FieldData {}
sig u_Order {
u_shippingMethod : FieldData,
u_orderTotal : FieldData,
u_user : u_User,
u_payment : u_Payment,
u_cartItemList : u_CartItem,
u_order_id : FieldData,
u_shippingDate : u_Date,
u_id : FieldData,
u_orederStatus : FieldData,
u_user_id : FieldData,
u_billingAddress : u_BillingAddress,
u_orderDate : u_Date,
u_shippingAddress : u_ShippingAddress,
}
sig u_BookToCartItem {
u_id : FieldData,
u_book_id : FieldData,
u_cartItem : u_CartItem,
u_cart_tem_id : FieldData,
u_book : u_Book,
}
sig u_Book {
u_title : FieldData,
u_cartItem : u_CartItem,
u_language : FieldData,
u_category : FieldData,
u_description : FieldData,
u_ourPrice : FieldData,
u_author : FieldData,
u_isbn : FieldData,
u_id : FieldData,
u_bookImage : u_MultipartFile,
u_book_id : FieldData,
u_format : FieldData,
u_active : FieldData,
u_shippingWeight : FieldData,
u_publicationDate : FieldData,
u_bookToCartItemsList : u_BookToCartItem,
u_listPrice : FieldData,
u_inStockNumber : FieldData,
u_publisher : FieldData,
u_numberOfPages : FieldData,
}
sig u_CartItem {
u_qty : FieldData,
u_shoppingCart_id : FieldData,
u_bookToCartItemList : u_BookToCartItem,
u_order_id : FieldData,
u_order : u_Order,
u_id : FieldData,
u_subtotal : FieldData,
u_cart_tem_id : FieldData,
u_book : u_Book,
u_cartItem_id : FieldData,
u_shoppingCart : u_ShoppingCart,
}
sig u_ShoppingCart {
u_shoppingCart_id : FieldData,
u_id : FieldData,
u_GrandTotal : FieldData,
u_user : u_User,
u_cartItemList : u_CartItem,
}

 sig u_Book1 in u_Book { NullNode } 

 fact { all alpha : u_Book1 |  all v2 : u_BookToCartItem | alpha.u_id = v2.u_book_id <=> v2 in alpha.u_bookToCartItemsList } 

 fact { all v0 : u_Book1 | all alpha : v0.u_bookToCartItemsList |  all v3 : u_Book | alpha.u_book_id = v3.u_id <=> v3 in alpha.u_book } 

 fact { all v0 : u_Book1 | all alpha : v0.u_bookToCartItemsList |  all v3 : u_CartItem | alpha.u_cart_tem_id = v3.u_id <=> v3 in alpha.u_cartItem } 

 fact { all alpha : u_Book1 |  all v2 : u_CartItem | alpha.u_id = v2.u_book_id <=> v2 in alpha.u_cartItem } 

 fact { all v0 : u_Book1 | all alpha : v0.u_cartItem |  all v3 : u_Book | alpha.u_id = v3.u_cartItem_id <=> v3 in alpha.u_book } 

 fact { all v0 : u_Book1 | all alpha : v0.u_cartItem |  all v3 : u_BookToCartItem | alpha.u_id = v3.u_cart_tem_id <=> v3 in alpha.u_bookToCartItemList } 

 fact { all v0 : u_Book1 | all alpha : v0.u_cartItem |  all v3 : u_ShoppingCart | alpha.u_shoppingCart_id = v3.u_id <=> v3 in alpha.u_shoppingCart } 

 fact { all v0 : u_Book1 | all alpha : v0.u_cartItem |  all v3 : u_Order | alpha.u_order_id = v3.u_id <=> v3 in alpha.u_order } 

sig mu___modelattribute__bookList in univ {}
fact { mu___modelattribute__bookList =  }
sig BottomNode in FieldData {}
