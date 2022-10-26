package com.se.cchat2.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.se.cchat2.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class MessageRepository {

    Firestore db = FirestoreClient.getFirestore();

    public String sendMessage(Message newMess) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> api = db.collection("Messages").document(newMess.getMsid()).set(newMess);
        return api.get().getUpdateTime().toString();
    }

    public List<Message> getMessages(String cid) throws ExecutionException, InterruptedException {
        List<Message> list = new ArrayList<>();
        CollectionReference ref = db.collection("Messages");
        Query query = ref.whereEqualTo("cid", cid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot d : querySnapshot.get().getDocuments()) {
            list.add(d.toObject(Message.class));
        }
        return list;
    }

}
