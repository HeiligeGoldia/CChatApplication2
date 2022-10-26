package com.se.cchat2.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.se.cchat2.entity.Conversation;
import com.se.cchat2.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class ConversationRepository {

    Firestore db = FirestoreClient.getFirestore();

    public Conversation getById(String cid) throws ExecutionException, InterruptedException {
        Conversation conv;
        DocumentReference ref = db.collection("Conversations").document(cid);
        ApiFuture<DocumentSnapshot> api = ref.get();
        DocumentSnapshot doc = api.get();
        if(doc.exists()){
            conv = doc.toObject(Conversation.class);
            return conv;
        }
        return new Conversation();
    }

    public List<Conversation> loadUserConv(List<Member> userMem) throws ExecutionException, InterruptedException {
        List<Conversation> list = new ArrayList<>();
        for(Member m : userMem){
            DocumentReference ref = db.collection("Conversations").document(m.getCid());
            ApiFuture<DocumentSnapshot> api = ref.get();
            DocumentSnapshot doc = api.get();
            list.add(doc.toObject(Conversation.class));
        }
        return list;
    }

}
