//this.employeeServiceImpl.employeeRepository : mu_this_employeeServiceImpl_employeeRepository
sig FieldData {}
one sig u_employee_email in FieldData {}
one sig u_employee_id in FieldData {}
one sig u_employee_lastName in FieldData {}
one sig u_employee_firstName in FieldData {}
sig u_this_employeeServiceImpl_employeeRepository in u_ts_this_employeeServiceImpl_employeeRepository {
}
sig u_ts_this_employeeServiceImpl_employeeRepository {
u_firstname : FieldData,
u_id : FieldData,
u_email : FieldData,
u_lastname : FieldData,
}
sig u_Sel___Cartesian_____3 in u_Cartesian___this_emp2 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____3[x: u_Cartesian___this_emp2] {
x.u_id = u_employee_id
}
fact { all y:u_Cartesian___this_emp2 | meets_selection_criteria_of_u_Sel___Cartesian_____3[y] <=> y in u_Sel___Cartesian_____3 }
fact { u_____Equals_____Sel__1 = ((u_Sel___Cartesian_____3 = none) => (u_UnionOp___this_emplo7) else (u_UnionOp___Relational6)) }
sig u_UnionOp___Relational6 in u_RelationalMinus___Ca4 + u_List___employee_id__5 {}
fact { u_UnionOp___Relational6 = u_RelationalMinus___Ca4 + u_List___employee_id__5 }

sig u_UnionOp___this_emplo7 in u_this_employeeServiceImpl_employeeRepository + u_List___employee_id__5 {}
fact { u_UnionOp___this_emplo7 = u_this_employeeServiceImpl_employeeRepository + u_List___employee_id__5 }

one sig u_List___employee_id__5 in u_ts_this_employeeServiceImpl_employeeRepository {}
fact { u_List___employee_id__5.u_id = u_employee_id }
fact { u_List___employee_id__5.u_firstname = u_employee_firstName }
fact { u_List___employee_id__5.u_lastname = u_employee_lastName }
fact { u_List___employee_id__5.u_email = u_employee_email }

sig u_RelationalMinus___Ca4 in u_this_employeeServiceImpl_employeeRepository {}
fact { u_RelationalMinus___Ca4 = u_this_employeeServiceImpl_employeeRepository - u_Sel___Cartesian_____3 }
sig u_____Equals_____Sel__1 in univ {}
sig u_Cartesian___this_emp2 in u_this_employeeServiceImpl_employeeRepository {}
fact { u_Cartesian___this_emp2 = u_this_employeeServiceImpl_employeeRepository }

sig mu_this_employeeServiceImpl_employeeRepository in univ {}
fact { mu_this_employeeServiceImpl_employeeRepository = u_____Equals_____Sel__1 }
sig BottomNode in FieldData {}
