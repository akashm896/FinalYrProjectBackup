//__modelattribute__pet : mu___modelattribute__pet
//__modelattribute__pet.birthDate : mu___modelattribute__pet_birthDate
//__modelattribute__pet.id : mu___modelattribute__pet_id
//__modelattribute__pet.name : mu___modelattribute__pet_name
//__modelattribute__pet.owner : mu___modelattribute__pet_owner
//__modelattribute__pet.type : mu___modelattribute__pet_type
//__modelattribute__pet.visits : mu___modelattribute__pet_visits
sig FieldData {}
one sig u_0 extends FieldData {}
one sig u___modelattribute__pet_visits in FieldData {}
one sig u_MethodWontHandleOp2 in FieldData {}
one sig u_pet_visits in FieldData {}
one sig u___modelattribute__pet_type in FieldData {}
one sig u___modelattribute__pet_id in FieldData {}
one sig u___modelattribute__pet_name in FieldData {}
one sig u_pet in FieldData {}
one sig u___modelattribute__pet_owner in FieldData {}
one sig u___modelattribute__pet in FieldData {}
one sig u___modelattribute__pet_birthDate in FieldData {}
fact { u__5 = ((u_MethodWontHandleOp2 != u_0) => (u_Unknown6) else (u___modelattribute__pet_id)) }
lone sig u_Unknown12 in FieldData {}

lone sig u_Unknown10 in FieldData {}

fact { u__11 = ((u_MethodWontHandleOp2 != u_0) => (u_Unknown12) else (u___modelattribute__pet_type)) }
sig u__9 in univ {}
sig u__5 in univ {}
sig u__1 in univ {}
sig u__3 in univ {}
fact { u__3 = ((u_MethodWontHandleOp2 != u_0) => (u_Unknown4) else (u___modelattribute__pet_birthDate)) }
lone sig u_Unknown6 in FieldData {}

sig u__11 in univ {}
sig u__7 in univ {}
fact { u__7 = ((u_MethodWontHandleOp2 != u_0) => (u_Unknown8) else (u___modelattribute__pet_name)) }
fact { u__1 = ((u_MethodWontHandleOp2 != u_0) => (u_pet) else (u___modelattribute__pet)) }
sig u__13 in univ {}
fact { u__9 = ((u_MethodWontHandleOp2 != u_0) => (u_Unknown10) else (u___modelattribute__pet_owner)) }
lone sig u_Unknown4 in FieldData {}

fact { u__13 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_visits) else (u___modelattribute__pet_visits)) }
lone sig u_Unknown8 in FieldData {}

sig mu___modelattribute__pet_name in univ {}
fact { mu___modelattribute__pet_name = u__7 }
sig mu___modelattribute__pet_id in univ {}
fact { mu___modelattribute__pet_id = u__5 }
sig mu___modelattribute__pet_birthDate in univ {}
fact { mu___modelattribute__pet_birthDate = u__3 }
sig mu___modelattribute__pet in univ {}
fact { mu___modelattribute__pet = u__1 }
sig mu___modelattribute__pet_owner in univ {}
fact { mu___modelattribute__pet_owner = u__9 }
sig mu___modelattribute__pet_type in univ {}
fact { mu___modelattribute__pet_type = u__11 }
sig mu___modelattribute__pet_visits in univ {}
fact { mu___modelattribute__pet_visits = u__13 }
sig BottomNode in FieldData {}
