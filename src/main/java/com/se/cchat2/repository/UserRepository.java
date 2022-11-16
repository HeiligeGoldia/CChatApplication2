package com.se.cchat2.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.se.cchat2.entity.User;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

@Repository
public class UserRepository {

    Firestore db = FirestoreClient.getFirestore();

    public String create(User newUser) throws ExecutionException, InterruptedException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(newUser.getPassword().getBytes());
        byte[] bytes = digest.digest();
        StringBuilder s = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        newUser.setPassword(s.toString());
        ApiFuture<WriteResult> api = db.collection("Users").document(newUser.getUid()).set(newUser);
        return api.get().getUpdateTime().toString();
    }

    public String register1(User newUser) throws ExecutionException, InterruptedException {
        String sdt = newUser.getPhoneNumber();
        String sdtp = "+84" + sdt.substring(1, sdt.length());
        if(findBySdt(sdt).getUid()!=null){
            return "Phone number already in use";
        }
        else{
            Twilio.init("AC96e6a911a4dbdeb1ba8e7d5aaabedd76", "a46fcbb8d7640d91183e38d5e8e0a73d");
            Verification verification = Verification.creator("VA86635201a4bbafb69394a1cf3c2e7b6b", sdtp, "sms").create();
            return "OTP sent";
        }
    }

    public String register2(User newUser, String otp) throws ExecutionException, InterruptedException, NoSuchAlgorithmException {
        String sdt = newUser.getPhoneNumber();
        String sdtp = "+84" + sdt.substring(1, sdt.length());
        VerificationCheck verificationCheck = VerificationCheck.creator("VA86635201a4bbafb69394a1cf3c2e7b6b", otp)
                .setTo(sdtp)
                .create();
        if(verificationCheck.getStatus().equals("approved")){
            return create(newUser);
        }
        else {
            return "OTP invalid";
        }
    }

    public User findByUid(String uid) throws ExecutionException, InterruptedException {
//        ArrayList<User> users = new ArrayList<>();
        User user;
        DocumentReference ref = db.collection("Users").document(uid);
        ApiFuture<DocumentSnapshot> api = ref.get();
        DocumentSnapshot doc = api.get();
//        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirestoreException e) {
//                users.add(documentSnapshot.toObject(User.class));
//                System.out.println(users.get(users.size()-1).toString());
//            }
//        });
        if(doc.exists()){
            user = doc.toObject(User.class);
            return user;
        }
        return new User();
    }

    public User findBySdt(String sdt) throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection("Users");
        Query query = ref.whereEqualTo("phoneNumber", sdt);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot d : querySnapshot.get().getDocuments()) {
            return d.toObject(User.class);
        }
        return new User();
    }

    public String delete(String uid) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResult = db.collection("Users").document(uid).delete();
        return writeResult.get().getUpdateTime().toString();
    }

}
