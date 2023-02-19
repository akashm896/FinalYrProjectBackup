u_Sel1
//__modelattribute__owner : mu___modelattribute__owner
u_Pi5.u_address
//__modelattribute__owner.address : mu___modelattribute__owner_address
u_Pi8.u_city
//__modelattribute__owner.city : mu___modelattribute__owner_city
u_Pi11.u_firstName
//__modelattribute__owner.firstName : mu___modelattribute__owner_firstName
u_Pi14.u_id
//__modelattribute__owner.id : mu___modelattribute__owner_id
u_Pi17.u_lastName
//__modelattribute__owner.lastName : mu___modelattribute__owner_lastName
Alloy Summary by Akash code starts 
 
 
////////////////////////////////////////////////////////////////////////
 Owner.pets=Pi
| Join
| | Sel
| | | Cartesian
| | | | ClassRef(Owner)
| | | Equals
| | | | FieldRef(owner.id)
| | | | ownerId
| | ClassRef(Pet)
| | Equals
| | | Alpha.id
| | | Pet.owner_id
| List
| | birthDate(org.springframework.samples.petclinic.owner.Pet.birthDate)
| | Pet.type=Pi
| | | Join
| | | | Alpha
| | | | ClassRef(PetType)
| | | | Equals
| | | | | Alpha.type_id
| | | | | PetType.id
| | | List
| | | | name
| | | | id
| | Pet.owner=Pi
| | | Join
| | | | Alpha
| | | | ClassRef(Owner)
| | | | Equals
| | | | | Alpha.owner_id
| | | | | Owner.id
| | | List
| | | | address
| | | | city
| | | | telephone
| | | | Owner.pets=Pi
| | | | | Join
| | | | | | Alpha
| | | | | | ClassRef(Pet)
| | | | | | Equals
| | | | | | | Alpha.id
| | | | | | | Pet.owner_id
| | | | | List
| | | | | | birthDate
| | | | | | NullOp
| | | | | | NullOp
| | | | | | NullOp
| | | | | | name
| | | | | | id
| | | | firstName
| | | | lastName
| | | | id
| | Pet.visits=Pi
| | | Join
| | | | Alpha
| | | | ClassRef(Visit)
| | | | Equals
| | | | | Alpha.id
| | | | | Visit.pet_id
| | | List
| | | | date
| | | | description
| | | | petId
| | | | id
| | name(org.springframework.samples.petclinic.owner.Pet.name)
| | id(org.springframework.samples.petclinic.owner.Pet.id)
 sig u_Owner18 in u_Owner {} 
fact{all v0 : u_Owner | v0.id = ownerId <=> v0 in u_Owner18 } 

 fact { all v0 : u_Owner18 | all v1 : u_Pet | v0.id = v1.u_owner_id <=> v1 in v0.u_pets }
 fact { all alpha : u_Owner18 |  all v2 : u_PetType | alpha.type_id = v2.u_id <=> v2 in alpha.u_type } 

 fact { all alpha : u_Owner18 |  all v2 : u_Owner | alpha.owner_id = v2.u_id <=> v2 in alpha.u_owner } 

 fact { all v0 : u_Owner18 | all alpha : v0.u_owner |  all v3 : u_Pet | alpha.id = v3.u_owner_id <=> v3 in alpha.u_pets } 

 fact { all alpha : u_Owner18 |  all v2 : u_Visit | alpha.id = v2.u_pet_id <=> v2 in alpha.u_visits } 

 Alloy Summary by Akash code ends 
 
 
//__modelattribute__owner.pets : mu___modelattribute__owner_pets
u_Pi21.u_telephone
//__modelattribute__owner.telephone : mu___modelattribute__owner_telephone
sig FieldData {}
one sig u_ownerId in FieldData {}
sig u_Owner {
u_firstName : FieldData,
u_id : FieldData,
u_city : FieldData,
u_address : FieldData,
u_lastName : FieldData,
u_telephone : FieldData,
}
sig u_Sel15 in u_Cartesian16 {}
pred meets_selection_criteria_of_u_Sel15[x: u_Cartesian16] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian16 | meets_selection_criteria_of_u_Sel15[y] <=> y in u_Sel15 }
sig u_Sel6 in u_Cartesian7 {}
pred meets_selection_criteria_of_u_Sel6[x: u_Cartesian7] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian7 | meets_selection_criteria_of_u_Sel6[y] <=> y in u_Sel6 }
sig u_Sel12 in u_Cartesian13 {}
pred meets_selection_criteria_of_u_Sel12[x: u_Cartesian13] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian13 | meets_selection_criteria_of_u_Sel12[y] <=> y in u_Sel12 }
sig u_Sel9 in u_Cartesian10 {}
pred meets_selection_criteria_of_u_Sel9[x: u_Cartesian10] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian10 | meets_selection_criteria_of_u_Sel9[y] <=> y in u_Sel9 }
sig u_Sel19 in u_Cartesian20 {}
pred meets_selection_criteria_of_u_Sel19[x: u_Cartesian20] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian20 | meets_selection_criteria_of_u_Sel19[y] <=> y in u_Sel19 }
sig u_Sel1 in u_Cartesian2 {}
pred meets_selection_criteria_of_u_Sel1[x: u_Cartesian2] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian2 | meets_selection_criteria_of_u_Sel1[y] <=> y in u_Sel1 }
sig u_Sel3 in u_Cartesian4 {}
pred meets_selection_criteria_of_u_Sel3[x: u_Cartesian4] {
x.u_id = u_ownerId
}
fact { all y:u_Cartesian4 | meets_selection_criteria_of_u_Sel3[y] <=> y in u_Sel3 }
sig u_Pi5 in u_Owner {}

