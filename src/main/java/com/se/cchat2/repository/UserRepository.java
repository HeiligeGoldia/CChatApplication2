package com.se.cchat2.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.se.cchat2.controller.UserController;
import com.se.cchat2.entity.User;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class UserRepository {
    Firestore db = FirestoreClient.getFirestore();

    public String create(User newUser) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> api = db.collection("Users").document(newUser.getUid()).set(newUser);
        return api.get().getUpdateTime().toString();
    }

    public User findByUid(String uid) throws ExecutionException, InterruptedException {
        ArrayList<User> users = new ArrayList<>();
        User user;
        DocumentReference ref = db.collection("Users").document(uid);
        ApiFuture<DocumentSnapshot> api = ref.get();
        DocumentSnapshot doc = api.get();
        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirestoreException e) {
                users.add(documentSnapshot.toObject(User.class));
                System.out.println(users.get(users.size()-1).toString());
            }
        });
        if(doc.exists()){
            user = doc.toObject(User.class);
            return user;
        }
        return new User();
    }

}
