package com.se.cchat2.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.se.cchat2.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

@Repository
public class MessageRepository {

    Firestore db = FirestoreClient.getFirestore();

    public String sendMessage(Message newMess) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> api = db.collection("Messages").document(newMess.getMsid()).set(newMess);
        return api.get().getUpdateTime().toString();
    }

    public String getLastMessId() throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection("Messages");
        ApiFuture<QuerySnapshot> api = ref.get();
        QuerySnapshot doc = api.get();
        List<QueryDocumentSnapshot> docs = doc.getDocuments();
        List<Integer> docId = new ArrayList<>();
        for(QueryDocumentSnapshot ds : docs){
            docId.add(Integer.parseInt(ds.getId()));
        }
        Collections.sort(docId);
        return String.valueOf(docId.get(docId.size()-1));
    }

//    public List<Message> getMessages(String cid) throws ExecutionException, InterruptedException {
//        List<Message> list = new ArrayList<>();
//        CollectionReference ref = db.collection("Messages");
//        Query query = ref.whereEqualTo("cid", cid);
//        ApiFuture<QuerySnapshot> querySnapshot = query.get();
//        for (DocumentSnapshot d : querySnapshot.get().getDocuments()) {
//            list.add(d.toObject(Message.class));
//        }
//        return list;
//    }

    public List<Message> getMessages(String cid) throws ExecutionException, InterruptedException {
        List<Message> list = new ArrayList<>();
        CollectionReference ref = db.collection("Messages");
        Query query = ref.whereEqualTo("cid", cid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> docs = querySnapshot.get().getDocuments();
        List<Integer> docId = new ArrayList<>();
        for(QueryDocumentSnapshot ds : docs){
            docId.add(Integer.parseInt(ds.getId()));
        }
        Collections.sort(docId);
        for(Integer i : docId){
            list.add(ref.document(String.valueOf(i)).get().get().toObject(Message.class));
        }
        return list;
    }

}
