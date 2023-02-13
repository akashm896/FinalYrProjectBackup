//__modelattribute__owner : mu___modelattribute__owner
//__modelattribute__owner.address : mu___modelattribute__owner_address
//__modelattribute__owner.city : mu___modelattribute__owner_city
//__modelattribute__owner.firstName : mu___modelattribute__owner_firstName
//__modelattribute__owner.id : mu___modelattribute__owner_id
//__modelattribute__owner.lastName : mu___modelattribute__owner_lastName
//__modelattribute__owner.pets : mu___modelattribute__owner_pets
//__modelattribute__owner.telephone : mu___modelattribute__owner_telephone
sig FieldData {}
one sig u_Owner_id in FieldData {}
one sig u__owner_id in FieldData {}
one sig u_PetType_id in FieldData {}
one sig u__type_id in FieldData {}
one sig u_ownerId in FieldData {}
sig u_PetType {
u_name : FieldData,
u_id : FieldData,
}
sig u_Pet {
u_owner : u_Owner,
u_visits : FieldData,
u_name : FieldData,
u_id : FieldData,
u_birthDate : FieldData,
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
sig u_Sel___Cartesian_____15 in u_Cartesian___ClassRef16 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____15[x: u_Cartesian___ClassRef16] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef16 | meets_selection_criteria_of_u_Sel___Cartesian_____15[y] <=> y in u_Sel___Cartesian_____15 }
sig u_Sel___Cartesian_____1 in u_Cartesian___ClassRef2 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____1[x: u_Cartesian___ClassRef2] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef2 | meets_selection_criteria_of_u_Sel___Cartesian_____1[y] <=> y in u_Sel___Cartesian_____1 }
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
sig u_Sel___Cartesian_____12 in u_Cartesian___ClassRef13 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____12[x: u_Cartesian___ClassRef13] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef13 | meets_selection_criteria_of_u_Sel___Cartesian_____12[y] <=> y in u_Sel___Cartesian_____12 }
sig u_Sel___Cartesian_____9 in u_Cartesian___ClassRef10 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____9[x: u_Cartesian___ClassRef10] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef10 | meets_selection_criteria_of_u_Sel___Cartesian_____9[y] <=> y in u_Sel___Cartesian_____9 }
sig u_Sel___Cartesian_____25 in u_Cartesian___ClassRef26 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____25[x: u_Cartesian___ClassRef26] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef26 | meets_selection_criteria_of_u_Sel___Cartesian_____25[y] <=> y in u_Sel___Cartesian_____25 }
sig u_Sel___Cartesian_____28 in u_Cartesian___ClassRef29 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____28[x: u_Cartesian___ClassRef29] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef29 | meets_selection_criteria_of_u_Sel___Cartesian_____28[y] <=> y in u_Sel___Cartesian_____28 }
sig u_Sel___Join_____Class18 in u_Join___ClassRef_Pet_19 {}
pred meets_selection_criteria_of_u_Sel___Join_____Class18[x: u_Join___ClassRef_Pet_19] {
u__type_id = u_PetType_id
}
fact { all y:u_Join___ClassRef_Pet_19 | meets_selection_criteria_of_u_Sel___Join_____Class18[y] <=> y in u_Sel___Join_____Class18 }
sig u_Sel___Join_____Class21 in u_Join___ClassRef_Pet_22 {}
pred meets_selection_criteria_of_u_Sel___Join_____Class21[x: u_Join___ClassRef_Pet_22] {
u__owner_id = u_Owner_id
}
fact { all y:u_Join___ClassRef_Pet_22 | meets_selection_criteria_of_u_Sel___Join_____Class21[y] <=> y in u_Sel___Join_____Class21 }
sig u_Cartesian___ClassRef29 in u_Owner {}
fact { u_Cartesian___ClassRef29 = u_Owner }

