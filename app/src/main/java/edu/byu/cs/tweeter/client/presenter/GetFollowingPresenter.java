package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
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
    }

    private View view;
    private FollowService followService;

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
