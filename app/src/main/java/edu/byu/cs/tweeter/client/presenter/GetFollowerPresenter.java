package edu.byu.cs.tweeter.client.presenter;

import android.content.Intent;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.handler.GetFollowersHandler;
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
    private User lastFollower;
    private boolean hasMorePages = true;
    private boolean isLoading = false;

    private static final int PAGE_SIZE = 10;

    public GetFollowerPresenter(View view) {
        this.view = view;
        this.followService = new FollowService();
        this.userService = new UserService();
    }

    public boolean isLoading() {
        return isLoading;
    }
    public void setIsLoading(boolean loading) {
        this.isLoading = loading;
    }
    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }
    public void loadUsers(String userAlias) {
        userService.loadUsers(userAlias, new GetUserObserver());
        view.displayMessage("Getting user's profile...");
    }

    public void loadMoreItems(User user) {
        if (!isLoading && hasMorePages) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            followService.loadMoreFollowers(user, PAGE_SIZE, lastFollower, new GetFollowerObserver());

        }
    }

    public interface View {

        void displayMessage(String message);

        void handleFollowingSuccess(User user);

        void setLoadingFooter(boolean add);

        void addMoreItems(List<User> followers);
    }

    private class GetFollowerObserver implements FollowService.Observer {

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
            view.displayMessage("Failed to get followers because of exception: " + ex.getMessage());
        }

        @Override
        public void addFollowees(List<User> followees, boolean hasMorePages) {
        }

        @Override
        public void addFollowers(List<User> followers, boolean hasMorePages) {
            lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.setLoadingFooter(false);
            view.addMoreItems(followers);
            isLoading = false;
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
