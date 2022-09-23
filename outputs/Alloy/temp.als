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
u_visits : FieldData,
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
sig u_Sel23 in u_Cartesian24 {}
pred meets_selection_criteria_of_u_Sel23[x: u_Cartesian24] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian24 | meets_selection_criteria_of_u_Sel23[y] <=> y in u_Sel23 }
sig u_Sel2 in u_Cartesian3 {}
pred meets_selection_criteria_of_u_Sel2[x: u_Cartesian3] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian3 | meets_selection_criteria_of_u_Sel2[y] <=> y in u_Sel2 }
sig u_Sel4 in u_Cartesian5 {}
pred meets_selection_criteria_of_u_Sel4[x: u_Cartesian5] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian5 | meets_selection_criteria_of_u_Sel4[y] <=> y in u_Sel4 }
sig u_Sel7 in u_Cartesian8 {}
pred meets_selection_criteria_of_u_Sel7[x: u_Cartesian8] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian8 | meets_selection_criteria_of_u_Sel7[y] <=> y in u_Sel7 }
sig u_Sel10 in u_Cartesian11 {}
pred meets_selection_criteria_of_u_Sel10[x: u_Cartesian11] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian11 | meets_selection_criteria_of_u_Sel10[y] <=> y in u_Sel10 }
sig u_Sel13 in u_Cartesian14 {}
pred meets_selection_criteria_of_u_Sel13[x: u_Cartesian14] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian14 | meets_selection_criteria_of_u_Sel13[y] <=> y in u_Sel13 }
sig u_Sel25 in u_Cartesian26 {}
pred meets_selection_criteria_of_u_Sel25[x: u_Cartesian26] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian26 | meets_selection_criteria_of_u_Sel25[y] <=> y in u_Sel25 }
sig u_Sel16 in u_Cartesian17 {}
pred meets_selection_criteria_of_u_Sel16[x: u_Cartesian17] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian17 | meets_selection_criteria_of_u_Sel16[y] <=> y in u_Sel16 }
sig u_Cartesian8 in u_Owner {}
fact { u_Cartesian8 = u_Owner }

sig u_Pi12 in u_Owner {}

fact { u_Join_u_Alpha21_u_Owner = u_Alpha21.u_Owner_c }

sig u_Cartesian24 in u_Owner {}
fact { u_Cartesian24 = u_Owner }

fact { u_Pi15 = u_Sel13 }
fact { u_Join_u_Alpha19_u_PetType = u_Alpha19.u_PetType_c }

sig u_pets in u_Pet {}

sig u_Cartesian5 in u_Owner {}
fact { u_Cartesian5 = u_Owner }

sig u_type in u_PetType {}

sig u_Pi18 in u_Owner {}

fact { u_Join_u_Sel23_u_Pet = u_Sel23.u_Pet_c }

sig u_Pi9 in u_Owner {}

sig u_Cartesian17 in u_Owner {}
fact { u_Cartesian17 = u_Owner }

fact { u_Pi9 = u_Sel7 }
sig u_Pi6 in u_Owner {}

sig u_Pi15 in u_Owner {}

sig u_Pi27 in u_Owner {}

fact { u_Pi18 = u_Sel16 }
sig u_owner in u_Owner {}

sig u_Cartesian14 in u_Owner {}
fact { u_Cartesian14 = u_Owner }

fact { u_Pi27 = u_Sel25 }
fact { u_Pi6 = u_Sel4 }
sig u_Cartesian26 in u_Owner {}
fact { u_Cartesian26 = u_Owner }

sig u_Cartesian11 in u_Owner {}
fact { u_Cartesian11 = u_Owner }

fact { u_Pi12 = u_Sel10 }
sig u_Cartesian3 in u_Owner {}
fact { u_Cartesian3 = u_Owner }

sig mu___modelattribute__owner_id in univ {}
fact { mu___modelattribute__owner_id = u_Pi15.u_id }
sig mu___modelattribute__owner_lastName in univ {}
fact { mu___modelattribute__owner_lastName = u_Pi18.u_lastName }
sig mu___modelattribute__owner_firstName in univ {}
fact { mu___modelattribute__owner_firstName = u_Pi12.u_firstName }
sig mu___modelattribute__owner_pets in univ {}
fact { mu___modelattribute__owner_pets = u_pets }
sig mu___modelattribute__owner in univ {}
fact { mu___modelattribute__owner = u_Sel2 }
sig mu___modelattribute__owner_city in univ {}
fact { mu___modelattribute__owner_city = u_Pi9.u_city }
sig mu___modelattribute__owner_address in univ {}
fact { mu___modelattribute__owner_address = u_Pi6.u_address }
sig mu___modelattribute__owner_telephone in univ {}
fact { mu___modelattribute__owner_telephone = u_Pi27.u_telephone }
sig BottomNode in FieldData {}
