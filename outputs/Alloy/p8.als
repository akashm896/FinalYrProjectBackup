//__modelattribute__pet : mu___modelattribute__pet
//__modelattribute__pet.birthDate : mu___modelattribute__pet_birthDate
//__modelattribute__pet.id : mu___modelattribute__pet_id
//__modelattribute__pet.name : mu___modelattribute__pet_name
//__modelattribute__pet.owner : mu___modelattribute__pet_owner
//__modelattribute__pet.owner.address : mu___modelattribute__pet_owner_address
//__modelattribute__pet.owner.city : mu___modelattribute__pet_owner_city
//__modelattribute__pet.owner.firstName : mu___modelattribute__pet_owner_firstName
//__modelattribute__pet.owner.id : mu___modelattribute__pet_owner_id
//__modelattribute__pet.owner.lastName : mu___modelattribute__pet_owner_lastName
//__modelattribute__pet.owner.pets : mu___modelattribute__pet_owner_pets
//__modelattribute__pet.owner.telephone : mu___modelattribute__pet_owner_telephone
//__modelattribute__pet.type : mu___modelattribute__pet_type
//__modelattribute__pet.type.id : mu___modelattribute__pet_type_id
//__modelattribute__pet.type.name : mu___modelattribute__pet_type_name
//__modelattribute__pet.visits : mu___modelattribute__pet_visits
sig FieldData {}
one sig u__r0_type_name in FieldData {}
one sig u_owner_firstName in FieldData {}
one sig u__r0_type in FieldData {}
one sig u_owner_lastName in FieldData {}
one sig u_owner_address in FieldData {}
one sig u_owner_telephone in FieldData {}
one sig u__r0_owner in FieldData {}
one sig u__r0_owner_pets in FieldData {}
one sig u__r0 in FieldData {}
one sig u__r0_type_id in FieldData {}
one sig u_owner_city in FieldData {}
one sig u__r0_owner_id in FieldData {}
one sig u__r0_visits in FieldData {}
sig u_BottomNode2 in BottomNode {}
sig u_BottomNode3 in BottomNode {}
sig u_BottomNode1 in BottomNode {}
sig mu___modelattribute__pet_owner_id in univ {}
fact { mu___modelattribute__pet_owner_id = u__r0_owner_id }
sig mu___modelattribute__pet_owner_telephone in univ {}
fact { mu___modelattribute__pet_owner_telephone = u_owner_telephone }
sig mu___modelattribute__pet_birthDate in univ {}
fact { mu___modelattribute__pet_birthDate = u_BottomNode1 }
sig mu___modelattribute__pet in univ {}
fact { mu___modelattribute__pet = u__r0 }
sig mu___modelattribute__pet_owner_address in univ {}
fact { mu___modelattribute__pet_owner_address = u_owner_address }
sig mu___modelattribute__pet_owner in univ {}
fact { mu___modelattribute__pet_owner = u__r0_owner }
sig mu___modelattribute__pet_owner_pets in univ {}
fact { mu___modelattribute__pet_owner_pets = u__r0_owner_pets }
sig mu___modelattribute__pet_type_name in univ {}
fact { mu___modelattribute__pet_type_name = u__r0_type_name }
sig mu___modelattribute__pet_name in univ {}
fact { mu___modelattribute__pet_name = u_BottomNode3 }
sig mu___modelattribute__pet_id in univ {}
fact { mu___modelattribute__pet_id = u_BottomNode2 }
sig mu___modelattribute__pet_owner_lastName in univ {}
fact { mu___modelattribute__pet_owner_lastName = u_owner_lastName }
sig mu___modelattribute__pet_type_id in univ {}
fact { mu___modelattribute__pet_type_id = u__r0_type_id }
sig mu___modelattribute__pet_type in univ {}
fact { mu___modelattribute__pet_type = u__r0_type }
sig mu___modelattribute__pet_owner_city in univ {}
fact { mu___modelattribute__pet_owner_city = u_owner_city }
sig mu___modelattribute__pet_owner_firstName in univ {}
fact { mu___modelattribute__pet_owner_firstName = u_owner_firstName }
sig mu___modelattribute__pet_visits in univ {}
fact { mu___modelattribute__pet_visits = u__r0_visits }
sig BottomNode in FieldData {}
