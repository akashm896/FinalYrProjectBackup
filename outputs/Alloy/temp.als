//__modelattribute__pet : mu___modelattribute__pet
//__modelattribute__pet.birthDate : mu___modelattribute__pet_birthDate
//__modelattribute__pet.id : mu___modelattribute__pet_id
//__modelattribute__pet.name : mu___modelattribute__pet_name
//__modelattribute__pet.owner : mu___modelattribute__pet_owner
//__modelattribute__pet.type : mu___modelattribute__pet_type
//__modelattribute__pet.visits : mu___modelattribute__pet_visits
sig FieldData {}
one sig u_0 extends FieldData {}
one sig u_MethodWontHandleOp2 in FieldData {}
one sig u_pet_birthDate in FieldData {}
one sig u___modelattribute__pet_type in FieldData {}
one sig u___modelattribute__pet_name in FieldData {}
one sig u_pet in FieldData {}
one sig u___modelattribute__pet in FieldData {}
one sig u___modelattribute__pet_birthDate in FieldData {}
one sig u___modelattribute__pet_visits in FieldData {}
one sig u_pet_visits in FieldData {}
one sig u_pet_type in FieldData {}
one sig u___modelattribute__pet_id in FieldData {}
one sig u_pet_id in FieldData {}
one sig u_pet_owner in FieldData {}
one sig u_pet_name in FieldData {}
one sig u___modelattribute__pet_owner in FieldData {}
sig u__6 in univ {}
fact { u__5 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_name) else (u___modelattribute__pet_name)) }
fact { u__8 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_visits) else (u___modelattribute__pet_visits)) }
sig u__5 in univ {}
sig u__1 in univ {}
fact { u__7 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_type) else (u___modelattribute__pet_type)) }
sig u__3 in univ {}
fact { u__6 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_owner) else (u___modelattribute__pet_owner)) }
sig u__7 in univ {}
fact { u__4 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_id) else (u___modelattribute__pet_id)) }
fact { u__1 = ((u_MethodWontHandleOp2 != u_0) => (u_pet) else (u___modelattribute__pet)) }
fact { u__3 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_birthDate) else (u___modelattribute__pet_birthDate)) }
sig u__8 in univ {}
sig u__4 in univ {}
sig mu___modelattribute__pet_name in univ {}
fact { mu___modelattribute__pet_name = u__5 }
sig mu___modelattribute__pet_id in univ {}
fact { mu___modelattribute__pet_id = u__4 }
sig mu___modelattribute__pet_birthDate in univ {}
fact { mu___modelattribute__pet_birthDate = u__3 }
sig mu___modelattribute__pet in univ {}
fact { mu___modelattribute__pet = u__1 }
sig mu___modelattribute__pet_owner in univ {}
fact { mu___modelattribute__pet_owner = u__6 }
sig mu___modelattribute__pet_type in univ {}
fact { mu___modelattribute__pet_type = u__7 }
sig mu___modelattribute__pet_visits in univ {}
fact { mu___modelattribute__pet_visits = u__8 }
sig BottomNode in FieldData {}
