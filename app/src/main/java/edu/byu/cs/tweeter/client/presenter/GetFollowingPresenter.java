package edu.byu.cs.tweeter.client.presenter;

import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingPresenter {
    private static final int PAGE_SIZE = 10;
    private User lastFollowee;

    private boolean hasMorePages = true;
    private boolean isLoading = false;



    public interface View {
        void setLoadingFooter(boolean add);

        void displayMessage(String message);

        void addMoreItems(List<User> followees);

        void handleFollowingSuccess(User user);
    }

    private View view;
    private FollowService followService;
    private UserService userService;

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }
    public GetFollowingPresenter(View view) {
        this.view = view;
        followService = new FollowService();
    }
    public void loadMoreItems(User user) {
        if (!isLoading && hasMorePages) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            followService.loadMoreItems(user,PAGE_SIZE,lastFollowee, new GetFollowingObserver());
        }
    }

    public void loadUsers(String userAlias) {
        userService.loadUsers(userAlias, new GetUserObserver());
        view.displayMessage("Getting user's profile...");
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

    private class GetFollowingObserver implements FollowService.Observer {

        @Override
        public void displayError(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get following because of exception: " + ex.getMessage());
        }

        @Override
        public void addFollowees(List<User> followees, boolean hasMorePages) {
            lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.setLoadingFooter(false);
            view.addMoreItems(followees);
            isLoading = false;
        }
    }
}
