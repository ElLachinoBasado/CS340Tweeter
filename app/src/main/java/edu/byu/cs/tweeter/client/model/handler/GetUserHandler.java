package edu.byu.cs.tweeter.client.model.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetUserTask.
 */
public class GetUserHandler extends Handler {

    private UserService.Observer observer;
    public GetUserHandler(UserService.Observer observer) {
        super(Looper.getMainLooper());
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
        if (success) {
            User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
            observer.handleFollowingSuccess(user);
        } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
            observer.handleFailure("Failed to get user's profile: " + message);
        } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
            observer.handleException(ex);
        }
    }
}