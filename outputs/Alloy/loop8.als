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
name : FieldData,
id : FieldData,
}
sig u_Visit {
date : FieldData,
petId : FieldData,
description : FieldData,
id : FieldData,
}
sig u_Pet {
u_owner : u_Owner,
u_visits : u_Visit,
type_id : FieldData,
owner_id : FieldData,
name : FieldData,
id : FieldData,
u_type : u_PetType,
birthDate : FieldData,
pet_id : FieldData,
}
sig u_Owner {
u_firstName : FieldData,
lastName : FieldData,
address : FieldData,
city : FieldData,
u_city : FieldData,
owner_id : FieldData,
u_address : FieldData,
u_pets : u_Pet,
telephone : FieldData,
u_telephone : FieldData,
firstName : FieldData,
u_id : FieldData,
id : FieldData,
u_lastName : FieldData,
}
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
sig u_Sel___Cartesian_____1 in u_Cartesian___ClassRef2 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____1[x: u_Cartesian___ClassRef2] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef2 | meets_selection_criteria_of_u_Sel___Cartesian_____1[y] <=> y in u_Sel___Cartesian_____1 }
sig u_Sel___Cartesian_____20 in u_Cartesian___ClassRef21 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____20[x: u_Cartesian___ClassRef21] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef21 | meets_selection_criteria_of_u_Sel___Cartesian_____20[y] <=> y in u_Sel___Cartesian_____20 }
sig u_Sel___Cartesian_____9 in u_Cartesian___ClassRef10 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____9[x: u_Cartesian___ClassRef10] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef10 | meets_selection_criteria_of_u_Sel___Cartesian_____9[y] <=> y in u_Sel___Cartesian_____9 }
sig u_Sel___Cartesian_____6 in u_Cartesian___ClassRef7 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____6[x: u_Cartesian___ClassRef7] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef7 | meets_selection_criteria_of_u_Sel___Cartesian_____6[y] <=> y in u_Sel___Cartesian_____6 }
sig u_Sel___Cartesian_____3 in u_Cartesian___ClassRef4 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____3[x: u_Cartesian___ClassRef4] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef4 | meets_selection_criteria_of_u_Sel___Cartesian_____3[y] <=> y in u_Sel___Cartesian_____3 }
fact { u_Pi___Sel_____Cartesi22 = u_Sel___Cartesian_____20 }
sig u_Pi___Sel_____Cartesi8 in u_Owner {}

sig u_Cartesian___ClassRef16 in u_Owner {}
fact { u_Cartesian___ClassRef16 = u_Owner }

sig u_Pi___Sel_____Cartesi17 in u_Owner {}

sig u_Pi___Sel_____Cartesi5 in u_Owner {}

sig u_Pi___Sel_____Cartesi14 in u_Owner {}

sig u_Cartesian___ClassRef13 in u_Owner {}
fact { u_Cartesian___ClassRef13 = u_Owner }

fact { u_Pi___Sel_____Cartesi8 = u_Sel___Cartesian_____6 }
sig u_Cartesian___ClassRef4 in u_Owner {}
fact { u_Cartesian___ClassRef4 = u_Owner }

sig u_Cartesian___ClassRef10 in u_Owner {}
fact { u_Cartesian___ClassRef10 = u_Owner }

fact { u_Pi___Sel_____Cartesi14 = u_Sel___Cartesian_____12 }
sig u_Pi___Sel_____Cartesi11 in u_Owner {}

fact { u_Pi___Sel_____Cartesi17 = u_Sel___Cartesian_____15 }
fact { u_Pi___Sel_____Cartesi5 = u_Sel___Cartesian_____3 }
sig u_Pi___Sel_____Cartesi22 in u_Owner {}

fact { u_Pi___Sel_____Cartesi11 = u_Sel___Cartesian_____9 }

 sig u_Owner18 in u_Owner {} 
fact{all v0 : u_Owner | v0.u_Id = ownerId <=> v0 in u_Owner18} 

 fact { all alpha : u_Owner18 |  all v2 : u_Pet | alpha.id = v2.u_owner_id <=> v2 in alpha.u_pets } 

 fact { all v0 : u_Owner18 | all alpha : v0.u_pets |  all v3 : u_PetType | alpha.type_id = v3.u_id <=> v3 in alpha.u_type } 

 fact { all v0 : u_Owner18 | all alpha : v0.u_pets |  all v3 : u_Owner | alpha.owner_id = v3.u_id <=> v3 in alpha.u_owner } 

 fact { all v0 : u_Owner18 | all v1 : v0.u_pets | all alpha : v1.u_owner |  all v4 : u_Pet | alpha.id = v4.u_owner_id <=> v4 in alpha.u_pets } 

 sig u_Visit19 in u_Visit {} 
fact{all v0 : u_Visit | v0.u_petId = u_Pet.u_id <=> v0 in u_Visit19} 

sig u_Cartesian___ClassRef7 in u_Owner {}
fact { u_Cartesian___ClassRef7 = u_Owner }

sig u_Cartesian___ClassRef2 in u_Owner {}
fact { u_Cartesian___ClassRef2 = u_Owner }

sig u_Cartesian___ClassRef21 in u_Owner {}
fact { u_Cartesian___ClassRef21 = u_Owner }

sig mu___modelattribute__owner_id in univ {}
fact { mu___modelattribute__owner_id = u_Pi___Sel_____Cartesi14.u_id }
sig mu___modelattribute__owner_lastName in univ {}
fact { mu___modelattribute__owner_lastName = u_Pi___Sel_____Cartesi17.u_lastName }
sig mu___modelattribute__owner_firstName in univ {}
fact { mu___modelattribute__owner_firstName = u_Pi___Sel_____Cartesi11.u_firstName }
sig mu___modelattribute__owner_pets in univ {}
fact { mu___modelattribute__owner_pets =  }
sig mu___modelattribute__owner in univ {}
fact { mu___modelattribute__owner = u_Sel___Cartesian_____1 }
sig mu___modelattribute__owner_city in univ {}
fact { mu___modelattribute__owner_city = u_Pi___Sel_____Cartesi8.u_city }
sig mu___modelattribute__owner_address in univ {}
fact { mu___modelattribute__owner_address = u_Pi___Sel_____Cartesi5.u_address }
sig mu___modelattribute__owner_telephone in univ {}
fact { mu___modelattribute__owner_telephone = u_Pi___Sel_____Cartesi22.u_telephone }
sig BottomNode in FieldData {}
