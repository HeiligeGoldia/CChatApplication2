package com.se.cchat2.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.se.cchat2.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
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

    public List<Member> loadUserMem(String uid) throws ExecutionException, InterruptedException {
        List<Member> list = new ArrayList<>();
        CollectionReference ref = db.collection("Members");
        Query query = ref.whereEqualTo("uid", uid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot d : querySnapshot.get().getDocuments()) {
            list.add(d.toObject(Member.class));
        }
        return list;
    }

    public List<Member> loadConvMem(String cid) throws ExecutionException, InterruptedException {
        List<Member> list = new ArrayList<>();
        CollectionReference ref = db.collection("Members");
        Query query = ref.whereEqualTo("cid", cid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot d : querySnapshot.get().getDocuments()) {
            list.add(d.toObject(Member.class));
        }
        return list;
    }

}
