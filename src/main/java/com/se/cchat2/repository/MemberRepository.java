package com.se.cchat2.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.se.cchat2.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
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

    public String getLastMemId() throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection("Members");
        ApiFuture<QuerySnapshot> api = ref.get();
        QuerySnapshot doc = api.get();
        List<QueryDocumentSnapshot> docs = doc.getDocuments();
        if(docs.size() == 0){
            return "0";
        }
        else{
            List<Integer> docId = new ArrayList<>();
            for(QueryDocumentSnapshot ds : docs){
                docId.add(Integer.parseInt(ds.getId()));
            }
            Collections.sort(docId);
            return String.valueOf(docId.get(docId.size()-1));
        }
    }

    public String create(String cid, String uid, String role) throws ExecutionException, InterruptedException {
        int newMid = Integer.parseInt(getLastMemId()) + 1;
        String mid = String.valueOf(newMid);
        Member newMem = new Member(mid, cid, role, uid);
        ApiFuture<WriteResult> api = db.collection("Members").document(mid).set(newMem);
        return api.get().getUpdateTime().toString();
    }

    public Member getByCidUid(String cid, String uid) throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection("Members");
        Query query = ref.whereEqualTo("cid", cid).whereEqualTo("uid", uid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot d : querySnapshot.get().getDocuments()) {
            return d.toObject(Member.class);
        }
        return new Member();
    }

}
