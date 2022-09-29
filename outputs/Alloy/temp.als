//__modelattribute__owner : mu___modelattribute__owner
//__modelattribute__owner.address : mu___modelattribute__owner_address
//__modelattribute__owner.city : mu___modelattribute__owner_city
//__modelattribute__owner.firstName : mu___modelattribute__owner_firstName
//__modelattribute__owner.id : mu___modelattribute__owner_id
//__modelattribute__owner.lastName : mu___modelattribute__owner_lastName
//__modelattribute__owner.pets : mu___modelattribute__owner_pets
//__modelattribute__owner.telephone : mu___modelattribute__owner_telephone
sig FieldData {}
sig u_PetType {
u_name : FieldData,
u_id : FieldData,
}
sig u_Pet {
u_owner : u_Owner,
u_visits : FieldData,
u_id : FieldData,
u_birthDate : FieldData,
u_owner_id : FieldData,
u_type_id : FieldData,
u_"Jimmy" : FieldData,
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
sig u_Sel3 in u_Cartesian4 {}
pred meets_selection_criteria_of_u_Sel3[x: u_Cartesian4] {
1 = 1
}
fact { all y:u_Cartesian4 | meets_selection_criteria_of_u_Sel3[y] <=> y in u_Sel3 }
sig u_Sel12 in u_Cartesian13 {}
pred meets_selection_criteria_of_u_Sel12[x: u_Cartesian13] {
1 = 1
}
fact { all y:u_Cartesian13 | meets_selection_criteria_of_u_Sel12[y] <=> y in u_Sel12 }
sig u_Sel15 in u_Cartesian16 {}
pred meets_selection_criteria_of_u_Sel15[x: u_Cartesian16] {
1 = 1
}
fact { all y:u_Cartesian16 | meets_selection_criteria_of_u_Sel15[y] <=> y in u_Sel15 }
sig u_Sel9 in u_Cartesian10 {}
pred meets_selection_criteria_of_u_Sel9[x: u_Cartesian10] {
1 = 1
}
fact { all y:u_Cartesian10 | meets_selection_criteria_of_u_Sel9[y] <=> y in u_Sel9 }
sig u_Sel25 in u_Cartesian26 {}
pred meets_selection_criteria_of_u_Sel25[x: u_Cartesian26] {
1 = 1
}
fact { all y:u_Cartesian26 | meets_selection_criteria_of_u_Sel25[y] <=> y in u_Sel25 }
sig u_Sel22 in u_Cartesian23 {}
pred meets_selection_criteria_of_u_Sel22[x: u_Cartesian23] {
1 = 1
}
fact { all y:u_Cartesian23 | meets_selection_criteria_of_u_Sel22[y] <=> y in u_Sel22 }
sig u_Sel6 in u_Cartesian7 {}
pred meets_selection_criteria_of_u_Sel6[x: u_Cartesian7] {
1 = 1
}
fact { all y:u_Cartesian7 | meets_selection_criteria_of_u_Sel6[y] <=> y in u_Sel6 }
sig u_Sel1 in u_Cartesian2 {}
pred meets_selection_criteria_of_u_Sel1[x: u_Cartesian2] {
1 = 1
}
fact { all y:u_Cartesian2 | meets_selection_criteria_of_u_Sel1[y] <=> y in u_Sel1 }
sig u_Pi5 in u_Owner {}

sig u_Cartesian10 in u_Owner {}
fact { u_Cartesian10 = u_Owner }

fact { u_Pi8 = u_Sel6 }
sig u_Cartesian2 in u_Owner {}
fact { u_Cartesian2 = u_Owner }

sig u_Pi17 in u_Owner {}

fact { u_Pi14 = u_Sel12 }
fact { u_Pi11 = u_Sel9 }
fact { u_Join_u_Alpha20_u_Owner = u_Alpha20.u_Owner_c }

sig u_pets in u_Pet {}

sig u_Cartesian23 in u_Owner {}
fact { u_Cartesian23 = u_Owner }

sig u_type in u_PetType {}

sig u_Pi11 in u_Owner {}

sig u_Pi27 in u_Owner {}

sig u_Cartesian7 in u_Owner {}
fact { u_Cartesian7 = u_Owner }

fact { u_Pi5 = u_Sel3 }
sig u_Cartesian16 in u_Owner {}
fact { u_Cartesian16 = u_Owner }

fact { u_Pi17 = u_Sel15 }
sig u_owner in u_Owner {}

fact { u_Pi27 = u_Sel25 }
sig u_Pi14 in u_Owner {}

fact { u_Join_u_Sel22_u_Pet = u_Sel22.u_Pet_c }

sig u_Cartesian4 in u_Owner {}
fact { u_Cartesian4 = u_Owner }

sig u_Cartesian13 in u_Owner {}
fact { u_Cartesian13 = u_Owner }

fact { u_Join_u_Alpha18_u_PetType = u_Alpha18.u_PetType_c }

sig u_Cartesian26 in u_Owner {}
fact { u_Cartesian26 = u_Owner }

sig u_Pi8 in u_Owner {}

sig mu___modelattribute__owner_id in univ {}
fact { mu___modelattribute__owner_id = u_Pi14.u_id }
sig mu___modelattribute__owner_lastName in univ {}
fact { mu___modelattribute__owner_lastName = u_Pi17.u_lastName }
sig mu___modelattribute__owner_firstName in univ {}
fact { mu___modelattribute__owner_firstName = u_Pi11.u_firstName }
sig mu___modelattribute__owner_pets in univ {}
fact { mu___modelattribute__owner_pets = u_pets }
sig mu___modelattribute__owner in univ {}
fact { mu___modelattribute__owner = u_Sel1 }
sig mu___modelattribute__owner_city in univ {}
fact { mu___modelattribute__owner_city = u_Pi8.u_city }
sig mu___modelattribute__owner_address in univ {}
fact { mu___modelattribute__owner_address = u_Pi5.u_address }
sig mu___modelattribute__owner_telephone in univ {}
fact { mu___modelattribute__owner_telephone = u_Pi27.u_telephone }
sig BottomNode in FieldData {}
