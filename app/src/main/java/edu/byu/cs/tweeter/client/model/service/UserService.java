package edu.byu.cs.tweeter.client.model.service;

import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.handler.LoginHandler;
import edu.byu.cs.tweeter.client.presenter.GetFollowingPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {


    public interface Observer {
        void handleSuccess(User user, AuthToken authtoken);
        void handleFailure(String message);
        void handleException(Exception exception);
        void handleFollowingSuccess(User user);
    }

    public void login(String username, String password, Observer observer) {
        LoginTask loginTask = new LoginTask(username, password, new LoginHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);
    }
    public void loadUsers(String userAlias, Observer getUserObserver) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new GetUserHandler(getUserObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }
}
