//__modelattribute__collection : mu___modelattribute__collection
sig FieldData {}
one sig lastName in FieldData {}
Alloy Summary by Akash code starts 
 
 
////////////////////////////////////////////////////////////////////////
 Pi
| Sel
| | Cartesian
| | | ClassRef(Owner)
| | Like
| | | FieldRef(owner.lastName)
| | | lastName
| List
| | address
| | city
| | telephone
| | Owner.pets=Pi
| | | Join
| | | | ClassRef(Owner)
| | | | ClassRef(Pet)
| | | | Equals
| | | | | Owner.id
| | | | | Pet.owner_id
| | | List
| | | | birthDate
| | | | Pet.type=Pi
| | | | | Join
| | | | | | ClassRef(Pet)
| | | | | | ClassRef(PetType)
| | | | | | Equals
| | | | | | | Pet.type_id
| | | | | | | PetType.id
| | | | | List
| | | | | | name
| | | | | | id
| | | | Pet.owner=Pi
| | | | | Join
| | | | | | ClassRef(Pet)
| | | | | | ClassRef(Owner)
| | | | | | Equals
| | | | | | | Pet.owner_id
| | | | | | | Owner.id
| | | | | List
| | | | | | address
| | | | | | city
| | | | | | telephone
| | | | | | firstName
| | | | | | lastName
| | | | | | id
| | | | visits
| | | | name
| | | | id
| | firstName
| | lastName
| | id
 sig u_Owner1 in u_Owner {} 
fact{all v0 : u_Owner | v0.lastname = lastName <=> v0 in u_Owner1} 

 fact { all alpha : u_Owner1 |  all v2 : u_Pet | alpha.id = v2.u_owner_id <=> v2 in alpha.u_pets } 

 fact { all v0 : u_Owner1 | all alpha : v0.u_pets |  all v3 : u_PetType | alpha.type_id = v3.u_id <=> v3 in alpha.u_type } 

 fact { all v0 : u_Owner1 | all alpha : v0.u_pets |  all v3 : u_Owner | alpha.owner_id = v3.u_id <=> v3 in alpha.u_owner } 

 Alloy Summary by Akash code ends 
 
 
sig mu___modelattribute__collection in univ {}
fact { mu___modelattribute__collection = u_Owner1 }
sig BottomNode in FieldData {}
