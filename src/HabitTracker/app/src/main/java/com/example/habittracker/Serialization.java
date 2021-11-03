package com.example.habittracker;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Primary class for all firebase database transactions. Uses singleton design
 * so there is only ever one instance of the firebase at any time. Used for
 * Profile construction and deconstruction, including: Adding/Deleting Profiles
 * Adding/Editing/Deleting Habits Adding/Removing Followers
 *
 * Will eventually be used for habit event transactions as well
 */
public class Serialization {
	private static FirebaseFirestore db = FirebaseFirestore.getInstance();

	private static final String TAG = "Serialization";

	// Firebase collection constants
	private static final String COLLECTION_USERS = "users";
	private static final String COLLECTION_HABITS = "habits";
	private static final String COLLECTION_SOCIAL = "social";

	//profile document keys
	private static final String KEY_FOLLOWERS = "follower";
	private static final String KEY_FOLLOWING = "following";
	private static final String KEY_FOLLOW_REQUESTS = "followRequests";



	// Habit document keys
	private static final String KEY_HABIT_TITLE = "title";
	private static final String KEY_HABIT_REASON = "reason";
	private static final String KEY_HABIT_PUBLIC_VISIBILITY = "publicVisibility";
	private static final String KEY_HABIT_HID = "hid";
	private static final String KEY_HABIT_DATE_TO_START = "dateToStart";
	private static final String KEY_HABIT_WEEKDAYS = "weekdays";

