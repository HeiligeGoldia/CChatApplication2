package com.se.cchat2.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.se.cchat2.entity.User;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class UserRepository {
    Firestore db = FirestoreClient.getFirestore();

//    FirebaseAuth auth = FirebaseAuth.getInstance();

    public String create(User newUser) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> api = db.collection("Users").document(newUser.getUid()).set(newUser);
        return api.get().getUpdateTime().toString();
    }

//    public String login(User newUser) throws FirebaseAuthException {
//        UserRecord ur = auth.getUserByPhoneNumber(newUser.getPhoneNumber());
////        ur
//        return "";
//    }
//
//    public String register(User newUser) throws FirebaseAuthException {
//        UserRecord.CreateRequest u = new UserRecord.CreateRequest();
//        u.setPhoneNumber(newUser.getPhoneNumber());
//        u.setPassword(newUser.getPassword());
//        auth.createUser(u);
////        auth.
//        return "";
//    }

    public User findByUid(String uid) throws ExecutionException, InterruptedException {
//        ArrayList<User> users = new ArrayList<>();
        User user;
        DocumentReference ref = db.collection("Users").document(uid);
        ApiFuture<DocumentSnapshot> api = ref.get();
        DocumentSnapshot doc = api.get();
//        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirestoreException e) {
//                users.add(documentSnapshot.toObject(User.class));
//                System.out.println(users.get(users.size()-1).toString());
//            }
//        });
        if(doc.exists()){
            user = doc.toObject(User.class);
            return user;
        }
        return new User();
    }

    public User findBySdt(String sdt) throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection("Users");
        Query query = ref.whereEqualTo("phoneNumber", sdt);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot d : querySnapshot.get().getDocuments()) {
            return d.toObject(User.class);
        }
        return new User();
    }

    public String delete(String uid) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResult = db.collection("Users").document(uid).delete();
        return writeResult.get().getUpdateTime().toString();
    }

}
