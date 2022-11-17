package com.se.cchat2.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.se.cchat2.entity.Friend;
import com.se.cchat2.entity.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class FriendRepository {

    Firestore db = FirestoreClient.getFirestore();

    public String getLastFriendId() throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection("Friend");
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

//    public Friend getById(String fid) throws ExecutionException, InterruptedException {
//        DocumentReference ref = db.collection("Friend").document(fid);
//        ApiFuture<DocumentSnapshot> api = ref.get();
//        DocumentSnapshot doc = api.get();
//        if(doc.exists()){
//            return doc.toObject(Friend.class);
//        }
//        return new Friend();
//    }

    public String create(Friend newFriend) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> api = db.collection("Friend").document(newFriend.getFid()).set(newFriend);
        return api.get().getUpdateTime().toString();
    }

    public String delete(String fid) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResult = db.collection("Friend").document(fid).delete();
        return writeResult.get().getUpdateTime().toString();
    }

    public Friend findBy2Uid(String uid1, String uid2) throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection("Friend");
        Query query1 = ref.whereEqualTo("uid1", uid1).whereEqualTo("uid2", uid2).whereEqualTo("status", "Friend");
        ApiFuture<QuerySnapshot> querySnapshot1 = query1.get();
        if(querySnapshot1.get().getDocuments().isEmpty()){
            Query query2 = ref.whereEqualTo("uid1", uid2).whereEqualTo("uid2", uid1).whereEqualTo("status", "Friend");
            ApiFuture<QuerySnapshot> querySnapshot2 = query2.get();
            if(querySnapshot2.get().getDocuments().isEmpty()){
                return new Friend();
            }
            else{
                return querySnapshot2.get().getDocuments().get(0).toObject(Friend.class);
            }
        }
        else{
            return querySnapshot1.get().getDocuments().get(0).toObject(Friend.class);
        }
    }

    public Friend findPendingBy2Uid(String uid1, String uid2) throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection("Friend");
        Query query1 = ref.whereEqualTo("uid1", uid1).whereEqualTo("uid2", uid2).whereEqualTo("status", "Pending");
        ApiFuture<QuerySnapshot> querySnapshot1 = query1.get();
        if(querySnapshot1.get().getDocuments().isEmpty()){
            Query query2 = ref.whereEqualTo("uid1", uid2).whereEqualTo("uid2", uid1).whereEqualTo("status", "Pending");
            ApiFuture<QuerySnapshot> querySnapshot2 = query2.get();
            if(querySnapshot2.get().getDocuments().isEmpty()){
                return new Friend();
            }
            else{
                return querySnapshot2.get().getDocuments().get(0).toObject(Friend.class);
            }
        }
        else{
            return querySnapshot1.get().getDocuments().get(0).toObject(Friend.class);
        }
    }

    public List<String> findUserFriends(String uid) throws ExecutionException, InterruptedException {
        List<String> uids = new ArrayList<>();
        CollectionReference ref = db.collection("Friend");
        Query query1 = ref.whereEqualTo("uid1", uid).whereEqualTo("status", "Friend");
        ApiFuture<QuerySnapshot> querySnapshot1 = query1.get();
        Query query2 = ref.whereEqualTo("uid2", uid).whereEqualTo("status", "Friend");
        ApiFuture<QuerySnapshot> querySnapshot2 = query2.get();
        for (DocumentSnapshot d1 : querySnapshot1.get().getDocuments()) {
            uids.add(d1.toObject(Friend.class).getUid2());
        }
        for (DocumentSnapshot d2 : querySnapshot2.get().getDocuments()) {
            uids.add(d2.toObject(Friend.class).getUid1());
        }
        return uids;
    }

    public List<String> findPendingRequests(String uid) throws ExecutionException, InterruptedException {
        List<String> uids = new ArrayList<>();
        CollectionReference ref = db.collection("Friend");
        Query query1 = ref.whereEqualTo("uid2", uid).whereEqualTo("status", "Pending");
        ApiFuture<QuerySnapshot> querySnapshot1 = query1.get();
        for (DocumentSnapshot d1 : querySnapshot1.get().getDocuments()) {
            uids.add(d1.toObject(Friend.class).getUid1());
        }
        return uids;
    }

    public List<String> findSentRequests(String uid) throws ExecutionException, InterruptedException {
        List<String> uids = new ArrayList<>();
        CollectionReference ref = db.collection("Friend");
        Query query1 = ref.whereEqualTo("uid1", uid).whereEqualTo("status", "Pending");
        ApiFuture<QuerySnapshot> querySnapshot1 = query1.get();
        for (DocumentSnapshot d1 : querySnapshot1.get().getDocuments()) {
            uids.add(d1.toObject(Friend.class).getUid2());
        }
        return uids;
    }

    public boolean checkF(String uid1, String uid2) throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection("Friend");
        Query query1 = ref.whereEqualTo("uid1", uid1).whereEqualTo("uid2", uid2);
        ApiFuture<QuerySnapshot> querySnapshot1 = query1.get();
        if(querySnapshot1.get().getDocuments().isEmpty()){
            Query query2 = ref.whereEqualTo("uid1", uid2).whereEqualTo("uid2", uid1);
            ApiFuture<QuerySnapshot> querySnapshot2 = query2.get();
            if(querySnapshot2.get().getDocuments().isEmpty()){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public String checkFStt(String uuid, String fuid) throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection("Friend");

        Query query1 = ref.whereEqualTo("uid1", uuid).whereEqualTo("uid2", fuid);
        ApiFuture<QuerySnapshot> querySnapshot1 = query1.get();

        for (DocumentSnapshot d1 : querySnapshot1.get().getDocuments()) {
            Friend fd1 = d1.toObject(Friend.class);
            if(fd1.getStatus().equals("Pending")){
                return "Cancel request";
            }
            else if(fd1.getStatus().equals("Friend")){
                return "Friend";
            }
        }

        Query query2 = ref.whereEqualTo("uid1", fuid).whereEqualTo("uid2", uuid);
        ApiFuture<QuerySnapshot> querySnapshot2 = query2.get();

        for (DocumentSnapshot d2 : querySnapshot2.get().getDocuments()) {
            Friend fd2 = d2.toObject(Friend.class);
            if(fd2.getStatus().equals("Pending")){
                return "Accept request";
            }
            else if(fd2.getStatus().equals("Friend")){
                return "Friend";
            }
        }

        return "Stranger";

    }

}