	public static void serializeUserProfile(UserProfile user) {
		ArrayList<Profile> followers = user.getFollowers();
		ArrayList<Profile> following = user.getFollowing();
		ArrayList<FollowRequest> requestInbox = user.getRequests();
		ArrayList<Habit> habits = user.getHabitList();

		String username = user.getUsername();
		ArrayList<String> followerNames = getProfileNames(followers);
		ArrayList<String> followingNames = getProfileNames(following);
		ArrayList<String> followRequestSenders = getRequestSenders(requestInbox);

		Map<String, Object> profile = new HashMap<>();
		profile.put("username", username);


		db.collection(COLLECTION_USERS).document(username)
				.set(profile)
				.addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						Log.d(TAG, "Profile successfully written!");
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.w(TAG, "Profile writing document", e);
					}
				});

		serializeSocials(followerNames, username, KEY_FOLLOWERS);
		serializeSocials(followingNames, username, KEY_FOLLOWING);
		serializeSocials(followRequestSenders, username, KEY_FOLLOW_REQUESTS);


		for (Habit habit : habits){
			addHabit(username, habit);
		}

	}

	private static void serializeSocials(ArrayList<String> names, String username, String mode) {
		Map<String, Object> map = new HashMap<>();
		map.put("usernames", names);
		db.collection(COLLECTION_USERS).document(username).collection(COLLECTION_SOCIAL).document(mode)
				.set(map)
				.addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						Log.d(TAG, "Profile successfully written!");
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.w(TAG, "Profile writing document", e);
					}
				});

	}

	public static UserProfile deserializeUserProfile(String username){
		UserProfile profile = new UserProfile(username);
		ArrayList<String> followerNames;
		ArrayList<String> followingNames;
		ArrayList<String> followRequestSenders;

		followerNames = deserializeSocials(username, KEY_FOLLOWERS);
		followingNames = deserializeSocials(username, KEY_FOLLOWING);
		followRequestSenders = deserializeSocials(username, KEY_FOLLOW_REQUESTS);


		for (String follower : followerNames) {
			FollowedProfile f = new FollowedProfile();
			f.setUsername(follower);
			f.setFollower(username);
			profile.addFollower(f);
		}

		for (String following : followingNames) {
			FollowedProfile f = new FollowedProfile();
			f.setUsername(following);
			f.setFollower(username);
			profile.followUser(f);
		}
		for (String sender : followRequestSenders) {
			FollowRequest f = new FollowRequest(sender, username);
			profile.getInbox().addRequest(f);
		}


		return profile;
	}
	private static ArrayList<String> deserializeSocials(String username, String mode) {

		final ArrayList<String>[] names = new ArrayList[]{new ArrayList<>()};
		DocumentReference docRef = db.collection(COLLECTION_USERS).document(username)
				.collection(COLLECTION_SOCIAL).document(mode);

		// Source can be CACHE, SERVER, or DEFAULT.
		Source source = Source.DEFAULT;

		// Get the document, forcing the SDK to use the offline cache
		docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.isSuccessful()) {

					DocumentSnapshot document = task.getResult();
					Map<String, Object> data = document.getData();
					names[0] = (ArrayList<String>) data.get("usernames");

					Log.d(TAG, "Cached document data: " + document.getData());
				} else {
					Log.d(TAG, "Cached get failed: ", task.getException());
				}
			}
		});

		return names[0];
	}


	private static ArrayList<String> getRequestSenders(ArrayList<FollowRequest> requests) {
		ArrayList<String> senders = new ArrayList<>();
		for (FollowRequest request : requests) {
			senders.add(request.getTarget());

		}
		return senders;
	}

	private static ArrayList<String> getProfileNames(ArrayList<Profile> users) {
		ArrayList<String> names = new ArrayList<>();

		for (Profile user : users) {
			names.add(user.getUsername());
		}

		return names;
	}

	/**
	 * Add a new habit to the user "username" in the firebase database
	 * 
	 * @param username User the habit will be added to
	 * @param habit    The new habit to be added to the Users firebase database
	 * @return true if database transaction was successful, false if unsuccessful
	 */
	public static boolean addHabit(String username, Habit habit) {
		final boolean[] successFlag = { false };
		Map<String, Object> habitMap = new HashMap<>();
		habitMap.put(KEY_HABIT_TITLE, habit.getTitle());
		habitMap.put(KEY_HABIT_REASON, habit.getReason());
		habitMap.put(KEY_HABIT_PUBLIC_VISIBILITY, habit.getPublicVisibility());
		habitMap.put(KEY_HABIT_HID, habit.getHid());
		habitMap.put(KEY_HABIT_DATE_TO_START, habit.getDateToStart());
		habitMap.put(KEY_HABIT_WEEKDAYS, habit.weeklySchedule.getSchedule());

		db.collection(COLLECTION_USERS).document(username).collection(COLLECTION_HABITS).document(habit.getTitle())
				.set(habitMap).addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void unused) {
						successFlag[0] = true;
						Log.d(TAG, "THE DATA WAS SUBMITTED");
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						successFlag[0] = false;
						Log.d(TAG, e.toString());
					}
				});

		// TODO(GLENN): successFlag not working as intended, need to fix later
		return true;
	}

	/**
	 * Update an existing habit in the database from the user "username"
	 * 
	 * @param username User the habit will be updated from
	 * @param habit    The updated habit to be sent to the database
	 * @return true if database transaction was successful, false if unsuccessful
	 */
	public static boolean updateHabit(String username, Habit habit) {
		Map<String, Object> habitMap = new HashMap<>();
		habitMap.put(KEY_HABIT_TITLE, habit.getTitle());
		habitMap.put(KEY_HABIT_REASON, habit.getReason());
		habitMap.put(KEY_HABIT_PUBLIC_VISIBILITY, habit.getPublicVisibility());
		habitMap.put(KEY_HABIT_HID, habit.getHid());
		habitMap.put(KEY_HABIT_DATE_TO_START, habit.getDateToStart());
		habitMap.put(KEY_HABIT_WEEKDAYS, habit.weeklySchedule.getSchedule());

		db.collection(COLLECTION_USERS).document(username).collection(COLLECTION_HABITS).document(habit.getTitle())
				.update(habitMap).addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void unused) {
						Log.d(TAG, "THE DATA WAS UPDATED");
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.d(TAG, e.toString());
					}
				});

		// TODO(GLENN): successFlag not working as intended, need to fix later
		return true;
	}

	// TODO(GLENN): finish this
	// W.I.P set to return Habit when complete
	// cannot figure out how to get data out of onSuccess into scope of getHabit
	public static void getHabit(String username, String habitTitle) {
		Habit habit = new Habit();

		db.collection(COLLECTION_USERS).document(username).collection(COLLECTION_HABITS).document(habitTitle).get()
				.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
					@Override
					public void onSuccess(DocumentSnapshot documentSnapshot) {
						// habit = documentSnapshot.toObject(Habit.class);
						// This is all a W.I.P
						Map dbMap = documentSnapshot.getData();
						habit.setTitle(documentSnapshot.getString("title"));
						habit.setReason(documentSnapshot.getString("reason"));
						// Figure out how to set hid, currently the title is the primary distinction
						habit.setDateToStart(documentSnapshot.getString("dateToStart"));
						habit.setWeeklySchedule(new WeeklySchedule((ArrayList<String>) dbMap.get("weekdays")));

					}
				});

	}

	/**
	 * Permanently delete the provided habit from the users database file
	 * 
	 * @param username The current user logged in
	 * @param habit    The habit to be deleted
	 */
	public void deleteHabit(String username, Habit habit) {
		db.collection(COLLECTION_USERS).document(username).collection(COLLECTION_HABITS).document(habit.getTitle())
				.delete().addOnSuccessListener(new OnSuccessListener<Void>() {

					@Override
					public void onSuccess(Void unused) {
						Log.d(TAG, "Habit has been removed successfully");
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.d(TAG, "Error while deleting habit", e);
					}
				});

	}

}


