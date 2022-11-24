package com.se.cchat2.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.se.cchat2.entity.Message;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Repository
public class MessageRepository {

    Firestore db = FirestoreClient.getFirestore();

    final String password = "cchat128";

    public String sendMessage(Message newMess) throws ExecutionException, InterruptedException {
        if(newMess.getContent().length()<2000){
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(password);
            String encrypted= encryptor.encrypt(newMess.getContent());
            newMess.setContent(encrypted);
        }

        ApiFuture<WriteResult> api = db.collection("Messages").document(newMess.getMsid()).set(newMess);

        return api.get().getUpdateTime().toString();
    }

    public String getLastMessId() throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection("Messages");
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
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);

        for(QueryDocumentSnapshot ds : docs){
            docId.add(Integer.parseInt(ds.getId()));
        }
        Collections.sort(docId);
        for(Integer i : docId){
            Message mes = ref.document(String.valueOf(i)).get().get().toObject(Message.class);
            if(mes.getContent().length()<2000){
                String decrypted = encryptor.decrypt(mes.getContent());
                mes.setContent(decrypted);
            }
            list.add(mes);
        }
        return list;
    }

    public String deleteMessage(String msid) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResult = db.collection("Messages").document(msid).delete();
        return writeResult.get().getUpdateTime().toString();
    }

    public List<Message> getMessageIds(String cid) throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection("Messages");
        Query query = ref.whereEqualTo("cid", cid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> docs = querySnapshot.get().getDocuments();
        List<Message> docId = new ArrayList<>();

        for(QueryDocumentSnapshot ds : docs){
            docId.add(ds.toObject(Message.class));
        }
        return docId;
    }

    public List<Message> getMessageId(String cid, String uid) throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection("Messages");
        Query query = ref.whereEqualTo("cid", cid).whereEqualTo("uid", uid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> docs = querySnapshot.get().getDocuments();
        List<Message> docId = new ArrayList<>();

        for(QueryDocumentSnapshot ds : docs){
            docId.add(ds.toObject(Message.class));
        }
        return docId;
    }

}
