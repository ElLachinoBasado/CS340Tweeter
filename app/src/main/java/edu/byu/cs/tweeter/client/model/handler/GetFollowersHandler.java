package edu.byu.cs.tweeter.client.model.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.model.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetFollowersTask.
 */
public class GetFollowersHandler extends Handler {

    private FollowService.Observer observer;
    public GetFollowersHandler(FollowService.Observer observer) {
        super(Looper.getMainLooper());
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
        if (success) {
            List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);
            boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
            observer.addFollowers(followers,hasMorePages);
        } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
            observer.displayError("Failed to get followers: " + message);
        } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
            observer.displayException(ex);
        }
    }
}