package com.se.cchat2.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.se.cchat2.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class MemberRepository {

    Firestore db = FirestoreClient.getFirestore();

    public Member getById(String mid) throws ExecutionException, InterruptedException {
        Member mem;
        DocumentReference ref = db.collection("Member").document(mid);
        ApiFuture<DocumentSnapshot> api = ref.get();
        DocumentSnapshot doc = api.get();
        if(doc.exists()){
            mem = doc.toObject(Member.class);
            return mem;
        }
        return new Member();
    }

}
