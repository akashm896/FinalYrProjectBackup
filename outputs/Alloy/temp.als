//__modelattribute__owner : mu___modelattribute__owner
//__modelattribute__owner.address : mu___modelattribute__owner_address
//__modelattribute__owner.city : mu___modelattribute__owner_city
//__modelattribute__owner.firstName : mu___modelattribute__owner_firstName
//__modelattribute__owner.id : mu___modelattribute__owner_id
//__modelattribute__owner.lastName : mu___modelattribute__owner_lastName
//__modelattribute__owner.pets : mu___modelattribute__owner_pets
//__modelattribute__owner.telephone : mu___modelattribute__owner_telephone
sig FieldData {}
one sig u_ownerId in FieldData {}
sig u_PetType {
u_name : FieldData,
u_id : FieldData,
}
sig u_Pet {
u_owner : u_Owner,
u_name : FieldData,
u_id : FieldData,
u_birthDate : FieldData,
u_owner_id : FieldData,
u_type_id : FieldData,
u_type : u_PetType,
}
sig u_Owner {
u_firstName : FieldData,
u_id : FieldData,
u_city : FieldData,
u_address : FieldData,
u_pets : u_Pet,
u_lastName : FieldData,
u_telephone : FieldData,
}
sig u_Sel___Cartesian_____24 in u_Cartesian___ClassRef25 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____24[x: u_Cartesian___ClassRef25] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef25 | meets_selection_criteria_of_u_Sel___Cartesian_____24[y] <=> y in u_Sel___Cartesian_____24 }
sig u_Sel___Cartesian_____1 in u_Cartesian___ClassRef2 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____1[x: u_Cartesian___ClassRef2] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef2 | meets_selection_criteria_of_u_Sel___Cartesian_____1[y] <=> y in u_Sel___Cartesian_____1 }
sig u_Sel___Cartesian_____27 in u_Cartesian___ClassRef28 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____27[x: u_Cartesian___ClassRef28] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef28 | meets_selection_criteria_of_u_Sel___Cartesian_____27[y] <=> y in u_Sel___Cartesian_____27 }
sig u_Sel___Cartesian_____3 in u_Cartesian___ClassRef4 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____3[x: u_Cartesian___ClassRef4] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef4 | meets_selection_criteria_of_u_Sel___Cartesian_____3[y] <=> y in u_Sel___Cartesian_____3 }
sig u_Sel___Cartesian_____6 in u_Cartesian___ClassRef7 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____6[x: u_Cartesian___ClassRef7] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef7 | meets_selection_criteria_of_u_Sel___Cartesian_____6[y] <=> y in u_Sel___Cartesian_____6 }
sig u_Sel___Cartesian_____9 in u_Cartesian___ClassRef10 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____9[x: u_Cartesian___ClassRef10] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef10 | meets_selection_criteria_of_u_Sel___Cartesian_____9[y] <=> y in u_Sel___Cartesian_____9 }
sig u_Sel___Cartesian_____12 in u_Cartesian___ClassRef13 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____12[x: u_Cartesian___ClassRef13] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef13 | meets_selection_criteria_of_u_Sel___Cartesian_____12[y] <=> y in u_Sel___Cartesian_____12 }
sig u_Sel___Cartesian_____15 in u_Cartesian___ClassRef16 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____15[x: u_Cartesian___ClassRef16] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef16 | meets_selection_criteria_of_u_Sel___Cartesian_____15[y] <=> y in u_Sel___Cartesian_____15 }
sig u_Pi___Sel_____Cartesi8 in u_Owner {}

sig u_Cartesian___ClassRef16 in u_Owner {}
fact { u_Cartesian___ClassRef16 = u_Owner }

sig u_Pi___Sel_____Cartesi17 in u_Owner {}

fact { u_Join_u_Alpha22_u_Owner = u_Alpha22.u_Owner_c }

sig u_pets in u_Pet {}

sig u_type in u_PetType {}

sig u_Cartesian___ClassRef4 in u_Owner {}
fact { u_Cartesian___ClassRef4 = u_Owner }

sig u_Cartesian___ClassRef25 in u_Owner {}
fact { u_Cartesian___ClassRef25 = u_Owner }

sig u_Cartesian___ClassRef10 in u_Owner {}
fact { u_Cartesian___ClassRef10 = u_Owner }

fact { u_Pi___Sel_____Cartesi14 = u_Sel___Cartesian_____12 }
sig u_Pi___Sel_____Cartesi11 in u_Owner {}

sig u_Pi___Sel_____Cartesi29 in u_Owner {}

fact { u_Pi___Sel_____Cartesi11 = u_Sel___Cartesian_____9 }
fact { u_Join_u_Sel___Cartesian_____24_u_Pet = u_Sel___Cartesian_____24.u_Pet_c }

sig u_owner in u_Owner {}

fact { u_Pi___Sel_____Cartesi29 = u_Sel___Cartesian_____27 }
sig u_Pi___Sel_____Cartesi5 in u_Owner {}

sig u_Pi___Sel_____Cartesi14 in u_Owner {}

sig u_Cartesian___ClassRef13 in u_Owner {}
fact { u_Cartesian___ClassRef13 = u_Owner }

fact { u_Join_u_Alpha18_u_PetType = u_Alpha18.u_PetType_c }

fact { u_Pi___Sel_____Cartesi8 = u_Sel___Cartesian_____6 }
fact { u_Join_u_Alpha20_u_Pet = u_Alpha20.u_Pet_c }

fact { u_Pi___Sel_____Cartesi17 = u_Sel___Cartesian_____15 }
fact { u_Pi___Sel_____Cartesi5 = u_Sel___Cartesian_____3 }
sig u_Cartesian___ClassRef28 in u_Owner {}
fact { u_Cartesian___ClassRef28 = u_Owner }

sig u_Cartesian___ClassRef7 in u_Owner {}
fact { u_Cartesian___ClassRef7 = u_Owner }

sig u_Cartesian___ClassRef2 in u_Owner {}
fact { u_Cartesian___ClassRef2 = u_Owner }

sig mu___modelattribute__owner_id in univ {}
fact { mu___modelattribute__owner_id = u_Pi___Sel_____Cartesi14.u_id }
sig mu___modelattribute__owner_lastName in univ {}
fact { mu___modelattribute__owner_lastName = u_Pi___Sel_____Cartesi17.u_lastName }
sig mu___modelattribute__owner_firstName in univ {}
fact { mu___modelattribute__owner_firstName = u_Pi___Sel_____Cartesi11.u_firstName }
sig mu___modelattribute__owner_pets in univ {}
fact { mu___modelattribute__owner_pets = u_pets }
sig mu___modelattribute__owner in univ {}
fact { mu___modelattribute__owner = u_Sel___Cartesian_____1 }
sig mu___modelattribute__owner_city in univ {}
fact { mu___modelattribute__owner_city = u_Pi___Sel_____Cartesi8.u_city }
sig mu___modelattribute__owner_address in univ {}
fact { mu___modelattribute__owner_address = u_Pi___Sel_____Cartesi5.u_address }
sig mu___modelattribute__owner_telephone in univ {}
fact { mu___modelattribute__owner_telephone = u_Pi___Sel_____Cartesi29.u_telephone }
sig BottomNode in FieldData {}
