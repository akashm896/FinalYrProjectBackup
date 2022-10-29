//$r0.postRepository : mu__r0_postRepository
sig FieldData {}
one sig u_0 extends FieldData {}
one sig u_post_id in FieldData {}
one sig u_post_title in FieldData {}
one sig u_post_createDate in FieldData {}
one sig u_MethodWontHandleOp8 in FieldData {}
one sig u_post_body in FieldData {}
sig u_ts__r0_postRepository {
u_id : FieldData,
u_body : FieldData,
u_createdate : FieldData,
u_title : FieldData,
}
sig u__r0_postRepository in u_ts__r0_postRepository {
}
sig u_Sel2 in u_Cartesian3 {}
pred meets_selection_criteria_of_u_Sel2[x: u_Cartesian3] {
x.u_id = u_post_id
}
fact { all y:u_Cartesian3 | meets_selection_criteria_of_u_Sel2[y] <=> y in u_Sel2 }
sig u_UnionOp5 in u__r0_postRepository + u_List4 {}
fact { u_UnionOp5 = u__r0_postRepository + u_List4 }

sig u_UnionOp7 in u_RelationalMinus6 + u_List4 {}
fact { u_UnionOp7 = u_RelationalMinus6 + u_List4 }

one sig u_List4 in u_ts__r0_postRepository {}
fact { u_List4.u_id = u_post_id }
fact { u_List4.u_title = u_post_title }
fact { u_List4.u_body = u_post_body }
fact { u_List4.u_createdate = u_post_createDate }

sig u_Cartesian3 in u__r0_postRepository {}
fact { u_Cartesian3 = u__r0_postRepository }

sig u_RelationalMinus6 in u__r0_postRepository {}
fact { u_RelationalMinus6 = u__r0_postRepository - u_Sel2 }
sig u__1 in univ {}
fact { u__1 = ((u_MethodWontHandleOp8 != u_0) => (u__r0_postRepository) else (((u_Sel2 = none) => (u_UnionOp5) else (u_UnionOp7)))) }
sig mu__r0_postRepository in univ {}
fact { mu__r0_postRepository = u__1 }
sig BottomNode in FieldData {}
