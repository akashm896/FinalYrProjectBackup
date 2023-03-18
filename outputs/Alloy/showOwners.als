//__modelattribute__collection : mu___modelattribute__collection
sig FieldData {}
one sig u_lastName in FieldData {}
sig u_PetType {
u_name : FieldData,
u_id : FieldData,
}
sig u_Visit {
u_description : FieldData,
u_id : FieldData,
u_date : FieldData,
u_petId : FieldData,
}
sig u_Pet {
u_owner : u_Owner,
u_visits : u_Visit,
u_name : FieldData,
u_id : FieldData,
u_birthDate : FieldData,
u_typeId : FieldData,
u_ownerId : FieldData,
u_type : u_PetType,
}
sig u_Owner {
u_firstName : FieldData,
u_id : FieldData,
u_city : FieldData,
u_address : FieldData,
u_pets : u_Pet,
u_lastName : FieldData,
u_lastname : FieldData,
u_telephone : FieldData,
}
sig u_Sel___Cartesian_____1 in u_Cartesian___ClassRef2 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____1[x: u_Cartesian___ClassRef2] {
x.u_lastname = u_lastName
}
fact { all y:u_Cartesian___ClassRef2 | meets_selection_criteria_of_u_Sel___Cartesian_____1[y] <=> y in u_Sel___Cartesian_____1 }
fact {  all v0 : u_Owner |  all v1 : u_Pet |  all v2 : u_PetType | v1.u_typeId = v2.u_id <=> v2 in v1.u_type }
fact {  all v0 : u_Owner |  all v1 : u_Pet |  all v2 : u_Visit | v1.u_id = v2.u_petId <=> v2 in v1.u_visits }
fact {  all v0 : u_Owner |  all v1 : u_Pet | v0.u_id = v1.u_ownerId <=> v1 in v0.u_pets }
fact {  all v0 : u_Owner |  all v1 : u_Pet |  all v2 : u_Owner | v1.u_ownerId = v2.u_id <=> v2 in v1.u_owner }
sig u_Cartesian___ClassRef2 in u_Owner {}
fact { u_Cartesian___ClassRef2 = u_Owner }

sig mu___modelattribute__collection in univ {}
fact { mu___modelattribute__collection = u_Sel___Cartesian_____1 }
sig BottomNode in FieldData {}
