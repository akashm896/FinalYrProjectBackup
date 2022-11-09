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
fact { u_____NotEq_____Method7 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_type) else (u___modelattribute__pet_type)) }
fact { u_____NotEq_____Method6 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_owner) else (u___modelattribute__pet_owner)) }
fact { u_____NotEq_____Method4 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_id) else (u___modelattribute__pet_id)) }
sig u_____NotEq_____Method8 in univ {}
fact { u_____NotEq_____Method5 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_name) else (u___modelattribute__pet_name)) }
sig u_____NotEq_____Method6 in univ {}
sig u_____NotEq_____Method4 in univ {}
fact { u_____NotEq_____Method1 = ((u_MethodWontHandleOp2 != u_0) => (u_pet) else (u___modelattribute__pet)) }
fact { u_____NotEq_____Method8 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_visits) else (u___modelattribute__pet_visits)) }
sig u_____NotEq_____Method1 in univ {}
sig u_____NotEq_____Method7 in univ {}
sig u_____NotEq_____Method5 in univ {}
sig u_____NotEq_____Method3 in univ {}
fact { u_____NotEq_____Method3 = ((u_MethodWontHandleOp2 != u_0) => (u_pet_birthDate) else (u___modelattribute__pet_birthDate)) }
sig mu___modelattribute__pet_name in univ {}
fact { mu___modelattribute__pet_name = u_____NotEq_____Method5 }
sig mu___modelattribute__pet_id in univ {}
fact { mu___modelattribute__pet_id = u_____NotEq_____Method4 }
sig mu___modelattribute__pet_birthDate in univ {}
fact { mu___modelattribute__pet_birthDate = u_____NotEq_____Method3 }
sig mu___modelattribute__pet in univ {}
fact { mu___modelattribute__pet = u_____NotEq_____Method1 }
sig mu___modelattribute__pet_owner in univ {}
fact { mu___modelattribute__pet_owner = u_____NotEq_____Method6 }
sig mu___modelattribute__pet_type in univ {}
fact { mu___modelattribute__pet_type = u_____NotEq_____Method7 }
sig mu___modelattribute__pet_visits in univ {}
fact { mu___modelattribute__pet_visits = u_____NotEq_____Method8 }
sig BottomNode in FieldData {}
