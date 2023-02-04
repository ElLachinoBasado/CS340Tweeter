package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.handler.GetFollowingHandler;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {
    public interface Observer {

        void displayError(String message);

        void displayException(Exception ex);

        void addFollowees(List<User> followees, boolean hasMorePages);
    }

    public void loadMoreItems(User user, int pageSize, User lastFollowee, Observer observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollowee, new GetFollowingHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }
}