sig u_Cartesian10 in u_Owner {}
fact { u_Cartesian10 = u_Owner }

fact { u_Pi8 = u_Sel6 }
sig u_Cartesian2 in u_Owner {}
fact { u_Cartesian2 = u_Owner }

sig u_Pi17 in u_Owner {}

Alloy Summary by Akash code starts 
 
 
////////////////////////////////////////////////////////////////////////
 Owner.pets=Pi
| Join
| | Sel
| | | Cartesian
| | | | ClassRef(Owner)
| | | Equals
| | | | FieldRef(owner.id)
| | | | ownerId
| | ClassRef(Pet)
| | Equals
| | | Alpha.id
| | | Pet.owner_id
| List
| | birthDate(org.springframework.samples.petclinic.owner.Pet.birthDate)
| | Pet.type=Pi
| | | Join
| | | | Alpha
| | | | ClassRef(PetType)
| | | | Equals
| | | | | Alpha.type_id
| | | | | PetType.id
| | | List
| | | | name
| | | | id
| | Pet.owner=Pi
| | | Join
| | | | Alpha
| | | | ClassRef(Owner)
| | | | Equals
| | | | | Alpha.owner_id
| | | | | Owner.id
| | | List
| | | | address
| | | | city
| | | | telephone
| | | | Owner.pets=Pi
| | | | | Join
| | | | | | Alpha
| | | | | | ClassRef(Pet)
| | | | | | Equals
| | | | | | | Alpha.id
| | | | | | | Pet.owner_id
| | | | | List
| | | | | | birthDate
| | | | | | NullOp
| | | | | | NullOp
| | | | | | NullOp
| | | | | | name
| | | | | | id
| | | | firstName
| | | | lastName
| | | | id
| | Pet.visits=Pi
| | | Join
| | | | Alpha
| | | | ClassRef(Visit)
| | | | Equals
| | | | | Alpha.id
| | | | | Visit.pet_id
| | | List
| | | | date
| | | | description
| | | | petId
| | | | id
| | name(org.springframework.samples.petclinic.owner.Pet.name)
| | id(org.springframework.samples.petclinic.owner.Pet.id)
 sig u_Owner18 in u_Owner {} 
fact{all v0 : u_Owner | v0.id = ownerId <=> v0 in u_Owner18 } 

 fact { all v0 : u_Owner18 | all v1 : u_Pet | v0.id = v1.u_owner_id <=> v1 in v0.u_pets }
 fact { all alpha : u_Owner18 |  all v2 : u_PetType | alpha.type_id = v2.u_id <=> v2 in alpha.u_type } 

 fact { all alpha : u_Owner18 |  all v2 : u_Owner | alpha.owner_id = v2.u_id <=> v2 in alpha.u_owner } 

 fact { all v0 : u_Owner18 | all alpha : v0.u_owner |  all v3 : u_Pet | alpha.id = v3.u_owner_id <=> v3 in alpha.u_pets } 

 fact { all alpha : u_Owner18 |  all v2 : u_Visit | alpha.id = v2.u_pet_id <=> v2 in alpha.u_visits } 

 Alloy Summary by Akash code ends 
 
 
sig u_Pi14 in u_Owner {}

sig u_Cartesian4 in u_Owner {}
fact { u_Cartesian4 = u_Owner }

fact { u_Pi14 = u_Sel12 }
fact { u_Pi11 = u_Sel9 }
sig u_Cartesian13 in u_Owner {}
fact { u_Cartesian13 = u_Owner }

sig u_Pi11 in u_Owner {}

fact { u_Pi21 = u_Sel19 }
sig u_Pi8 in u_Owner {}

sig u_Cartesian7 in u_Owner {}
fact { u_Cartesian7 = u_Owner }

sig u_Cartesian20 in u_Owner {}
fact { u_Cartesian20 = u_Owner }

fact { u_Pi5 = u_Sel3 }
sig u_Cartesian16 in u_Owner {}
fact { u_Cartesian16 = u_Owner }

fact { u_Pi17 = u_Sel15 }
sig u_Pi21 in u_Owner {}

sig mu___modelattribute__owner_id in univ {}
fact { mu___modelattribute__owner_id = u_Pi14.u_id }
sig mu___modelattribute__owner_lastName in univ {}
fact { mu___modelattribute__owner_lastName = u_Pi17.u_lastName }
sig mu___modelattribute__owner_firstName in univ {}
fact { mu___modelattribute__owner_firstName = u_Pi11.u_firstName }
sig mu___modelattribute__owner_pets in univ {}
fact { mu___modelattribute__owner_pets = Alloy Summary by Akash code starts 
 
 
////////////////////////////////////////////////////////////////////////
 Owner.pets=Pi
| Join
| | Sel
| | | Cartesian
| | | | ClassRef(Owner)
| | | Equals
| | | | FieldRef(owner.id)
| | | | ownerId
| | ClassRef(Pet)
| | Equals
| | | Alpha.id
| | | Pet.owner_id
| List
| | birthDate(org.springframework.samples.petclinic.owner.Pet.birthDate)
| | Pet.type=Pi
| | | Join
| | | | Alpha
| | | | ClassRef(PetType)
| | | | Equals
| | | | | Alpha.type_id
| | | | | PetType.id
| | | List
| | | | name
| | | | id
| | Pet.owner=Pi
| | | Join
| | | | Alpha
| | | | ClassRef(Owner)
| | | | Equals
| | | | | Alpha.owner_id
| | | | | Owner.id
| | | List
| | | | address
| | | | city
| | | | telephone
| | |