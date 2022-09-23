//__modelattribute__addBookSuccess : mu___modelattribute__addBookSuccess
//__modelattribute__notEnoughStrock : mu___modelattribute__notEnoughStrock
sig FieldData {}
one sig u_1 extends FieldData {}
one sig u_MethodWontHandleOp2 in Int {}
one sig u___modelattribute__notEnoughStrock in FieldData {}
one sig u_book_id in FieldData {}
one sig u___modelattribute__addBookSuccess in FieldData {}
sig u_Book {
u_id : FieldData,
u_book_id : FieldData,
u_inStockNumber : FieldData,
}
sig u_Sel4 in u_Book {}
pred meets_selection_criteria_of_u_Sel4[x: u_Book] {
x.u_id = u_book_id
}
fact { all y:u_Book | meets_selection_criteria_of_u_Sel4[y] <=> y in u_Sel4 }
sig u_Sel3 in u_Book {}
pred meets_selection_criteria_of_u_Sel3[x: u_Book] {
x.u_id = u_Pi5.u_book_id
}
fact { all y:u_Book | meets_selection_criteria_of_u_Sel3[y] <=> y in u_Sel3 }
fact { u__1 = ((u_MethodWontHandleOp2 > u_Pi6.u_inStockNumber) => (u___modelattribute__addBookSuccess) else (u_1)) }
sig u__7 in univ {}
sig u_Pi6 in u_Book {}

fact { u__7 = ((u_MethodWontHandleOp2 > u_Pi6.u_inStockNumber) => (u_1) else (u___modelattribute__notEnoughStrock)) }
fact { u_Pi5 = u_Sel4 }
sig u__1 in univ {}
fact { u_Pi6 = u_Sel3 }
sig u_Pi5 in u_Book {}

sig mu___modelattribute__notEnoughStrock in univ {}
fact { mu___modelattribute__notEnoughStrock = u__7 }
sig mu___modelattribute__addBookSuccess in univ {}
fact { mu___modelattribute__addBookSuccess = u__1 }
sig BottomNode in FieldData {}
