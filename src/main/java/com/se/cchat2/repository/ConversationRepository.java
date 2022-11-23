package com.se.cchat2.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.se.cchat2.entity.Conversation;
import com.se.cchat2.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
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

    public String getLastConvId() throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection("Conversations");
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

    public String create(Conversation newConversation) throws ExecutionException, InterruptedException {
        int newCid = Integer.parseInt(getLastConvId()) + 1;
        String cid = String.valueOf(newCid);
        newConversation.setCid(cid);
        newConversation.setType("group");
        ApiFuture<WriteResult> api = db.collection("Conversations").document(cid).set(newConversation);
        api.get();
        return newConversation.getCid();
    }

    public String createDefault() throws ExecutionException, InterruptedException {
        Conversation newConversation = new Conversation();
        int newCid = Integer.parseInt(getLastConvId()) + 1;
        String cid = String.valueOf(newCid);
        newConversation.setCid(cid);
        newConversation.setName(cid);
        newConversation.setType("direct");
        ApiFuture<WriteResult> api = db.collection("Conversations").document(cid).set(newConversation);
        api.get();
        return cid;
    }

    public List<Conversation> getAllDirects() throws ExecutionException, InterruptedException {
        List<Conversation> cons = new ArrayList<>();
        CollectionReference ref = db.collection("Conversations");
        Query query = ref.whereEqualTo("type", "direct");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot d : querySnapshot.get().getDocuments()) {
            cons.add(d.toObject(Conversation.class));
        }
        return cons;
    }

    public String deleteConv(String cid) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResult = db.collection("Conversations").document(cid).delete();
        return writeResult.get().getUpdateTime().toString();
    }

}
