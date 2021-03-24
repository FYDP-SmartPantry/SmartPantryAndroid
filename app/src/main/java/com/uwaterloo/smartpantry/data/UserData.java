package com.uwaterloo.smartpantry.data;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.Meta;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Ordering;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;
import com.uwaterloo.smartpantry.database.DatabaseManager;

public class UserData {
    private UserInfo userInfo = new UserInfo();

    public UserData() {}

    public void setUserInfo(UserInfo userInfo){
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public boolean loadUser() {
        try {
            Database database = DatabaseManager.getDatabase(DatabaseManager.userInfoDbStr);
            Query query = QueryBuilder.select(
                    SelectResult.expression(Meta.id),
                    SelectResult.property(UserInfo.usernameString),
                    SelectResult.property(UserInfo.hashString)).from(DataSource.database(database)).orderBy(Ordering.expression(Meta.id));
            try {
                ResultSet rs = query.execute();
                for (Result result : rs) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUsername(result.getString(UserInfo.usernameString));
                    userInfo.setHash(result.getString(UserInfo.hashString));
                    this.userInfo = userInfo;
                }
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveUser() {
        try {
            Database database = DatabaseManager.getDatabase(DatabaseManager.userInfoDbStr);

            MutableDocument mutableDocument = new MutableDocument();
            mutableDocument.setString(UserInfo.usernameString, userInfo.username);
            mutableDocument.setString(UserInfo.hashString, userInfo.hash);
            try {
                database.save(mutableDocument);
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
