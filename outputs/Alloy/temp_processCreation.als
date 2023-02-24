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
one sig u_0 extends FieldData {}
one sig u_MethodWontHandleOp2 in FieldData {}
one sig u___modelattribute__pet_owner_telephone in FieldData {}
one sig u_owner_lastName in FieldData {}
one sig u___modelattribute__pet_type in FieldData {}
one sig u_owner_address in FieldData {}
one sig u___modelattribute__pet_owner_id in FieldData {}
one sig u_owner_telephone in FieldData {}
one sig u_pet in FieldData {}
one sig u___modelattribute__pet in FieldData {}
one sig u_owner_pets in FieldData {}
one sig u___modelattribute__pet_owner_pets in FieldData {}
one sig u_pet_type_name in FieldData {}
one sig u_pet_type in FieldData {}
one sig u___modelattribute__pet_id in FieldData {}
one sig u___modelattribute__pet_owner_city in FieldData {}
one sig u_pet_owner in FieldData {}
one sig u_pet_type_id in FieldData {}
one sig u_pet_name in FieldData {}
one sig u___modelattribute__pet_owner in FieldData {}
one sig u_pet_birthDate in FieldData {}
one sig u_owner_firstName in FieldData {}
one sig u___modelattribute__pet_owner_firstName in FieldData {}
one sig u___modelattribute__pet_owner_lastName in FieldData {}
one sig u___modelattribute__pet_type_id in FieldData {}
one sig u___modelattribute__pet_name in FieldData {}
one sig u___modelattribute__pet_birthDate in FieldData {}
one sig u___modelattribute__pet_visits in FieldData {}
one sig u_pet_visits in FieldData {}
one sig u_pet_id in FieldData {}
one sig u___modelattribute__pet_type_name in FieldData {}
one sig u___modelattribute__pet_owner_address in FieldData {}
one sig u_owner_city in FieldData {}
fact { u_____NotEq_____Method10 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_id) else (u___modelattribute__pet_owner_id)) }
fact { u_____NotEq_____Method9 = ((u_MethodWontHandleOp2 != u_0) => (u_owner_firstName) else (u___modelattribute__pet_owner_firstName)) }
sig u_____NotEq_____Method10 in univ {}
sig u_____NotEq_____Method13 in univ {}
fact { u_____NotEq_____Method7 = ((u_MethodWontHandleOp2 != u_0) => (u_owner_address) else (u___modelattribute__pet_owner_address)) }
sig u_____NotEq_____Method17 in univ {}
sig u_____NotEq_____Method8 in univ {}
sig u_____NotEq_____Method4 in univ {}
fact { u_____NotEq_____Method1 = ((u_MethodWontHandleOp2 != u_0) => (u_pet) else (u___modelattribute__pet)) }
fact { u_____NotEq_____Method16 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_type_name) else (u___modelattribute__pet_type_name)) }
sig u_____NotEq_____Method14 in univ {}
fact { u_____NotEq_____Method14 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_type) else (u___modelattribute__pet_type)) }
sig u_____NotEq_____Method11 in univ {}
sig u_____NotEq_____Method1 in univ {}
sig u_____NotEq_____Method5 in univ {}
fact { u_____NotEq_____Method11 = ((u_MethodWontHandleOp2 != u_0) => (u_owner_lastName) else (u___modelattribute__pet_owner_lastName)) }
fact { u_____NotEq_____Method17 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_visits) else (u___modelattribute__pet_visits)) }
sig u_____NotEq_____Method9 in univ {}
sig u_____NotEq_____Method15 in univ {}
fact { u_____NotEq_____Method13 = ((u_MethodWontHandleOp2 != u_0) => (u_owner_telephone) else (u___modelattribute__pet_owner_telephone)) }
fact { u_____NotEq_____Method6 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_owner) else (u___modelattribute__pet_owner)) }
fact { u_____NotEq_____Method4 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_id) else (u___modelattribute__pet_id)) }
fact { u_____NotEq_____Method5 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_name) else (u___modelattribute__pet_name)) }
sig u_____NotEq_____Method6 in univ {}
fact { u_____NotEq_____Method15 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_type_id) else (u___modelattribute__pet_type_id)) }
sig u_____NotEq_____Method16 in univ {}
fact { u_____NotEq_____Method8 = ((u_MethodWontHandleOp2 != u_0) => (u_owner_city) else (u___modelattribute__pet_owner_city)) }
sig u_____NotEq_____Method12 in univ {}
sig u_____NotEq_____Method7 in univ {}
sig u_____NotEq_____Method3 in univ {}
fact { u_____NotEq_____Method12 = ((u_MethodWontHandleOp2 != u_0) => (u_owner_pets) else (u___modelattribute__pet_owner_pets)) }
fact { u_____NotEq_____Method3 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_birthDate) else (u___modelattribute__pet_birthDate)) }
sig mu___modelattribute__pet_owner_id in univ {}
fact { mu___modelattribute__pet_owner_id = u_____NotEq_____Method10 }
sig mu___modelattribute__pet_owner_telephone in univ {}
fact { mu___modelattribute__pet_owner_telephone = u_____NotEq_____Method13 }
sig mu___modelattribute__pet_birthDate in univ {}
fact { mu___modelattribute__pet_birthDate = u_____NotEq_____Method3 }
sig mu___modelattribute__pet in univ {}
fact { mu___modelattribute__pet = u_____NotEq_____Method1 }
sig mu___modelattribute__pet_owner_address in univ {}
fact { mu___modelattribute__pet_owner_address = u_____NotEq_____Method7 }
sig mu___modelattribute__pet_owner in univ {}
fact { mu___modelattribute__pet_owner = u_____NotEq_____Method6 }
sig mu___modelattribute__pet_owner_pets in univ {}
fact { mu___modelattribute__pet_owner_pets = u_____NotEq_____Method12 }
sig mu___modelattribute__pet_type_name in univ {}
fact { mu___modelattribute__pet_type_name = u_____NotEq_____Method16 }
sig mu___modelattribute__pet_name in univ {}
fact { mu___modelattribute__pet_name = u_____NotEq_____Method5 }
sig mu___modelattribute__pet_id in univ {}
fact { mu___modelattribute__pet_id = u_____NotEq_____Method4 }
sig mu___modelattribute__pet_owner_lastName in univ {}
fact { mu___modelattribute__pet_owner_lastName = u_____NotEq_____Method11 }
sig mu___modelattribute__pet_type_id in univ {}
fact { mu___modelattribute__pet_type_id = u_____NotEq_____Method15 }
sig mu___modelattribute__pet_type in univ {}
fact { mu___modelattribute__pet_type = u_____NotEq_____Method14 }
sig mu___modelattribute__pet_owner_city in univ {}
fact { mu___modelattribute__pet_owner_city = u_____NotEq_____Method8 }
sig mu___modelattribute__pet_owner_firstName in univ {}
fact { mu___modelattribute__pet_owner_firstName = u_____NotEq_____Method9 }
sig mu___modelattribute__pet_visits in univ {}
fact { mu___modelattribute__pet_visits = u_____NotEq_____Method17 }
sig BottomNode in FieldData {}