sig u_Join___Sel_____Carte24 in u_Pet {}
fact { u_Join___Sel_____Carte24 = u_Sel___Cartesian_____25.u_Pet_c }

sig u_Pi___Sel_____Cartesi8 in u_Owner {}

sig u_Cartesian___ClassRef16 in u_Owner {}
fact { u_Cartesian___ClassRef16 = u_Owner }

sig u_Pi___Sel_____Cartesi17 in u_Owner {}

sig u_Cartesian___ClassRef4 in u_Owner {}
fact { u_Cartesian___ClassRef4 = u_Owner }

sig u_Cartesian___ClassRef10 in u_Owner {}
fact { u_Cartesian___ClassRef10 = u_Owner }

fact { u_Pi___Sel_____Cartesi14 = u_Sel___Cartesian_____12 }
sig u_Pi___Sel_____Cartesi11 in u_Owner {}

fact { u_Pi___Sel_____Cartesi11 = u_Sel___Cartesian_____9 }
sig u_Owner_pets=Pi___Join27 in u_Pet {}

fact { u_Pi___Sel_____Cartesi30 = u_Sel___Cartesian_____28 }
sig u_Pet_owner=Pi___Sel__23 in u_Owner {}

sig u_Join___ClassRef_Pet_19 in u_PetType {}
fact { u_Join___ClassRef_Pet_19 = u_Pet.u_PetType_c }

sig u_Join___ClassRef_Pet_22 in u_Owner {}
fact { u_Join___ClassRef_Pet_22 = u_Pet.u_Owner_c }

sig u_Pi___Sel_____Cartesi5 in u_Owner {}

sig u_Pi___Sel_____Cartesi14 in u_Owner {}

sig u_Pi___Sel_____Cartesi30 in u_Owner {}

sig u_Cartesian___ClassRef13 in u_Owner {}
fact { u_Cartesian___ClassRef13 = u_Owner }

sig u_Pet_type=Pi___Sel___20 in u_PetType {}

fact { u_Pi___Sel_____Cartesi8 = u_Sel___Cartesian_____6 }
fact { u_Pi___Sel_____Cartesi17 = u_Sel___Cartesian_____15 }
fact { u_Pi___Sel_____Cartesi5 = u_Sel___Cartesian_____3 }
sig u_Cartesian___ClassRef7 in u_Owner {}
fact { u_Cartesian___ClassRef7 = u_Owner }

sig u_Cartesian___ClassRef26 in u_Owner {}
fact { u_Cartesian___ClassRef26 = u_Owner }

sig u_Cartesian___ClassRef2 in u_Owner {}
fact { u_Cartesian___ClassRef2 = u_Owner }

sig mu___modelattribute__owner_id in univ {}
fact { mu___modelattribute__owner_id = u_Pi___Sel_____Cartesi14.u_id }
sig mu___modelattribute__owner_lastName in univ {}
fact { mu___modelattribute__owner_lastName = u_Pi___Sel_____Cartesi17.u_lastName }
sig mu___modelattribute__owner_firstName in univ {}
fact { mu___modelattribute__owner_firstName = u_Pi___Sel_____Cartesi11.u_firstName }
sig mu___modelattribute__owner_pets in univ {}
fact { mu___modelattribute__owner_pets = u_Owner_pets=Pi___Join27 }
sig mu___modelattribute__owner in univ {}
fact { mu___modelattribute__owner = u_Sel___Cartesian_____1 }
sig mu___modelattribute__owner_city in univ {}
fact { mu___modelattribute__owner_city = u_Pi___Sel_____Cartesi8.u_city }
sig mu___modelattribute__owner_address in univ {}
fact { mu___modelattribute__owner_address = u_Pi___Sel_____Cartesi5.u_address }
sig mu___modelattribute__owner_telephone in univ {}
fact { mu___modelattribute__owner_telephone = u_Pi___Sel_____Cartesi30.u_telephone }
sig BottomNode in FieldData {}
