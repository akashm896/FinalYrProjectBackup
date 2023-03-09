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
one sig U_ID in FieldData {}
sig u_PetType {
u_name : FieldData,
u_id : FieldData,
}
sig u_org_springframework_samples_petclinic_visit_VisitRepository {
u_petid : FieldData,
}
sig u_Pet {
u_owner : u_Owner,
u_name : FieldData,
u_id : FieldData,
u_birthDate : FieldData,
u_birthdate : FieldData,
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
u_telephone : FieldData,
}
sig u_Sel___Cartesian_____1 in u_Cartesian___ClassRef2 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____1[x: u_Cartesian___ClassRef2] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef2 | meets_selection_criteria_of_u_Sel___Cartesian_____1[y] <=> y in u_Sel___Cartesian_____1 }
sig u_Sel___Cartesian_____18 in u_Cartesian___ClassRef19 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____18[x: u_Cartesian___ClassRef19] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef19 | meets_selection_criteria_of_u_Sel___Cartesian_____18[y] <=> y in u_Sel___Cartesian_____18 }
sig u_Sel___Cartesian_____12 in u_Cartesian___ClassRef13 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____12[x: u_Cartesian___ClassRef13] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef13 | meets_selection_criteria_of_u_Sel___Cartesian_____12[y] <=> y in u_Sel___Cartesian_____12 }
sig u_Sel___Cartesian_____21 in u_Cartesian___ClassRef22 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____21[x: u_Cartesian___ClassRef22] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef22 | meets_selection_criteria_of_u_Sel___Cartesian_____21[y] <=> y in u_Sel___Cartesian_____21 }
sig u_Sel___ClassRef_org_s20 in u_org_springframework_samples_petclinic_visit_VisitRepository {}
pred meets_selection_criteria_of_u_Sel___ClassRef_org_s20[x: u_org_springframework_samples_petclinic_visit_VisitRepository] {
x.u_petid = U_ID
}
fact { all y:u_org_springframework_samples_petclinic_visit_VisitRepository | meets_selection_criteria_of_u_Sel___ClassRef_org_s20[y] <=> y in u_Sel___ClassRef_org_s20 }
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
sig u_Sel___Cartesian_____3 in u_Cartesian___ClassRef4 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____3[x: u_Cartesian___ClassRef4] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef4 | meets_selection_criteria_of_u_Sel___Cartesian_____3[y] <=> y in u_Sel___Cartesian_____3 }
sig u_Sel___Cartesian_____15 in u_Cartesian___ClassRef16 {}
pred meets_selection_criteria_of_u_Sel___Cartesian_____15[x: u_Cartesian___ClassRef16] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian___ClassRef16 | meets_selection_criteria_of_u_Sel___Cartesian_____15[y] <=> y in u_Sel___Cartesian_____15 }
sig u_Pi___Sel_____Cartesi8 in u_Owner {}

sig u_Cartesian___ClassRef16 in u_Owner {}
fact { u_Cartesian___ClassRef16 = u_Owner }

sig u_Pi___Sel_____Cartesi17 in u_Owner {}

fact {  all v0 : u_Owner |  all v1 : u_Pet |  all v2 : u_PetType | v1.u_typeId = v2.u_id <=> v2 in v1.u_type }
sig u_Pi___Sel_____Cartesi5 in u_Owner {}

sig u_Pi___Sel_____Cartesi14 in u_Owner {}

fact {  all v0 : u_Owner |  all v1 : u_Pet | v0.u_id = v1.u_ownerId <=> v1 in v0.u_pets }
sig u_Pi___Sel_____Cartesi23 in u_Owner {}

sig u_Cartesian___ClassRef13 in u_Owner {}
fact { u_Cartesian___ClassRef13 = u_Owner }

fact {  all v0 : u_Owner |  all v1 : u_Pet |  all v2 : u_Owner | v1.u_ownerId = v2.u_id <=> v2 in v1.u_owner }
fact { u_Pi___Sel_____Cartesi8 = u_Sel___Cartesian_____6 }
sig u_Cartesian___ClassRef4 in u_Owner {}
fact { u_Cartesian___ClassRef4 = u_Owner }

sig u_Cartesian___ClassRef10 in u_Owner {}
fact { u_Cartesian___ClassRef10 = u_Owner }

sig u_Cartesian___ClassRef19 in u_Owner {}
fact { u_Cartesian___ClassRef19 = u_Owner }

fact { u_Pi___Sel_____Cartesi23 = u_Sel___Cartesian_____21 }
fact { u_Pi___Sel_____Cartesi14 = u_Sel___Cartesian_____12 }
sig u_Pi___Sel_____Cartesi11 in u_Owner {}

fact { u_Pi___Sel_____Cartesi17 = u_Sel___Cartesian_____15 }
fact { u_Pi___Sel_____Cartesi5 = u_Sel___Cartesian_____3 }
fact { u_Pi___Sel_____Cartesi11 = u_Sel___Cartesian_____9 }
fact {  all v0 : u_Owner |  all v1 : u_Pet |  all v2 : u_Owner |  all v3 : u_Pet | v2.u_id = v3.u_ownerId <=> v3 in v2.u_pets }
sig u_Cartesian___ClassRef7 in u_Owner {}
fact { u_Cartesian___ClassRef7 = u_Owner }

sig u_Cartesian___ClassRef2 in u_Owner {}
fact { u_Cartesian___ClassRef2 = u_Owner }

sig u_Cartesian___ClassRef22 in u_Owner {}
fact { u_Cartesian___ClassRef22 = u_Owner }

sig mu___modelattribute__owner_id in univ {}
fact { mu___modelattribute__owner_id = u_Pi___Sel_____Cartesi14.u_id }
sig mu___modelattribute__owner_lastName in univ {}
fact { mu___modelattribute__owner_lastName = u_Pi___Sel_____Cartesi17.u_lastName }
sig mu___modelattribute__owner_firstName in univ {}
fact { mu___modelattribute__owner_firstName = u_Pi___Sel_____Cartesi11.u_firstName }
sig mu___modelattribute__owner_pets in univ {}
fact { mu___modelattribute__owner_pets = u_Sel___Cartesian_____18.u_pets }
sig mu___modelattribute__owner in univ {}
fact { mu___modelattribute__owner = u_Sel___Cartesian_____1 }
sig mu___modelattribute__owner_city in univ {}
fact { mu___modelattribute__owner_city = u_Pi___Sel_____Cartesi8.u_city }
sig mu___modelattribute__owner_address in univ {}
fact { mu___modelattribute__owner_address = u_Pi___Sel_____Cartesi5.u_address }
sig mu___modelattribute__owner_telephone in univ {}
fact { mu___modelattribute__owner_telephone = u_Pi___Sel_____Cartesi23.u_telephone }
sig BottomNode in FieldData {}
