package com.example.habittracker;

import android.graphics.Bitmap;
import android.media.Image;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");;
	private static Calendar calendar;

	private static final String TAG = "Serialization";

	// Firebase collection constants
	private static final String COLLECTION_USERS = "users";
	private static final String COLLECTION_HABITS = "habits";
	private static final String COLLECTION_HABIT_EVENTS = "habitEvents";

	// profile document keys
	private static final String KEY_FOLLOWERS = "followers";
	private static final String KEY_FOLLOWING = "following";
	private static final String KEY_INBOX = "followrequestinbox";

	// Habit document keys
	private static final String KEY_HABIT_TITLE = "title";
	private static final String KEY_HABIT_REASON = "reason";
	private static final String KEY_HABIT_PUBLIC_VISIBILITY = "publicVisibility";
	private static final String KEY_HABIT_HID = "hid";
	private static final String KEY_HABIT_DATE_TO_START = "dateToStart";
	private static final String KEY_HABIT_WEEKDAYS = "weekdays";

	//habitEvents document keys
	private static final String KEY_EVENT_DATETIME = "dateTime";
	private static final String KEY_EVENT_COMMENT = "comment";
	private static final String KEY_EVENT_IMAGE = "image";
	private static final String KEY_EVENT_LOCATION = "location";
	private static final String KEY_EVENT_DONE = "done";

	/**
	 * This method saves an entire User to the database
	 * 
	 * @param user UserProfile: The User to be saved to the database
	 */
	public static void serializeUserProfile(UserProfile user) {
		ArrayList<String> followerNames = user.getFollowers();
		ArrayList<String> followingNames = user.getFollowing();
		ArrayList<FollowRequest> requestInbox = user.getRequests();
		ArrayList<Habit> habits = user.getHabitList();

		String username = user.getUsername();

		Map<String, Object> profile = new HashMap<>();
		profile.put("username", username);

		db.collection("users").document(username).set(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				Log.d(TAG, "Profile successfully written!");
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Log.w(TAG, "Profile writing document", e);
			}
		});

		// Saves all 'Social' elements of a user (following, followers, and inbox)
		for (String follower : followerNames) {
			addFollow(follower, username, KEY_FOLLOWERS);
		}
		for (String following : followingNames) {
			addFollow(following, username, KEY_FOLLOWING);
		}
		for (FollowRequest request : requestInbox) {
			addRequest(request);
		}

	}

	/**
	 * A method for serializing a follower or following into the database
	 * 
	 * @param followname String: The username of the follower/following to be placed
	 * @param username   String: The username of the current User
	 * @param mode       String: The name of the current collection to be added to
	 */
	private static void addFollow(String followname, String username, String mode) {
		Map<String, Object> data = new HashMap<>();
		data.put("username", followname);
		db.collection(COLLECTION_USERS).document(username).collection(mode).document(followname).set(data)
				.addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void unused) {
						Log.d("Followings", mode + " sucessfully added");
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.d("Following", mode + " request failed" + e.toString());
					}
				});

	}// end addFollow

	/**
	 * A method for serializing a follower or following into the database
	 * 
	 * @param followname String: The username of the follower/following to be
	 *                   deleted
	 * @param username   String: The username of the current User
	 * @param mode       String: The name of the current collection to be deleted
	 *                   from
	 */
	private static void deleteFollow(String followname, String username, String mode) {
		db.collection(COLLECTION_USERS).document(username).collection("followers").document(followname).delete()
				.addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void unused) {
						Log.d("Followers", mode + "sucessfully deleted");
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.d("Following", mode + " deletion failed" + e.toString());
					}
				});
	}// end deleteFollow

	/**
	 * This methods adds an inputted request to a User's inbox
	 * 
	 * @param fRequest FollowRequest: The request to be added
	 * @return
	 */
	public static void addRequest(FollowRequest fRequest) {
		HashMap<String, String> data = new HashMap<>();
		data.put("sender", fRequest.getSender());
		data.put("target", fRequest.getTarget());
		db.collection(COLLECTION_USERS).document(fRequest.getTarget()).collection(KEY_INBOX)
				.document(fRequest.getSender()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void unused) {
						Log.d("Follow Request Inbox", "Follow Request successfully added ");
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.d("Follow Request Inbox", "Follow request failed to add " + e.toString());
					}
				});
	}// end addRequest

	/**
	 * This methods deletes the inputted request from a User's inbox
	 * 
	 * @param fRequest FollowRequest: The request to be deleted
	 */
	public static void removeRequest(FollowRequest fRequest) {
		db.collection(COLLECTION_USERS).document(fRequest.getTarget()).collection(KEY_INBOX)
				.document(fRequest.getSender()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void unused) {
						Log.d("Follow Request Inbox", "Follow Request successfully deleted");
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.d("Follow Request Inbox", "Follow request failed to delete" + e.toString());
					}
				});
	}// end deleteRequest

	/**
	 * This method takes in a Follow Request and deals with The addition of
	 * followers and followings
	 * 
	 * @param fRequest FollowRequest: The accepted request to be dealt with
	 */
	public static void acceptRequest(FollowRequest fRequest) {
		addFollow(fRequest.getSender(), fRequest.getTarget(), KEY_FOLLOWERS);
		addFollow(fRequest.getTarget(), fRequest.getSender(), KEY_FOLLOWING);
		removeRequest(fRequest);

	}

	/**
	 * Add a new habit to the user "username" in the firebase database
	 * 
	 * @param username User the habit will be added to
	 * @param habit    The new habit to be added to the Users firebase database
	 * @return true if database transaction was successful, false if unsuccessful
	 */
	public static void addHabit(String username, Habit habit) {
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
						Log.d(TAG, "THE DATA WAS SUBMITTED");
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.d(TAG, e.toString());
					}
				});

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

		// MAY NEED TO USE .set(data, SetOptions.merge()
		// https://firebase.google.com/docs/firestore/manage-data/add-data

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
	public static Habit getHabit(String username, String habitTitle) {
		final Habit[] habit = new Habit[1];

		Log.d(TAG, "SHOULD SEE DOCUMENTSNAPSHOT JUST BELOW HERE");
		Log.d(TAG, "Habit Title :" + habitTitle);

		DocumentReference docRef = db.collection(COLLECTION_USERS).document(username).collection(COLLECTION_HABITS)
				.document(habitTitle);

		Source source = Source.DEFAULT;

		docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.isSuccessful()) {
					DocumentSnapshot document = task.getResult();
					Map<String, Object> data = document.getData();

					// habit[0] = documentSnapshot.toObject(Habit.class);
					// This is all a W.I.P

					String title = (String) data.get("title");
					String reason = (String) data.get("reason");
					String dateToStart = (String) data.get("dateToStart");
					boolean publicVisibility = (boolean) data.get("publicVisibility");
					ArrayList<String> weeklySchedule = (ArrayList<String>) data.get("weekdays");

					habit[0] = new Habit(title, reason, dateToStart, publicVisibility, weeklySchedule);

					/*
					 * habit[0].setTitle(data.get("title"));
					 * habit[0].setReason(documentSnapshot.getString("reason")); //Figure out how to
					 * set hid, currently the title is the primary distinction
					 * habit[0].setDateToStart(documentSnapshot.getString("dateToStart"));
					 * habit[0].setPublicVisibility(documentSnapshot.getBoolean("publicVisibility"))
					 * ; habit[0].setWeeklySchedule(new WeeklySchedule((ArrayList<String>)
					 * dbMap.get("weekdays")));
					 */

					Log.d(TAG, data.toString());
					Log.d(TAG, "toObject Habit: " + habit[0].toString());

				}
			}
		});

		return habit[0];

	}

	/**
	 * Permanently delete the provided habit from the users database file
	 * 
	 * @param username The current user logged in
	 * @param habit    The habit to be deleted
	 */
	public static void deleteHabit(String username, Habit habit) {
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

	public static void writeHabitEvent(String username, String hid, HabitEvent habitEvent){
		String habitEventDateName = sdf.format(habitEvent.getDate().getTime());
		Map<String, Object> habitMap = new HashMap<>();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if(habitEvent.getPhotograph() != null) {
			habitEvent.getPhotograph().compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
			byte[] b = baos.toByteArray();
			String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
			habitMap.put(KEY_EVENT_IMAGE, encodedImage);
		}else{
			habitMap.put(KEY_EVENT_IMAGE, null);
		}


		habitMap.put(KEY_EVENT_DATETIME, habitEventDateName);
		habitMap.put(KEY_EVENT_COMMENT, habitEvent.getComment());
		habitMap.put(KEY_EVENT_LOCATION, habitEvent.getLocation());
		habitMap.put(KEY_EVENT_DONE, habitEvent.getDone());


		db.collection(COLLECTION_USERS).document(username).collection(COLLECTION_HABITS)
				.document(hid).collection(COLLECTION_HABIT_EVENTS).document(habitEventDateName)
				.set(habitMap).addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void unused) {
				Log.d(TAG, "THE HABIT EVENT DATA WAS SUBMITTED");
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Log.d(TAG, e.toString());
			}
		});
	}

	public static boolean checkForHabitEvent(String username, String hid, String Date){
		final boolean[] eventExists = {false};
		db.collection(COLLECTION_USERS).document(username).collection(COLLECTION_HABITS).document(hid)
				.collection(COLLECTION_HABIT_EVENTS).document(Date)
				.get()
				.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
					@Override
					public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
						eventExists[0] = true;
						Log.d(TAG, "Habit Event occurs " + Date);
					}
				});
		return eventExists[0];
	}

	public static CollectionReference getHabitCollection(String username) {
		return db.collection(COLLECTION_USERS).document(username).collection(COLLECTION_HABITS);
	}

}
