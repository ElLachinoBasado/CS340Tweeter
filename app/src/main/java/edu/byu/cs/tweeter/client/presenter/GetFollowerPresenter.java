package edu.byu.cs.tweeter.client.presenter;

import android.content.Intent;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.followers.FollowersFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowerPresenter {
    private View view;
    private FollowService followService;
    private UserService userService;

    public GetFollowerPresenter(View view) {
        this.view = view;
        this.followService = new FollowService();
        this.userService = new UserService();
    }

    public void loadUsers(String userAlias) {
        userService.loadUsers(userAlias, new GetUserObserver());
        view.displayMessage("Getting user's profile...");
    }

    public interface View {

        void displayMessage(String message);

        void handleFollowingSuccess(User user);
    }

    private class GetFollowerObserver implements FollowService.Observer {

        @Override
        public void displayError(String message) {

        }

        @Override
        public void displayException(Exception ex) {

        }

        @Override
        public void addFollowees(List<User> followees, boolean hasMorePages) {

        }
    }

    private class GetUserObserver implements UserService.Observer {

        @Override
        public void handleSuccess(User user, AuthToken authtoken) {

        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage(message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to get user's profile because of exception: " + exception.getMessage());
        }

        @Override
        public void handleFollowingSuccess(User user) {
            view.handleFollowingSuccess(user);
        }
    }
}
